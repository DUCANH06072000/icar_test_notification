package vn.icar.baseauthentication.data.logout;


import android.content.Context;

import com.tatv.baseapp.api.ApiService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.icar.baseauthentication.api.IAuthDataService;
import vn.icar.baseauthentication.data.login.Login;
import vn.icar.baseauthentication.data.login.LoginListener;


public class LogoutAndLoginRepository {
    LoginListener loginListener;

    public LogoutAndLoginRepository(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void GetLogoutAndLogin(LogoutAndLogin logoutAndLogin, Context context, String url) {
        IAuthDataService dataService = ApiService.getService(context, url, IAuthDataService.class);
        Call<Login> call = dataService.getLogoutAndLogin(logoutAndLogin);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.code() == 200) {
                    loginListener.dataLogin(response.body(), response.code(), "", null);
                } else if (response.code() == 403) {
                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        loginListener.dataLogin(null, response.code(), "", jObjError);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    try {
                        String message = "";
                        if (response.errorBody() != null) {
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            message = jObjError.getString("message");
                        } else if (response.body() != null) {
                            message = response.body().getMessage();
                        }
                        loginListener.dataLogin(null, response.code(), message, null);

                    } catch (Exception e) {

                    }


                }

            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                loginListener.dataLogin(null, 600, "Không nhận dược phản hồi từ hệ thống", null);
            }
        });
    }
}
