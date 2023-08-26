package vn.icar.baseauthentication.data.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BodyLogin {
    @SerializedName("deviceName")
    @Expose
    private String deviceName;
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

    public BodyLogin(String deviceName, String username, String password, String appId, String appVersion, String deviceId, String fcmToken) {
        this.deviceName = deviceName;
        this.username = username;
        this.password = password;
        this.appId = appId;
        this.appVersion = appVersion;
        this.deviceId = deviceId;
        this.fcmToken = fcmToken;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppVersion() {
        return appVersion;
    }

    public void setAppVersion(String appVersion) {
        this.appVersion = appVersion;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }
}