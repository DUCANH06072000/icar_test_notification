package com.tatv.baseapp.data.model.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tatv on 28/02/2023.
 */
public class LogoutDeviceRequest {
    @SerializedName("appId")
    @Expose
    private String appId;
    @SerializedName("accessToken")
    @Expose
    private String accessToken;

    public LogoutDeviceRequest(String appId, String accessToken) {
        this.appId = appId;
        this.accessToken = accessToken;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "LogoutDeviceRequest{" +
                "appId='" + appId + '\'' +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
