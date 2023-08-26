package com.tatv.baseapp.utils.map;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import com.tatv.baseapp.utils.system.AppUtils;

public class GoogleMapUtils {

    /**
     * add to manifest:
     * <queries>
     *     <intent>
     *         <action android:name="android.intent.action.MAIN" />
     *     </intent>
     * </queries>
     * */
    public boolean isInstalled(Context context){
        return AppUtils.isPackageInstalled(context, getPackageName());
    }

    public String getPackageName(){
        return "com.google.android.apps.maps";
    }

    @SuppressLint("DefaultLocale")
    public void sendMarker(Context context, double lat, double lng, String label){
        if(isInstalled(context)){
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("geo:%f, %f?q=%f, %f(%s)", lat, lng, lat, lng, label))).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    public void open(Context context){
        if(isInstalled(context)){
            context.startActivity(context.getPackageManager().getLaunchIntentForPackage(getPackageName()).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }
}
