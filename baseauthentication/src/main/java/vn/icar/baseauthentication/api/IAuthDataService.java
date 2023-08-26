package vn.icar.baseauthentication.api;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import vn.icar.baseauthentication.data.login.BodyLogin;
import vn.icar.baseauthentication.data.login.Login;
import vn.icar.baseauthentication.data.logout.LogoutAndLogin;
import vn.icar.baseauthentication.data.model.DataServer;
import vn.icar.baseauthentication.data.token.RefreshToken;
import vn.icar.baseauthentication.data.model.QREvent;
import vn.icar.baseauthentication.data.signup.BodySendOTP;
import vn.icar.baseauthentication.data.signup.BodySignUp;
import vn.icar.baseauthentication.data.signup.BodyverifyOTP;
import vn.icar.baseauthentication.forgot_password.BodyForgotPassword;
import vn.icar.baseauthentication.forgot_password.BodySendOTPForgotPassword;
import vn.icar.baseauthentication.forgot_password.BodyverifyOTPForgotPassword;

/**
 * Created by tatv on 11/10/2022.
 */
public interface IAuthDataService {
    @POST("auth/refresh-token")
    Call<QREvent> refreshToken(@Body RefreshToken refreshToken);

    @GET("auth/logout")
    Call<QREvent> logout();

    @POST("auth/trial")
    Call<Login> getTrial(@Body BodyLogin login);

    @POST("auth/login")
    Call<Login> getLogin(@Body BodyLogin login);

    @POST("auth/logout-app-id-then-login")
    Call<Login> getLogoutAndLogin(@Body LogoutAndLogin logoutAndLogin);

    @POST("auth/register/send-otp")
    Call<DataServer> sendOPTSignup(@Body BodySendOTP sendOTP);

    @POST("auth/register/verify-otp")
    Call<DataServer> verifyOPTSignup(@Body BodyverifyOTP bodyverifyOTP);

    @POST("auth/register/create-customer")
    Call<DataServer> getSignUp(@Body BodySignUp bodySignUp);

    @POST("auth/lost-password/send-otp")
    Call<DataServer> sendOPTForgotPassword(@Body BodySendOTPForgotPassword sendOTP);

    @POST("auth/lost-password/verify-otp")
    Call<DataServer> verifyOPTForgotPassword(@Body BodyverifyOTPForgotPassword bodyverifyOTPForgotPassword);

    @POST("auth/lost-password/create-new-password")
    Call<DataServer> getForgotPassword(@Body BodyForgotPassword bodyForgotPassword);
}
