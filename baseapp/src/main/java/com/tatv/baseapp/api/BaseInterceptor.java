package com.tatv.baseapp.api;
import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.tatv.baseapp.data.model.auth.AuthResponse;
import com.tatv.baseapp.data.shared.BaseSharedPreference;
import com.tatv.baseapp.utils.gzip.GZIPUtils;
import com.tatv.baseapp.utils.log.LogUtils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by tatv on 07/03/2023.
 */
public class BaseInterceptor implements Interceptor {
    public static final String TAG = "RetryInterceptor";

    private final String baseUrl;
    private final String appVersion;
    private final String buildType;
    private final BaseSharedPreference preference;

    public BaseInterceptor(Context context, String baseUrl) {
        this.baseUrl = baseUrl;
        this.appVersion = "";
        this.buildType = "";
        preference = new BaseSharedPreference(context);
    }

    public BaseInterceptor(Context context, String baseUrl, String appVersion, String buildType) {
        this.baseUrl = baseUrl;
        this.appVersion = appVersion;
        this.buildType = buildType;
        preference = new BaseSharedPreference(context);
    }

    @NonNull
    @Override
    public Response intercept(Chain chain) throws IOException {
        // try the request
        Response response = chain.proceed(getRequest(chain));
        if(response.code() == 200){
            byte[] data = response.body().bytes();
            if(GZIPUtils.isGzip(response.headers())){
                data = GZIPUtils.uncompress(data);
            }

            return response.newBuilder().body(ResponseBody.create(response.body().contentType(), data)).build();
        }
        if(response.code() == 401){
            // refresh token
            if(refreshToken()){
                return chain.proceed(getRequest(chain));
            }
        }else{
            int tryCount = 0;
            while (!response.isSuccessful() && tryCount < 3 && response.code() >= 500) {
                LogUtils.e(TAG, String.format("Request is not successful, code: %s, count: %s", response.code(), tryCount));
                tryCount++;
                // retry the request
                response.close();
                response = chain.proceed(getRequest(chain));
            }
        }
        return response;
    }

    /**
     * Create Request with token, language
     * */
    private Request getRequest(Chain chain){
        return chain.request().newBuilder()
                .addHeader("Authorization", "Bearer " + preference.getToken())
                .addHeader("Accept-Encoding", "gzip, deflate")
                .addHeader("accept-language", preference.getLanguage())
                .addHeader("version", appVersion)
                .addHeader("buildtype", buildType)
                .build();
    }


    /**
     * Call API refresh token
     * */
    public boolean refreshToken()
            throws IOException {
        Log.d(TAG, "refreshToken");
        URL refreshUrl = new URL(baseUrl + WSConfig.Api.REFRESH_TOKEN);
        HttpURLConnection urlConnection = (HttpURLConnection) refreshUrl.openConnection();
        urlConnection.setDoInput(true);
        urlConnection.setRequestMethod("POST");
        urlConnection.setUseCaches(false);

        urlConnection.setDoOutput(true);
        DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream());
        wr.writeBytes("refreshToken=" + preference.getRefreshToken());
        wr.flush();
        wr.close();

        int responseCode = urlConnection.getResponseCode();

        if (responseCode == 200) {
            BufferedReader in =
                    new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // this gson part is optional , you can read response directly from Json too
            Gson gson = new Gson();
            AuthResponse authResponse =
                    gson.fromJson(response.toString(), AuthResponse.class);
            Log.d(TAG, "refresh token success: " + authResponse.toString());
            // handle new token ...
            preference.setToken(authResponse.getData().get(0).getAccessToken());
            preference.setRefreshToken(authResponse.getData().get(0).getRefreshToken());
            preference.setSubId(authResponse.getData().get(0).getSub());
            preference.setUserId(authResponse.getData().get(0).getUserId());
            return true;
        } else {
            LogUtils.e(TAG, "cannot refresh token");
            return false;
        }
    }
}
