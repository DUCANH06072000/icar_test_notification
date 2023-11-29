package com.example.icar_notification.listener;

import android.graphics.Bitmap;

public interface IMusicListener {
    public void onChangePlaying(int state);

    public void onChangeTitleMusic(String title);

    public void onChangeImageApp(Bitmap bitmap);

    public void onChangeTimeMusic(long mediaCurrentTime,long mediaTotalTime);
}
