package com.tatv.baseapp.data.model.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tatv on 28/02/2023.
 */
public class LogoutDeviceThenLoginRequest {
    @SerializedName("login")
    @Expose
    private AuthRequest authRequest;
    @SerializedName("logout")
    @Expose
    private LogoutDeviceRequest logoutDeviceRequest;

    public LogoutDeviceThenLoginRequest(AuthRequest authRequest, LogoutDeviceRequest logoutDeviceRequest) {
        this.authRequest = authRequest;
        this.logoutDeviceRequest = logoutDeviceRequest;
    }

    @Override
    public String toString() {
        return "LogoutThenLoginDeviceRequest{" +
                "authRequest=" + authRequest +
                ", logoutDeviceRequest=" + logoutDeviceRequest +
                '}';
    }
}
