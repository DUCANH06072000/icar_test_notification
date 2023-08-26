package com.tatv.baseapp.utils.map;

import android.content.Context;
import android.content.Intent;

import com.tatv.baseapp.utils.system.AppUtils;

public class VietMapUtils {

    public boolean isInstalled(Context context){
        return AppUtils.isPackageInstalled(context, getPackageName());
    }

    public String getPackageName(){
        return "com.vietmap.S2OBU";
    }

    public void open(Context context){
        if(isInstalled(context)){
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(getPackageName()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
