package com.tatv.baseapp.view.activity;

import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.tatv.baseapp.listener.TPermissionListener;

public abstract class BasePermissionCombatActivity extends AppCompatActivity implements TPermissionListener {
    private static final int REQUEST_CODE_PERMISSION = 12;
    protected Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
    }

    protected void requestPermission(){
        if(getPermissionArray().length > 0){
            ActivityCompat.requestPermissions(
                    this,
                    getPermissionArray(),
                    REQUEST_CODE_PERMISSION
            );
        }
    }

    /**
     * Lấy danh sách quyền cơ bản
     * */
    protected abstract String[] getPermissionArray();

    /**
     * Kiểm tra quyền đã được cấp hay chưa
     * */
    protected boolean isPermissionGranted(){
        for (String permission: getPermissionArray()) {
            if(!checkPermission(permission)){
                return false;
            }
        }
        return true;
    }

    /**
     * Kiểm tra quyền
     * */
    protected boolean checkPermission(String permission){
        if(ContextCompat.checkSelfPermission(context, permission)
                != PackageManager.PERMISSION_GRANTED){
            return false;
        }
        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_PERMISSION){
            if(!isPermissionGranted()){
                onPermissionDenied();
            }
        }
    }
}
