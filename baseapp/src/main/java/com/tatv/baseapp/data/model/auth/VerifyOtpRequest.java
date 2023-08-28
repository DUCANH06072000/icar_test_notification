package com.tatv.baseapp.data.model.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tatv on 08/03/2023.
 */
public class VerifyOtpRequest {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("appId")
    @Expose
    private String appId;

    public VerifyOtpRequest(String username, String otp, String appId) {
        this.username = username;
        this.otp = otp;
        this.appId = appId;
    }

    @Override
    public String toString() {
        return "VerifyOtpRequest{" +
                "username='" + username + '\'' +
                ", otp='" + otp + '\'' +
                ", appId='" + appId + '\'' +
                '}';
    }
}
