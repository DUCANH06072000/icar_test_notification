package com.example.icar_notification.model;

import android.graphics.drawable.Drawable;

public class AppDevice {
    private String nameApp;
    private String packageName;
    private Drawable drawableApp;

    public AppDevice(String nameApp, String packageName, Drawable drawableApp) {
        this.nameApp = nameApp;
        this.packageName = packageName;
        this.drawableApp = drawableApp;
    }

    public Drawable getDrawableApp() {
        return drawableApp;
    }

    public void setDrawableApp(Drawable drawableApp) {
        this.drawableApp = drawableApp;
    }

    public String getNameApp() {
        return nameApp;
    }

    public void setNameApp(String nameApp) {
        this.nameApp = nameApp;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
