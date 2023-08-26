package com.tatv.baseapp.view.tview.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.LinearLayout;

public abstract class TLinearLayout extends LinearLayout {
    public Context context;
    public TLinearLayout(Context context) {
        super(context);
        initializeView();
    }

    public TLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public TLinearLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView();
    }

    public TLinearLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initializeView();
    }

    private void initializeView() {
        context = getContext();
        inflate(context, getView(), this);
        initView();
        initEvent();
    }

    public abstract int getView();
    public abstract void initView();
    public abstract void initEvent();
}
