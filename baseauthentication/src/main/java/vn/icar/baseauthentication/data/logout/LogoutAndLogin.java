package vn.icar.baseauthentication.data.logout;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class LogoutAndLogin {

    @SerializedName("logout")
    @Expose
    private Logout logout;
    @SerializedName("login")
    @Expose
    private Login login;

    public Logout getLogout() {
        return logout;
    }

    public void setLogout(Logout logout) {
        this.logout = logout;
    }

    public Login getLogin() {
        return login;
    }

    public void setLogin(Login login) {
        this.login = login;
    }

    public LogoutAndLogin(Logout logout, Login login) {
        this.logout = logout;
        this.login = login;
    }

    public static class Logout {

        @SerializedName("appId")
        @Expose
        private String appId;
        @SerializedName("accessToken")
        @Expose
        private String accessToken;

        public Logout(String appId, String accessToken) {
            this.appId = appId;
            this.accessToken = accessToken;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        @Override
        public String toString() {
            return "Logout{" +
                    "appId='" + appId + '\'' +
                    ", accessToken='" + accessToken + '\'' +
                    '}';
        }
    }

    public static class Login {

        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("appId")
        @Expose
        private String appId;
        @SerializedName("appVersion")
        @Expose
        private String appVersion;
        @SerializedName("deviceId")
        @Expose
        private String deviceId;
        @SerializedName("fcmToken")
        @Expose
        private String fcmToken;

        public Login(String username, String password, String appId, String appVersion, String deviceId, String fcmToken) {
            this.username = username;
            this.password = password;
            this.appId = appId;
            this.appVersion = appVersion;
            this.deviceId = deviceId;
            this.fcmToken = fcmToken;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getAppId() {
            return appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAppVersion() {
            return appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getFcmToken() {
            return fcmToken;
        }

        public void setFcmToken(String fcmToken) {
            this.fcmToken = fcmToken;
        }

        @Override
        public String toString() {
            return "Login{" +
                    "username='" + username + '\'' +
                    ", password='" + password + '\'' +
                    ", appId='" + appId + '\'' +
                    ", appVersion='" + appVersion + '\'' +
                    ", deviceId='" + deviceId + '\'' +
                    ", fcmToken='" + fcmToken + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "LogoutAndLogin{" +
                "logout=" + logout +
                ", login=" + login +
                '}';
    }
}