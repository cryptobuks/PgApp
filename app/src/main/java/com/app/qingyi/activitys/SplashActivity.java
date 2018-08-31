package com.app.qingyi.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.app.qingyi.R;
import com.app.qingyi.http.httputils.AllUrl;
import com.app.qingyi.http.httputils.AsyncTaskManager;
import com.app.qingyi.http.httputils.GsonUtils;
import com.app.qingyi.http.httputils.HttpUtil;
import com.app.qingyi.http.httputils.Url;
import com.app.qingyi.http.requestparams.BaseRequestParm;
import com.app.qingyi.http.responsebeans.BaseResponseBean;
import com.app.qingyi.http.responsebeans.RequestListener;
import com.app.qingyi.models.AD;
import com.app.qingyi.utils.LoginConfig;
import com.squareup.picasso.Picasso;


public class SplashActivity extends BaseActivity {

    RelativeLayout topLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
        topLayout = (RelativeLayout) findViewById(R.id.topLayout);
        ImageView imgView = (ImageView) findViewById(R.id.imgView);
        RelativeLayout topLayout = (RelativeLayout) findViewById(R.id.topLayout);
        String url = mLoginConfig.getADUrl();
        if (url != null && url.length() > 6 && url.contains("http")) {
            topLayout.setVisibility(View.VISIBLE);
            Picasso.with(this)
                    .load(url)
                    .into(imgView);
            imgView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(SplashActivity.this, WebViewActivity.class);
                    intent.putExtra("ADhttp", mLoginConfig.getADhttpUrl());
                    intent.putExtra("title", mLoginConfig.getAdTitle());
                    startActivity(intent);
                }
            });
        }
        anim();
    }

    // 设置动画
    private void anim() {
        Animation anim = AnimationUtils.loadAnimation(this,
                R.anim.main_loadingpage);

        anim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationRepeat(Animation animation) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                goLogin();
            }

        });
        topLayout.setAnimation(anim);
        anim.start();
    }

    private void goLogin() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
}
