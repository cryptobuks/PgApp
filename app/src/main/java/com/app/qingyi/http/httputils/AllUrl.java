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

    public String getRegisterUrl() {
        return Url.BASE_URL + "/account/register";
    }

    //登录账户
    public String getLoginAccountUrl() {
        return Url.BASE_URL + "/account/login";
    }

    public String getTitlesUrl() {
        return Url.BASE_URL + "/get/citys/by/city/";
    }

    public String getLoginOutAccountUrl() {
        return Url.BASE_URL + "/account/loginout";
    }

    //刷新token
    public String getRefreshTokenUrl() {
        return Url.BASE_URL + "/token/refresh";
    }

    public String getDefailtGoodsUrl(int curPage, int limit) {
        return Url.BASE_URL + "/goods/list/" + curPage + "/" + limit;
    }

    public String getHistoryGoodsUrl(int curPage, int limit) {
        return Url.BASE_URL + "/goods/history/" + curPage + "/" + limit;
    }

    public String getGoodsByAreaUrl(int curPage, int limit) {
        return Url.BASE_URL + "/goods/list/by/area/" + curPage + "/" + limit;
    }

    public String getAccountUrl() {
        return Url.BASE_URL + "/account/dobi/detailed";
    }

    public String getAppVersionUrl() {
        return Url.BASE_URL + "/app/appcheck/version";
    }

    public String getCapTurnCode() {
        return Url.BASE_URL + "/account/recaptcha/captcha.jpg";
    }

    public String getGoodsDetailsUrl(int id) {
        return Url.BASE_URL + "/goods/list/item/details/" + id;
    }

    public String getGoodsPrivateUrl(int id) {
        return Url.BASE_URL + "/goods/list/item/private/" + id;
    }

    public String getGoodsPrivateByPayUrl(int id) {
        return Url.BASE_URL + "/goods/list/item/private/bybuy/" + id;
    }

    public String getAdvertisementUrl() {
        return Url.BASE_URL + "/manage/advertisement/getFirstPageAd";
    }

    public String getfileuploadUrl() {
        return Url.BASE_URL + "/file/upload";
    }

    public String getGoodsUploadUrl() {
        return Url.BASE_URL + "/goods/create";
    }
}
