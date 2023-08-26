package vn.icar.baseauthentication.socket;

import android.content.Context;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.NonNull;

import com.tatv.baseapp.api.ApiService;
import com.tatv.baseapp.listener.ReceiveData;
import com.tatv.baseapp.utils.datetime.DateTimeUtils;
import com.tatv.baseapp.utils.json.JsonUtils;
import com.tatv.baseapp.utils.jwt.JWTUtils;
import com.tatv.baseapp.utils.log.LogUtils;
import com.tatv.baseapp.utils.network.Connectivity;

import org.json.JSONObject;

import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import vn.icar.baseauthentication.api.IAuthDataService;
import vn.icar.baseauthentication.data.shared.AuthSharedPreference;
import vn.icar.baseauthentication.data.model.ErrorEvent;
import vn.icar.baseauthentication.data.model.QRAuth;
import vn.icar.baseauthentication.data.model.QREvent;
import vn.icar.baseauthentication.data.token.RefreshToken;
import vn.icar.baseauthentication.data.model.SocketAuth;


/**
 * Created by tatv on 07/10/2022.
 */
public class SocketManager {
    private final String TAG = "SocketManager";

    private static SocketManager instance;

    public static SocketManager getInstance() {
        if (instance == null) {
            instance = new SocketManager();
        }
        return instance;
    }

    private Socket socket;
    private String socketId = "";
    private IO.Options opts;

    private Context context;
    private AuthSharedPreference preference;
    private String url;
    private SocketAuth socketAuth;
    private Connectivity connectivity;

    private SocketAuthListener listener;

    private CountDownTimer timeoutCdt;

    /**
     * Khởi tạo socket
     * @param context context ứng dụng
     * @param url đường dẫn socket
     * @param socketAuth thông tin authorization
     * */
    public void init(Context context, String url, SocketAuth socketAuth) {
        if(socketAuth != null){
            LogUtils.d(TAG, "Socket init","Khởi tạo kết nối quét mã QR");
        }else {
            LogUtils.d(TAG, "Socket init","Khởi tạo kết nối thời gian thực");
        }
        this.context = context;
        this.url = url;
        this.socketAuth = socketAuth;
        if(preference == null){
            preference = new AuthSharedPreference(context);
        }
        if(connectivity == null){
            connectivity = new Connectivity(context);
        }
        opts = new IO.Options();
        opts.transports = new String[]{"websocket"};
        Map<String, List<String>> extraHeaders = new HashMap<>();
        extraHeaders.put("accept-language", Arrays.asList(preference.getLanguage()));
        if(socketAuth != null){
            String dataBase64 = Base64.encodeToString(new JsonUtils().getJsonFromObj(socketAuth)
                            .getBytes(StandardCharsets.UTF_8),
                    Base64.NO_WRAP);
            extraHeaders.put("authorization", Arrays.asList("QR " + dataBase64));
        }else {
            refreshTokenProactive();
            extraHeaders.put("authorization", Arrays.asList("Bearer " + preference.getToken()));
        }

        opts.extraHeaders = extraHeaders;
        //* Delay between two consecutive attempts:
        // 1st attempt: 1000 +/- 500 ms
        // 2nd attempt: 2000 +/- 1000 ms
        // 3nd attempt: 4000 +/- 2000 ms
        // following attempts: 8000 +/- 2500 ms

        // whether to reconnect automatically
        opts.reconnection = true;
        // number of reconnection attempts before giving up
        opts.reconnectionAttempts = 9999;
        // how long to initially wait before attempting a new reconnection
        opts.reconnectionDelay = 1000;
        // maximum amount of time to wait between reconnection attempts. Each attempt increases the reconnection delay by 2x along with a randomization factor
        opts.reconnectionDelayMax = 8000;
        opts.randomizationFactor = 0.5;
        try {
            if(socket != null){
                if(socket.connected()){
                    socket.disconnect();
                }
                socket.close();
            }
            socket = null;
            socket = IO.socket(url, opts);
        } catch (URISyntaxException e) {
            LogUtils.e(TAG, e.getMessage());
        }
    }


    /**
     * Khởi tạo lại từ những tham số trước đó
     * **/
    public void reInit(){
        LogUtils.d(TAG, "reInit");
        if(context != null && url != null){
            init(context, url, socketAuth);
        }
    }

    /**
     * Truyền interface sự kiện
     * */
    public void setListener(SocketAuthListener listener){
        this.listener = listener;
    }

    /**
     * Tạo lại mã QR Code
     * */
    public void recreateQRCode(){
        if(socket == null){
            Log.e(TAG, "socket null");
        }else if(socketAuth == null){
            Log.e(TAG, "socketAuth null");
        }else {
            socket.disconnect();
            socket.connect();
        }
    }


