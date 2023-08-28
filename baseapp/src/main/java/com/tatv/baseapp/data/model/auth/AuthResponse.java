package com.tatv.baseapp.data.model.auth;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.tatv.baseapp.data.model.ResponseError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tatv on 28/02/2023.
 */
public class AuthResponse {

    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<AuthResponseData> data;
    @SerializedName("errors")
    @Expose
    private List<ResponseError> errors;

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<AuthResponseData> getData() {
        return data;
    }

    public void setData(List<AuthResponseData> data) {
        this.data = data;
    }

    public List<ResponseError> getErrors() {
        return errors;
    }

    public void setErrors(List<ResponseError> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "LoginResponse{" +
                "statusCode=" + statusCode +
                ", message='" + message + '\'' +
                ", data=" + data +
                ", errors='" + errors + '\'' +
                '}';
    }

    public class AuthResponseData {
        @SerializedName("deviceMac")
        @Expose
        private String deviceMac;
        @SerializedName("deviceName")
        @Expose
        private String deviceName;
        @SerializedName("appId")
        @Expose
        private String appId;
        @SerializedName("accessToken")
        @Expose
        private String accessToken;
        @SerializedName("account")
        @Expose
        private String account;
        @SerializedName("appVersion")
        @Expose
        private String appVersion;
        @SerializedName("car")
        @Expose
        private Car car;
        @SerializedName("createdAt")
        @Expose
        private Long createdAt;
        @SerializedName("deviceId")
        @Expose
        private String deviceId;
        @SerializedName("displayName")
        @Expose
        private String displayName;
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
        @SerializedName("userId")
        @Expose
        private String userId;
        @SerializedName("exp")
        @Expose
        private Long exp;
        @SerializedName("workingStatus")
        @Expose
        private Integer workingStatus;
        @SerializedName("field")
        @Expose
        private String field;
        @SerializedName("error")
        @Expose
        private String error;

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

        public String getAccount() {
            return account == null ? "" : account;
        }

        public void setAccount(String account) {
            this.account = account;
        }

        public Car getCar() {
            return car;
        }

        public void setCar(Car car) {
            this.car = car;
        }

        public Long getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(Long createdAt) {
            this.createdAt = createdAt;
        }

        public String getDisplayName() {
            return displayName == null || displayName.equals("") ? getAccount().equals("") ? "N/A" : getAccount() : displayName;
        }

        public void setDisplayName(String displayName) {
            this.displayName = displayName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public Long getExp() {
            return exp == null ? 0 : exp;
        }

        public void setExp(Long exp) {
            this.exp = exp;
        }

        public String getField() {
            return field;
        }

        public void setField(String field) {
            this.field = field;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        @Override
        public String toString() {
            return "AuthResponseData{" +
                    "appId='" + appId + '\'' +
                    ", accessToken='" + accessToken + '\'' +
                    ", account='" + account + '\'' +
                    ", appVersion='" + appVersion + '\'' +
                    ", car=" + car +
                    ", createdAt=" + createdAt +
                    ", deviceId='" + deviceId + '\'' +
                    ", displayName='" + displayName + '\'' +
                    ", fcmToken='" + fcmToken + '\'' +
                    ", lang='" + lang + '\'' +
                    ", lastTime=" + lastTime +
                    ", refreshToken='" + refreshToken + '\'' +
                    ", sub='" + sub + '\'' +
                    ", userId='" + userId + '\'' +
                    ", exp=" + exp +
                    ", workingStatus=" + workingStatus +
                    ", field='" + field + '\'' +
                    ", error='" + error + '\'' +
                    '}';
        }

        public class Car {
            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("userId")
            @Expose
            private String userId;
            @SerializedName("plate")
            @Expose
            private String plate;
            @SerializedName("brand")
            @Expose
            private String brand;
            @SerializedName("model")
            @Expose
            private String model;
            @SerializedName("year")
            @Expose
            private String year;
            @SerializedName("option")
            @Expose
            private String option;
            @SerializedName("color")
            @Expose
            private String color;
            @SerializedName("image")
            @Expose
            private String image;

            public String getId() {
                return id == null ? "" : id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserId() {
                return userId == null ? "" : userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getPlate() {
                return plate == null ? "" : plate;
            }

            public void setPlate(String plate) {
                this.plate = plate;
            }

            public String getBrand() {
                return brand == null ? "" : brand;
            }

            public void setBrand(String brand) {
                this.brand = brand;
            }

            public String getModel() {
                return model == null ? "" : model;
            }

            public void setModel(String model) {
                this.model = model;
            }

            public String getYear() {
                return year == null ? "" : year;
            }

            public void setYear(String year) {
                this.year = year;
            }

            public String getOption() {
                return option == null ? "" : option;
            }

            public void setOption(String option) {
                this.option = option;
            }

            public String getColor() {
                return color == null ? "" : color;
            }

            public void setColor(String color) {
                this.color = color;
            }

            public String getImage() {
                return image == null ? "" : image;
            }

            public void setImage(String image) {
                this.image = image;
            }


            @Override
            public String toString() {
                return "Car{" +
                        "id='" + id + '\'' +
                        ", userId='" + userId + '\'' +
                        ", plate='" + plate + '\'' +
                        ", brand='" + brand + '\'' +
                        ", model='" + model + '\'' +
                        ", year='" + year + '\'' +
                        ", option='" + option + '\'' +
                        ", color='" + color + '\'' +
                        ", image='" + image + '\'' +
                        '}';
            }
        }

    }

}