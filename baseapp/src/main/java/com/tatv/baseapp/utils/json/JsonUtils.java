package com.tatv.baseapp.utils.json;

import android.content.Context;
import android.util.Log;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

public class JsonUtils {
    private static final String TAG = "JsonUtil";

    /**
     * Convert Obj to Json Raw
     * */
    public static  <T> T getObjFromJsonResource(Context context, int id, Class<T> type){
        InputStream is = context.getResources().openRawResource(id);
        Writer writer = new StringWriter();
        char[] buffer = new char[1024];
        try {
            Reader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            int n;
            while ((n = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, n);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        String jsonString = writer.toString();
        return getObjFromJson(jsonString, type);
    }

    /**
     * Convert Json to Obj
     * */
    public static  <T> T getObjFromJson(String json, Class<T> type){
        try {
            JSONObject response = new JSONObject(json);
            T obj = (T) new Gson().fromJson(response.toString(), type);
            return obj;
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * Convert Json to List Obj
     * */
    public static  <T> List<T> getListObjFromJson(String json, Class<T> type){
        try {
            List<T> data = new ArrayList<>();
            JSONArray response = new JSONArray (json);
            for(int i = 0; i < response.length(); i++){
                JSONObject obj = response.getJSONObject(i);
                data.add((T) new Gson().fromJson(obj.toString(), type));
            }
            return data;
        } catch (JSONException e) {
            Log.e(TAG, e.getMessage());
        }
        return null;
    }

    /**
     * Convert Obj to Json
     * */
    public static  <T> String getJsonFromObj(T obj){
        return new Gson().toJson(obj);
    }


    /**
     * Check json valid
     * */
    public static boolean isJSONValid(String test) {
        try {
            new JSONObject(test);
        } catch (JSONException ex) {
            // edited, to include @Arthur's comment
            // e.g. in case JSONArray is valid as well...
            try {
                new JSONArray(test);
            } catch (JSONException ex1) {
                return false;
            }
        }
        return true;
    }
}
