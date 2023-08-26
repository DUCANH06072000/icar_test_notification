package com.tatv.baseapp.utils.device;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.provider.Settings;

import java.util.Date;
import java.util.Random;

public class DeviceUtils {
    private static DeviceUtils instance;

    public static DeviceUtils getInstance(){
        if(instance == null){
            instance = new DeviceUtils();
        }
        return instance;
    }

    private Context context;

    public void init(Context context){
        this.context = context;
    }

    /**
     * Trả về AppId
     * */
    @SuppressLint("HardwareIds")
    public String getAppId(){
        return Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    /**
     * Trả về appId random
     * @param appPrefix Tiền tố ứng dụng
     * */
    public String getAppIdRandom(String appPrefix) {
        Date currentDate = new Date();
        long currentTime = currentDate.getTime();

        final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        int randomInt = random.nextInt(alphabet.length());
        int randomInt1 = random.nextInt(999999999);
        char[] alphabetArr = alphabet.toCharArray();
        return  appPrefix + alphabetArr[randomInt] + randomInt + randomInt1 + currentTime;
    }

    /**
     * Trả về appId random
     * */
    public String getAppIdRandom() {
        Date currentDate = new Date();
        long currentTime = currentDate.getTime();

        final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        Random random = new Random();
        int randomInt = random.nextInt(alphabet.length());
        int randomInt1 = random.nextInt(999999999);
        char[] alphabetArr = alphabet.toCharArray();
        return  "" + alphabetArr[randomInt] + randomInt + randomInt1 + currentTime;
    }

    /**
     * Trả về Device Model (Device Name)
     * */
    public String getModel(){
        return Build.MODEL;
    }
}
