package vn.icar.baseauthentication.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.tatv.baseapp.utils.qr.QRUtils;

import java.util.Date;

import vn.icar.baseauthentication.R;
import vn.icar.baseauthentication.databinding.ActivityQrAuthBinding;
import vn.icar.baseauthentication.socket.SocketManager;

public abstract class BaseQRAuthActivity extends BaseAuthActivity<ActivityQrAuthBinding> implements View.OnClickListener {
    private static String TAG = "QRAuthActivity";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initEvent();
    }

    @Override
    protected void initView() {
        super.initView();
        binding.txtDescription.setText(getDescription());
        binding.btnBack.setVisibility(canBack() ? View.VISIBLE : View.GONE);
    }

    @Override
    protected void initEvent() {
        binding.btnRecreateCode.setOnClickListener(this);
        binding.btnBack.setOnClickListener(this);
    }

    @Override
    protected void onReceiverBroadcast(Context context, Intent intent) {

    }

    @Override
    protected String[] getIntentFilters() {
        return new String[0];
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_qr_auth;
    }


    /**
     * Override function để đặt mô tả
     * */
    public String getDescription(){
        return getString(R.string.qr_description);
    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if(id == R.id.btn_recreate_code){
            SocketManager.getInstance().recreateQRCode();
            binding.progressBar.setVisibility(View.VISIBLE);
            binding.btnRecreateCode.setVisibility(View.GONE);
        }else if(id == R.id.btn_back){
            onBack();
        }
    }


    @Override
    public void onReceiveQRCode(String qr, long time) {
        new QRUtils().setQRView(qr, binding.imgQrcode, 500);
        binding.imgQrcode.setVisibility(View.VISIBLE);
        binding.btnRecreateCode.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.GONE);
        Log.e(TAG, "time qrcode: " + (time * 1000 - new Date().getTime()));

        new Handler().postDelayed(() -> {
            binding.btnRecreateCode.setVisibility(View.VISIBLE);
            binding.imgQrcode.setVisibility(View.GONE);
        }, time * 1000 - new Date().getTime());
    }

    @Override
    public <T> void onQRScanSuccessful(T obj) {
        getSharedPreference().setAuthOption(2);
        SocketManager.getInstance().init(context, getSocketBaseUrl(), null);
        SocketManager.getInstance().connect();
    }

    @Override
    public void onConnectTimeout() {
        binding.btnRecreateCode.setVisibility(View.VISIBLE);
        binding.imgQrcode.setVisibility(View.GONE);
        binding.progressBar.setVisibility(View.GONE);
    }

    public abstract void onBack();

    public abstract boolean canBack();
}
