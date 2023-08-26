package vn.icar.baseauthentication.data.login;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class Login {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Data> data = null;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;

    public String getMessage() {
        return message == null ? "" : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Data> getData() {
        return data == null ? new ArrayList<>() : data;
    }

    public void setData(List<Data> data) {
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode == null ? -1 : statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public class Data {

        @SerializedName("appId")
        @Expose
        private String appId;
        @SerializedName("accessToken")
        @Expose
        private String accessToken;
        @SerializedName("appVersion")
        @Expose
        private String appVersion;
        @SerializedName("deviceId")
        @Expose
        private String deviceId;
        @SerializedName("fcmToken")
        @Expose
        private String fcmToken;
        @SerializedName("lang")
        @Expose
        private String lang;
        @SerializedName("lastTime")
        @Expose
        private Long lastTime;
        @SerializedName("refreshToken")
        @Expose
        private String refreshToken;
        @SerializedName("sub")
        @Expose
        private String sub;
        @SerializedName("workingStatus")
        @Expose
        private Integer workingStatus;

        public String getAppId() {
            return appId == null ? "" : appId;
        }

        public void setAppId(String appId) {
            this.appId = appId;
        }

        public String getAccessToken() {
            return accessToken == null ? "" : accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getAppVersion() {
            return appVersion == null ? "" : appVersion;
        }

        public void setAppVersion(String appVersion) {
            this.appVersion = appVersion;
        }

        public String getDeviceId() {
            return deviceId == null ? "" : deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public String getFcmToken() {
            return fcmToken == null ? "" : fcmToken;
        }

        public void setFcmToken(String fcmToken) {
            this.fcmToken = fcmToken;
        }

        public String getLang() {
            return lang == null ? "" : lang;
        }

        public void setLang(String lang) {
            this.lang = lang;
        }

        public Long getLastTime() {
            return lastTime == null ? -1 : lastTime;
        }

        public void setLastTime(Long lastTime) {
            this.lastTime = lastTime;
        }

        public String getRefreshToken() {
            return refreshToken == null ? "" : refreshToken;
        }

        public void setRefreshToken(String refreshToken) {
            this.refreshToken = refreshToken;
        }

        public String getSub() {
            return sub == null ? "" : sub;
        }

        public void setSub(String sub) {
            this.sub = sub;
        }

        public Integer getWorkingStatus() {
            return workingStatus == null ? -1 : workingStatus;
        }

        public void setWorkingStatus(Integer workingStatus) {
            this.workingStatus = workingStatus;
        }

    }
}