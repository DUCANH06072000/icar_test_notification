package com.tatv.baseapp.utils.location;

import android.annotation.SuppressLint;
import android.location.GnssStatus;
import android.location.GpsSatellite;
import android.location.GpsStatus;
import android.location.LocationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.Iterator;

public class Satellite {
    private static final String TAG = "Satellite";

    private LocationManager mLocationManager;
    private GpsStatus.Listener gpsStatusListener;
    private GnssStatus.Callback gnssStatusCallback;
    private GpsStatus gpsStatus;
    private SatelliteListener listener;

    @SuppressLint("MissingPermission")
    public  Satellite(LocationManager mLocationManager){
        this.mLocationManager = mLocationManager;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            gnssStatusCallback = new GnssStatus.Callback() {
                @Override
                public void onSatelliteStatusChanged(@NonNull GnssStatus status) {
                    super.onSatelliteStatusChanged(status);
//                    Log.i(TAG, "onSatelliteStatusChanged ");
                    int inViewCount = 0;
                    int intUseCount = 0;
                    for (int i = 0; i < status.getSatelliteCount(); i++){
                        if(status.usedInFix(i)) intUseCount++;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                            if(status.getBasebandCn0DbHz(i) > 0){
                                inViewCount++;
                            }
                        }
                    }
                    if(inViewCount < intUseCount) inViewCount = intUseCount;
                    if(listener != null) listener.onSatellitesChanged(intUseCount, inViewCount);
                }
            };
        }else {
            gpsStatusListener = event -> {
                Log.i(TAG, "onGpsStatusListener ");
                gpsStatus = mLocationManager.getGpsStatus(gpsStatus);
                if(gpsStatus != null){
                    Iterable<GpsSatellite> satellites = gpsStatus.getSatellites();
                    Iterator<GpsSatellite> sat = satellites.iterator();
                    int inViewCount = 0;
                    int intUseCount = 0;
                    while (sat.hasNext()) {
                        GpsSatellite satellite = sat.next();
                        if(satellite.usedInFix()){
                            intUseCount++;
                        }
                        inViewCount++;
                    }
                    if(listener != null) listener.onSatellitesChanged(intUseCount, inViewCount);
                }
            };
        }
    }

    @SuppressLint("MissingPermission")
    public void registerSatelliteListener(SatelliteListener listener){
        Log.i(TAG, "registerSatelliteListener ");
        this.listener = listener;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            mLocationManager.registerGnssStatusCallback(gnssStatusCallback, null);
        }else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            mLocationManager.registerGnssStatusCallback(gnssStatusCallback);
        }else {
            mLocationManager.addGpsStatusListener(gpsStatusListener);
        }
    }

    public void unregisterSatelliteListener(){
        Log.i(TAG, "unregisterSatelliteListener ");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            mLocationManager.unregisterGnssStatusCallback(gnssStatusCallback);
        }
    }

    public interface SatelliteListener {
        void onSatellitesChanged(int inUse, int inView);
    }
}
