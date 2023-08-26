package com.tatv.baseapp.utils.tts;

import android.content.Context;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.util.Log;

import java.util.Locale;

/**
 * Created by tatv on 05/10/2022.
 */
public class TtsUtils {
    public static final String TAG = "TtsUtils";
    private static TtsUtils instance;
    public static TtsUtils getInstance(){
        if(instance == null){
            instance = new TtsUtils();
        }
        return instance;
    }

    private Context context;
    private TextToSpeech tts;

    /**
     * Khởi tạo
     * */
    public void init(Context context){
        this.context = context;
        tts  = new TextToSpeech(context, status -> {
            if(status == TextToSpeech.SUCCESS){
                setLanguage(Locale.getDefault());
            }else {
                Log.e(TAG, "error: " + status);
            }
        });
    }

    /***
     * Đặt ngôn ngữ
     */
    public void setLanguage(Locale locale){
        if(tts != null){
            tts.setLanguage(locale);
        }
    }

    /**
     * Chuyển văn bản thành giọng nói
     * */
    public void speak(String message){
        if(tts != null){
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
        }else {
            Log.e(TAG, "Chưa khởi tạo");
        }
    }

    /**
     * Chuyển văn bản thành giọng nói có callback về thời gian hoàn thành hoặc lỗi
     * */
    public void speak(String message, TtsListener listener){
        if(tts != null){
            tts.speak(message, TextToSpeech.QUEUE_FLUSH, null, null);
            new Handler().postDelayed(() -> {
                if(listener != null ) listener.onSaid();
            }, message.length() < 80 ? message.length() * 90L : message.length() * 80L);
        }else {
            listener.onError("Chưa khởi tạo");
        }
    }

    public interface TtsListener{
        /**
         * Ước lượng hoàn thành chuyển văn bản thành giọng nói
         * */
        void onSaid();
        /**
         * Khi có lỗi
         * @param error - message lỗi
         * */
        void onError(String error);
    }

}
