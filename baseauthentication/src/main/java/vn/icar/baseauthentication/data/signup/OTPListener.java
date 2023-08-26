package vn.icar.baseauthentication.data.signup;


import vn.icar.baseauthentication.data.model.DataServer;

public interface OTPListener {
    void dataSendOTP(DataServer dataServer, int status, String message);
    void dataVerifyOTP(DataServer dataServer, int status, String message);
}
