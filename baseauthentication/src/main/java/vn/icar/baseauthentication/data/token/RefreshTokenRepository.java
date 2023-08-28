package vn.icar.baseauthentication.data.token;


import android.content.Context;

import androidx.annotation.NonNull;

import com.tatv.baseapp.api.ApiService;
import com.tatv.baseapp.utils.log.LogUtils;
import com.tatv.baseapp.view.dialog.ErrorDialog;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.icar.baseauthentication.api.IAuthDataService;
import vn.icar.baseauthentication.data.model.QREvent;
import vn.icar.baseauthentication.data.shared.AuthSharedPreference;
import vn.icar.baseauthentication.data.utils.LogBotUtils;

public class RefreshTokenRepository {
    public static final String TAG="RefreshTokenRepository";
    TokenListener tokenListener;

    public RefreshTokenRepository(TokenListener tokenListener) {
        this.tokenListener = tokenListener;
    }

    public void refreshToken(RefreshToken refreshToken, Context context, String url) {
        AuthSharedPreference preference = new AuthSharedPreference(context);
        IAuthDataService service = ApiService.getService(context, url, IAuthDataService.class);
        Call<QREvent> call = service.refreshToken(refreshToken);
        call.enqueue(new Callback<QREvent>() {
            @Override
            public void onResponse(@NonNull Call<QREvent> call, @NonNull Response<QREvent> response) {
                if (response.code() == 200) {
                    QREvent qrEvent = response.body();
                    preference.setToken(qrEvent.getData().get(0).getAccessToken());
                    preference.setRefreshToken(qrEvent.getData().get(0).getRefreshToken());
                    preference.setSubId(qrEvent.getData().get(0).getSub());
                    preference.setUserId(qrEvent.getData().get(0).getUserId());
                    preference.setTimeTokenExp(qrEvent.getData().get(0).getExp());
                    tokenListener.token(true,response.code());
                } else {
                    LogUtils.e(TAG,response.code()+" RefreshToken: "+preference.getRefreshToken()+"//token: "+preference.getToken());
                    LogBotUtils.getInstance(context).addLog(TAG+"//"+response.code()+" RefreshToken: "+preference.getRefreshToken()+"//token: "+preference.getToken());
                    if (response.code() ==401){
                        String message="";
                        try {
                            if (response.errorBody()!=null){
                                JSONObject jObjError = new JSONObject(response.errorBody().string());
                                message = jObjError.getString("message");
                            }else if (response.body()!=null){
                                message=response.body().getMessage();
                            }
                        }catch (Exception e){

                        }
                        new ErrorDialog(context, new ErrorDialog.DialogErrorListener() {
                            @Override
                            public void onRetry(ErrorDialog dialog) {
                                tokenListener.token(false,response.code());
                            }

                            @Override
                            public void onCancel(ErrorDialog dialog) {

                            }
                        }).setTitle("Thông báo").setTextBottomRetry("Ok").setContent(message).showBottomCancel(false).show();
                    }else {
                        tokenListener.token(false,response.code());
                    }

                }
            }

            @Override
            public void onFailure(@NonNull Call<QREvent> call, @NonNull Throwable t) {
                LogUtils.i(TAG,"onFailure"+t.getMessage());
                new ErrorDialog(context, new ErrorDialog.DialogErrorListener() {
                    @Override
                    public void onRetry(ErrorDialog dialog) {
                        dialog.dismiss();
                        refreshToken(refreshToken,context,url);
                    }

                    @Override
                    public void onCancel(ErrorDialog dialog) {

                    }
                }).setContent("Không nhận dược phản hồi từ hệ thống")
                        .showBottomCancel(false)
                        .show();

            }
        });
    }
}
