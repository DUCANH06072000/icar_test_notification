package com.tatv.baseapp.view.dialog;


import android.content.Context;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;

import com.tatv.baseapp.R;
import com.tatv.baseapp.databinding.DialogErrorMessageBinding;
import com.tatv.baseapp.utils.ui.UiUtils;


public class ErrorDialog extends BaseDialog<DialogErrorMessageBinding> {
    private DialogErrorListener listener;
    private String title, content,btRetry;
    private boolean showBottomCancel=true;

    private static ErrorDialog instance;

    public static ErrorDialog getInstance(Context context, DialogErrorListener listener) {
        if (instance == null) {
            instance = new ErrorDialog(context, listener);
        }
        return instance;
    }


    public ErrorDialog(@NonNull Context context, DialogErrorListener listener) {
        super(context);
        this.listener = listener;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(false);
    }

    @Override
    protected void initView() {
        super.initView();
        if (title != null && !title.equals("")) {
            binding.txtTitle.setText(title);
        }
        if (btRetry != null && !btRetry.equals("")){
            binding.btRetry.setText(btRetry);
        }
        binding.txtContent.setText(content);
        if (showBottomCancel) {
            binding.btCancel.setVisibility(View.VISIBLE);
        } else {
            binding.btCancel.setVisibility(View.GONE);
        }
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_error_message;
    }

    @Override
    protected void initEvent() {
        binding.btCancel.setOnClickListener(v -> {
            if (listener != null) listener.onCancel(this);
        });
        binding.btRetry.setOnClickListener(v -> {
            if (listener != null) listener.onRetry(this);
            UiUtils.fixMultiClick(v);
        });
    }

    public ErrorDialog setTitle(String title) {
            this.title = title;
        return this;
    }
    public ErrorDialog setTextBottomRetry(String btRetry) {
        this.btRetry = btRetry;
        return this;
    }
    public ErrorDialog setContent(String content) {
        this.content = content;
        return this;
    }

    public ErrorDialog showBottomCancel(boolean show) {
        showBottomCancel=show;
        return this;
    }

    public interface DialogErrorListener {
        void onRetry(ErrorDialog dialog);

        void onCancel(ErrorDialog dialog);
    }
}
