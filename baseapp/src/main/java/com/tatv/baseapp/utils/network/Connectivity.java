package com.tatv.baseapp.utils.network;

import static android.content.Context.CONNECTIVITY_SERVICE;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.NetworkRequest;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by tatv on 18/8/2021.
 */

public class Connectivity {
    private static final String TAG = "Connectivity";

    private final ConnectivityManager cm;

    private static final int PING_TIMEOUT = 2000;
    private static final int PING_INTERVAL = 4000;
    private static final int PING_ERROR = 999;
    private static long pingCurrent = -1;

    private static Thread thread;

    private static final List<ConnectivityChildListener> listeners = new ArrayList<>();

    private static Connectivity instance;

    public static Connectivity getInstance(Context context) {
        if (instance == null) {
            instance = new Connectivity(context);
        }
        return instance;
    }

    /**
     * Khởi tạo
     */

    public Connectivity(Context context) {
        cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            cm.registerDefaultNetworkCallback(mNetworkCallback);
        } else {
            NetworkRequest request = new NetworkRequest.Builder()
                    .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET).build();
            cm.registerNetworkCallback(request, mNetworkCallback);
        }
        addPingGoogleListener();
    }

    ConnectivityManager.NetworkCallback mNetworkCallback = new ConnectivityManager.NetworkCallback() {
        @Override
        public void onAvailable(Network network) {
            // network available
            notifyNetworkTypeChanged(getNetworkType());
        }

        @Override
        public void onLost(Network network) {
            // network unavailable
            notifyNetworkTypeChanged(-1);
        }
    };


    /**
     * Trả về tốc độ mạng hiện tại
     */
    public Speed getSpeed() {
        if (isNetworkConnected()) {
            NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
            return new Speed(nc.getLinkDownstreamBandwidthKbps(), nc.getLinkUpstreamBandwidthKbps());
        } else {
            return new Speed(0, 0);
        }
    }


    /**
     * Đăng ký ping từ google
     */
    private void addPingGoogleListener() {
        if(thread == null){
            thread = new Thread(() -> {
                while (true){
                    if(!Thread.currentThread().isInterrupted()){
                        try {
                            long time = System.currentTimeMillis() % 1000;
                            // Connect to Google DNS to check for connection
                            Socket socket = new Socket();
                            socket.connect(new InetSocketAddress("8.8.8.8", 53), PING_TIMEOUT);
                            socket.close();

                            pingCurrent = System.currentTimeMillis() % 1000 - time;
                            Log.i(TAG, "ping google: " + pingCurrent);
                            if (isNetworkConnected()) {
                                notifyConnectivityChanged(true, pingCurrent < 0 ? PING_ERROR : pingCurrent);
                            } else {
                                notifyConnectivityChanged(false, PING_ERROR);
                            }
                        } catch (IOException e) {
                            Log.e(TAG, "ping error: " + e.getMessage());
                            notifyConnectivityChanged(false, PING_ERROR);
                        }
                        try {
                            Thread.sleep(PING_INTERVAL);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }

            });
            thread.start();
        }
    }


    private void notifyConnectivityChanged(boolean connected, long ping) {
        for(int i = 0; i < listeners.size(); i++){
            listeners.get(i).getListener().onConnectivityChanged(connected);
            listeners.get(i).getListener().onPingChanged(ping);
        }
    }


    private void notifyNetworkTypeChanged(int type) {
        for(int i = 0; i < listeners.size(); i++){
            listeners.get(i).getListener().onNetworkTypeChanged(type);
        }
    }


    /**
     * Kiểm tra kết nối internet chuẩn hệ thống
     */
    public boolean isNetworkConnected() {
        boolean isOnline = false;
        try {
            NetworkCapabilities capabilities = cm.getNetworkCapabilities(cm.getActiveNetwork());
            isOnline = capabilities != null && capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED);
        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
        return isOnline;
    }

    /**
     * Kiểm tra internet có thực sự kết nối hay không
     */
    public boolean isInternetAvailable() {
        try {
            InetAddress inetAddress = InetAddress.getByName("google.com");
            //You can replace it with your name
            return !inetAddress.equals("");

        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Lấy connectivity string type
     */
    public String getNetworkTypeName() {
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            return activeNetwork.getTypeName();
        }
        return null;
    }

    /**
     * Lấy connectivity type
     */
    public int getNetworkType() {
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            return activeNetwork.getType();
        }
        return -1;
    }

    public long getPing() {
        return pingCurrent;
    }


    public void addChildListener(ConnectivityChildListener listener) {
        for (int i = 0; i < listeners.size(); i++) {
            if (listeners.get(i).getTag().equals(listener.getTag())) {
                return;
            }
        }
        listener.getListener().onConnectivityChanged(isNetworkConnected());
        listener.getListener().onNetworkTypeChanged(getNetworkType());
        listeners.add(listener);
    }

    public void removeChildListener(String tag) {
        for (int i = 0; i < listeners.size(); ) {
            if (listeners.get(i).getTag().equals(tag)) {
                listeners.remove(i);
            } else {
                i++;
            }
        }
    }


    public static class ConnectivityChildListener {
        private String tag;
        private ConnectivityListener listener;

        public ConnectivityChildListener(String tag, ConnectivityListener listener) {
            this.tag = tag;
            this.listener = listener;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public ConnectivityListener getListener() {
            return listener;
        }

        public void setListener(ConnectivityListener listener) {
            this.listener = listener;
        }
    }
}