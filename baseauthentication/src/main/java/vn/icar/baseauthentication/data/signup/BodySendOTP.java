package vn.icar.baseauthentication.data.signup;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BodySendOTP {

@SerializedName("username")
@Expose
private String username;


    public BodySendOTP(String username) {
        this.username = username;

    }

    public String getUsername() {
return username;
}

public void setUsername(String username) {
this.username = username;
}



}