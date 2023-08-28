package com.tatv.baseapp.data.model.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tatv on 28/02/2023.
 */
public class AuthRequest {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("appId")
    @Expose
    private String appId;
    @SerializedName("appVersion")
    @Expose
    private String appVersion;
    @SerializedName("deviceId")
    @Expose
    private String deviceId;
    @SerializedName("fcmToken")
    @Expose
    private String fcmToken;

    public String getUsername() {
        return username;
    }

    public AuthRequest setUsername(String username) {
        this.username = username;
        return this;
    }

    public String getPassword() {
        return password;
    }

    public AuthRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getAppId() {
        return appId;
    }

    public AuthRequest setAppId(String appId) {
        this.appId = appId;
        return this;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public AuthRequest setAppVersion(String appVersion) {
        this.appVersion = appVersion;
        return this;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public AuthRequest setDeviceId(String deviceId) {
        this.deviceId = deviceId;
        return this;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public AuthRequest setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
        return this;
    }

    @Override
    public String toString() {
        return "LoginRequest{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", appId='" + appId + '\'' +
                ", appVersion='" + appVersion + '\'' +
                ", deviceId='" + deviceId + '\'' +
                ", fcmToken='" + fcmToken + '\'' +
                '}';
    }
}
