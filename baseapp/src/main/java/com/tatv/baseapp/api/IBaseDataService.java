package com.tatv.baseapp.api;


import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IBaseDataService {
    @Multipart
    @POST("data/upload")
    Call<Object> uploadLog(@Header("account") String account, @Header("appId") String appId, @Header("appName") String appName, @Part MultipartBody.Part file);
}