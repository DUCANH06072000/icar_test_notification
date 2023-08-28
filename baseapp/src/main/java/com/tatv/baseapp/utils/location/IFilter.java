package com.tatv.baseapp.utils.location;

import android.util.Log;

/**
 * Created by tatv on 03/01/2023.
 */
public class IFilter {
    private static IFilter instance;

    public static IFilter getInstance(){
        if(instance == null){
            instance = new IFilter();
        }

        return instance;
    }

    /**
     * Location cache
     * */
    private ILocation lastLocation;


    /**
     * Filter location
     * */
    public ILocation getLocation(ILocation location){
        if(lastLocation == null){
            lastLocation = location;
        }else {
            if(lastLocation.distanceTo(location) > 1 && location.getTime() - lastLocation.getTime() >= 1000){
                if(location.getBearing() == 0 && location.getSpeed() == 0){
                    double bearing = lastLocation.bearingTo(location);
                    double distance = lastLocation.distanceTo(location);
                    long time = (location.getTime() - lastLocation.getTime())/1000;
                    float speed = (float) (distance/time);
                    if(speed > 50){
                        speed = 50;
                    }
                    location.setSpeed(speed);
                    location.setBearing(bearing);
                }else {
                    location.setSpeed(SpeedFilter.getInstance().filter(location.getSpeed()));
                }
                lastLocation = location;
            }else {
                if(location.getBearing() == 0 && location.getSpeed() == 0){
                    location.setBearing(lastLocation.getBearing());
                }else {
                    location.setSpeed(SpeedFilter.getInstance().filter(location.getSpeed()));
                }
                return location;
            }
        }
        return lastLocation;
    }

}
