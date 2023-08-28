package com.tatv.baseapp.utils.tpms;

import static android.content.Context.VIBRATOR_SERVICE;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;

import com.tatv.baseapp.R;
import com.tatv.baseapp.data.shared.BaseSharedPreference;

import java.io.IOException;

/**
 * Created by tatv on 01/11/2022.
 */
public class AlertManager {
    private static final String TAG = "AlertManager";
    /**
     * Singleton
     */
    private static AlertManager instance;

    public static AlertManager getInstance() {
        if (instance == null) {
            instance = new AlertManager();
        }
        return instance;
    }

    /**
     * Khởi tạo
     */
    private Context context;
    private TireUtil tireUtil;
    private BaseSharedPreference pref;
    private MediaPlayer mediaPlayer = null;
    private Vibrator vibrator;
    double lastTimeAlert = 0;

    public void init(Context context) {
        this.context = context;
        vibrator = (Vibrator) context.getSystemService(VIBRATOR_SERVICE);
        pref = new BaseSharedPreference(context);
        tireUtil = new TireUtil(context);
    }

    /**
     * Làm mới kiểm tra cảnh báo
     */
    public void refresh(Tire tire) {
        // Đối với ngôn ngữ tiếng việt thì cảnh báo tiếng việt từng lốp
        if (pref.getLanguage().equals("vi")) {
            if (tireUtil.isTirePressHighWarning(tire.getPress())) {
                if (System.currentTimeMillis() - lastTimeAlert > 1000 * 4) {
                    if (pref.getSoundAlert()) {
                        startSoundAlert(tire.getId(), 1);
                    }
                    if (pref.getVibrationAlert()) {
                        startVibrationAlert();
                    }
                    lastTimeAlert = System.currentTimeMillis();
                }
            } else if (tireUtil.isTirePressLowWarning(tire.getPress())) {
                if (System.currentTimeMillis() - lastTimeAlert > 1000 * 4) {
                    if (pref.getSoundAlert()) {
                        startSoundAlert(tire.getId(), -1);
                    }
                    if (pref.getVibrationAlert()) {
                        startVibrationAlert();
                    }
                    lastTimeAlert = System.currentTimeMillis();
                }
            } else if (tireUtil.isTireTempWarning(tire.getTemp())) {
                if (System.currentTimeMillis() - lastTimeAlert > 1000 * 4) {
                    if (pref.getSoundAlert()) {
                        startSoundAlert();
                    }
                    if (pref.getVibrationAlert()) {
                        startVibrationAlert();
                    }
                    lastTimeAlert = System.currentTimeMillis();
                }
            }
        } else {
            // Đối với ngoại ngữ cảnh báo didi
            if (tireUtil.isTirePressWarning(tire.getPress()) || tireUtil.isTireTempWarning(tire.getTemp())) {
                if (System.currentTimeMillis() - lastTimeAlert > 1000 * 4) {
                    if (pref.getSoundAlert()) {
                        startSoundAlert();
                    }
                    if (pref.getVibrationAlert()) {
                        startVibrationAlert();
                    }
                    lastTimeAlert = System.currentTimeMillis();
                }
            }
        }
    }

    /**
     * Cảnh báo âm thanh
     */
    public void startSoundAlert() {
        Log.e("xxxxx", "startSoundAlert");
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        mediaPlayer = MediaPlayer.create(context, R.raw.didi);
        mediaPlayer.start();
    }

    /**
     * Cảnh báo theo từng lốp
     */
    public void startSoundAlert(int id, int status) {
        if (mediaPlayer != null) {
            mediaPlayer.release();
        }
        switch (id) {
            case 1:
                if (status == 1) {
                    mediaPlayer = MediaPlayer.create(context, R.raw.left_front_tire_hight);
                } else {
                    mediaPlayer = MediaPlayer.create(context, R.raw.left_front_tire_low);
                }
                break;
            case 2:
                if (status == 1) {
                    mediaPlayer = MediaPlayer.create(context, R.raw.right_front_tire_hight);
                } else {
                    mediaPlayer = MediaPlayer.create(context, R.raw.right_front_tire_low);
                }
                break;
            case 3:
                if (status == 1) {
                    mediaPlayer = MediaPlayer.create(context, R.raw.left_back_tire_hight);
                } else {
                    mediaPlayer = MediaPlayer.create(context, R.raw.left_back_tire_low);
                }
                break;
            case 4:
                if (status == 1) {
                    mediaPlayer = MediaPlayer.create(context, R.raw.right_back_tire_hight);
                } else {
                    mediaPlayer = MediaPlayer.create(context, R.raw.right_back_tire_low);
                }
                break;
        }
        if (mediaPlayer != null) {
            mediaPlayer.start();
        }
    }

    public void stopSoundAlert() {
        try {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                mediaPlayer.stop();
                mediaPlayer.prepare();
            }
        } catch (IOException e) {
            Log.e(TAG, e.getMessage());
        }
    }


    /**
     * Cảnh báo rung
     */
    public void startVibrationAlert() {
        if (vibrator.hasVibrator()) {
            long[] pattern = {100, 100, 100, 1000, 100, 100, 100, 1000};
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                vibrator.vibrate(VibrationEffect.createWaveform(pattern, -1));
            } else {
                vibrator.vibrate(pattern, -1);
            }
        }
    }

    public void stopVibrationAlert() {
        if (vibrator.hasVibrator()) {
            vibrator.cancel();
        }
    }


}
