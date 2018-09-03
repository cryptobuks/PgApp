package com.app.qingyi.models;

import com.google.gson.annotations.Expose;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by ylkjcjq on 2017/12/9.
 */

public class PayStatus implements Serializable {
    @Expose
    private boolean isSuccess;
    @Expose
    private String message;
    @Expose
    private String qq ;
    @Expose
    private String wechat;
    @Expose
    private String tel;
    @Expose
    private String do3Fei;
    @Expose
    private String balance;

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public void setDo3Fei(String do3Fei) {
        this.do3Fei = do3Fei;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public String getMessage() {
        return message;
    }

    public String getQq() {
        return qq;
    }

    public String getWechat() {
        return wechat;
    }

    public String getTel() {
        return tel;
    }

    public String getDo3Fei() {
        return do3Fei;
    }

    public String getBalance() {
        return balance;
    }
}
