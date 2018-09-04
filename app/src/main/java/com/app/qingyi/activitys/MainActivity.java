package com.app.qingyi.activitys;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import android.widget.TextView;
import android.widget.Toast;

import com.app.qingyi.Dialogs.UpdateAppDialog;
import com.app.qingyi.R;
import com.app.qingyi.adapters.TabFragmentPagerAdapter;
import com.app.qingyi.fragments.HomeMain1Fragment;
import com.app.qingyi.fragments.HomeMain2Fragment;
import com.app.qingyi.http.httputils.AllUrl;
import com.app.qingyi.http.httputils.AsyncTaskManager;
import com.app.qingyi.http.httputils.GsonUtils;
import com.app.qingyi.http.httputils.HttpUtil;
import com.app.qingyi.http.requestparams.BaseRequestParm;
import com.app.qingyi.http.responsebeans.BaseResponseBean;
import com.app.qingyi.http.responsebeans.RequestListener;
import com.app.qingyi.models.AD;
import com.app.qingyi.models.AppUpdate;
import com.app.qingyi.utils.DownLoadBR;
import com.app.qingyi.utils.GlobleValue;
import com.app.qingyi.utils.Utils;
import com.google.gson.JsonObject;
import java.util.ArrayList;
import java.util.List;


import es.dmoral.toasty.Toasty;

