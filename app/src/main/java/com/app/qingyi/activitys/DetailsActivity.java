package com.app.qingyi.activitys;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.app.qingyi.R;
import com.app.qingyi.adapters.ImageAdapter;
import com.app.qingyi.http.httputils.AllUrl;
import com.app.qingyi.http.httputils.AsyncTaskManager;
import com.app.qingyi.http.httputils.GsonUtils;
import com.app.qingyi.http.httputils.HttpUtil;
import com.app.qingyi.http.requestparams.BaseRequestParm;
import com.app.qingyi.http.responsebeans.BaseResponseBean;
import com.app.qingyi.http.responsebeans.RequestListener;
import com.app.qingyi.models.Goods;
import com.app.qingyi.models.MessageData;
import com.app.qingyi.utils.GlobleValue;
import com.app.qingyi.utils.LoginConfig;
import com.app.qingyi.utils.Utils;
import com.bumptech.glide.util.Util;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;

public class DetailsActivity extends BaseActivity implements View.OnClickListener {

    private Goods.GoodsItem goodsItem;
    private ListView mListView;
    private TextView buy;
    private  int goodsId;
    private  boolean iflike = false;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobleValue.SUCCESS:
                    buy.setText("查看联系方式： " + goodsItem.getSeePrice() + " DO");
                        setImgs();
                    break;
                case GlobleValue.FAIL:
                    break;
                case GlobleValue.SUCCESS2:
                    notifyTop();
                    break;
                case GlobleValue.SUCCESS3:
                    goodsItem.setLike(iflike);
                    notifyTopByNet();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        initView();
        getData();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_tit)).setText("详情");
        mListView = (ListView) findViewById(R.id.mListView);
        buy = (TextView) findViewById(R.id.buy);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.buy:
                if (mLoginConfig.getAuthorization() != null && !mLoginConfig.getAuthorization().equals("")) {
                    Intent mIntent = new Intent(DetailsActivity.this, SeePrivateActivity.class);
                    mIntent.putExtra("id",goodsId);
                    startActivity(mIntent);
                }else {
                    Toasty.normal(this,"请先登录").show();
                    startActivity(new Intent(DetailsActivity.this, LoginActivity.class));
                }
                break;
        }
    }

    private void getData() {
        goodsId = getIntent().getExtras().getInt("id");
        String url = AllUrl.getInstance().getGoodsDetailsUrl(goodsId);
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
                            goodsItem = GsonUtils.JsonObjectToBean(GsonUtils.getRootJsonObject(bean.getResult()), Goods.GoodsItem.class);
                            handler.sendEmptyMessage(GlobleValue.SUCCESS);
                        }
                    }, this);
        } else {
            Snackbar.make(mListView, "网络未连接", Snackbar.LENGTH_LONG).show();
        }
    }

    private  ImageAdapter mImageAdapter;
    private void setImgs() {
        mImageAdapter = new ImageAdapter(this, goodsItem.getPictures(),goodsItem){

            @Override
            public void ilike() {
                like(!goodsItem.isLike());
            }
        };
        mListView.setAdapter(mImageAdapter);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListView.scrollTo(0, 0);
            }
        }, 0);
        iflike();
    }

    private void notifyTop(){
        goodsItem.setLike(!goodsItem.isLike());
        mImageAdapter.setGoodsItem(goodsItem);
    }

    private void notifyTopByNet(){
        mImageAdapter.setGoodsItem(goodsItem);
    }

    private void like(boolean like) {
        if (mLoginConfig.getAuthorization() != null && !mLoginConfig.getAuthorization().equals("")) {
            String url = AllUrl.getInstance().getGoodsLikeUrl(goodsId,like);
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
                                MessageData mMessageData = GsonUtils.JsonObjectToBean(GsonUtils.getRootJsonObject(bean.getResult()), MessageData.class);
                                if(mMessageData.isSuccess()){
                                    handler.sendEmptyMessage(GlobleValue.SUCCESS2);
                                }else  handler.sendEmptyMessage(GlobleValue.FAIL);

                            }
                        }, this);
            } else {
                Snackbar.make(mListView, "网络未连接", Snackbar.LENGTH_LONG).show();
            }
        }
    }

    private void iflike() {
        if (mLoginConfig.getAuthorization() != null && !mLoginConfig.getAuthorization().equals("")) {
            String url = AllUrl.getInstance().getGoodsifLikeUrl(goodsId);
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
                                MessageData mMessageData = GsonUtils.JsonObjectToBean(GsonUtils.getRootJsonObject(bean.getResult()), MessageData.class);
                                if(mMessageData.isSuccess()){
                                    iflike = true;
                                    handler.sendEmptyMessage(GlobleValue.SUCCESS3);
                                }else  handler.sendEmptyMessage(GlobleValue.FAIL);

                            }
                        }, this);
            } else {
                Snackbar.make(mListView, "网络未连接", Snackbar.LENGTH_LONG).show();
            }
        }
    }
}
