package vn.icar.baseauthentication.data.signup;



import android.content.Context;

import com.tatv.baseapp.api.ApiService;

import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.icar.baseauthentication.api.IAuthDataService;
import vn.icar.baseauthentication.data.model.DataServer;

public class SignUpRepository {
    SignUpListener signUpListener;
    public SignUpRepository(SignUpListener signUpListener){
        this.signUpListener = signUpListener;
    }
    public  void  getSignup(BodySignUp bodySignUp, Context context,String url){
        IAuthDataService dataService = ApiService.getService(context, url, IAuthDataService.class);
        Call<DataServer> call = dataService.getSignUp(bodySignUp);
        call.enqueue(new Callback<DataServer>() {
            @Override
            public void onResponse(Call<DataServer> call, Response<DataServer> response) {
                if (response.code()==200){
                    signUpListener.dataSignUp(response.body(),response.code(),"",null);
                }else if (response.code()==400){

                    try {
                        JSONObject jObjError = new JSONObject(response.errorBody().string());
                        String message = jObjError.getString("message");
                        signUpListener.dataSignUp(null,response.code(),message,jObjError);

                    }catch (Exception e){

                    }
                }else {
                    try {
                        String message="";
                        if (response.errorBody()!=null){
                            JSONObject jObjError = new JSONObject(response.errorBody().string());
                            message = jObjError.getString("message");
                        }else if (response.body()!=null){
                            message=response.body().getMessage();
                        }
                        signUpListener.dataSignUp(null,response.code(),message,null);

                    }catch (Exception e){

                    }


                }

            }

            @Override
            public void onFailure(Call<DataServer> call, Throwable t) {
                signUpListener.dataSignUp(null,600,"Không nhận dược phản hồi từ hệ thống",null);
            }
        });
    }
}
