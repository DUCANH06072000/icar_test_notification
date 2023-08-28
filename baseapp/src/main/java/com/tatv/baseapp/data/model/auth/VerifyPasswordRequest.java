package com.tatv.baseapp.data.model.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tatv on 08/03/2023.
 */
public class VerifyPasswordRequest {
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("appId")
    @Expose
    private String appId;
    @SerializedName("oldPassword")
    @Expose
    private String oldPassword;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("rePassword")
    @Expose
    private String rePassword;
    @SerializedName("fcmToken")
    @Expose
    private String fcmToken;

    public VerifyPasswordRequest(String username, String otp, String appId, String password, String fcmToken) {
        this.username = username;
        this.otp = otp;
        this.appId = appId;
        this.password = password;
        this.rePassword = password;
        this.fcmToken = fcmToken;
    }

    public VerifyPasswordRequest(String username, String appId, String oldPassword, String password) {
        this.username = username;
        this.appId = appId;
        this.oldPassword = oldPassword;
        this.password = password;
        this.rePassword = password;
    }

    @Override
    public String toString() {
        return "VerifyPasswordRequest{" +
                "username='" + username + '\'' +
                ", otp='" + otp + '\'' +
                ", appId='" + appId + '\'' +
                ", oldPassword='" + oldPassword + '\'' +
                ", password='" + password + '\'' +
                ", rePassword='" + rePassword + '\'' +
                ", fcmToken='" + fcmToken + '\'' +
                '}';
    }
}
