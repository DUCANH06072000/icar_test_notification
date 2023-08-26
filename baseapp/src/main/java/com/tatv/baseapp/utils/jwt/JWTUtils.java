package com.tatv.baseapp.utils.jwt;

import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;

/**
 * Created by tatv on 12/10/2022.
 */
public class JWTUtils {
    private static String TAG = "JWTUtils";

    public static void decoded(String JWTEncoded) {
        try {
            String[] split = JWTEncoded.split("\\.");
            Log.e(TAG, "Header: " + getJson(split[0]));
            Log.e(TAG, "Body: " + getJson(split[1]));
        } catch (UnsupportedEncodingException e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    public static long getTokenExpired(String token){
        try {
            String[] split = token.split("\\.");
            JSONObject jsonObject = new JSONObject(getJson(split[1]));
            return jsonObject.getLong("exp");
        } catch (UnsupportedEncodingException | JSONException | ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }
}
