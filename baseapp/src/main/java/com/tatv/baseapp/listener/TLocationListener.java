package com.tatv.baseapp.listener;

import android.location.Location;

import androidx.annotation.NonNull;

public interface TLocationListener {
    void onLocationUpdate(Location location);
}
