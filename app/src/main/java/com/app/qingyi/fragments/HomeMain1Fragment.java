package com.app.qingyi.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.app.qingyi.R;
import com.app.qingyi.adapters.GradViewAdapter;
import com.app.qingyi.http.httputils.AllUrl;
import com.app.qingyi.http.httputils.AsyncTaskManager;
import com.app.qingyi.http.httputils.GsonUtils;
import com.app.qingyi.http.httputils.HttpUtil;
import com.app.qingyi.http.httputils.JsonObjectBuilder;
import com.app.qingyi.http.requestparams.BaseRequestParm;
import com.app.qingyi.http.responsebeans.BaseResponseBean;
import com.app.qingyi.http.responsebeans.RequestListener;
import com.app.qingyi.models.Area;
import com.app.qingyi.models.Goods;
import com.app.qingyi.models.MiningStatus;
import com.app.qingyi.models.TodayCoin;
import com.app.qingyi.utils.ACache;
import com.app.qingyi.utils.GlobleValue;
import com.app.qingyi.utils.LoginConfig;
import com.app.qingyi.utils.Utils;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;

/**
 * 首页
 *
 * @author CJQ
 */
public class HomeMain1Fragment extends Fragment implements View.OnClickListener {
    private Context mContext;
    private TabLayout mTabLayout;
    private GridView mGridView;
    private int curPage = 1;
    private int limit = 10;
    private String filter = "推荐";
    private String city = "北京市";
    private RefreshLayout refreshLayout;
    private List<Goods.GoodsItem> allData = new ArrayList<>();
    private GradViewAdapter mGradViewAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            refreshLayout.finishRefresh(1000/*,false*/);//传入false表示刷新失败
            refreshLayout.finishLoadMore(1000/*,false*/);//传入false表示加载失败
            switch (msg.what) {
                case GlobleValue.SUCCESS:
                    mGradViewAdapter.setObjects(allData);
                    break;
                case GlobleValue.SUCCESS2:
                    curPage--;
                    Snackbar.make(mTabLayout, "没有更多数据", Snackbar.LENGTH_LONG).show();
                    break;
                case GlobleValue.SUCCESS3:
                    String favarate = "推荐";
                    mTabLayout.addTab(mTabLayout.newTab().setText(favarate), true);
                    for (String title : mTitleList) {
                        mTabLayout.addTab(mTabLayout.newTab().setText(title), false);
                    }
                    mTitleList.add(0, favarate);
                    mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            int position = tab.getPosition();
                            filter = mTitleList.get(position);
                            curPage = 1;
                            getDefailtData(curPage);
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });
                    break;
                case GlobleValue.FAIL:
                    break;
            }
        }
    };

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main1, null);
        mContext = view.getContext();
        initView(view);
        getTitles();
        return view;
    }

    private List<String> mTitleList = new ArrayList<String>();//页卡标题集合

    private void initView(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mGridView = (GridView) view.findViewById(R.id.mGridView);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        mGradViewAdapter = new GradViewAdapter(mContext);
        mGradViewAdapter.setObjects(allData);
        mGridView.setAdapter(mGradViewAdapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
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
    }

    @Override
    public void onResume() {
        super.onResume();
        getDefailtData(curPage);
    }

    private void getDefailtData(int page) {
        String url = AllUrl.getInstance().getDefailtGoodsUrl(page, limit);
        JsonObjectBuilder builder = new JsonObjectBuilder();
        if (!filter.equals("推荐")) {
            url = AllUrl.getInstance().getGoodsByAreaUrl(page, limit);
            builder.append("area",filter);
        }
        if (HttpUtil.isNetworkAvailable(mContext)) {
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(mContext, url, builder.toString(),
                            AsyncTaskManager.ContentTypeJson, "POST", LoginConfig.getAuthorization()),
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
                    }, mContext);
        } else {
            Snackbar.make(mTabLayout, "网络未连接", Snackbar.LENGTH_LONG).show();
        }
    }


    private void getTitles() {
        String url = AllUrl.getInstance().getTitlesUrl();
        if (HttpUtil.isNetworkAvailable(mContext)) {
            JsonObjectBuilder builder = new JsonObjectBuilder();
            builder.append("city", city);
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(mContext, url, builder.toString(),
                            AsyncTaskManager.ContentTypeJson, "POST", LoginConfig.getAuthorization()),
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
                            List<Area> aLLAreas = GsonUtils.JsonArrayToListBean(GsonUtils.getRootJsonArray(bean.getResult()), Area.class);
                            for (Area mArea : aLLAreas) {
                                mTitleList.add(mArea.getArea());
                            }
                            handler.sendEmptyMessage(GlobleValue.SUCCESS3);
                        }
                    }, mContext);
        } else {
            Snackbar.make(mTabLayout, "网络未连接", Snackbar.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
//            case R.id.tvAllbox:
//                startActivity(new Intent(mContext, BoxManageActivity.class));
//                break;
        }
    }
}
