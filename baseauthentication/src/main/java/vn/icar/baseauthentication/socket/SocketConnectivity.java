package vn.icar.baseauthentication.socket;

/**
 * Created by tatv on 25/11/2022.
 */
public class SocketConnectivity {
    public String tag;
    public SocketDataConnectivityListener listener;

    public SocketConnectivity(String tag, SocketDataConnectivityListener listener) {
        this.tag = tag;
        this.listener = listener;
    }
}
