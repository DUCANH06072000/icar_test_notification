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
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.tatv.baseapp.data.shared.BaseSharedPreference;
import com.tatv.baseapp.utils.log.LogUtils;
import com.tatv.baseapp.utils.network.Connectivity;
import com.tatv.baseapp.utils.network.ConnectivityListener;

public abstract class BaseService extends Service implements ConnectivityListener {
    private static String TAG = "BaseService";
    private BroadcastReceiver receiver;
    protected Context context;
    protected BaseSharedPreference preference;
    protected Connectivity connectivity;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    protected void init(){
        context = this;
        preference = new BaseSharedPreference(context);
        registerReceiver();
        connectivity = new Connectivity(context);
        connectivity.addChildListener(new Connectivity.ConnectivityChildListener(TAG,this));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(receiver != null){
            try{
                unregisterReceiver(receiver);
            }catch (IllegalArgumentException e){
                Log.e(TAG, e.getMessage());
            }
        }
        connectivity.removeChildListener(TAG);
    }

    public void registerReceiver(){
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
        try {
            registerReceiver(receiver, filter);
        }catch (Exception e){
            LogUtils.e(TAG, e.getMessage());
        }
    }



    protected abstract void onReceiverBroadcast(Context context, Intent intent);

    protected abstract String[] getIntentFilters();

    /**
     * Hiển thị toast
     * */
    public void showToast(String msg) {
        new Handler(Looper.getMainLooper()).post(() -> Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show());
    }

    /**
     * Lấy ra thể hiện của share prefrence
     * */
    protected <T extends BaseSharedPreference> T getSharedPreference(){
        return (T) preference;
    }


    /**
     * Tạo thông báo
     * */
    @SuppressLint({"WrongConstant", "InvalidWakeLockTag", "UnspecifiedImmutableFlag"})
    public void createNotification(Context context, String channelId, String channelName, String contentText, int thumbnail, Class activityClass, Class serviceClass, String actionClose) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan;
            chan = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_NONE);
            chan.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            NotificationManager manager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            assert manager != null;
            manager.createNotificationChannel(chan);
            startForeground(1, new NotificationCompat.Builder(context, channelId)
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
            startForeground(2, new Notification.Builder(context)
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
    }


    /**
     * Loại bỏ thông báo
     */
    private void cancelNotification() {
        stopForeground(true);
    }


    @Override
    public void onConnectivityChanged(boolean enabled) {
//        Log.e(TAG, "onConnectivityChanged: " + enabled);
    }

    @Override
    public void onPingChanged(long timeofping) {
//        Log.e(TAG, "onPingChanged: " + timeofping);
    }

    /**
     * Kiểm tra kết nối mạng
     * */
    protected boolean isNetworkConnected(){
        return connectivity.isNetworkConnected();
    }
}
