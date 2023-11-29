package com.example.icar_notification.share;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.icar_notification.helper.SharedPreferencesHelper;
import com.example.icar_notification.model.Music;
import com.tatv.baseapp.utils.json.JsonUtils;

import java.util.ArrayList;
import java.util.List;

public class DataMusicStore {
    private static final String PREFERENCE_NAME = "MyPrefs";
    private static final String KEY_DATA_MUSIC = "datamusic";

    private Context context;

    public DataMusicStore(Context context) {
        this.context = context;
    }

    public void setAppMusic(List<Music> data) {
        String json = JsonUtils.getJsonFromObj(data);
        SharedPreferencesHelper.put(context, KEY_DATA_MUSIC, json);
    }

    public List<Music> getAppMusic() {
        List<Music> data = new ArrayList<>();
        String json = (String) SharedPreferencesHelper.get(context, KEY_DATA_MUSIC, "");
        if (json != null && !json.equals("")) {
            try {
                data.addAll(JsonUtils.getListObjFromJson(json, Music.class));
            } catch (Exception e) {

            }
        }
        return data;
    }

}
