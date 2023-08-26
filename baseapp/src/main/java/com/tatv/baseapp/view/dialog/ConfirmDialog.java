package com.tatv.baseapp.view.dialog;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

import com.tatv.baseapp.R;
import com.tatv.baseapp.databinding.DialogConfirmBinding;
import com.tatv.baseapp.utils.ui.UiUtils;
import com.tatv.baseapp.view.tview.rounded.RoundedRelativeLayout;

public class ConfirmDialog extends BaseDialog<DialogConfirmBinding>{
    private ConfirmDialogListener listener;
    private String title, content;
    private int color;

    private static ConfirmDialog instance;

    public static ConfirmDialog getInstance(Context context, ConfirmDialogListener listener){
        if(instance == null){
            instance = new ConfirmDialog(context, listener);
        }
        return instance;
    }


    public ConfirmDialog(@NonNull Context context, ConfirmDialogListener listener) {
        super(context);
        this.listener = listener;
        color = context.getColor(R.color.orange);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    protected void initView() {
        super.initView();
        binding.txtTitle.setText(title);
        binding.txtContent.setText(content);
        binding.layoutAction.setBackgroundColor(color);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_confirm;
    }

    @Override
    protected void initEvent() {
        binding.txtConfirm.setOnClickListener(v -> {
            if(listener != null) listener.onConfirm(this);
            UiUtils.fixMultiClick(v);
        });
        binding.txtCancel.setOnClickListener(v -> {
            if(listener != null) listener.onCancel(this);
        });
    }

    public ConfirmDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public ConfirmDialog setContent(String content){
        this.content = content;
        return this;
    }

    public ConfirmDialog setActionBackground(int color){
        this.color = color;
        return this;
    }

    public interface ConfirmDialogListener{
        void onConfirm(ConfirmDialog dialog);
        void onCancel(ConfirmDialog dialog);
    }
}
