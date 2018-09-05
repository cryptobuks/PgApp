package com.app.qingyi.fragments;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.qingyi.Dialogs.SureDialog;
import com.app.qingyi.Dialogs.UpdateAppDialog;
import com.app.qingyi.R;
import com.app.qingyi.activitys.HistoryActivity;
import com.app.qingyi.activitys.LoginActivity;
import com.app.qingyi.activitys.RechargeActivity;
import com.app.qingyi.http.httputils.AllUrl;
import com.app.qingyi.http.httputils.AsyncTaskManager;
import com.app.qingyi.http.httputils.GsonUtils;
import com.app.qingyi.http.httputils.HttpUtil;
import com.app.qingyi.http.requestparams.BaseRequestParm;
import com.app.qingyi.http.responsebeans.BaseResponseBean;
import com.app.qingyi.http.responsebeans.RequestListener;
import com.app.qingyi.models.AccountStatus;
import com.app.qingyi.models.AppUpdate;
import com.app.qingyi.utils.GlobleValue;
import com.app.qingyi.utils.LoginConfig;
import com.app.qingyi.utils.Utils;
import com.app.qingyi.views.SquareRelativeLayout;
import com.google.gson.JsonObject;
import com.zhy.autolayout.utils.AutoUtils;

/**
 * @author CJQ
 */
public class HomeMain2Fragment extends LazyFragment implements View.OnClickListener {

