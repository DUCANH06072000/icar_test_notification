package com.example.icar_notification.share;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.icar_notification.model.AppStore;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
public class DataStore {
    private static final String PREFERENCE_NAME = "MyPrefs";
    private static final String KEY_DATA_APP = "dataApp";
    private static final String KEY_FIRST_APP = "firstApp";


    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public DataStore(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveFirstApp(boolean firstApp) {
        editor.putBoolean(KEY_FIRST_APP, firstApp);
        editor.apply();
    }

    public boolean loadFirstApp() {
        boolean isFirst = sharedPreferences.getBoolean(KEY_FIRST_APP, false);
        return isFirst;
    }

    public void saveData(List<AppStore> dataList) {
        Gson gson = new Gson();
        String json = gson.toJson(dataList);
        editor.putString(KEY_DATA_APP, json);
        editor.apply();
    }

    public void addData(AppStore newData) {
        List<AppStore> dataList = loadData();
        if (dataList == null) {
            dataList = new ArrayList<>();
        }

        dataList.add(newData);
        saveData(dataList);
    }

    public List<AppStore> loadData() {
        Gson gson = new Gson();
        String json = sharedPreferences.getString(KEY_DATA_APP, null);
        Type type = new TypeToken<List<AppStore>>() {
        }.getType();
        return gson.fromJson(json, type);
    }


    public void removeDataApp(String textToRemove) {
        List<AppStore> dataList = loadData();
        if (dataList != null) {
            // Tìm và xóa phần tử dựa vào trường "text"
            for (int i = 0; i < dataList.size(); i++) {
                if (dataList.get(i).getNameApp().equals(textToRemove)) {
                    dataList.remove(i);
                    saveData(dataList);
                    break; // Nếu đã tìm thấy và xóa, thoát khỏi vòng lặp
                }
            }
        }
    }
}
