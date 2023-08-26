package com.tatv.baseapp.service;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import com.tatv.baseapp.data.shared.BaseSharedPreference;
import com.tatv.baseapp.utils.log.LogUtils;
import com.tatv.baseapp.utils.network.Connectivity;

public abstract class BaseService extends Service {
    private static final String TAG = "BaseService";
    private BroadcastReceiver receiver;
    protected Context context;
    protected BaseSharedPreference preference;

    private boolean register = false;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    protected void init(){
        context = this;
        preference = new BaseSharedPreference(context);
        registerReceiver();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver();
    }

    public void registerReceiver(){
        if(!register){
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    onReceiverBroadcast(context, intent);
                }
            };

            IntentFilter filter = new IntentFilter();
            for (String action : getIntentFilters()){
                filter.addAction(action);
            }
            registerReceiver(receiver, filter);
            register = true;
        }
    }

    public void unregisterReceiver(){
        if(register){
            try{
                unregisterReceiver(receiver);
            }catch (IllegalArgumentException e){
                Log.e(TAG, e.getMessage());
            }
        }
    }

    protected abstract void onReceiverBroadcast(Context context, Intent intent);

    protected abstract String[] getIntentFilters();


    /**
     * Lấy danh sách quyền cơ bản
     * */
    protected String[] getPermissionArray(){
        return new String[0];
    }

    /**
     * Kiểm tra quyền đã được cấp hay chưa
     * */
    protected boolean isPermissionGranted(){
        for (String permission: getPermissionArray()) {
            if(!checkPermission(permission)){
                return false;
            }
        }
        return true;
    }

    /**
     * Kiểm tra quyền
     * */
    protected boolean checkPermission(String permission){
        return ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Hiển thị toast
     * */
    public void showToast(String msg) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show());
    }


    /**
     * Tạo thông báo
     * */
    @SuppressLint({"WrongConstant", "InvalidWakeLockTag", "UnspecifiedImmutableFlag"})
    public void createNotification(Context context,int notificationId, String channelId, String channelName, String contentText, int thumbnail, Class activityClass, Class serviceClass, String actionClose) {
        try{
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel chan;
                chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
                chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
                NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
                assert manager != null;
                manager.createNotificationChannel(chan);
                startForeground(notificationId, new NotificationCompat.Builder(context, channelId)
                        .setAutoCancel(false)
                        .setOngoing(true)
                        .setSmallIcon(thumbnail)
                        .setContentText(contentText)
                        .setContentIntent(PendingIntent.getActivity(context,
                                0,
                                new Intent(context, activityClass),
                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE))
                        .setPriority(NotificationManager.IMPORTANCE_MAX)
                        .setCategory(Notification.CATEGORY_SERVICE)
                        .setCategory(Notification.CATEGORY_SERVICE)
                        .addAction(
                                thumbnail,
                                "Close",
                                PendingIntent.getService(
                                        context,
                                        0,
                                        new Intent(this, serviceClass)
                                                .setAction(actionClose),
                                        PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE))
                        .build());
            } else {
                startForeground(notificationId, new Notification.Builder(context)
                        .setAutoCancel(false)
                        .setOngoing(true)
                        .setSmallIcon(thumbnail)
                        .setContentText(contentText)
                        .setContentIntent(PendingIntent.getActivity(context,
                                0,
                                new Intent(context, activityClass),
                                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE))
                        .setPriority(Notification.PRIORITY_MAX)
                        .build());
            }
        }catch (Exception e){
            LogUtils.e(TAG, e.getMessage());
        }
    }


    /**
     * Loại bỏ thông báo
     */
    protected void cancelNotification() {
        stopForeground(true);
    }


    /**
     * Kiểm tra kết nối mạng
     * */
    protected boolean isNetworkConnected(){
        return Connectivity.getInstance(context).isNetworkConnected();
    }
}
