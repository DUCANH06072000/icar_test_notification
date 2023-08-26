package vn.icar.baseauthentication.forgot_password;


import org.json.JSONObject;

import vn.icar.baseauthentication.data.model.DataServer;


public interface ForgotPasswordListener {
    void dataForgotPassword(DataServer dataServer, int status, String message, JSONObject data);

}
