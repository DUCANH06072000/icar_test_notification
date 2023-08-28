package com.tatv.baseapp.data.shared;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.tatv.baseapp.utils.json.JsonUtils;

/**
 * Created by tatv on 10/10/2022.
 */
public class BaseSharedPreference {

    protected Context context;
    protected JsonUtils jsonUtils;

    public BaseSharedPreference(Context context) {
        this.context = context;
        jsonUtils = new JsonUtils();
    }

    /**
     * Trả về thể hiện của shared preference
     */
    public SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

    /**
     * Xóa toàn bộ dữ liệu
     */
    public static final String PREF_APP_ID = "pref_app_id";

    public void clear() {
        //đọc ra appId và mac
        String appId = getSharedPreferences().getString(PREF_APP_ID, "");
        //xóa toàn bộ dữ liệu
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.clear();
        editor.apply();
        //lưu lại appId và mac
        editor.putString(PREF_APP_ID, appId);
        editor.apply();
    }


    /**
     * get - set ngôn ngữ
     */
    public static final String PREF_LANGUAGE = "pref_language";

    public String getLanguage() {
        return getSharedPreferences().getString(PREF_LANGUAGE, "vi");
    }

    public void setLanguage(String language) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREF_LANGUAGE, language);
        editor.apply();
    }


    /**
     * get - set token
     */
    public static final String PREF_TOKEN = "pref_token";

    public String getToken() {
        return getSharedPreferences().getString(PREF_TOKEN, "");
    }

    public void setToken(String token) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREF_TOKEN, token);
        editor.apply();
    }

    /**
     * get - set refresh token
     */
    public static final String PREF_REFRESH_TOKEN = "pref_refresh_token";

    public String getRefreshToken() {
        return getSharedPreferences().getString(PREF_REFRESH_TOKEN, "");
    }

    public void setRefreshToken(String token) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREF_REFRESH_TOKEN, token);
        editor.apply();
    }

    /**
     * get - set subId(userId cũ)
     */
    public static final String PREF_SUB_ID = "pref_sub_id";

    public String getSubId() {
        return getSharedPreferences().getString(PREF_SUB_ID, "");
    }

    public void setSubId(String token) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREF_SUB_ID, token);
        editor.apply();
    }

    /**
     * get - set userId
     */
    public static final String PREF_USER_ID = "pref_user_id";

    public String getUserId() {
        return getSharedPreferences().getString(PREF_USER_ID, "");
    }

    public void setUserId(String token) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(PREF_USER_ID, token);
        editor.apply();
    }

    /**
     * get - set the last time to send report
     */
    public static final String PREF_LAST_TIME_REPORT = "pref_last_time_report";

    public long getLastTimeReport() {
        return getSharedPreferences().getLong(PREF_LAST_TIME_REPORT, 0);
    }

    public void setLastTimeReport(long timestamp) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(PREF_LAST_TIME_REPORT, timestamp);
        editor.apply();
    }

    /**
     * get - set time token expiry
     */
    public static final String PREF_TIME_TOKEN_EXP = "pref_time_token_exp";

    public long getTimeTokenExp() {
        return getSharedPreferences().getLong(PREF_TIME_TOKEN_EXP, 0);
    }

    public void setTimeTokenExp(long timeExp) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putLong(PREF_TIME_TOKEN_EXP, timeExp);
        editor.apply();
    }


    /**
     * @param type : 0 - Bar, 1 - Psi, 2 - Kpa
     */
    public static final String KEY_TIRE_PRESS_UNIT = "pref_tire_press_unit";

    public void setPressUnit(int type) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(KEY_TIRE_PRESS_UNIT, type);
        editor.apply();
    }

    public int getPressUnit() {
        return getSharedPreferences().getInt(KEY_TIRE_PRESS_UNIT, 0);
    }


    /**
     * @param type : 0 - °C, 1 - °F
     */
    public static final String KEY_TIRE_TEMP_UNIT = "pref_tire_temp_unit";

    public void setTempUnit(int type) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(KEY_TIRE_TEMP_UNIT, type);
        editor.apply();
    }

    public int getTempUnit() {
        return getSharedPreferences().getInt(KEY_TIRE_TEMP_UNIT, 0);
    }


    /**
     * get - set windows overlay
     */
    public static final String KEY_OVERLAY = "pref_overlay";

    public void setOverlay(boolean enabled) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(KEY_OVERLAY, enabled);
        editor.apply();
    }

    /**
     * get - set sound alert
     */
    public static final String KEY_SOUND_ALERT = "pref_sound_alert";

    public boolean getOverlay() {
        return getSharedPreferences().getBoolean(KEY_OVERLAY, false);
    }

    public void setSoundAlert(boolean enabled) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(KEY_SOUND_ALERT, enabled);
        editor.apply();
    }

    /**
     * get - set vibration alert
     */
    public static final String KEY_VIBRATION_ALERT = "pref_vibration_alert";

    public boolean getSoundAlert() {
        return getSharedPreferences().getBoolean(KEY_SOUND_ALERT, false);
    }


    public void setVibrationAlert(boolean enabled) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putBoolean(KEY_VIBRATION_ALERT, enabled);
        editor.apply();
    }

    public boolean getVibrationAlert() {
        return getSharedPreferences().getBoolean(KEY_VIBRATION_ALERT, false);
    }


    /**
     * get - set tire press max
     */
    public static final String KEY_TIRE_PRESS_MAX = "pref_tire_press_max_v2";

    public void setPressMax(float value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putFloat(KEY_TIRE_PRESS_MAX, value);
        editor.apply();
    }

    public float getPressMax() {
        return getSharedPreferences().getFloat(KEY_TIRE_PRESS_MAX, 3.2f);
    }


    /**
     * get - set tire press min
     */
    public static final String KEY_TIRE_PRESS_MIN = "pref_tire_press_min_v2";

    public void setPressMin(float value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putFloat(KEY_TIRE_PRESS_MIN, value);
        editor.apply();
    }

    public float getPressMin() {
        return getSharedPreferences().getFloat(KEY_TIRE_PRESS_MIN, 1.8f);
    }

    /**
     * get - set tire temp max
     */
    public static final String KEY_TIRE_TEMP_MAX = "pref_tire_temp_max_v2";

    public void setTempMax(int value) {
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putInt(KEY_TIRE_TEMP_MAX, value);
        editor.apply();
    }

    public int getTempMax() {
        return getSharedPreferences().getInt(KEY_TIRE_TEMP_MAX, 70);
    }

    /**
     * get - set thiết bị bluetooth
     * */
    public static final String KEY_DEVICE_BLUETOOTH = "pref_device_bluetooth";
    public void setDeviceBluetooth(String device){
        SharedPreferences.Editor editor = getSharedPreferences().edit();
        editor.putString(KEY_DEVICE_BLUETOOTH, device);
        editor.apply();
    }

    public String getDeviceBluetooth(){
        return getSharedPreferences().getString(KEY_DEVICE_BLUETOOTH, "");
    }
}
