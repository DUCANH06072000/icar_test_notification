package com.tatv.baseapp.api;


import com.tatv.baseapp.data.model.BaseResponse;
import com.tatv.baseapp.data.model.auth.AuthRequest;
import com.tatv.baseapp.data.model.auth.AuthResponse;
import com.tatv.baseapp.data.model.auth.LogoutDeviceThenLoginRequest;
import com.tatv.baseapp.data.model.auth.RefreshToken;
import com.tatv.baseapp.data.model.auth.SendOtpRequest;
import com.tatv.baseapp.data.model.auth.VerifyOtpRequest;
import com.tatv.baseapp.data.model.auth.VerifyPasswordRequest;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface IBaseDataService {
    @Multipart
    @POST("data/upload")
    Call<Object> uploadLog(@Header("account") String account, @Header("appId") String appId, @Header("appName") String appName, @Part MultipartBody.Part file);

    @POST(WSConfig.Api.LOGIN)
    Call<AuthResponse> login(@Body AuthRequest loginRequest);

    @POST(WSConfig.Api.REFRESH_TOKEN)
    Call<AuthResponse> refreshToken(@Body RefreshToken refreshToken);

    @GET(WSConfig.Api.LOGOUT)
    Call<AuthResponse> logout();

    @POST(WSConfig.Api.LOGOUT_DEVICE_THEN_LOGIN)
    Call<AuthResponse> logoutDeviceThenLogin(@Body LogoutDeviceThenLoginRequest request);


    /**
     * Đăng ký
     * */
    @POST(WSConfig.Api.REGISTER_SEND_OTP)
    Call<BaseResponse<Object>> registerSendOtp(@Body SendOtpRequest sendOtpRequest);

    @POST(WSConfig.Api.REGISTER_VERITY_OTP)
    Call<BaseResponse<Object>> registerVerityOtp(@Body VerifyOtpRequest verifyOtpRequest);

    @POST(WSConfig.Api.REGISTER_CREATE_CUSTOMER)
    Call<AuthResponse> registerCreateCustomer(@Body VerifyPasswordRequest verifyPasswordRequest);


    /**
     * Quên mật khẩu
     * */
    @POST(WSConfig.Api.FORGOT_PASSWORD_SEND_OTP)
    Call<BaseResponse<Object>> forgotPasswordSendOtp(@Body SendOtpRequest sendOtpRequest);

    @POST(WSConfig.Api.FORGOT_PASSWORD_VERITY_OTP)
    Call<BaseResponse<Object>> forgotPasswordVerityOtp(@Body VerifyOtpRequest verifyOtpRequest);

    @POST(WSConfig.Api.FORGOT_PASSWORD_CREATE_NEW_PASSWORD)
    Call<AuthResponse> forgotPasswordCreateNewPassword(@Body VerifyPasswordRequest verifyPasswordRequest);

    @POST(WSConfig.Api.CHANGE_PASSWORD)
    Call<AuthResponse> changePassword(@Body VerifyPasswordRequest verifyPasswordRequest);
}