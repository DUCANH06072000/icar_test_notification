package com.tatv.baseapp.view.activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.tatv.baseapp.data.shared.BaseSharedPreference;
import com.tatv.baseapp.utils.network.Connectivity;
import com.tatv.baseapp.utils.network.ConnectivityListener;
/**
 * Created by tatv on 18/5/2021.
 */

public abstract class BaseActivity<V extends ViewDataBinding> extends AppCompatActivity implements BaseView, ConnectivityListener {
    private final IntentFilter filter = new IntentFilter();
    private BroadcastReceiver receiver;
    public Context context;
    protected V binding;
    protected BaseSharedPreference preference;
    protected Connectivity connectivity;

    /**
     * Singleton
     * */
    private static BaseActivity instance;

    public static BaseActivity getInstance(){
        return instance;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        instance = this;
        preference = new BaseSharedPreference(context);
        addActionFilter();
        onInitial();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != receiver) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
        }
    }

    /**
     * Khởi tạo
     * */
    @Override
    public void onInitial() {

    }

    /**
     * Khởi tạo giao diện
     * */
    protected void initView(){
        if(getLayoutId() != 0) binding = DataBindingUtil.setContentView(this, getLayoutId());
    }

    /**
     * Khởi tạo sự kiện
     * */
    protected void initEvent() {

    }

    /**
     * override receiver broadcast
     * */
    protected void onReceiverBroadcast(Context context, Intent intent){}

    /**
     * override intent filters
     * */
    protected String[] getIntentFilters(){
        return new String[0];
    }

    /**
     * Thêm Action filter lắng nghe receiver
     * */
    private void addActionFilter() {
        for (String action : getIntentFilters()){
            filter.addAction(action);
        }
        if (receiver == null) {
            receiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    onReceiverBroadcast(context, intent);
                }
            };
            registerReceiver(receiver, filter);
        }
    }

    /**
     * Hiển thị toast
     * */
    protected void showToast(final String message){
        runOnUiThread(() -> Toast.makeText(context,message,Toast.LENGTH_SHORT).show());
    }


    /**
     * Lấy tỉ lệ chiều rộng/chiều cao của màn hình
     * */
    protected double getScreenRatio(){
        DisplayMetrics metrics= context.getResources().getDisplayMetrics();
        return (double) metrics.widthPixels / metrics.heightPixels;
    }

    /**
     * Kiểm tra có phải xoay ngang màn hình hay không
     * */
    protected boolean isScreenLandscape(){
        return this.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /**
     * Lấy ra thể hiện của share prefrence
     * */
    protected <T extends BaseSharedPreference> T getSharedPreference(){
        return (T) preference;
    }


    /**
     * kiểm tra kết nối mạng hiện tại
     * */
    protected boolean checkNetworkConnected() {
        return connectivity.isNetworkConnected();
    }
    @Override
    public void onConnectivityChanged(boolean enabled) {

    }

    @Override
    public void onPingChanged(long timeofping) {

    }

}
