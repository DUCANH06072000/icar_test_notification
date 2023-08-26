package com.tatv.baseapp.utils.network;

/**
 * Created by tatv on 16/11/2022.
 */
public interface ConnectivityListener {
    void onConnectivityChanged(boolean enabled);
    default void onPingChanged(long timeofping){

    }

    default void onNetworkTypeChanged(int type){

    }
}
