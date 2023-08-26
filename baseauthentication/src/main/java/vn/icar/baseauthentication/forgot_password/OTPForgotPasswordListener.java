package vn.icar.baseauthentication.forgot_password;

import vn.icar.baseauthentication.data.model.DataServer;

public interface OTPForgotPasswordListener {
    void dataSendOTPForgotPassword(DataServer dataServer, int status, String message);
    void dataVerifyOTPForgotPassword(DataServer dataServer, int status, String message);
}
