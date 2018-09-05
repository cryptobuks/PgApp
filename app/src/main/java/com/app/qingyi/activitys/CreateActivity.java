package com.app.qingyi.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.TextView;

import com.app.qingyi.R;
import com.app.qingyi.adapters.PIctureAdapter;
import com.app.qingyi.http.httputils.AllUrl;
import com.app.qingyi.http.httputils.AsyncTaskManager;
import com.app.qingyi.http.httputils.GsonUtils;
import com.app.qingyi.http.httputils.HttpUtil;
import com.app.qingyi.http.httputils.JsonObjectBuilder;
import com.app.qingyi.http.requestparams.BaseRequestParm;
import com.app.qingyi.http.responsebeans.BaseResponseBean;
import com.app.qingyi.http.responsebeans.RequestListener;
import com.app.qingyi.models.AD;
import com.app.qingyi.models.Goods;
import com.app.qingyi.models.MessageData;
import com.app.qingyi.utils.ChooseCityUtil;
import com.app.qingyi.utils.GlobleValue;
import com.app.qingyi.utils.LoginConfig;
import com.google.gson.JsonArray;
import com.google.zxing.WriterException;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import me.iwf.photopicker.PhotoPicker;

public class CreateActivity extends BaseActivity implements View.OnClickListener {

