package com.tatv.baseapp.utils.location;

import android.location.Location;

import com.tatv.baseapp.utils.log.LogUtils;
import com.tatv.baseapp.utils.speed.SpeedUtils;

import org.json.JSONException;
import org.json.JSONObject;

public class ILocation extends Location {
    public int DISTANCE_CLOSE_TOGETHER = 50;
    public int SAME_LOCATION_ANGEL_EPSILON = 10;
    public int PASSED_A_LOCATION_ANGEL_EPSILON = 50;

    private Location location;
    private long time;
    private float compass;
    private double altitude;
    private int inUse;
    private int inView;


    public ILocation(Location location) {
        super(location);
        init(location);
    }

    public ILocation(double latitude, double longitude, float bearing) {
        super(new Location(""));
        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setBearing(bearing);
        init(location);
    }

    /**
     * Khởi tạo
     * */
    private void init(Location location){
        this.location = location;
        altitude = location.hasAltitude() ? location.getAltitude() : -999;
        time = System.currentTimeMillis();
    }

    public ILocation setBearing(double bearing) {
        super.setBearing((float) bearing);
        return this;
    }

    @Override
    public void setBearing(float bearing) {
        super.setBearing(bearing);
    }

    @Override
    public double getAltitude() {
        return altitude;
    }

    @Override
    public void setAltitude(double altitude) {
        this.altitude = altitude;
    }

    @Override
    public long getTime() {
        return time;
    }

    @Override
    public void setTime(long time) {
        this.time = time;
    }

    public float getCompass() {
        return compass;
    }

    public void setCompass(float compass) {
        this.compass = compass;
    }

    public int getInUse() {
        return inUse;
    }

    public ILocation setInUse(int inUse) {
        this.inUse = inUse;
        return this;
    }

    public int getInView() {
        return inView;
    }

    public ILocation setInView(int inView) {
        this.inView = inView;
        return this;
    }

    public ILocation filter(){
        setSpeed(new SpeedUtils().filter(getSpeed()));
        return this;
    }


    public Location getLocation(){
        return location;
    }






    /**
     * Kiểm tra dã đi qua một điểm
     * @param location : là location của điểm cần kiểm tra
     * */
    public boolean isLocationPassedALocation(Location location){
        double bearing = location.bearingTo(this);
        if (bearing < 0) {
            bearing += 360;
        }

        if (bearing >= 360) {
            bearing -= 360;
        }
        double temp = Math.abs(location.getBearing() - bearing);

        return temp <= PASSED_A_LOCATION_ANGEL_EPSILON || temp >= 360 - PASSED_A_LOCATION_ANGEL_EPSILON && distanceTo(location) < DISTANCE_CLOSE_TOGETHER;
    }

    /**
     * Kiểm tra 2 điểm có gần nhau hay không
     * @param location : là location của điểm cần kiểm tra
     * */
    public boolean isLocationNearEachOther(Location location){
        return bearingTo(location) < DISTANCE_CLOSE_TOGETHER;
    }

    /**
     * Kiểm tra 2 điểm ngược hướng nhau
     * @param location : là location của điểm cần kiểm tra
     */
    public boolean isLocationOppositeDirections(Location location){
        double temp = Math.abs(getBearing() - location.getBearing());
        return temp > 180 - SAME_LOCATION_ANGEL_EPSILON || temp < 180 + SAME_LOCATION_ANGEL_EPSILON;
    }


    /**
     * Kiểm tra 2 location có cùng hướng hay không
     * @param location : là location của điểm cần kiểm tra
     */
    public boolean isLocationSameDirection(Location location){
        //code here
        double temp = Math.abs(getBearing() - location.getBearing());
        return temp < SAME_LOCATION_ANGEL_EPSILON || temp > 360 - SAME_LOCATION_ANGEL_EPSILON;
    }


    /**
     * Kiểm tra 2 location có cùng hướng hay không
     * @param location : là location của điểm cần kiểm tra
     * @param angle : là số độ chênh lệch cần kiểm tra
     */
    public boolean isLocationSameDirection(Location location, double angle){
        //code here
        double temp = Math.abs(getBearing() - location.getBearing());
        return temp < angle || temp > 360 - angle;
    }


    @Override
    public String toString() {
        JSONObject jsonObject= new JSONObject();
        try {
            jsonObject.put("time", time);
            jsonObject.put("provider", getProvider());
            jsonObject.put("latitude", getLatitude());
            jsonObject.put("longitude", getLongitude());
            jsonObject.put("bearing", getBearing());
            jsonObject.put("speed", getSpeed());
            jsonObject.put("compass", compass);
            jsonObject.put("altitude", altitude);
            jsonObject.put("inUse", inUse);
            jsonObject.put("inView", inView);
        } catch (JSONException e) {
            LogUtils.e("ILocation", e.toString());
        }
        return jsonObject.toString();
    }
}
