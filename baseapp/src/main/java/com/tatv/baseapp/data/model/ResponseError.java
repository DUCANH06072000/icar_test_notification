package com.tatv.baseapp.data.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by tatv on 08/03/2023.
 */
public class ResponseError {
    @SerializedName("field")
    @Expose
    private String field;
    @SerializedName("error")
    @Expose
    private String error;

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
        return "ResponseError{" +
                "field='" + field + '\'' +
                ", error='" + error + '\'' +
                '}';
    }
}
