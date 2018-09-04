package com.app.qingyi.activitys;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.app.qingyi.R;
import com.app.qingyi.http.httputils.AllUrl;
import com.app.qingyi.http.httputils.AsyncTaskManager;
import com.app.qingyi.http.httputils.GsonUtils;
import com.app.qingyi.http.httputils.HttpUtil;
import com.app.qingyi.http.requestparams.BaseRequestParm;
import com.app.qingyi.http.responsebeans.BaseResponseBean;
import com.app.qingyi.http.responsebeans.RequestListener;
import com.app.qingyi.models.AD;
import com.app.qingyi.models.MessageData;
import com.app.qingyi.utils.GlobleValue;
import com.app.qingyi.utils.LoginConfig;
import com.google.zxing.WriterException;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;
import me.iwf.photopicker.PhotoPicker;

public class CreateActivity extends BaseActivity implements View.OnClickListener {

    private EditText name;
    private MessageData mMessageData;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            hideDialog();
            switch (msg.what) {
                case GlobleValue.SUCCESS:
                    Snackbar.make(name, mMessageData.getFilePath(), Snackbar.LENGTH_LONG).show();
                    break;
                case GlobleValue.FAIL:
                    Snackbar.make(name, "上传失败", Snackbar.LENGTH_LONG).show();
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
        name = (EditText) findViewById(R.id.name);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
            case R.id.imgSelect:
                PhotoPicker.builder()
                        .setPhotoCount(1)
                        .setShowCamera(true)
                        .setShowGif(true)
                        .setPreviewEnabled(false)
                        .start(this, PhotoPicker.REQUEST_CODE);
                break;
        }
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
}
