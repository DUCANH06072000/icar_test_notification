package vn.icar.baseauthentication.data.shared;

import android.content.Context;
import android.content.SharedPreferences;

import com.tatv.baseapp.data.shared.BaseSharedPreference;

import vn.icar.baseauthentication.data.model.QREvent;

/**
 * Created by tatv on 10/10/2022.
 */
public class AuthSharedPreference extends BaseSharedPreference {

    public AuthSharedPreference(Context context) {
        super(context);
    }

    /**
     * get - set auth option
     * @return 0 là chưa lựa chọn, 1 là option thứ nhất, 2 là option thứ 2
     * */
    public static final int AUTH_OPTION_INITIAL = 1;
    public static final String PREF_AUTH_OPTION = "pref_auth_option";
    public static final String PREF_AUTH = "pref_auth";
    public static final String PREF_PHONE_NUMBER = "pref_phone_number";

    public int getAuthOption() {
        return getSharedPreferences().getInt(PREF_AUTH_OPTION, AUTH_OPTION_INITIAL);
    }

    public void setAuthOption(int value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(PREF_AUTH_OPTION, value);
        editor.apply();
    }

    public boolean isLogin(){
        return !getToken().equals("");
    }


    /**
     * get - set auth
     * */
    public void setAuth(QREvent qrEvent){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREF_AUTH, jsonUtils.getJsonFromObj(qrEvent));
        editor.apply();
    }

    public QREvent getAuth(){
        String result = getSharedPreferences().getString(PREF_AUTH, "");
        if(result.equals("")){
            return null;
        }else {
            return jsonUtils.getObjFromJson(result, QREvent.class);
        }
    }

    /**
     * get-set số điện thoại
     * */
    public void setPhoneNumber(String phoneNumber){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREF_PHONE_NUMBER,phoneNumber);
        editor.apply();
    }

    public String getPhoneNumber(){
        return getSharedPreferences().getString(PREF_PHONE_NUMBER, "0000000000");

    }
}
