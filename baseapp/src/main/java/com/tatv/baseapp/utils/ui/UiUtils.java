package com.tatv.baseapp.utils.ui;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.tatv.baseapp.R;
import com.tatv.baseapp.utils.system.KeyboardUtils;

/**
 * Created by tatv on 17/11/2022.
 */
public class UiUtils {

    /**
     * Fix nhấn liên tục
     * */
    public static void fixMultiClick(View v){
        v.setEnabled(false);
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                v.setEnabled(true);
            }
        },550);
    }

    /**
     * Fix nhấn liên tục
     * @param ms  khoảng thời gian giữa các lần nhấn
     * */
    public static void fixMultiClick(View v, int ms){
        v.setEnabled(false);
        v.postDelayed(new Runnable() {
            @Override
            public void run() {
                v.setEnabled(true);
            }
        },ms);
    }

    /**
     * Ẩn bàn phím khi chạm vào view
     * */
    public static void setupUI(Activity activity, View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    KeyboardUtils.hideKeyboard(activity.getApplicationContext());
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(activity ,innerView);
            }
        }
    }

    public static void setupUI(Activity activity, View view, TextView txtTitle, EditText edtSearch) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    KeyboardUtils.hideKeyboard(activity.getApplicationContext());
                    txtTitle.setVisibility(View.VISIBLE);
                    edtSearch.setVisibility(View.GONE);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(activity ,innerView, txtTitle, edtSearch);
            }
        }
    }

    /**
     * trả về tên thiết bị
     * */
    public static String getModel(){
        return Build.MODEL;
    }

    /**
     * ẩn bàn phím hệ thống
     * */
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);

        View view = activity.getCurrentFocus();

        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
