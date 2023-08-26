package com.tatv.baseapp.utils.log;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by tatv on 05/12/2022.
 */
public class Log {
    private static SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS", Locale.getDefault());
    private String time;
    private String severity;
    private String tag;
    private String data;
    private String message;

    public Log() {}

    public Log(String severity, String tag, String data, String message) {
        this.time = timeFormat.format(new Date());
        this.severity = severity;
        this.tag = tag;
        this.data = data;
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public Log setTime(String time) {
        this.time = time;
        return this;
    }

    public String getSeverity() {
        return severity;
    }

    public Log setSeverity(String severity) {
        this.severity = severity;
        return this;
    }

    public String getTag() {
        return tag;
    }

    public Log setTag(String tag) {
        this.tag = tag;
        return this;
    }

    public String getData() {
        return data;
    }

    public Log setData(String data) {
        this.data = data;
        return this;
    }

    public String getMessage() {
        return message;
    }

    public Log setMessage(String message) {
        this.message = message;
        return this;
    }

    @Override
    public String toString() {
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("time", time);
            jsonObject.put("severity", severity);
            jsonObject.put("tag", tag);
            jsonObject.put("data", data);
            jsonObject.put("msg", message);
        } catch (JSONException e) {
            LogUtils.e("Log", e.toString());
        }
        return jsonObject.toString();
    }
}
