package com.example.icar_notification.service;

import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.icar_notification.MainActivity;
import com.example.icar_notification.R;
import com.tatv.baseapp.service.BaseService;

public class NotificationService extends BaseService {
    private static final String CHANNEL_ID = "ForegroundServiceChannel";
    private static final int NOTIFICATION_THUMBNAIL = R.drawable.ic_launcher_background
            ; // Thay thế bằng resource icon của bạn

    @Override
    protected void onReceiverBroadcast(Context context, Intent intent) {
        // Xử lý broadcast khi nhận được
    }

    @Override
    protected String[] getIntentFilters() {
        // Trả về danh sách các Intent Filter mà bạn muốn đăng ký
        return new String[] {
                "your.intent.filter.action1",
                "your.intent.filter.action2"
        };
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createForegroundService();
    }

    private void createForegroundService() {
        createNotification(
                this,
                CHANNEL_ID,
                "Foreground Service",
                "Foreground service is running.",
                NOTIFICATION_THUMBNAIL,
                MainActivity.class, // Thay thế bằng lớp hoạt động mà bạn muốn mở khi bấm vào thông báo
                NotificationService.class,
                "actionClose"
        );
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Xử lý logic của foreground service ở đây
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
