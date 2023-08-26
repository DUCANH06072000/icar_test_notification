package vn.icar.baseauthentication.forgot_password;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BodySendOTPForgotPassword {

@SerializedName("username")
@Expose
private String username;


    public BodySendOTPForgotPassword(String username) {
        this.username = username;

    }

    public String getUsername() {
return username;
}

public void setUsername(String username) {
this.username = username;
}



}