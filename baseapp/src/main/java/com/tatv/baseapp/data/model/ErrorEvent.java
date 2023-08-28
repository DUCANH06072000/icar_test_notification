package com.tatv.baseapp.data.model;

import java.util.List;

/**
 * Created by tatv on 07/10/2022.
 */
public class ErrorEvent {
    private String message;
    private List<String> data = null;
    private int statusCode;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<String> getData() {
        return data;
    }

    public void setData(List<String> data) {
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
}