import static android.os.Build.*;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private ImageView btn_pre_01,btn_pre_04;
    private int INSTALL_PACKAGES_REQUESTCODE = 0x7777;
    private TextView tv_tab_01,  tv_tab_04;
    private FrameLayout fmpan;
    private LayoutInflater inflater;
    private String updateContent = "";
    private DownLoadBR mDownLoadBR;
    private AppUpdate appdata;
    private PermissionListener permissionListener;
    private int curFragment = 1;
    private ViewPager mViewPager;
    private ArrayList<Fragment> list;

    public interface PermissionListener {

        void onGranted();

        void onDenied(List<String> deniedPermissions);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mViewPager = (ViewPager)findViewById(R.id.mViewPager);
        list = new ArrayList<>();
        list.add(new HomeMain1Fragment());
        list.add(new HomeMain2Fragment());
        TabFragmentPagerAdapter adapter = new TabFragmentPagerAdapter(getSupportFragmentManager(), list);
        mViewPager.setAdapter(adapter);
        mViewPager.setCurrentItem(0);
        checkOutappVersion();
        openrequestPermission();
        getAdvertisement();
    }

    private void openrequestPermission() {
        if (VERSION.SDK_INT >= VERSION_CODES.M) {
            //如果是6.0或6.0以上，则要申请运行时权限,这里需要申请拍照的权限
            requestRuntimePermission(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, new PermissionListener() {
                @Override
                public void onGranted() {
                }

                @Override
                public void onDenied(List<String> deniedPermissions) {
                    Snackbar.make(mViewPager,"所需权限被拒绝,如无法正常使用，请设置修改",Snackbar.LENGTH_LONG).show();
                }
            });
        }
    }

    /**
     * 申请运行时权限
     */
    public void requestRuntimePermission(String[] permissions, PermissionListener permissionListener) {
        this.permissionListener = permissionListener;
        List<String> permissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionList.add(permission);
            }
        }

        if (!permissionList.isEmpty()) {
            ActivityCompat.requestPermissions(this, permissionList.toArray(new String[permissionList.size()]), 1);
        } else {
            permissionListener.onGranted();
        }
    }

    private void initView() {
        inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        fmpan = (FrameLayout) findViewById(R.id.bottom);
        btn_pre_01 = (ImageView) findViewById(R.id.btn_pre_01);
        btn_pre_04 = (ImageView) findViewById(R.id.btn_pre_04);
        tv_tab_01 = (TextView) findViewById(R.id.tv_tab_01);
        tv_tab_04 = (TextView) findViewById(R.id.tv_tab_04);
        btn_pre_01.setVisibility(View.VISIBLE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tab1:
                if (curFragment != 1) {
                    hidebtn();
                    curFragment = 1;
                    btn_pre_01.setVisibility(View.VISIBLE);
                    tv_tab_01.setVisibility(View.VISIBLE);
                    mViewPager.setCurrentItem(0);
                }
                break;

            case R.id.tab4:
                if (curFragment != 4) {
                    hidebtn();
                    curFragment = 4;
                    btn_pre_04.setVisibility(View.VISIBLE);
                    tv_tab_04.setVisibility(View.VISIBLE);
                    mViewPager.setCurrentItem(3);
                }
                break;
            case R.id.layoutAdd:
                if (mLoginConfig.getAuthorization() != null && !mLoginConfig.getAuthorization().equals("")) {
                    startActivity(new Intent(MainActivity.this, CreateActivity.class));
                }else {
                    Toasty.normal(this,"请先登录").show();
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                break;
            default:

                break;
        }
    }

    private void hidebtn() {
        tv_tab_01.setVisibility(View.GONE);
        tv_tab_04.setVisibility(View.GONE);
        btn_pre_01.setVisibility(View.GONE);
        btn_pre_04.setVisibility(View.GONE);
    }

    private void checkOutappVersion() {
        String url = AllUrl.getInstance().getAppVersionUrl();
        if (HttpUtil.isNetworkAvailable(this) && !GlobleValue.Downloading) {
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(this, url, "",
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
                                    appdata = GsonUtils.JsonObjectToBean(object.getAsJsonObject("android"), AppUpdate.class);
                                    mLoginConfig.setVersion(appdata.getVersionCode());
                                    if (Utils.getVerCode(MainActivity.this) < appdata.getVersionCode()) {
                                        updateContent = appdata.getUpdateContent();
                                        handler.sendEmptyMessage(GlobleValue.SUCCESS);
                                    } else {
                                        handler.sendEmptyMessage(GlobleValue.FAIL);
                                    }
                                }
                            }
                        }
                    }, this);
        }else {
            Snackbar.make(mViewPager,"网络未连接",Snackbar.LENGTH_LONG).show();
        }
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobleValue.SUCCESS://isMustUpdate
                    createUpdateDialog(updateContent);
                    break;
                case GlobleValue.FAIL:
                    break;
            }
        }
    };
    UpdateAppDialog.Builder dialog;

    private void createUpdateDialog(String updateContent) {
        dialog = new UpdateAppDialog.Builder(this);
        dialog.setTitle("App升级提示").
                setShowNegativeButton(!appdata.isMustUpdate()).setContent(updateContent)
                .setNegativeButton(new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialog.dismiss();
                    }
                }).setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (!appdata.isMustUpdate()) {
                    dialog.dismiss();
                }
                if (GlobleValue.Downloading != true) {
                    checkIsAndroidO();
                    return;
                }
                Snackbar.make(mViewPager,"正在下载安装包",Snackbar.LENGTH_LONG).show();
            }
        }).create().show();
    }

    private void  installApk(){
        if(dialog != null) dialog.setCommitText("请稍后");
        IntentFilter mIntentFilter = new IntentFilter("com.can.downloadComplete");
        //创建广播接收者的对象
        mDownLoadBR =  new DownLoadBR();
        registerReceiver(mDownLoadBR, mIntentFilter);
        Utils.startAppUpdate(MainActivity.this, appdata.getDownLoad(), "mobipromoBox-" + appdata.getVersion() + ".apk");
    };

    /**
     * 判断是否是8.0,8.0需要处理未知应用来源权限问题,否则直接安装
     */
    private void checkIsAndroidO() {
        if (Build.VERSION.SDK_INT >= 26) {
            boolean b = getPackageManager().canRequestPackageInstalls();
            if (b) {
                installApk();//安装应用的逻辑(写自己的就可以)
            } else {
                Uri packageURI = Uri.parse("package:" + getPackageName());
                //注意这个是8.0新API
                Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
                startActivityForResult(intent, INSTALL_PACKAGES_REQUESTCODE);
            }
        } else {
            installApk();
        }
    }


    private void getAdvertisement() {
        String url = AllUrl.getInstance().getAdvertisementUrl();
        if (HttpUtil.isNetworkAvailable(this) && !GlobleValue.Downloading) {
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(this, url, "",
                            AsyncTaskManager.ContentTypeJson, "GET", null),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onFailed() {
                            handler.sendEmptyMessage(GlobleValue.FAIL);
                        }

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            analiData(bean);
                        }

                        private void analiData(BaseResponseBean bean) {
                            if (GsonUtils.isGoodJson(bean.getResult())) {
                                AD mAD = GsonUtils.JsonObjectToBean(GsonUtils.getRootJsonObject(bean.getResult()), AD.class);
                                mLoginConfig.setADUrl(mAD.getImgUrl());
                                mLoginConfig.setADhttpUrl(mAD.getHttpUrl());
                                mLoginConfig.setAdTitle(mAD.getTitle());
                            }else {
                                mLoginConfig.setADUrl("");
                                mLoginConfig.setADhttpUrl("");
                                mLoginConfig.setAdTitle("");
                            }
                        }
                    }, this);
        }
    }

    @RequiresApi(api = VERSION_CODES.O)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == INSTALL_PACKAGES_REQUESTCODE){
            boolean b = getPackageManager().canRequestPackageInstalls();
            if (b) {
                installApk();//安装应用的逻辑(写自己的就可以)
            } else {
                Toasty.error(this,"授权失败，无法更新").show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        if(mDownLoadBR != null){
            unregisterReceiver(mDownLoadBR);
        }
        super.onDestroy();
    }
}

