package com.app.qingyi.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.text.InputType;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.qingyi.base.BaseApplication;
import com.app.qingyi.models.MessageData;
import com.google.gson.JsonObject;
import com.app.qingyi.R;
import com.app.qingyi.http.httputils.AllUrl;
import com.app.qingyi.http.httputils.AsyncTaskManager;
import com.app.qingyi.http.httputils.GsonUtils;
import com.app.qingyi.http.httputils.HttpUtil;
import com.app.qingyi.http.requestparams.BaseRequestParm;
import com.app.qingyi.http.responsebeans.BaseResponseBean;
import com.app.qingyi.http.responsebeans.RequestListener;
import com.app.qingyi.utils.GlobleValue;


public class RegisterActivity extends BaseActivity implements View.OnClickListener {

    private EditText mAccount;
    private MessageData mMessage;
    private EditText mPassword, edtCode,mPassword2;
    private View mLoginFormView;
    private ImageView imgCode;
    private Bitmap bit;
    private Boolean isChecked = true;
    private TextView reload,tvYZM,tvMM,tvZH,tvMMNo;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideDialog();
            setInvisible();
            switch (msg.what) {
                case GlobleValue.SUCCESS:
                    if(mMessage.isSuccess()){
                        Snackbar.make(mAccount,"注册成功",Snackbar.LENGTH_LONG).addCallback(new Snackbar.Callback(){
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                mLoginConfig.setAccount(mAccount.getText().toString().trim());
                                finish();
                            }
                        }).show();
                    }else {
                        Snackbar.make(mAccount,mMessage.getMessage(),Snackbar.LENGTH_LONG).show();
                    }
                    break;
                case 0x888:
                    if (bit != null) {
                        reload.setVisibility(View.INVISIBLE);
                        imgCode.setImageBitmap(bit);
                    } else reload.setVisibility(View.VISIBLE);
                    break;
                case GlobleValue.FAIL:
                    Snackbar.make(mAccount,"注册出现错误，请重试",Snackbar.LENGTH_LONG).show();
                    break;
                case GlobleValue.FAIL2:
                    tvYZM.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAccount = (EditText) findViewById(R.id.account);
        mPassword = (EditText) findViewById(R.id.password);
        mPassword2 = (EditText) findViewById(R.id.password2);

        edtCode = (EditText) findViewById(R.id.edtCode);
        mLoginFormView = findViewById(R.id.login_form);
        imgCode = (ImageView) findViewById(R.id.imgCode);
        reload = (TextView) findViewById(R.id.reload);

        tvZH = (TextView) findViewById(R.id.tvZH);
        tvMM = (TextView) findViewById(R.id.tvMM);
        tvMMNo = (TextView) findViewById(R.id.tvMMNo);
        tvYZM = (TextView) findViewById(R.id.tvYZM);
        imgCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getHttpBitmap();
            }
        });
    }

    @Override
    protected void onResume() {
        getHttpBitmap();
        super.onResume();
    }


    /**
     * 获取网落图片资源
     *
     * @param
     * @return
     */
    private void getHttpBitmap() {
        String url = AllUrl.getInstance().getCapTurnCode();
        if (HttpUtil.isNetworkAvailable(this)) {
            AsyncTaskManager.getInstance().StartBitmapHttp(new BaseRequestParm(this, url, "",
                            AsyncTaskManager.ContentTypeXfl, "GET", AsyncTaskManager.LoginToken),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onFailed() {
                            handler.sendEmptyMessage(GlobleValue.FAIL);
                        }

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            if (bean.getBitmap() != null) {
                                bit = bean.getBitmap();
                                handler.sendEmptyMessage(0x888);
                            }
                        }
                    }, this);
        } else {
            Snackbar.make(mAccount,"网络未连接",Snackbar.LENGTH_LONG).show();
        }
    }

    private void attemptRegister() {
        setInvisible();
        String email = mAccount.getText().toString();
        String password = mPassword.getText().toString();
        String password2 = mPassword2.getText().toString();
        String captchaCode = edtCode.getText().toString();
        if (TextUtils.isEmpty(email)) {
            tvZH.setVisibility(View.VISIBLE);
            return;
        }

        if (TextUtils.isEmpty(password) || password.length() < 6) {
            tvMM.setVisibility(View.VISIBLE);
            return;
        }

        if (!password.equals(password2)) {
            tvMMNo.setVisibility(View.VISIBLE);
            return;
        }

        if (TextUtils.isEmpty(captchaCode) || captchaCode.length() != 4) {
            tvYZM.setVisibility(View.VISIBLE);
            return;
        }

        doRegister(email, password, captchaCode);
    }

    private void setInvisible(){
        tvZH.setVisibility(View.GONE);
        tvMM.setVisibility(View.GONE);
        tvYZM.setVisibility(View.GONE);
        tvMMNo.setVisibility(View.GONE);
    }
    private void doRegister(final String name, String psw, String captchaCode) {
        String date = "account=" + name + "&password=" + psw + "&grant_type=password" + "&captcha=" + captchaCode;
        String url = AllUrl.getInstance().getRegisterUrl();
        if (HttpUtil.isNetworkAvailable(this)) {
            showDialog();
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(this, url, date,
                            AsyncTaskManager.ContentTypeXfl, "POST", AsyncTaskManager.LoginToken),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onFailed() {
                            handler.sendEmptyMessage(GlobleValue.FAIL);
                        }

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            if (bean.isSuccess()) {
                                analiData(bean);
                            } else
                                handler.sendEmptyMessage(GlobleValue.FAIL);
                        }
                    }, this);
        } else {
            Snackbar.make(mAccount,"网络未连接",Snackbar.LENGTH_LONG).show();
        }
    }

    private void analiData(BaseResponseBean bean) {
        try {
            mMessage = GsonUtils.JsonObjectToBean(GsonUtils.getRootJsonObject(bean.getResult()), MessageData.class);
            handler.sendEmptyMessage(GlobleValue.SUCCESS);
        }catch (Exception e){
            handler.sendEmptyMessage(GlobleValue.FAIL);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register:
                finish();
                break;
            case R.id.loginBtn:
                attemptRegister();
                break;
            case R.id.tv_info:
                startActivity(new Intent(this, MainActivity.class));
                break;
        }
    }
}

