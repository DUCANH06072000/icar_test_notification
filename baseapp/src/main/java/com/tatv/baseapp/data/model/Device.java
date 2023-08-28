package com.tatv.baseapp.data.model;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class Device implements Parcelable {
    public Device(String deviceMac, String deviceName, String accessToken, String appId) {
        this.deviceMac = deviceMac;
        this.deviceName = deviceName;
        this.accessToken = accessToken;
        this.appId = appId;
    }

    @SerializedName("deviceMac")
    @Expose
    private String deviceMac;
    @SerializedName("deviceName")
    @Expose
    private String deviceName;
    @SerializedName("accessToken")
    @Expose
    private String accessToken;
    @SerializedName("appId")
    @Expose
    private String appId;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    protected Device(Parcel in) {
        deviceMac = in.readString();
        deviceName = in.readString();
        accessToken = in.readString();
    }

    public static final Creator<Device> CREATOR = new Creator<Device>() {
        @Override
        public Device createFromParcel(Parcel in) {
            return new Device(in);
        }

        @Override
        public Device[] newArray(int size) {
            return new Device[size];
        }
    };

    public String getDeviceMac() {
        return deviceMac;
    }

    public void setDeviceMac(String deviceMac) {
        this.deviceMac = deviceMac;
    }

    public String getDeviceName() {
        return deviceName == null || deviceName.equals("") ? "-/-" : deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(deviceMac);
        dest.writeString(deviceName);
        dest.writeString(accessToken);
    }

    @Override
    public String toString() {
        return "Device{" +
                "deviceMac='" + deviceMac + '\'' +
                ", deviceName='" + deviceName + '\'' +
                ", accessToken='" + accessToken + '\'' +
                ", appId='" + appId + '\'' +
                '}';
    }
}