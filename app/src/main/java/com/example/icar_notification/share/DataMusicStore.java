package com.example.icar_notification.share;

import android.content.Context;
import android.content.SharedPreferences;

public class DataMusicStore {
    private static final String PREFERENCE_NAME = "MyPrefs";
    private static final String KEY_NAME_MUSIC = "nameMusic";

    private static final String KEY_PACKAGENAME = "packageName";

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private Context context;

    public DataMusicStore(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveDataNameMusic(String nameMusic) {
        editor.putString(KEY_NAME_MUSIC, nameMusic);
        editor.apply();
    }

    public void saveDataPackageName(String packageName) {
        editor.putString(KEY_PACKAGENAME, packageName);
        editor.apply();
    }

    public String loadDataNameMusic() {
        String nameMusic = sharedPreferences.getString(KEY_NAME_MUSIC, null);
        return nameMusic;
    }

    public String loadDataPackageName() {
        String packageName = sharedPreferences.getString(KEY_PACKAGENAME, null);
        return packageName;
    }
}
