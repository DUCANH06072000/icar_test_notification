package vn.icar.baseauthentication.data.login;

import org.json.JSONObject;

public interface LoginListener {
    void dataLogin(Login login, int status, String message, JSONObject jsonObject);
}
