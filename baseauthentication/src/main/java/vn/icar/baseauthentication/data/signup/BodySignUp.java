package vn.icar.baseauthentication.data.signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BodySignUp {

@SerializedName("username")
@Expose
private String username;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("appId")
    @Expose
    private String appId;
    @SerializedName("password")
    @Expose
    private String password;
    @SerializedName("rePassword")
    @Expose
    private String rePassword;
    @SerializedName("fcmToken")
    @Expose
    private String fcmToken;

    public BodySignUp(String username, String otp, String appId, String password, String rePassword, String fcmToken) {
        this.username = username;
        this.otp = otp;
        this.appId = appId;
        this.password = password;
        this.rePassword = rePassword;
        this.fcmToken = fcmToken;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRePassword() {
        return rePassword;
    }

    public void setRePassword(String rePassword) {
        this.rePassword = rePassword;
    }

    public String getFcmToken() {
        return fcmToken;
    }

    public void setFcmToken(String fcmToken) {
        this.fcmToken = fcmToken;
    }

    public String getUsername() {
return username;
}

public void setUsername(String username) {
this.username = username;
}



}