    /**
     * Kết nối socket sau khi khởi tạo
     */
    public void connect() {
        socket.on("connect", onConnect);
        socket.on("connect_error", onConnectError);
        socket.on("disconnect", onDisconnect);
        socket.connect();
        startTimeoutHandler();
    }

    /**
     * Kiểm tra socket có đang kết nối hay không
     * */
    public boolean isConnected(){
        if(socket == null){
            return false;
        }else {
            return socket.connected();
        }
    }

    /**
     * Bắt đầu handler đếm thời gian bắt đầu kết nối
     * */
    void startTimeoutHandler(){
        cancelTimeoutHandler();
        if(timeoutCdt == null){
            timeoutCdt = new CountDownTimer(8000, 1000) {
                @Override
                public void onTick(long l) {
                }
                @Override
                public void onFinish() {
                    if(listener != null) listener.onConnectTimeout();
                }
            };
        }
        timeoutCdt.start();
    }

    /**
     * Dừng handler đếm thời gian bắt đầu kết nối
     * */
    void cancelTimeoutHandler(){
        if(timeoutCdt != null){
            timeoutCdt.cancel();
        }
    }


    /**
     * Thêm sự kiện lắng nghe
     * */
    public void addEvent(String event, ReceiveData listener){
        if(socket != null){
            socket.on(event, args -> new Handler(Looper.getMainLooper()).post(() -> {
                if(listener != null){
                    listener.onReceiveData(args[0]);
                }
            }));
        }
    }

