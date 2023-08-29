package com.example.icar_notification;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.icar_notification.adapter.AppAdapter;
import com.example.icar_notification.databinding.ActivityMainBinding;
import com.example.icar_notification.listener.MusicStateListener;
import com.example.icar_notification.listener.SelectAppListener;
import com.example.icar_notification.model.AppDevice;
import com.example.icar_notification.model.AppStore;
import com.example.icar_notification.service.NotificationListener;
import com.example.icar_notification.service.NotificationService;
import com.example.icar_notification.share.DataMusicStore;
import com.example.icar_notification.share.DataStore;
import com.example.icar_notification.utils.MusicUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectAppListener, MusicStateListener {
    private List<ApplicationInfo> appList;
    private List<AppDevice> appDevices;
    private ActivityMainBinding binding;
    private DataStore dataStore;
    private Dialog dialog;
    private AppAdapter appAdapter;

    private DataMusicStore dataMusicStore;
    private MusicUtil musicUtil;

    private boolean isPlaying = false;

    private MediaSessionCompat mediaSessionCompat;

    private MediaControllerCompat mediaController;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dataStore = new DataStore(this);
        appDevices = new ArrayList<>();
        appList = getAppInDevice();
        dialog = new Dialog(this);
        dataMusicStore = new DataMusicStore(this);
        musicUtil = new MusicUtil(this, this);
        showDialog(dialog);
        initListener();
        boolean isNotificationListenerEnabled = isNotificationListenerServiceEnabled();
        if (isNotificationListenerEnabled) {
            Log.e("MainActivity", "Chạy service");
            startService(new Intent(this, NotificationListener.class));
        } else {
            requestNotificationListenerServicePermission();
        }
        if (dataStore.loadData() != null && dataStore.loadData().size() > 0) {
            Log.e("Số lượng ứng dụng trong kho", dataStore.loadData().size() + "");
        }
        runService();

    }

    private void runService() {
        startForegroundService(new Intent(this, NotificationService.class));
    }

    // Kiểm tra xem dịch vụ NotificationListenerService đã được bật và có quyền truy cập thông báo hay không
    private boolean isNotificationListenerServiceEnabled() {
        String packageName = getPackageName();
        String enabledListeners = Settings.Secure.getString(getContentResolver(), "enabled_notification_listeners");
        return enabledListeners != null && enabledListeners.contains(packageName);
    }


    // Yêu cầu người dùng bật dịch vụ và cấp quyền trong màn hình cài đặt
    private void requestNotificationListenerServicePermission() {
        Intent intent = new Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS);
        startActivity(intent);
    }

    private List<ApplicationInfo> getAppInDevice() {
        List<ApplicationInfo> installedApps = new ArrayList<>();
        PackageManager pm = getPackageManager();
        List<ApplicationInfo> apps = pm.getInstalledApplications(PackageManager.GET_META_DATA);
        for (ApplicationInfo appInfo : apps) {
            if ((appInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                installedApps.add(appInfo);
            }
        }
        return installedApps;
    }


    private void initListener() {
        if (dataStore.loadData() != null && dataStore.loadData().size() > 0) {
            binding.txtNameApp.setText(dataStore.loadData().get(0).getNameApp());
            binding.txtNameMusic.setText(dataMusicStore.loadDataNameMusic());
            binding.txtNameApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openApp(dataStore.loadData().get(0).getPackageName());
                }
            });
        }
        binding.btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        binding.btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicUtil.preMusic();
            }
        });
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                musicUtil.nextMusic();
            }
        });
        binding.btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mediaController.getTransportControls().pause();
            }
        });
    }

    public void showDialog(Dialog dialog) {
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_app);
        Window window = dialog.getWindow();
        if (window == null) {
            return;
        }
        window.setLayout(WindowManager.LayoutParams.WRAP_CONTENT, WindowManager.LayoutParams.WRAP_CONTENT);
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams windowAttribute = window.getAttributes();
        windowAttribute.gravity = Gravity.CENTER;
        window.setAttributes(windowAttribute);
        dialog.setCanceledOnTouchOutside(true);
        RecyclerView recyclerAppDevice = dialog.findViewById(R.id.lv_app);
        for (ApplicationInfo applicationInfo : appList) {
            String appName = applicationInfo.loadLabel(getPackageManager()).toString();
            String packageName = applicationInfo.packageName;
            Drawable appIcon = applicationInfo.loadIcon(getPackageManager());
            appDevices.add(new AppDevice(appName, packageName, appIcon));
        }
        appAdapter = new AppAdapter(MainActivity.this, appDevices, this);
        recyclerAppDevice.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        recyclerAppDevice.setAdapter(appAdapter);
    }


    @Override
    protected void onResume() {
        super.onResume();
        registerReceiverNotification();
        musicUtil = new MusicUtil(this, this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unRegisterReceiverNotification();
    }


    private void unRegisterReceiverNotification() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(notificationReceiver);
    }

    private void registerReceiverNotification() {
        LocalBroadcastManager.getInstance(this).registerReceiver(notificationReceiver, new IntentFilter("Msg"));
    }

    private BroadcastReceiver notificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String notificationTitle = intent.getStringExtra("title");
            binding.txtNameMusic.setText(notificationTitle);
        }
    };

    private void openApp(String packageName) {
        PackageManager packageManager = getPackageManager();
        Intent intent = packageManager.getLaunchIntentForPackage(packageName);
        if (intent != null) {
            startActivity(intent);
        } else {
            Toast.makeText(this, "Ứng dụng không tồn tại", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onSelectAppListener(AppDevice appDevice) {
        if (dataStore.loadData() != null && dataStore.loadData().size() > 0) {
            dataStore.clearData();
            dataStore.addData(new AppStore(appDevice.getNameApp(), appDevice.getPackageName()));
            openApp(appDevice.getPackageName());
            dialog.dismiss();
        } else {
            dataStore.addData(new AppStore(appDevice.getNameApp(), appDevice.getPackageName()));
            openApp(appDevice.getPackageName());
            dialog.dismiss();
        }
        binding.txtNameApp.setText(dataStore.loadData().get(0).getNameApp());
        binding.txtNameMusic.setText("");
        mediaSessionCompat = new MediaSessionCompat(this, appDevice.getNameApp());
        mediaSessionCompat.setActive(true); // Activate the session
        mediaController = new MediaControllerCompat(this,mediaSessionCompat.getSessionToken());
    }

    @Override
    public void onMusicSateListener(boolean isPlaying) {
        this.isPlaying = isPlaying;
        if (isPlaying) {
            binding.btnMusic.setImageResource(R.drawable.start);
        } else {
            binding.btnMusic.setImageResource(R.drawable.pause);
        }
    }
}