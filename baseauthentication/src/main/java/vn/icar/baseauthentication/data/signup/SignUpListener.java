package vn.icar.baseauthentication.data.signup;


import org.json.JSONObject;

import vn.icar.baseauthentication.data.model.DataServer;


public interface SignUpListener {
    void dataSignUp(DataServer dataServer, int status, String message, JSONObject data);

}