    private Context mContext;
    private AccountStatus mAccountStatus;
    private LoginConfig mLoginConfig;
    private AppUpdate appUpdate;
    private TextView tvAmount, account,logout;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobleValue.SUCCESS:
//                    ((BaseApplication) mContext.getApplicationContext()).exitApp();
                    LoginConfig.setAuthorization(null);
                    startActivity(new Intent(mContext, LoginActivity.class));
                    break;
                case GlobleValue.SUCCESS2:
                    if (mAccountStatus != null) {
                        tvAmount.setText(mAccountStatus.getBalance());
                        account.setText(mAccountStatus.getAccount());
                    }
                    break;
                case GlobleValue.SUCCESS3:
                    if (mLoginConfig.getVersion() > Utils.getVerCode(mContext)) {
                        createUpdateDialog(appUpdate.getUpdateContent());
                    } else {
                        Snackbar.make(tvAmount, "当前已是最新版本", Snackbar.LENGTH_LONG).show();
                    }
                    break;
                case GlobleValue.FAIL:
                    tvAmount.setText("0");
                    account.setText("未登录");
                    logout.setVisibility(View.GONE);
                    break;
            }
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main2, null);
        mContext = view.getContext();
        initView(view);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        lazyLoad();
    }

    private void initView(View view) {
        tvAmount = (TextView) view.findViewById(R.id.tvAmount);
        account = (TextView) view.findViewById(R.id.account);
        view.findViewById(R.id.layout001).setOnClickListener(this);
        view.findViewById(R.id.layout002).setOnClickListener(this);
        logout = (TextView)view.findViewById(R.id.logout);
        logout.setOnClickListener(this);
        account.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.layout001:
                if (mAccountStatus != null) {
                    Intent intent = new Intent(view.getContext(), RechargeActivity.class);
                    intent.putExtra("address",mAccountStatus.getAddress());
                    intent.putExtra("minEth",mAccountStatus.getMinEth());
                    intent.putExtra("ethToDo",mAccountStatus.getEthToDo());
                    startActivity(intent);
                }
                break;
            case R.id.layout002:
                if (mAccountStatus != null) {
                    startActivity(new Intent(mContext,HistoryActivity.class));
                }
                break;
            case R.id.account:
                if (LoginConfig.getAuthorization() == null || LoginConfig.getAuthorization().equals("")) {
                    startActivity(new Intent(mContext, LoginActivity.class));
                }
                break;
            case R.id.logout:
                sureLogOut();
                break;
            default:

                break;
        }
    }

    private void sureLogOut() {
        String msg = "您确定要退出登录吗？";
        final SureDialog.Builder dialog = new SureDialog.Builder(mContext);
        dialog.setTitle(msg).setNegativeButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        }).setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
                logOut();
            }
        }).create().show();
    }

    private void logOut() {
        String url = AllUrl.getInstance().getLoginOutAccountUrl();
        if (HttpUtil.isNetworkAvailable(mContext)) {
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(mContext, url, null,
                            AsyncTaskManager.ContentTypeJson, "GET", LoginConfig.getAuthorization()),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onFailed() {
                            handler.sendEmptyMessage(GlobleValue.SUCCESS);
                        }

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            handler.sendEmptyMessage(GlobleValue.SUCCESS);
                        }
                    }, mContext);
        } else {
            Snackbar.make(tvAmount, "网络未连接", Snackbar.LENGTH_LONG).show();
        }
    }

    private void getAccount() {
        if (LoginConfig.getAuthorization() == null || LoginConfig.getAuthorization().equals("")) {
            tvAmount.setText("0");
            account.setText("未登录");
            logout.setVisibility(View.GONE);
            return;
        }
        logout.setVisibility(View.VISIBLE);
        String url = AllUrl.getInstance().getAccountUrl();
        if (HttpUtil.isNetworkAvailable(mContext)) {
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(mContext, url, null,
                            AsyncTaskManager.ContentTypeJson, "GET", LoginConfig.getAuthorization()),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onFailed() {
                            handler.sendEmptyMessage(GlobleValue.FAIL);
                        }

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            if (bean.isSuccess()) {
                                analiData(bean);
                            }else handler.sendEmptyMessage(GlobleValue.FAIL);
                        }

                        private void analiData(BaseResponseBean bean) {
                            mAccountStatus = GsonUtils.JsonObjectToBean(GsonUtils.getRootJsonObject(bean.getResult()), AccountStatus.class);
                            handler.sendEmptyMessage(GlobleValue.SUCCESS2);
                        }
                    }, mContext);
        } else {
            Snackbar.make(tvAmount, "网络未连接", Snackbar.LENGTH_LONG).show();
        }
    }

    private void checkOutappVersion() {
        String url = AllUrl.getInstance().getAppVersionUrl();
        if (HttpUtil.isNetworkAvailable(mContext) && !GlobleValue.Downloading) {
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(mContext, url, "",
                            AsyncTaskManager.ContentTypeJson, "GET", null),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onFailed() {
                            handler.sendEmptyMessage(GlobleValue.FAIL);
                        }

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            if (bean.isSuccess()) {
                                analiData(bean);
                            } else {
                                handler.sendEmptyMessage(GlobleValue.FAIL);
                            }
                        }

                        private void analiData(BaseResponseBean bean) {
                            handler.sendEmptyMessage(GlobleValue.FAIL);
                            if (GsonUtils.isGoodJson(bean.getResult())) {
                                Log.e("update", bean.getResult());
                                JsonObject object = GsonUtils.getRootJsonObject(bean.getResult());
                                if (object.has("android")) {
                                    appUpdate = GsonUtils.JsonObjectToBean(object.getAsJsonObject("android"), AppUpdate.class);
                                    mLoginConfig.setVersion(appUpdate.getVersionCode());
                                    handler.sendEmptyMessage(GlobleValue.SUCCESS3);
                                }
                            }
                        }
                    }, mContext);
        } else {
            Snackbar.make(tvAmount, "网络未连接", Snackbar.LENGTH_LONG).show();
        }
    }

    private void createUpdateDialog(String updateContent) {
        final UpdateAppDialog.Builder dialog = new UpdateAppDialog.Builder(mContext);
        dialog.setTitle("App升级提示").
                setShowNegativeButton(!appUpdate.isMustUpdate()).setContent(updateContent)
                .setNegativeButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                }).setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!appUpdate.isMustUpdate()) {
                    dialog.dismiss();
                }
                if (GlobleValue.Downloading != true) {
                    Utils.startAppUpdate(mContext, appUpdate.getDownLoad(), "do-" + appUpdate.getVersion() + ".apk");
                    return;
                }
                Snackbar.make(tvAmount, "正在下载安装包", Snackbar.LENGTH_LONG).show();
            }
        }).create().show();
    }

    @Override
    protected void lazyLoad() {
        getAccount();
    }
}
