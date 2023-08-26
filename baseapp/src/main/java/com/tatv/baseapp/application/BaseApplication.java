package com.tatv.baseapp.application;

import android.app.Application;
import android.content.Context;

import com.tatv.baseapp.utils.device.DeviceUtils;

/**
 * Created by tatv on 18/8/2021.
 */
public class BaseApplication extends Application {

    public Context getContext(){
        return getApplicationContext();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        DeviceUtils.getInstance().init(getContext());
    }
}
