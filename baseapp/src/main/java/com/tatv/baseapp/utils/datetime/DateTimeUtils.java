package com.tatv.baseapp.utils.datetime;

import java.text.SimpleDateFormat;
import java.util.Locale;

/**
 * Created by tatv on 12/10/2022.
 */
public class DateTimeUtils {
    public static final String TAG = "DateTimeUtils";

    public static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
    public static SimpleDateFormat DATETIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());


    /**
     * return timestamp second
     * */
    public static long getTimestamp(){
        return System.currentTimeMillis()/1000;
    }

    /**
     * return timestamp millisecond
     * */
    public static long getTimestampMillis(){
        return System.currentTimeMillis();
    }
}
