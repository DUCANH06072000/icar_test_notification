package com.tatv.baseapp.data.model;

/**
 * Created by tatv on 07/10/2022.
 */
public class SocketAuth {
    String appId;
    String deviceId;
    String appVersion;
    String deviceName;
    String fcmToken;


    public String getAppId() {
        return appId;
    }

    public SocketAuth setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public SocketAuth setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public SocketAuth setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public SocketAuth setDeviceName(String deviceName) {
        this.deviceName = deviceName;
        return this;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public SocketAuth setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
        return this;
    }

    @Override
    public String toString() {
        return "Auth{" +
                "appId='" + appId + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", fcmToken='" + fcmToken + '\'' +
                '}';
    }
}
