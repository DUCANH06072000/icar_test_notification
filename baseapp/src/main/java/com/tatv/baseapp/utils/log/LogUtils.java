package com.tatv.baseapp.utils.log;

import android.util.Log;

public class LogUtils {

    /**
     * Log error
     * @param tag - String: Class
     * @param data - String: Nội dung raw
     * */
    public static void e(String tag, String data) {
        String message = new com.tatv.baseapp.utils.log.Log("E", tag, data, "").toString();
        Log.e(tag, message);
        LogFile.getInstance().addLog(message);
    }

    /**
     * Log error
     * @param tag - String: Class
     * @param data - String: Nội dung raw
     * @param msg - String: Nội dung cho người dùng đọc
     * */
    public static void e(String tag, String data, String msg) {
        String message = new com.tatv.baseapp.utils.log.Log("E", tag, data, msg).toString();
        Log.e(tag, message);
        LogFile.getInstance().addLog(message);
    }


    /**
     * Log warning
     * @param tag - String: Class
     * @param data - String: Nội dung raw
     * */
    public static void w(String tag, String data) {
        String message = new com.tatv.baseapp.utils.log.Log("W", tag, data, "").toString();
        Log.e(tag, message);
        LogFile.getInstance().addLog(message);
    }

    /**
     * Log warning
     * @param tag - String: Class
     * @param data - String: Nội dung raw
     * @param msg - String: Nội dung cho người dùng đọc
     * */
    public static void w(String tag, String data, String msg) {
        String message = new com.tatv.baseapp.utils.log.Log("W", tag, data, msg).toString();
        Log.e(tag, message);
        LogFile.getInstance().addLog(message);
    }


    /**
     * Log info
     * @param tag - String: Class
     * @param data - String: Nội dung raw
     * */
    public static void i(String tag, String data) {
        String message = new com.tatv.baseapp.utils.log.Log("I", tag, data, "").toString();
        Log.e(tag, message);
        LogFile.getInstance().addLog(message);
    }

    /**
     * Log info
     * @param tag - String: Class
     * @param data - String: Nội dung raw
     * @param msg - String: Nội dung cho người dùng đọc
     * */
    public static void i(String tag, String data, String msg) {
        String message = new com.tatv.baseapp.utils.log.Log("I", tag, data, msg).toString();
        Log.e(tag, message);
        LogFile.getInstance().addLog(message);
    }


    /**
     * Log debug
     * @param tag - String: Class
     * @param data - String: Nội dung raw
     * */
    public static void d(String tag, String data) {
        String message = new com.tatv.baseapp.utils.log.Log("D", tag, data, "").toString();
        Log.e(tag, message);
        LogFile.getInstance().addLog(message);
    }

    /**
     * Log debug
     * @param tag - String: Class
     * @param data - String: Nội dung raw
     * @param msg - String: Nội dung cho người dùng đọc
     * */
    public static void d(String tag, String data, String msg) {
        String message = new com.tatv.baseapp.utils.log.Log("D", tag, data, msg).toString();
        Log.e(tag, message);
        LogFile.getInstance().addLog(message);
    }

}