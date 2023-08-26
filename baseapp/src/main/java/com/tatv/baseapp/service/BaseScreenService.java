package com.tatv.baseapp.service;

import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.WindowManager;

import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

public abstract class BaseScreenService<V extends ViewDataBinding> extends BaseService implements BaseOverlayView {
    protected V binding;
    protected WindowManager windowManager;
    protected WindowManager.LayoutParams wmParams;

    @Override
    public void onCreate() {
        super.onCreate();
        init();
    }

    /**
     * Khởi tạo
     * */
    protected void init(){
        super.init();
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
    }

    /**
     * Khởi tạo ui
     * */
    protected void initView(){
        if(getLayoutId() != 0 && binding == null){
            binding = DataBindingUtil.inflate(LayoutInflater.from(this), getLayoutId(), null, false);
            windowManager.addView(binding.getRoot(), getLayoutParams());
        }
    }

    protected abstract void initData();

    protected abstract void initEvent();

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (binding != null) windowManager.removeView(binding.getRoot());
    }

    /**
     * Tạo layout params cho window manager
     * */
    protected WindowManager.LayoutParams getLayoutParams(){
        int LAYOUT_FLAG;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            LAYOUT_FLAG = WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
        }
        wmParams = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                LAYOUT_FLAG,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        wmParams.gravity = Gravity.TOP | Gravity.START;
        wmParams.x = getDx();
        wmParams.y = getDy();
        return wmParams;
    }

    /**
     * Khởi tạo vị trí trục x
     * */
    @Override
    public int getDx() {
        return 0;
    }

    /**
     * Khởi tạo vị trí trục y
     * */
    @Override
    public int getDy() {
        return 0;
    }

}
