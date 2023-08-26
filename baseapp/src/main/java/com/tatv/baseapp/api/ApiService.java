package com.tatv.baseapp.api;

import android.content.Context;

public class ApiService {
    public static <T> T getService(Context context, String url, Class<T> type) {
        return ApiRetrofitClient.getClient(context, url).create(type);
    }
}
