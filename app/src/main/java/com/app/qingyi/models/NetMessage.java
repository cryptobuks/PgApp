package com.app.qingyi.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;

/**
 * Created by ylkjcjq on 2017/12/9.
 */

public class NetMessage implements Serializable {

    @Expose
    private boolean isSuccess = false;
    @Expose
    private String reason;
    @Expose
    private int code = -1;
    @Expose
    private String message;

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
