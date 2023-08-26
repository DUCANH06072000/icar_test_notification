package com.tatv.baseapp.view.dialog;

import android.content.Context;

import androidx.annotation.NonNull;

import com.tatv.baseapp.R;
import com.tatv.baseapp.databinding.DialogReportBinding;


/**
 * Created by tatv on 06/12/2022.
 */
public class ReportDialog extends BaseDialog<DialogReportBinding> {

    public ReportDialog(@NonNull Context context) {
        super(context);
        setCancelable(false);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.dialog_report;
    }


    @Override
    protected void initEvent() {

    }

    /**
     * Đặt mô tả report
     * */
    public void setDescription(String description){
        binding.txtDescription.setText(description);
    }

}
