package com.app.qingyi.activitys;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.app.qingyi.Dialogs.SureDialog;
import com.app.qingyi.R;
import com.app.qingyi.http.httputils.AllUrl;
import com.app.qingyi.http.httputils.AsyncTaskManager;
import com.app.qingyi.http.httputils.GsonUtils;
import com.app.qingyi.http.httputils.HttpUtil;
import com.app.qingyi.http.requestparams.BaseRequestParm;
import com.app.qingyi.http.responsebeans.BaseResponseBean;
import com.app.qingyi.http.responsebeans.RequestListener;
import com.app.qingyi.models.Goods;
import com.app.qingyi.models.PayStatus;
import com.app.qingyi.utils.GlobleValue;
import com.app.qingyi.utils.LoginConfig;
import com.squareup.picasso.Picasso;

public class SeePrivateActivity extends BaseActivity implements View.OnClickListener {

    private TextView wechat, qq, tel, payAmount, tvAccountBalance,buy;
    private  int goodsId;
    private PayStatus mPayStatus = new PayStatus();
    private PayStatus mPayStatusByPay = new PayStatus();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobleValue.SUCCESS:
                    if(mPayStatus.isSuccess()){
                        qq.setText(mPayStatus.getQq() == null ? "无":mPayStatus.getQq() );
                        wechat.setText(mPayStatus.getWechat() == null ? "无":mPayStatus.getWechat());
                        tel.setText(mPayStatus.getTel() == null ? "无":mPayStatus.getTel());
                        buy.setText("已支付： "+mPayStatus.getDo3Fei()+" DO");
                        buy.setOnClickListener(null);
                    }else {
                        payAmount.setText(mPayStatus.getDo3Fei());
                        buy.setText("支付： "+mPayStatus.getDo3Fei()+" DO");
                    }

                    tvAccountBalance.setText(mPayStatus.getBalance()+" DO");
                    break;
                case GlobleValue.FAIL:
                    break;

                case GlobleValue.SUCCESS2:
                    if(mPayStatusByPay.isSuccess()){
                        Snackbar.make(wechat, "支付成功", Snackbar.LENGTH_LONG).show();
                        qq.setText(mPayStatusByPay.getQq() == null ? "无":mPayStatusByPay.getQq() );
                        wechat.setText(mPayStatusByPay.getWechat() == null ? "无":mPayStatusByPay.getWechat());
                        tel.setText(mPayStatusByPay.getTel() == null ? "无":mPayStatusByPay.getTel());
                        buy.setText("已支付： "+mPayStatus.getDo3Fei()+" DO");
                        buy.setOnClickListener(null);
                    }else {
                        Snackbar.make(wechat, "支付失败，"+mPayStatus.getMessage(), Snackbar.LENGTH_LONG).show();
                    }
                    break;

                case GlobleValue.FAIL2:
                    Snackbar.make(wechat, "支付失败，请重试", Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_see_private);
        initView();
        getData();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_tit)).setText("详情");
        wechat = (TextView) findViewById(R.id.wechat);
        qq = (TextView) findViewById(R.id.qq);
        tel = (TextView) findViewById(R.id.tel);

        payAmount = (TextView) findViewById(R.id.payAmount);
        tvAccountBalance = (TextView) findViewById(R.id.tvAccountBalance);
        buy = (TextView) findViewById(R.id.buy);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.buy:
                surePay();
                break;
        }
    }

    private void surePay() {
        String msg = "您确定要支付吗？";
        final SureDialog.Builder dialog = new SureDialog.Builder(this);
        dialog.setTitle(msg).setNegativeButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.dismiss();
            }
        }).setPositiveButton(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                pay();
                dialog.dismiss();
            }
        }).create().show();
    }

    private void getData() {
        goodsId = getIntent().getExtras().getInt("id");
        String url = AllUrl.getInstance().getGoodsPrivateUrl(goodsId);
        if (HttpUtil.isNetworkAvailable(this)) {
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(this, url, null,
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
                            } else
                                handler.sendEmptyMessage(GlobleValue.FAIL);
                        }

                        private void analiData(BaseResponseBean bean) {
                            mPayStatus = GsonUtils.JsonObjectToBean(GsonUtils.getRootJsonObject(bean.getResult()),PayStatus.class);
                            handler.sendEmptyMessage(GlobleValue.SUCCESS);
                        }
                    }, this);
        } else {
            Snackbar.make(wechat, "网络未连接", Snackbar.LENGTH_LONG).show();
        }
    }

    private void pay() {
        String url = AllUrl.getInstance().getGoodsPrivateByPayUrl(goodsId);
        if (HttpUtil.isNetworkAvailable(this)) {
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(this, url, null,
                            AsyncTaskManager.ContentTypeJson, "GET", LoginConfig.getAuthorization()),
                    new RequestListener<BaseResponseBean>() {

                        @Override
                        public void onFailed() {
                            handler.sendEmptyMessage(GlobleValue.FAIL2);
                        }

                        @Override
                        public void onComplete(BaseResponseBean bean) {
                            if (bean.isSuccess()) {
                                analiData(bean);
                            } else
                                handler.sendEmptyMessage(GlobleValue.FAIL2);
                        }

                        private void analiData(BaseResponseBean bean) {
                            mPayStatusByPay = GsonUtils.JsonObjectToBean(GsonUtils.getRootJsonObject(bean.getResult()),PayStatus.class);
                            handler.sendEmptyMessage(GlobleValue.SUCCESS2);
                        }
                    }, this);
        } else {
            Snackbar.make(wechat, "网络未连接", Snackbar.LENGTH_LONG).show();
        }
    }

}
