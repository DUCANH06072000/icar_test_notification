package com.tatv.baseapp.view.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;

/**
 * Created by tatv on 12/10/2022.
 */
public class BaseTransparentActivity extends Activity {
    protected Context context;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.context = this;
    }
}
