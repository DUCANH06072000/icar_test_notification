package vn.icar.baseauthentication.socket;

/**
 * Created by tatv on 25/11/2022.
 */
public interface SocketDataConnectivityListener {
    void onConnected();
    void onAuthError();
    void onConnectError();
    void onDisconnected();
}
