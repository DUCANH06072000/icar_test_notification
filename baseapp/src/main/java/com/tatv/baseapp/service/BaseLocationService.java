package com.tatv.baseapp.service;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;

import com.tatv.baseapp.listener.TLocationListener;


public abstract class BaseLocationService extends BaseService implements LocationListener, TLocationListener {
    protected LocationManager mLocationManager;

    @Override
    public void onCreate() {
        super.onCreate();
        initLocation();
    }

    private void initLocation(){
        mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {
        onLocationUpdate(location);
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {

    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {

    }



}
