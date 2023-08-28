package vn.icar.baseauthentication.data.login;


import android.content.Context;


import com.tatv.baseapp.api.ApiService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.icar.baseauthentication.api.IAuthDataService;


public class TrialRepository {
    LoginListener loginListener;

    public TrialRepository(LoginListener loginListener) {
        this.loginListener = loginListener;
    }

    public void GetTrial(BodyLogin bodyLogin, Context context, String url) {
        IAuthDataService dataService = ApiService.getService(context, url, IAuthDataService.class);
        Call<Login> call = dataService.getTrial(bodyLogin);
        call.enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                if (response.code() == 200) {
                    loginListener.dataLogin(response.body(), response.code(), "", null);
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
