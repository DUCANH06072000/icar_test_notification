package vn.icar.baseauthentication.data.signup;




import android.app.Activity;
import android.content.Context;

import com.tatv.baseapp.api.ApiService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.icar.baseauthentication.api.IAuthDataService;
import vn.icar.baseauthentication.data.model.DataServer;


public class VerifyOTPRepository {
    OTPListener OTPListener;
    public VerifyOTPRepository(OTPListener OTPListener){
        this.OTPListener = OTPListener;
    }
    public  void  verifyOTPSignup(BodyverifyOTP bodyverifyOTP, Context context,String url){
        IAuthDataService dataService = ApiService.getService(context, url, IAuthDataService.class);
        Call<DataServer> call = dataService.verifyOPTSignup(bodyverifyOTP);
        call.enqueue(new Callback<DataServer>() {
            @Override
            public void onResponse(Call<DataServer> call, Response<DataServer> response) {
                if (response.code()==200||response.code()==201){
                    OTPListener.dataVerifyOTP(response.body(),response.code(),"");
                } else {
                    try {
                        String message="";
                        if (response.errorBody()!=null){
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            message = jObjError.getString("message");
                        }else if (response.body()!=null){
                            message=response.body().getMessage();
                        }
                        OTPListener.dataVerifyOTP(null,response.code(),message);

                    }catch (Exception e){

                    }


                }

            }

            @Override
            public void onFailure(Call<DataServer> call, Throwable t) {
                OTPListener.dataVerifyOTP(null, 600, "Không nhận dược phản hồi từ hệ thống");
            }
        });
    }
}
