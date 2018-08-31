package com.app.qingyi.activitys;

import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.os.Bundle;

import com.app.qingyi.base.BaseApplication;
import com.app.qingyi.utils.LoginConfig;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;
import com.zhy.autolayout.AutoLayoutActivity;
import java.util.Timer;
import java.util.TimerTask;


public class BaseActivity extends AutoLayoutActivity {

    private SweetAlertDialog pDialog;
    public LoginConfig mLoginConfig;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginConfig = new LoginConfig(this);
        ((BaseApplication)getApplication()).addActivity(this);
    }

    private void initDialog(){
        pDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#4174ad"));
        pDialog.setTitleText("Waiting...");
        pDialog.setCancelable(false);
    }

    public void showDialog(){
        initDialog();
        pDialog.show();
        TimerTask task = new TimerTask(){
            public void run(){
                dialogHandler.sendEmptyMessage(0x00009);
            }
        };
        new Timer().schedule(task, 20*1000);
    }

    public void hideDialog(){
        if(pDialog != null){
            pDialog.dismissWithAnimation();
        }
    }

    private Handler dialogHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideDialog();
        }
    };
}
