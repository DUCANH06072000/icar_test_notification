package com.example.icar_notification.service;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.example.icar_notification.share.DataMusicStore;
import com.example.icar_notification.share.DataStore;

public class NotificationListener extends NotificationListenerService {

    private DataMusicStore dataMusicStore;
    private DataStore dataStore;
    private Context context;


    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        dataMusicStore = new DataMusicStore(context);
        dataStore = new DataStore(context);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return super.onBind(intent);
    }

    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        super.onNotificationPosted(sbn);
        String packageName = sbn.getPackageName();
        Log.e("MyNotification", packageName);
        Notification notification = sbn.getNotification();
        if (notification != null) {
            CharSequence title = notification.extras.getCharSequence(Notification.EXTRA_TITLE);
            for (int i = 0; i < dataStore.loadData().size(); i++) {
                if (packageName.equals(dataStore.loadData().get(i).getPackageName())) {
                    if (title != null) {
                        String notificationTitle = title.toString();
                        Intent intent = new Intent("Msg");
                        intent.putExtra("title", notificationTitle);
                        Log.e("MyNotification", notificationTitle);
                        title = notificationTitle;
                        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
                        dataMusicStore.saveData(notificationTitle);
                    }
                }
            }

        } else {
            Log.e("MyNotificationService", "Thông báo");
        }
    }
}
