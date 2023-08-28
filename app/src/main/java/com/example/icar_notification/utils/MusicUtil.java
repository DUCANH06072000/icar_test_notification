package com.example.icar_notification.utils;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;

import com.example.icar_notification.listener.MusicStateListener;
import com.example.icar_notification.share.DataMusicStore;
import com.example.icar_notification.share.DataStore;

import java.util.List;

public class MusicUtil {
    private AudioManager audioManager;
    private MusicStateListener listener;
    private Context context;

    private DataStore dataStore;
    private DataMusicStore dataMusicStore;

    public MusicUtil(MusicStateListener listener, Context context) {
        this.listener = listener;
        this.context = context;
        dataStore = new DataStore(context);
        dataMusicStore = new DataMusicStore(context);
        audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        updateMusicState();
    }

    private void updateMusicState() {
        if (audioManager != null) {
            boolean isPlaying = audioManager.isMusicActive();
            listener.onMusicSateListener(isPlaying);
        }
    }


    public void nextMusic() {
        if (audioManager != null) {
            // Kiểm tra nếu thiết bị hỗ trợ chức năng next bài hát
            if (audioManager != null && audioManager.isMusicActive()) {
                KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_NEXT);
                audioManager.dispatchMediaKeyEvent(event);
                event = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_NEXT);
                audioManager.dispatchMediaKeyEvent(event);
            } else {
                // Xử lý khi không thể next bài hát (ví dụ: không có bài hát đang phát)
                Toast.makeText(context, "Không thể next bài hát", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void preMusic() {
        // Kiểm tra nếu thiết bị hỗ trợ chức năng lùi bài đang phát
        if (audioManager != null && audioManager.isMusicActive()) {
            KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PREVIOUS);
            audioManager.dispatchMediaKeyEvent(event);
            event = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PREVIOUS);
            audioManager.dispatchMediaKeyEvent(event);
        } else {
            // Xử lý khi không thể lùi bài đang phát
            Toast.makeText(context, "Không thể lùi bài đang phát", Toast.LENGTH_SHORT).show();
        }
    }

    public void pauseMusic() {
        if (audioManager != null && audioManager.isMusicActive()) {
            KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
            audioManager.dispatchMediaKeyEvent(event);
            event = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
            audioManager.dispatchMediaKeyEvent(event);
            listener.onMusicSateListener(false);
        }
    }

    public void playMusic() {
        KeyEvent event = new KeyEvent(KeyEvent.ACTION_DOWN, KeyEvent.KEYCODE_MEDIA_PLAY);
        audioManager.dispatchMediaKeyEvent(event);
        event = new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_MEDIA_PLAY);
        audioManager.dispatchMediaKeyEvent(event);
        listener.onMusicSateListener(true);
    }
}
