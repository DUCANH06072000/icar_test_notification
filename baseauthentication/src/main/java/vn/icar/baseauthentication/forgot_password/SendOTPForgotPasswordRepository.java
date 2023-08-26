package vn.icar.baseauthentication.forgot_password;



import android.app.Activity;
import android.content.Context;

import com.tatv.baseapp.api.ApiService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.icar.baseauthentication.api.IAuthDataService;
import vn.icar.baseauthentication.data.model.DataServer;

public class SendOTPForgotPasswordRepository {
    OTPForgotPasswordListener OTPListener;
    public SendOTPForgotPasswordRepository(OTPForgotPasswordListener OTPListener){
        this.OTPListener = OTPListener;
    }
    public  void  sendOTPForgotPassword(BodySendOTPForgotPassword bodySendOTP, Context context,String url){
        IAuthDataService dataService = ApiService.getService(context, url, IAuthDataService.class);
        Call<DataServer> call = dataService.sendOPTForgotPassword(bodySendOTP);
        call.enqueue(new Callback<DataServer>() {
            @Override
            public void onResponse(Call<DataServer> call, Response<DataServer> response) {
                if (response.code()==200){
                    OTPListener.dataSendOTPForgotPassword(response.body(),response.code(),"");
                }else {
                    try {
                        String message="";
                        if (response.errorBody()!=null){
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            message = jObjError.getString("message");
                        }else if (response.body()!=null){
                            message=response.body().getMessage();
                        }
                        OTPListener.dataSendOTPForgotPassword(null,response.code(),message);

                    }catch (Exception e){

                    }


                }

            }

            @Override
            public void onFailure(Call<DataServer> call, Throwable t) {
                OTPListener.dataSendOTPForgotPassword(null,600,"Không nhận dược phản hồi từ hệ thống");
            }
        });
    }
}
