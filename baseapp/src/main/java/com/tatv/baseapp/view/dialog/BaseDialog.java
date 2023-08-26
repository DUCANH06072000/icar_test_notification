package com.tatv.baseapp.view.dialog;

import static android.view.ViewGroup.LayoutParams.MATCH_PARENT;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.tatv.baseapp.R;

public abstract class BaseDialog<V extends ViewDataBinding> extends Dialog {
    public Context context;
    protected V binding;

    public BaseDialog(@NonNull Context context) {
        super(context);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initialization();
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void initialization() {
        this.context = getContext();
        getWindow().setBackgroundDrawable(context.getDrawable(R.drawable.background_transparent));
        getWindow().setLayout(MATCH_PARENT, MATCH_PARENT);
        initView();
        initEvent();
    }

    protected void initView(){
        if(getLayoutId() != 0){
            binding = DataBindingUtil.inflate(LayoutInflater.from(context), getLayoutId(), null, false);
            setContentView(binding.getRoot());
        }
    }

    protected abstract int getLayoutId();
    protected abstract void initEvent();

}
