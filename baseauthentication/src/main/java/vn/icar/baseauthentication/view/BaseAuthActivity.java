package vn.icar.baseauthentication.view;

import androidx.databinding.ViewDataBinding;

import com.tatv.baseapp.view.activity.BaseActivity;

import vn.icar.baseauthentication.data.shared.AuthSharedPreference;
import vn.icar.baseauthentication.data.model.ErrorEvent;
import vn.icar.baseauthentication.data.model.SocketAuth;
import vn.icar.baseauthentication.socket.SocketAuthListener;
import vn.icar.baseauthentication.socket.SocketManager;

/**
 * Created by tatv on 11/10/2022.
 */
public abstract class BaseAuthActivity<V extends ViewDataBinding> extends BaseActivity<V> implements SocketAuthListener {

    @Override
    public void onInitial() {
        super.onInitial();
        preference = new AuthSharedPreference(context);
        SocketManager.getInstance().setListener(this);
    }

    @Override
    public <T> void onQRScanSuccessful(T obj) {

    }

    @Override
    public void onConnected(String socketId) {

    }

    @Override
    public void onDisconnected() {

    }

    @Override
    public void onConnectError(ErrorEvent errorEvent) {

    }


    @Override
    public void onReceiveQRCode(String qr, long time) {

    }

    @Override
    public void onConnectTimeout() {

    }

    @Override
    public void onAuthError() {

    }

    @Override
    public void onLogout() {

    }


    /**
     * Kết nối socket
     * */
    protected void connectSocket(SocketAuth socketAuth){
        SocketManager.getInstance().init(context, getSocketBaseUrl(), socketAuth);
        SocketManager.getInstance().connect();
    }

    protected abstract String getSocketBaseUrl();


    protected AuthSharedPreference getSharedPreference(){
        return (AuthSharedPreference) preference;
    }
}
