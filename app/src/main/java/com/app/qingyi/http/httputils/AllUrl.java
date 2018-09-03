package com.app.qingyi.http.httputils;


/**
 * Created by cjq on 16/9/26.
 */

public class AllUrl {

    private static AllUrl mAllUrl;

    public static AllUrl getInstance() {
        if (mAllUrl == null) {
            mAllUrl = new AllUrl();
        }
        return mAllUrl;
    }

    //登录账户
    public String getLoginAccountUrl() {
        return Url.BASE_URL + "/account/login";
    }

    public String getRegisterAccountUrl() {
        return Url.BASE_URL + "/account/register";
    }

    public String getLoginOutAccountUrl() {
        return Url.BASE_URL + "/token/logout";
    }

    //刷新token
    public String getRefreshTokenUrl() {
        return Url.BASE_URL + "/token/refresh";
    }

    public String getDefailtGoodsUrl(int curPage, int limit) {
        return Url.BASE_URL + "/goods/list/" + curPage + "/" + limit;
    }

    public String getGoodsByAreaUrl(String area, int curPage, int limit) {
        return Url.BASE_URL + "/goods/list/by/area/" + area + "/" + curPage + "/" + limit;
    }

    public String get7days() {
        return Url.BASE_URL + "/authed/account/allbox/recentSevenDayCoin";
    }

    public String getAppVersionUrl() {
        return Url.BASE_URL + "/app/appcheck/version";
    }

    public String getCapTurnCode() {
        return Url.BASE_URL + "/account/recaptcha/captcha.jpg";
    }

    public String getCANStatusUrl() {
        return Url.BASE_URL + "/authed/account/can/status";
    }


    public String getAdvertisementUrl() {
        return Url.BASE_URL + "/manage/advertisement/getFirstPageAd";
    }
}
