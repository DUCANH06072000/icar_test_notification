package com.tatv.baseapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;


public class DataServer {

    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("data")
    @Expose
    private List<Object> data = null;
    @SerializedName("statusCode")
    @Expose
    private Integer statusCode;

    public String getMessage() {
        return message == null ? "" : message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<Object> getData() {
        return data == null ? new ArrayList<>() : data;
    }

    public void setData(List<Object> data) {
        this.data = data;
    }

    public Integer getStatusCode() {
        return statusCode == null ? -1 : statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    @Override
    public String toString() {
        return "DataServer{" +
                "message='" + message + '\'' +
                ", data=" + data +
                ", statusCode=" + statusCode +
                '}';
    }
}