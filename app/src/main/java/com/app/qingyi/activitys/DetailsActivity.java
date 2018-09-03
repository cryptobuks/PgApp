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
import com.app.qingyi.utils.GlobleValue;
import com.app.qingyi.utils.LoginConfig;
import com.squareup.picasso.Picasso;

import es.dmoral.toasty.Toasty;

public class DetailsActivity extends BaseActivity implements View.OnClickListener {

    private Goods.GoodsItem goodsItem;
    private TextView name, area, price, age, face, describe, service,buy;
    private ImageView topImg;
    private ScrollView mScrollView;
    private ListView mListView;
    private  int goodsId;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobleValue.SUCCESS:
                    if (goodsItem != null) {
                        service.setText(goodsItem.getService());
                        face.setText(goodsItem.getFace());
                        describe.setText(goodsItem.getDescribe());

                        buy.setText("查看联系方式： "+goodsItem.getSeePrice()+" DO");

                        name.setText(goodsItem.getName());
                        age.setText(goodsItem.getAge());
                        price.setText(goodsItem.getPrice());
                        area.setText(goodsItem.getProvince() + goodsItem.getCity() + goodsItem.getArea());
                        if (goodsItem.getPictures() != null && goodsItem.getPictures().length > 0) {
                            Picasso.with(DetailsActivity.this)
                                    .load(goodsItem.getPictures()[0])
                                    .error(R.mipmap.ic_default)
                                    .fit()
                                    .centerCrop()
                                    .into(topImg);
                            setImgs();
                        }
                    }
                    break;
                case GlobleValue.FAIL:
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
        name = (TextView) findViewById(R.id.name);
        area = (TextView) findViewById(R.id.area);
        price = (TextView) findViewById(R.id.price);
        age = (TextView) findViewById(R.id.age);

        describe = (TextView) findViewById(R.id.describe);
        service = (TextView) findViewById(R.id.service);
        face = (TextView) findViewById(R.id.face);

        buy = (TextView) findViewById(R.id.buy);

        topImg = (ImageView) findViewById(R.id.topImg);

        mListView = (ListView) findViewById(R.id.mListView);
        mScrollView = (ScrollView) findViewById(R.id.mScrollView);
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
            Snackbar.make(name, "网络未连接", Snackbar.LENGTH_LONG).show();
        }
    }

    private void setImgs() {
        ImageAdapter mImageAdapter = new ImageAdapter(this, goodsItem.getPictures());
        mListView.setAdapter(mImageAdapter);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mScrollView.scrollTo(0, 0);
            }
        }, 0);
    }
}
