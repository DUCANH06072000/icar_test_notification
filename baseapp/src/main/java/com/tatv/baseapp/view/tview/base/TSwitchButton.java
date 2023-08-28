package com.tatv.baseapp.view.tview.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.tatv.baseapp.R;

public class TSwitchButton extends androidx.appcompat.widget.AppCompatImageView implements View.OnClickListener {
    private boolean enabled;
    private SwitchOnClickListener listener;

    public TSwitchButton(Context context) {
        super(context);
        init();
    }

    public TSwitchButton(Context context, boolean enabled) {
        super(context);
        init(enabled);
    }

    public TSwitchButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TSwitchButton(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }




    /**
     * Khởi tạo
     * */

    private void init() {
        setOnClickListener(this);
        refresh();
    }

    private void init(boolean enabled) {
        this.enabled = enabled;
        init();
    }

    /**
     * Đặt trạng thái switch
     * */
    public void setEnabled(boolean enabled){
        this.enabled = enabled;
        refresh();
    }

    /**
     * Đăng ký sự kiện
     * */
    public void setSwitchOnClickListener(SwitchOnClickListener listener){
        this.listener = listener;
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void refresh(){
        setImageDrawable(getContext().getDrawable(enabled ? R.drawable.ic_switch_on : R.drawable.ic_switch_off));
        invalidate();
    }


    @Override
    public void onClick(View v) {
        enabled ^= true;
        refresh();
        if(listener != null){
            listener.onSwitchChanged(this,enabled);
        }
    }


    public interface SwitchOnClickListener{
        void onSwitchChanged(View view,boolean enabled);
    }
}
