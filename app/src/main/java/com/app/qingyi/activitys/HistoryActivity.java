package com.app.qingyi.activitys;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.app.qingyi.R;
import com.app.qingyi.adapters.HistoryAdapter;
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
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class HistoryActivity extends BaseActivity implements OnClickListener {

    private int curPage = 1;
    private int limit = 10;
    private ListView mListView;
    private RefreshLayout refreshLayout;
    private HistoryAdapter mHistoryAdapter;
    private List<Goods.GoodsItem> allData = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreshLayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            refreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
            switch (msg.what) {
                case GlobleValue.SUCCESS:
                    mHistoryAdapter.setObjects(allData);
                    break;
                case GlobleValue.SUCCESS2:
                    curPage--;
                    Snackbar.make(mListView, "没有更多数据", Snackbar.LENGTH_LONG).show();
                    break;
                case GlobleValue.SUCCESS3:

                    break;
                case GlobleValue.FAIL:
                    break;
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        initView();
        getDefailtData(curPage);
    }

    private void initView() {
        ((TextView) findViewById(R.id.tv_tit)).setText("购买记录");
        mListView = (ListView) findViewById(R.id.mListView);
        refreshLayout = (RefreshLayout) findViewById(R.id.refreshLayout);
        mHistoryAdapter = new HistoryAdapter(this);
        mListView.setAdapter(mHistoryAdapter);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                curPage = 1;
                getDefailtData(curPage);
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
                getDefailtData(++curPage);
            }
        });
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(HistoryActivity.this, DetailsActivity.class);
                intent.putExtra("id",allData.get(i).getId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_close:
                finish();
                break;
        }
    }

    private void getDefailtData(int page){
        String url = AllUrl.getInstance().getHistoryGoodsUrl(page, limit);
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
                            Goods mGoods = GsonUtils.JsonObjectToBean(GsonUtils.getRootJsonObject(bean.getResult()), Goods.class);
                            if (curPage == 1) {
                                allData.clear();
                            } else if (curPage > 1 && mGoods.getList().size() == 0) {
                                handler.sendEmptyMessage(GlobleValue.SUCCESS2);
                                return;
                            }
                            allData.addAll(mGoods.getList());
                            handler.sendEmptyMessage(GlobleValue.SUCCESS);
                        }
                    }, this);
        } else {
            Snackbar.make(mListView, "网络未连接", Snackbar.LENGTH_LONG).show();
        }
    }
}