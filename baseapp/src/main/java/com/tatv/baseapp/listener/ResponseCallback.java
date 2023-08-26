package com.tatv.baseapp.listener;

/**
 * Created by tatv on 06/12/2022.
 */
public interface ResponseCallback {
    <T> void onSuccess(T data);
    void onError(int code, String message);
}
