package com.example.icar_notification.model;
public class AppStore {
    private String nameApp;
    private String packageName;

    public AppStore(String nameApp, String packageName) {
        this.nameApp = nameApp;
        this.packageName = packageName;
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
