package vn.icar.baseauthentication.socket;

import vn.icar.baseauthentication.data.model.ErrorEvent;

/**
 * Created by tatv on 07/10/2022.
 */
public interface SocketAuthListener {
    void onConnected(String socketId);
    void onConnectTimeout();
    void onDisconnected();
    void onConnectError(ErrorEvent errorEvent);
    void onReceiveQRCode(String qr, long time);
    <T> void onQRScanSuccessful(T obj);
    void onAuthError();
    void onLogout();
}
