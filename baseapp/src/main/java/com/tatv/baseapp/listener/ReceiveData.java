package com.tatv.baseapp.listener;

/**
 * Created by tatv on 10/10/2022.
 */
public interface ReceiveData {
    <T> void onReceiveData(T data);
}
