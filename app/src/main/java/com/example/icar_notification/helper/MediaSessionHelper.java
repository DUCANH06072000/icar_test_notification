package com.example.icar_notification.helper;

import android.content.ComponentName;
import android.content.Context;
import android.media.session.MediaController;
import android.media.session.MediaSessionManager;
import android.os.Build;
import android.support.v4.media.session.MediaSessionCompat;
import android.util.Log;

import com.example.icar_notification.service.NotificationListener;

public class MediaSessionHelper {
    public static MediaSessionCompat.Token getMediaSessionTokenForPackage(Context context, String packageName) {
        MediaSessionManager mediaSessionManager = (MediaSessionManager) context.getSystemService(Context.MEDIA_SESSION_SERVICE);
        for (MediaController controller : mediaSessionManager.getActiveSessions(new ComponentName(context, NotificationListener.class)))
            if (packageName.equals(controller.getPackageName())) {
                MediaSessionCompat.Token token = MediaSessionCompat.Token.fromToken(controller.getSessionToken());
                return token;
            }
        return null;
    }
}
