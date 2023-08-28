
package vn.icar.baseauthentication.data.utils;


import android.annotation.SuppressLint;
import android.content.Context;
import android.provider.Settings;
import android.util.Log;

import com.tatv.baseapp.process.TaskRunner;
import com.tatv.baseapp.utils.log.LogUtils;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.Callable;

import vn.icar.baseauthentication.data.shared.AuthSharedPreference;


/**
 * Created by tatv on 28/04/2023.
 */
public class LogBotUtils {
    public static final String TAG = "LogBotUtils";
    private static LogBotUtils instance;
    private AuthSharedPreference preference;

    private Context context;

    public static LogBotUtils getInstance(Context context) {
        if (instance == null) {
            instance = new LogBotUtils(context);
        }
        return instance;
    }

    public LogBotUtils(Context context) {
        this.context = context;
        this.preference = new AuthSharedPreference(context);
    }

    /**
     * Call API refresh token
     */
    public void addLog(String message) {
        @SuppressLint("HardwareIds") String deviceId = Settings.Secure.getString(
                context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
        new TaskRunner().executeAsync(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                try {
                    JSONObject object = new JSONObject();
                    object.put("phone", preference.getPhoneNumber());
                    object.put("userId", preference.getUserId());
                    object.put("appId","icare."+deviceId);
                    object.put("message", message);

                    URL refreshUrl = new URL("https://www.larksuite.com/flow/api/trigger-webhook/399ff980f60cbe5e577923e35b8c67c2");
                    HttpURLConnection urlConnection = (HttpURLConnection) refreshUrl.openConnection();
                    urlConnection.setDoInput(true);
                    urlConnection.setRequestMethod("POST");
                    urlConnection.setUseCaches(false);
                    urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                    urlConnection.setRequestProperty("Accept", "application/json");

                    OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream(), StandardCharsets.UTF_8);
                    wr.write(object.toString());
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
                        Log.d(TAG, "add logging success: " + response.toString());
                        // handle new token ...
                    } else {
                        LogUtils.e(TAG, "cannot add logging");
                    }
                } catch (IOException e) {
                    LogUtils.e(TAG, e.getMessage());
                }
                return null;
            }
        }, new TaskRunner.Callback<Object>() {
            @Override
            public void onComplete(Object result) {

            }
        });
//


//
    }
}

