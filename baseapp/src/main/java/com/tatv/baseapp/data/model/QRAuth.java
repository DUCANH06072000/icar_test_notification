package com.tatv.baseapp.data.model;

import java.util.List;

/**
 * Created by tatv on 07/10/2022.
 */
public class QRAuth {
    private String message;
    private List<DataQRAuth> data = null;
    private int statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DataQRAuth> getData() {
        return data;
    }

    public void setData(List<DataQRAuth> data) {
        this.data = data;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "QRAuth{" +
                "message='" + message + '\'' +
                ", data=" + data +
                ", statusCode='" + statusCode + '\'' +
                '}';
    }

    public class DataQRAuth {
        private String authEvent;
        private long qrExpiresAt;
        private String qrCode;

        public String getAuthEvent() {
            return authEvent;
        }

        public void setAuthEvent(String authEvent) {
            this.authEvent = authEvent;
        }

        public long getQrExpiresAt() {
            return qrExpiresAt;
        }

        public void setQrExpiresAt(long qrExpiresAt) {
            this.qrExpiresAt = qrExpiresAt;
        }

        public String getQrCode() {
            return qrCode;
        }

        public void setQrCode(String qrCode) {
            this.qrCode = qrCode;
        }

        @Override
        public String toString() {
            return "DataQRAuth{" +
                    "authEvent='" + authEvent + '\'' +
                    ", qrExpiresAt=" + qrExpiresAt +
                    ", qrCode='" + qrCode + '\'' +
                    '}';
        }
    }
}
