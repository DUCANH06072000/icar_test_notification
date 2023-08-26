package com.tatv.baseapp.view.activity;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

public abstract class BaseFragment<V extends ViewDataBinding> extends Fragment implements BaseView{
    protected Context context;
    protected V binding;
    
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getContext();
        onInitial();
    }

    @Override
    public void onInitial() {

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false);
        initEvent();
        initData();
        return binding.getRoot();
    }

    protected abstract void initEvent();

    protected abstract void initData();


    protected void showToast(final String message){
        getActivity().runOnUiThread(() -> Toast.makeText(context,message,Toast.LENGTH_SHORT).show());
    }
}
