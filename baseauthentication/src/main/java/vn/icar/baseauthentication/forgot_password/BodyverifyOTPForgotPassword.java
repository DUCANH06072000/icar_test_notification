package vn.icar.baseauthentication.forgot_password;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BodyverifyOTPForgotPassword {

@SerializedName("username")
@Expose
private String username;
    @SerializedName("otp")
    @Expose
    private String otp;
    @SerializedName("appId")
    @Expose
    private String appId;

    public BodyverifyOTPForgotPassword(String username, String otp, String appId) {
        this.username = username;
        this.otp = otp;
        this.appId = appId;
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

    public String getUsername() {
return username;
}

public void setUsername(String username) {
this.username = username;
}



}