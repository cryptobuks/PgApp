package com.app.qingyi.http.httputils;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.util.Log;

import com.app.qingyi.activitys.LoginActivity;
import com.app.qingyi.activitys.MainActivity;
import com.app.qingyi.base.BaseApplication;
import com.google.gson.JsonObject;
import com.app.qingyi.http.requestparams.BaseRequestParm;
import com.app.qingyi.http.responsebeans.BaseResponseBean;
import com.app.qingyi.http.responsebeans.RequestListener;
import com.app.qingyi.utils.LoginConfig;
import com.app.qingyi.utils.SystemLog;
import com.app.qingyi.utils.Utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @description异步任务管理类 concurrent java.util.concurrent 是在并发编程中很常用的实用工具类。
 */
public class AsyncTaskManager {

    private ExecutorService mPool = null;
    public static String ContentTypeXfl = "application/x-www-form-urlencoded";
    public static String ContentTypeTS = "application/octet-stream";
    public static String ContentTypeJson = "application/json; charset=UTF-8";
    /* token */
    public static String LoginToken = "Basic cHJvbW9zZXJ2ZXI6ZTYxOTcyMDViYTZmOWM2";
    private static AsyncTaskManager mTask;
    private String result = "";

    // 声明TASK函数类
    private AsyncTaskManager(int nThreads) {
        mPool = Executors.newFixedThreadPool(nThreads);
    }

    public static AsyncTaskManager getInstance() {
        int nThreads = Utils.getNumCores();
        mTask = new AsyncTaskManager(nThreads * 2);
        return mTask;
    }

    // 网络交互
    public void StartHttp(final BaseRequestParm parms, final RequestListener<BaseResponseBean> listener,
                          final Context context) {
        mPool.execute(new Runnable() {

            public void run() {
                final LoginConfig mloginConfig = new LoginConfig(context);
                // token未过期，继续操作
                BaseResponseBean responseBean = getResponseBean(parms);
                if (responseBean.response == null) {
                    listener.onFailed();// 认证失败
                } else {
                    if (responseBean.response.equals("401")) {
                        refresh(parms, listener, mloginConfig);
                    } else {
                        listener.onComplete(responseBean);
                    }
                }
            }
        });
    }

    // bitmap
    public void StartBitmapHttp(final BaseRequestParm parms, final RequestListener<BaseResponseBean> listener,
                          final Context context) {
        mPool.execute(new Runnable() {

            public void run() {
                final LoginConfig mloginConfig = new LoginConfig(context);
                // token未过期，继续操作
                BaseResponseBean responseBean = getResponseBitmap(parms);
                if (responseBean.response == null) {
                    listener.onFailed();// 认证失败
                } else {
                    listener.onComplete(responseBean);
                }
            }
        });
    }

    private void refresh(final BaseRequestParm parms, final RequestListener<BaseResponseBean> listener, LoginConfig mloginConfig) {
        // 刷新token
        String jsonData = "grant_type=refresh_token" + "&refresh_token="
                + mloginConfig.getReAuthorization();
        BaseRequestParm refreshParm = new BaseRequestParm(parms.getContext(), AllUrl.getInstance().getRefreshTokenUrl(), jsonData,
                AsyncTaskManager.ContentTypeXfl, "POST", AsyncTaskManager.LoginToken);
        BaseResponseBean refreshBean = getResponseBean(refreshParm);
        if (analysis(refreshBean, mloginConfig)) {
            // 刷新成功 继续下一步
            parms.setAuthorization(LoginConfig.getAuthorization());// token重新设置
            BaseResponseBean responseBean = getResponseBean(parms);
            listener.onComplete(responseBean);
        } else {
//            ((BaseApplication) parms.getContext().getApplicationContext()).exitApp();
            LoginConfig.setAuthorization(null);
            parms.getContext().startActivity(new Intent(parms.getContext(),LoginActivity.class));
            listener.onFailed();
            SystemLog.Log("token刷新失败");
        }
    }

    // 解析刷新的数据
    private boolean analysis(BaseResponseBean bean, LoginConfig mloginConfig) {
        if (bean.isSuccess()) {
            JsonObject json = GsonUtils.getRootJsonObject(bean.getResult());
            String token = GsonUtils.getKeyValue(json, "access_token").getAsString();
            String refresh_token = GsonUtils.getKeyValue(json, "refresh_token").getAsString();
            String expires_in = GsonUtils.getKeyValue(json, "expires_in").getAsString();// 有效时间
            mloginConfig.setAuthorization("Bearer " + token);
            mloginConfig.setReAuthorization(refresh_token);
            return true;
        }
        return false;
    }

    private BaseResponseBean getResponseBean(BaseRequestParm parms) {
        result = HttpUtil.uploadData(parms);
        return new BaseResponseBean(result);
    }

    public void StartHttpUpload(final BaseRequestParm parms, final RequestListener<BaseResponseBean> listener) {
        mPool.execute(new Runnable() {
            @Override
            public void run() {
                HttpUtil.uploadFile(parms,new RequestListener<BaseResponseBean>(){
                    @Override
                    public void onComplete(BaseResponseBean bean) {
                        listener.onComplete(bean);
                    }

                    @Override
                    public void onFailed() {
                        listener.onFailed();
                    }
                });
            }
        });
    }

    private BaseResponseBean getResponseBitmap(BaseRequestParm parms) {
        Bitmap bitmap = HttpUtil.getHttpBitmap(parms);
        BaseResponseBean base = new BaseResponseBean("");
        base.setBitmap(bitmap);
        return base;
    }
}
