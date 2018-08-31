package com.app.qingyi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**
 * Created by cjq on 17/8/23.
 * 系统配置
 */

public class LoginConfig {
    public static SharedPreferences sp;

    public LoginConfig(Context context) {
        super();
        sp = context.getSharedPreferences("Loginconfig", Context.MODE_PRIVATE);
    }

    // Cookie
    public static void setCookie(String Cookie) {
        Editor ed = sp.edit();
        ed.putString("Cookie", Cookie);
        ed.commit();
    }

    public static String getCookie() {
        return sp.getString("Cookie", "");
    }

    // 认证码,保持登陆状态
    public static String getAuthorization() {
        return sp.getString("Authorization", "");
    }

    // 认证码,保持登陆状态
    public static void setAuthorization(String ContentType) {
        Editor ed = sp.edit();
        ed.putString("Authorization", ContentType);
        ed.commit();
    }

    // 刷新token
    public static String getReAuthorization() {
        return sp.getString("reAuthorization", "");
    }

    // 刷新token
    public void setReAuthorization(String ContentType) {
        Editor ed = sp.edit();
        ed.putString("reAuthorization", ContentType);
        ed.commit();
    }

    public void setAccount(String account) {
        Editor ed = sp.edit();
        ed.putString("account", account);
        ed.commit();
    }

    public static String getAccount() {
        String account = sp.getString("account", "");
        return account == null ? "":account;
    }


    public static String getADUrl() {
        return sp.getString("ADUrl", null);
    }

    public static void setADUrl(String url) {
        Editor ed = sp.edit();
        ed.putString("ADUrl", url);
        ed.commit();
    }

    public static String getAdTitle() {
        return sp.getString("title", null);
    }

    public static void setAdTitle(String url) {
        Editor ed = sp.edit();
        ed.putString("title", url);
        ed.commit();
    }

    public static String getADhttpUrl() {
        return sp.getString("ADhttpUrl", "");
    }

    public static void setADhttpUrl(String url) {
        Editor ed = sp.edit();
        ed.putString("ADhttpUrl", url);
        ed.commit();
    }

    public void setVersion(int version) {
        Editor ed = sp.edit();
        ed.putInt("version", version);
        ed.commit();
    }

    public static int getVersion() {
        return sp.getInt("version", 0);
    }
}
