package com.tatv.baseapp.utils.location;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.tatv.baseapp.utils.log.LogUtils;

public class Locate implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, Satellite.SatelliteListener {
    private static final String TAG = "Locate";
    @SuppressLint("StaticFieldLeak")
    private static Locate instance;

    public static Locate getInstance(Context context) {
        if (instance == null) instance = new Locate(context);
        return instance;
    }

    public static final int LOCATION_INTERVAL = 1000; //thời gian nhỏ nhất
    public static final float LOCATION_DISTANCE = 0; //khoảng cách ngắn nhất

    private LocationManager mLocationManager;
    private LocateListener listener;
    private Satellite satellite;
    private Context context;
    private int inUse, inView;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;


    public Locate(Context context) {
        this.context = context;
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        satellite = new Satellite(mLocationManager);
    }

    /**
     * Đăng ký lắng nghe sự kiện thay đổi vị trí
     * */
    @SuppressLint("MissingPermission")
    public Locate registerLocate(LocateListener listener) {
        if(this.listener == null){
            addGpsRequestLocationUpdates();
            satellite.registerSatelliteListener(this);
        }
        this.listener = listener;
        return this;
    }

    /**
     * Đăng ký lắng nghe sự kiện thay đổi vị trí từ google service
     * */
    @SuppressLint("MissingPermission")
    public Locate registerFuelLocate(LocateListener listener) {
        if(this.listener == null){
            addGoogleServiceRequestLocationUpdates();
            satellite.registerSatelliteListener(this);
        }
        this.listener = listener;
        return this;
    }

    /**
     * Thêm lắng nghe sự kiện thay đổi từ gps
     * */
    @SuppressLint("MissingPermission")
    private Locate addGpsRequestLocationUpdates(){
        if(isGpsProviderEnabled()){
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, this);
        }
        return this;
    }

    /**
     * Thêm lắng nghe sự kiện thay đổi từ network
     * */
    @SuppressLint("MissingPermission")
    public Locate addNetworkRequestLocationUpdates(){
        if(isNetworkProviderEnabled()){
            mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, LOCATION_INTERVAL, LOCATION_DISTANCE, this);
        }
        return this;
    }

    /**
     * Kiểm tra đã bật gps provider chưa
     * */
    public boolean isGpsProviderEnabled(){
        return mLocationManager != null && mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }


    /**
     * Kiểm tra đã bật network provider chưa
     * */
    public boolean isNetworkProviderEnabled(){
        return mLocationManager != null && mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    /**
     * Thêm lắng nghe sự kiện thay đổi từ google service
     * */
    @SuppressLint("MissingPermission")
    public Locate addGoogleServiceRequestLocationUpdates(){
        if (mGoogleApiClient == null) {
            try{
                mGoogleApiClient = new GoogleApiClient.Builder(context)
                        .addConnectionCallbacks(this)
                        .addOnConnectionFailedListener(this)
                        .addApi(LocationServices.API)
                        .build();
                mGoogleApiClient.connect();
            }catch (Exception e){
                LogUtils.e(TAG, e.toString());
            }
        }
        return this;
    }

    /**
     * Hủy lắng nghe thay đổi vị trí
     * */
    public void unregisterLocate(){
        if(mLocationManager != null){
            mLocationManager.removeUpdates(this);
        }
        if(satellite != null){
            satellite.unregisterSatelliteListener();
        }
        if(mGoogleApiClient != null && mGoogleApiClient.isConnected()){
            mGoogleApiClient.disconnect();
        }
        listener = null;
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onLocationChanged(@NonNull Location location) {
        if(listener != null){
            listener.onLocationChanged(new ILocation(location).setInUse(inUse).setInView(inView).filter());
        }
    }


    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        LocationListener.super.onStatusChanged(provider, status, extras);
    }

    @Override
    public void onProviderEnabled(@NonNull String provider) {
        LocationListener.super.onProviderEnabled(provider);
        LogUtils.i(TAG, "onProviderEnabled: " + provider);
        if(listener != null){
            listener.onProviderChanged(provider, true);
        }
    }

    @Override
    public void onProviderDisabled(@NonNull String provider) {
        LocationListener.super.onProviderDisabled(provider);
        LogUtils.i(TAG, "onProviderDisabled: " + provider);
        if(provider.equals(LocationManager.GPS_PROVIDER)){
            onSatellitesChanged(0,0);
        }
        if(listener != null){
            listener.onProviderChanged(provider, false);
        }
    }


    @Override
    public void onSatellitesChanged(int inUse, int inView) {
        this.inUse = inUse;
        this.inView = inView;
        if(listener != null){
            listener.onSatellitesChanged(inUse, inView);
        }
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onConnected(@Nullable Bundle bundle) {
        LogUtils.i(TAG, "onConnected");
        if(mLocationRequest == null){
            try{
                mLocationRequest = new LocationRequest();
                mLocationRequest.setInterval(1000);
                mLocationRequest.setFastestInterval(1000);
                mLocationRequest.setSmallestDisplacement(1);
                mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this::onLocationChanged);
            }catch (Exception e){
                LogUtils.e(TAG, e.getMessage());
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "onConnectionSuspended: " + i);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.i(TAG, "onConnectionFailed, connectionResult: " + connectionResult.isSuccess());
    }


    public interface LocateListener{
        void onLocationChanged(ILocation location);
        void onSatellitesChanged(int inUse, int inView);
        void onProviderChanged(String provider, boolean enabled);
    }
}