    private TextView city, nameLimit, titleLimit, ageLimit, faceLimit, priceLimit, telLimit, cityLimit, serviceLimit, detailsLimit, seePriceLimit, allImageLimit;
    private EditText name, title, age, face, price, qq, wechat, tel, service, details, seePrice;
    private MessageData mMessageData;
    private MessageData mMessageDataCreate;
    private GridView mGridView;
    private String[] cityArray;
    private PIctureAdapter mPIctureAdapter;
    private List<String> allImg = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideDialog();
            switch (msg.what) {
                case GlobleValue.SUCCESS:
                    allImg.add(mMessageData.getFilePath());
                    mPIctureAdapter.setObjects(allImg);
                    break;
                case GlobleValue.FAIL:
                    Snackbar.make(name, "文件上传失败", Snackbar.LENGTH_LONG).show();
                    break;
                case GlobleValue.SUCCESS2:
                    if (mMessageDataCreate.isSuccess()) {
                        Snackbar.make(name, "发布信息成功", Snackbar.LENGTH_LONG).addCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar transientBottomBar, int event) {
                                super.onDismissed(transientBottomBar, event);
                                finish();
                            }
                        }).show();
                    } else Snackbar.make(name, "发布信息失败", Snackbar.LENGTH_LONG).show();
                    break;
                case GlobleValue.FAIL2:
                    Snackbar.make(name, "发布信息失败", Snackbar.LENGTH_LONG).show();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        initView();
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_tit)).setText("发布");
        city = (TextView) findViewById(R.id.city);
        name = (EditText) findViewById(R.id.name);
        title = (EditText) findViewById(R.id.title);
        age = (EditText) findViewById(R.id.age);
        face = (EditText) findViewById(R.id.face);
        price = (EditText) findViewById(R.id.price);

        qq = (EditText) findViewById(R.id.qq);
        wechat = (EditText) findViewById(R.id.wechat);
        tel = (EditText) findViewById(R.id.tel);

        service = (EditText) findViewById(R.id.service);
        details = (EditText) findViewById(R.id.details);
        seePrice = (EditText) findViewById(R.id.seePrice);

        nameLimit = (TextView) findViewById(R.id.nameLimit);
        titleLimit = (TextView) findViewById(R.id.titleLimit);
        ageLimit = (TextView) findViewById(R.id.ageLimit);
        faceLimit = (TextView) findViewById(R.id.faceLimit);
        priceLimit = (TextView) findViewById(R.id.priceLimit);
        telLimit = (TextView) findViewById(R.id.telLimit);
        cityLimit = (TextView) findViewById(R.id.cityLimit);
        serviceLimit = (TextView) findViewById(R.id.serviceLimit);
        detailsLimit = (TextView) findViewById(R.id.detailsLimit);
        seePriceLimit = (TextView) findViewById(R.id.seePriceLimit);
        allImageLimit = (TextView) findViewById(R.id.allImageLimit);

        mGridView = (GridView) findViewById(R.id.mGridView);
        mPIctureAdapter = new PIctureAdapter(this) {

            @Override
            public void onFirstClick(String bean) {
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(false)
                        .start(CreateActivity.this, PhotoPicker.REQUEST_CODE);
            }
        };
        mGridView.setAdapter(mPIctureAdapter);
    }

    private void sevVisibleGONE() {
        nameLimit.setVisibility(View.GONE);
        titleLimit.setVisibility(View.GONE);
        ageLimit.setVisibility(View.GONE);
        faceLimit.setVisibility(View.GONE);
        priceLimit.setVisibility(View.GONE);
        telLimit.setVisibility(View.GONE);
        cityLimit.setVisibility(View.GONE);
        serviceLimit.setVisibility(View.GONE);
        detailsLimit.setVisibility(View.GONE);
        seePriceLimit.setVisibility(View.GONE);
        allImageLimit.setVisibility(View.GONE);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.layoutCity:
            case R.id.city:
                final ChooseCityUtil cityUtil = new ChooseCityUtil();
                String difail = "北京-北京-昌平";
                String[] oldCityArray = difail.split("-");//将TextView上的文本分割成数组 当做默认值
                cityUtil.createDialog(this, oldCityArray, new ChooseCityUtil.ChooseCityInterface() {
                    @Override
                    public void sure(String[] newCityArray) {
                        //oldCityArray为传入的默认值 newCityArray为返回的结果
                        cityArray = newCityArray;
                        city.setText(newCityArray[0] + "-" + newCityArray[1] + "-" + newCityArray[2]);
                    }
                });
                break;
            case R.id.id_add_btn:
                sevVisibleGONE();
                commit();
                break;
        }
    }

    private void commit() {
        String titleStr = title.getText().toString().trim();
        if (titleStr.equals("")) {
            titleLimit.setVisibility(View.VISIBLE);
            return;
        }
        String nameStr = name.getText().toString().trim();
        if (nameStr.equals("")) {
            nameLimit.setVisibility(View.VISIBLE);
            return;
        }
        String ageStr = age.getText().toString().trim();
        if (ageStr.equals("")) {
            ageLimit.setVisibility(View.VISIBLE);
            return;
        }
        String faceStr = face.getText().toString().trim();
        if (faceStr.equals("")) {
            faceLimit.setVisibility(View.VISIBLE);
            return;
        }
        String priceStr = price.getText().toString().trim();
        if (priceStr.equals("")) {
            priceLimit.setVisibility(View.VISIBLE);
            return;
        }
        String qqStr = qq.getText().toString().trim();
        String wechatStr = wechat.getText().toString().trim();
        String telStr = tel.getText().toString().trim();
        if (qqStr.equals("") && wechatStr.equals("") && telStr.equals("")) {
            telLimit.setVisibility(View.VISIBLE);
            return;
        }
        if (cityArray == null || cityArray.length == 0) {
            cityLimit.setVisibility(View.VISIBLE);
            return;
        }
        String serviceStr = service.getText().toString().trim();
        if (serviceStr.equals("")) {
            serviceLimit.setVisibility(View.VISIBLE);
            return;
        }
        String detailsStr = details.getText().toString().trim();
        if (detailsStr.equals("")) {
            detailsLimit.setVisibility(View.VISIBLE);
            return;
        }
        String seePriceStr = seePrice.getText().toString().trim();
        if (seePriceStr.equals("")) {
            seePriceLimit.setVisibility(View.VISIBLE);
            return;
        }
        if (allImg == null || allImg.size() == 0) {
            allImageLimit.setVisibility(View.VISIBLE);
            return;
        }
        JsonObjectBuilder builder = new JsonObjectBuilder();
        builder.append("title", titleStr);
        builder.append("name", nameStr);
        builder.append("age", ageStr);
        builder.append("country", "china");
        builder.append("province", cityArray[0]);
        builder.append("city", cityArray[1]);
        builder.append("area", cityArray[2]);
        builder.append("seePrice", seePriceStr);
        builder.append("price", priceStr);
        builder.append("service", serviceStr);
        builder.append("describe", detailsStr);
        JsonArray array = new JsonArray();
        for (int i = 0; i < allImg.size(); i++) {
            array.add(allImg.get(i));
        }
        builder.append("pictures", array);
        builder.append("brief", faceStr);
        builder.append("qq", qqStr);
        builder.append("wechat", wechatStr);
        builder.append("tel", telStr);

        uploadData(builder.toString());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK && requestCode == PhotoPicker.REQUEST_CODE) {
            if (data != null) {
                ArrayList<String> photos =
                        data.getStringArrayListExtra(PhotoPicker.KEY_SELECTED_PHOTOS);
                fileupload(photos.get(0));
            }
        }
    }

    private void fileupload(String fileurl) {
        String url = AllUrl.getInstance().getfileuploadUrl();
        if (HttpUtil.isNetworkAvailable(this) && !GlobleValue.Downloading) {
            AsyncTaskManager.getInstance().StartHttpUpload(new BaseRequestParm(this, url, fileurl,
                            LoginConfig.getAuthorization()),
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
                                mMessageData = GsonUtils.JsonObjectToBean(GsonUtils.getRootJsonObject(bean.getResult()), MessageData.class);
                                handler.sendEmptyMessage(GlobleValue.SUCCESS);
                            } else {
                                handler.sendEmptyMessage(GlobleValue.FAIL);
                            }
                        }
                    });
        } else {
            Snackbar.make(name, "网络未连接", Snackbar.LENGTH_LONG).show();
        }
    }

    private void uploadData(String data) {
        String url = AllUrl.getInstance().getGoodsUploadUrl();
        if (HttpUtil.isNetworkAvailable(this)) {
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(this, url, data,
                            AsyncTaskManager.ContentTypeJson, "POST", LoginConfig.getAuthorization()),
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
                            mMessageDataCreate = GsonUtils.JsonObjectToBean(GsonUtils.getRootJsonObject(bean.getResult()), MessageData.class);
                            handler.sendEmptyMessage(GlobleValue.SUCCESS2);
                        }
                    }, this);
        } else {
            Snackbar.make(name, "网络未连接", Snackbar.LENGTH_LONG).show();
        }
    }
}