    /**
     * Sử lý sự kiện khi kết nối thành công
     */
    Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            new Handler(Looper.getMainLooper()).post(() -> {
                Log.e(TAG, "onConnect " + (args.length > 0 ? args[0].toString() : ""));
                Log.e(TAG, "socketId: " + socket.id());
                if(socket.id() != null){
                    /// Kiểm tra thời gian gần nhất nhảy vào onConnect
                    if(!socketId.equals(socket.id())){
                        Log.e(TAG, "socket id changed");
                        socketId = socket.id();
                        if(listener != null) listener.onConnected(socketId);
                        socket.on(socketId, tokenError);
                        if(socketAuth != null){
                            socket.once("get-qr-login-" + socketId, onReceiveQRCode);
                        }else {
                            updateConnectedListener();
                        }
                    }
                    cancelTimeoutHandler();
                }else {
                    LogUtils.e(TAG, "socketId null");
                }
            });
        }
    };

    /**
     * Nhận sự kiện khi kết nối lỗi
     */
    Emitter.Listener onConnectError = args -> new Handler(Looper.getMainLooper()).post(() -> {
        if(connectivity.isNetworkConnected()){
            Log.e(TAG, "onConnectError: " + args[0].toString());
            ErrorEvent errorEvent = new JsonUtils().getObjFromJson(args[0].toString(), ErrorEvent.class);
            if(listener != null) listener.onConnectError(errorEvent);
        }else {
            if(listener != null) listener.onConnectError(new ErrorEvent());
        }
        updateConnectErrorListener();
    });

    /**
     * Nhận sự kiện khi ngắt kết nối
     */
    Emitter.Listener onDisconnect = args -> new Handler(Looper.getMainLooper()).post(() -> {
        Log.e(TAG, "onDisconnect");
        if(listener != null) listener.onDisconnected();
        updateDisconnectedListener();
    });

    /**
     * Nhận sự kiện Mã QR
     */
    Emitter.Listener onReceiveQRCode = args -> new Handler(Looper.getMainLooper()).post(() -> {
        Log.e(TAG, "onReceiveQRCode: " + args[0].toString());
        QRAuth qrAuth = new JsonUtils().getObjFromJson(args[0].toString(), QRAuth.class);
        if(listener != null) {
            listener.onReceiveQRCode(qrAuth.getData().get(0).getQrCode(), qrAuth.getData().get(0).getQrExpiresAt());
        }
        if(socket != null){
            socket.once(qrAuth.getData().get(0).getAuthEvent(), SocketManager.getInstance().onScanQRCode);
        }
    });

    /**
     * Nhận sự kiện khi quét QR
     */
    Emitter.Listener onScanQRCode = args -> new Handler(Looper.getMainLooper()).post(() -> {
        Log.e(TAG, "onScanQRCode: " + args[0].toString());
        QREvent qrEvent = new JsonUtils().getObjFromJson(args[0].toString(), QREvent.class);
        updateAuth(qrEvent);
        if(listener != null) listener.onQRScanSuccessful(qrEvent);
    });

    /**
     * Nhận sự kiện khi token lỗi
     */
    Emitter.Listener tokenError = args -> new Handler(Looper.getMainLooper()).post(() -> {
        Log.e(TAG, "tokenError: " + args[0].toString());
        ErrorEvent errorEvent = new JsonUtils().getObjFromJson(args[0].toString(), ErrorEvent.class);
        if(errorEvent.getStatusCode() == 401){
            refreshTokenPassive();
        }
    });


    /**
     * Làm mới token bị động
     * */
    private void refreshTokenPassive(){
        refreshToken(new ReceiveData() {
            @Override
            public <T> void onReceiveData(T data) {
                if(data == null){
                    if(listener != null) listener.onAuthError();
                    updateAuthErrorListener();
                }else {
                    QREvent qrEvent = (QREvent) data;
                    updateAuth(qrEvent);
                    init(context, url, null);
                    connect();
                }
            }
        });
    }

    /**
     * Làm mới token chủ động
     * */
    private void refreshTokenProactive(){
        // Nếu thời gian exp access token còn dưới 6h thì sẽ call refresh
        if(JWTUtils.getTokenExpired(preference.getToken()) - DateTimeUtils.getTimestamp()  < 6 * 60 * 60){
            refreshToken(null);
        }
    }

    /**
     * Làm mới lại token
     * */
    public void refreshToken(ReceiveData callback){
        IAuthDataService service = ApiService.getService(context, url, IAuthDataService.class);
        Call<QREvent> call = service.refreshToken(new RefreshToken(
                preference.getRefreshToken()));
        call.enqueue(new Callback<QREvent>() {
            @Override
            public void onResponse(@NonNull Call<QREvent> call, @NonNull Response<QREvent> response) {
                Log.e(TAG, "onResponse: " + response.code() + "-" + (response.body() != null ? response.body().toString() : ""));
                if(response.code() == 200){
                    if(callback != null) callback.onReceiveData(response.body());
                }else if(response.code() == 401){
                    if(preference.getAuthOption() == 2){
                        preference.clear();
                        if(listener != null) listener.onLogout();
                        LogUtils.d(TAG, "onLogout");
                    }
                }else {
                    if(callback != null) callback.onReceiveData(null);
                }
            }

            @Override
            public void onFailure(@NonNull Call<QREvent> call, @NonNull Throwable t) {
                Log.e(TAG, "onFailure: " + t.getMessage());
                if(callback != null) callback.onReceiveData(null);
            }
        });
    }


    /**
     * Cập nhật auth xuống shared prefrence
     * */
    private void updateAuth(QREvent qrEvent){
        preference.setToken(qrEvent.getData().get(0).getAccessToken());
        preference.setRefreshToken(qrEvent.getData().get(0).getRefreshToken());
        preference.setSubId(qrEvent.getData().get(0).getSub());
        preference.setUserId(qrEvent.getData().get(0).getUserId());
        preference.setTimeTokenExp(qrEvent.getData().get(0).getExp());
    }

    /**
     * Gửi sự kiện
     * */
    public void emit(String event, JSONObject json) {
        if(socket != null){
            socket.emit(event, json);
        }
    }
    public void emit(String event, String json) {
        if(socket != null){
            socket.emit(event, json);
        }
    }


    /**
     * Connectivity
     * */
    private List<SocketConnectivity> socketConnectivityList = new ArrayList<>();

    // Đăng ký lắng nghe thay đổi kết nối
    public void registerSocketConnectivityListener(SocketConnectivity socketConnectivity){
        socketConnectivityList.add(socketConnectivity);
    }

    // Hủy đăng ký lắng nghe thay đổi kết nối
    public void unregisterSocketConnectivityListener(String tag){
        for(int i = 0; i < socketConnectivityList.size(); i++){
            if(socketConnectivityList.get(i).tag.equals(tag)){
                socketConnectivityList.remove(i);
                return;
            }
        }
    }

    // Callback connected cho tất cả
    void updateConnectedListener(){
        for(int i = 0; i < socketConnectivityList.size(); i++){
            socketConnectivityList.get(i).listener.onConnected();
        }
    }

    // Callback connect error cho tất cả
    void updateConnectErrorListener(){
        for(int i = 0; i < socketConnectivityList.size(); i++){
            socketConnectivityList.get(i).listener.onConnectError();
        }
    }

    // Callback auth error cho tất cả
    void updateAuthErrorListener(){
        for(int i = 0; i < socketConnectivityList.size(); i++){
            socketConnectivityList.get(i).listener.onAuthError();
        }
    }

    // Callback disconnected cho tất cả
    void updateDisconnectedListener(){
        for(int i = 0; i < socketConnectivityList.size(); i++){
            socketConnectivityList.get(i).listener.onDisconnected();
        }
    }


}
