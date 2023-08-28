package com.tatv.baseapp.data.model.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tatv on 08/03/2023.
 */
public class SendOtpRequest {
    @SerializedName("username")
    @Expose
    private String username;

    public SendOtpRequest(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return "SendOtpRequest{" +
                "username='" + username + '\'' +
                '}';
    }
}
