package com.tatv.baseapp.data.model.auth;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RefreshToken {
    @SerializedName("refreshToken")
    @Expose
    private String refreshToken;

    public RefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    @Override
    public String toString() {
        return "RefreshToken{" +
                "refreshToken='" + refreshToken + '\'' +
                '}';
    }
}