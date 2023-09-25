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
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.Toast;

import com.example.icar_notification.adapter.AppAdapter;
import com.example.icar_notification.databinding.ActivityMainBinding;
import com.example.icar_notification.listener.SelectAppListener;
import com.example.icar_notification.model.AppDevice;
import com.example.icar_notification.model.AppStore;
import com.example.icar_notification.service.NotificationListener;
import com.example.icar_notification.service.NotificationService;
import com.example.icar_notification.share.DataMusicStore;
import com.example.icar_notification.share.DataStore;
import com.example.icar_notification.utils.MediaSessionHelper;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SelectAppListener {
    private List<ApplicationInfo> appList;
    private List<AppDevice> appDevices;
    private ActivityMainBinding binding;
    private DataStore dataStore;
    private Dialog dialog;
    private AppAdapter appAdapter;

    private DataMusicStore dataMusicStore;

    private boolean isPlaying = false;

    private MediaSessionCompat mediaSessionCompat;
    private MediaControllerCompat mediaController;
    private MediaBrowserCompat mediaBrowserCompat;

    private long currentPlaybackPosition = 0;
    private long totalDuration = 0;

    private final Handler handler = new Handler();
    private final Runnable updateProgressRunnable = new Runnable() {
        @Override
        public void run() {
            if (isPlaying) {
                currentPlaybackPosition = mediaController.getPlaybackState().getPosition();
                updateProgressViews(); // Cập nhật giao diện với thời gian hiện tại
                handler.postDelayed(this, 1000); // Lặp lại sau mỗi giây
            }
        }
    };

    private void updateProgressViews() {
        // Format thời gian thành định dạng phút:giây
        String currentTimeStr = formatTime(currentPlaybackPosition);
        String totalDurationStr = formatTime(totalDuration);
        Log.e("Thời gian hiện tại", currentTimeStr + " /s ");
        // Cập nhật giá trị và trạng thái của SeekBar
        binding.seekbar.setMax((int) totalDuration);
        binding.seekbar.setProgress((int) currentPlaybackPosition);
    }

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
        setUpMedia();
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
        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    // Cập nhật thời gian phát nhạc theo giá trị của SeekBar
                    mediaController.getTransportControls().seekTo(progress);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // Dừng việc cập nhật tự động trong thời gian người dùng vuốt SeekBar
                handler.removeCallbacks(updateProgressRunnable);
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // Bắt đầu việc cập nhật tự động sau khi người dùng dừng vuốt SeekBar
                handler.post(updateProgressRunnable);
            }
        });
        binding.btnOpenDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.show();
            }
        });
        binding.btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheckNotification()) {
                    mediaController.getTransportControls().skipToPrevious();
                } else {
                    if (dataStore.loadData() != null && dataStore.loadData().size() > 0) {
                        openApp(dataStore.loadData().get(0).getPackageName());
                    } else {
                        Toast.makeText(MainActivity.this, "Chưa chọn app", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        binding.btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheckNotification()) {
                    mediaController.getTransportControls().skipToNext();
                } else {
                    if (dataStore.loadData() != null && dataStore.loadData().size() > 0) {
                        openApp(dataStore.loadData().get(0).getPackageName());
                    } else {
                        Toast.makeText(MainActivity.this, "Chưa chọn app", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        binding.btnMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isCheckNotification()) {
                    if (isPlaying) {
                        mediaController.getTransportControls().pause();
                    } else {
                        mediaController.getTransportControls().play();
                    }
                } else {
                    if (dataStore.loadData() != null && dataStore.loadData().size() > 0) {
                        openApp(dataStore.loadData().get(0).getPackageName());
                    } else {
                        Toast.makeText(MainActivity.this, "Chưa chọn app", Toast.LENGTH_LONG).show();
                    }
                }
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
        setUpMedia();
        registerReceiverNotification();

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
        setUpMedia();
    }

    private void setUpMedia() {
        if (dataStore.loadData() != null && dataStore.loadData().size() > 0) {
            MediaSessionCompat.Token token = MediaSessionHelper.getMediaSessionTokenForPackage(this, dataStore.loadData().get(0).getPackageName());
            if (token != null) {
                mediaController = new MediaControllerCompat(this, token);
                mediaController.registerCallback(mCallback);
                mCallback.onPlaybackStateChanged(mediaController.getPlaybackState());
                mCallback.onMetadataChanged(mediaController.getMetadata());
            } else {
                Toast.makeText(this, "Ứng dụng chưa chạy", Toast.LENGTH_SHORT).show();

            }
        }
    }

    private final MediaControllerCompat.Callback mCallback = new MediaControllerCompat.Callback() {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat playbackState) {
            onUpdate();
            if (playbackState != null) {
                if (playbackState.getState() == PlaybackStateCompat.STATE_PLAYING) {
                    isPlaying = true;
                    binding.btnMusic.setImageResource(R.drawable.start);
                    handler.post(updateProgressRunnable); // Bắt đầu cập nhật thời gian hiện tại
                } else if (playbackState.getState() == PlaybackStateCompat.STATE_PAUSED) {
                    isPlaying = false;
                    binding.btnMusic.setImageResource(R.drawable.pause);
                    handler.removeCallbacks(updateProgressRunnable); // Dừng cập nhật thời gian hiện tại
                }
            }
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat metadata) {
            onUpdate();
            totalDuration = metadata.getLong(MediaMetadataCompat.METADATA_KEY_DURATION);
            updateProgressViews();
        }

        @Override
        public void onSessionDestroyed() {
        }

        private void onUpdate() {

        }
    };

    private String formatTime(long timeInMillis) {
        int seconds = (int) (timeInMillis / 1000) % 60;
        int minutes = (int) ((timeInMillis / (1000 * 60)) % 60);

        return String.format("%02d:%02d", minutes, seconds);
    }


    public boolean isCheckNotification() {
        if (mediaController != null) {
            return true;
        } else {
            return false;
        }
    }
}