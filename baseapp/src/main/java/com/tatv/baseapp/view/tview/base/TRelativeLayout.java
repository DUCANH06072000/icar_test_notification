package com.tatv.baseapp.view.tview.base;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public abstract class TRelativeLayout extends RelativeLayout {
    public Context context;
    public TRelativeLayout(Context context) {
        super(context);
        initializeView();
    }

    public TRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initializeView();
    }

    public TRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initializeView();
    }

    public TRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
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
