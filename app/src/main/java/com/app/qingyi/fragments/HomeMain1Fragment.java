package com.app.qingyi.fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.qingyi.R;
import com.app.qingyi.activitys.DetailsActivity;
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
import com.app.qingyi.utils.ChooseCityUtil;
import com.app.qingyi.utils.GlobleValue;
import com.app.qingyi.utils.LoginConfig;
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
    private TextView tv_tit;
    private int curPage = 1;
    private int limit = 10;
    private String filter = "推荐";
    private String city = "北京";
    private RefreshLayout refreshLayout;
    private List<String> mTitleList = new ArrayList<String>();//页卡标题集合
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
                    filter = favarate;
                    curPage = 1;
                    mTabLayout.removeAllTabs();
                    mTitleList.add(0, favarate);
                    for (String title : mTitleList) {
                        if (title.equals(mTitleList.get(0))) {
                            mTabLayout.addTab(mTabLayout.newTab().setText(title), true);
                        } else
                            mTabLayout.addTab(mTabLayout.newTab().setText(title), false);
                    }
                    mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
                        @Override
                        public void onTabSelected(TabLayout.Tab tab) {
                            if (mTitleList.size() > 0) {
                                int position = tab.getPosition();
                                filter = mTitleList.get(position);
                                curPage = 1;
                                getDefailtData(curPage);
                            }
                        }

                        @Override
                        public void onTabUnselected(TabLayout.Tab tab) {

                        }

                        @Override
                        public void onTabReselected(TabLayout.Tab tab) {

                        }
                    });
                    getDefailtData(curPage);
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

    private void initView(View view) {
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mGridView = (GridView) view.findViewById(R.id.mGridView);
        tv_tit = (TextView) view.findViewById(R.id.tv_tit);
        view.findViewById(R.id.iv_one).setOnClickListener(this);
        tv_tit.setOnClickListener(this);

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
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(mContext, DetailsActivity.class);
                intent.putExtra("id", allData.get(i).getId());
                startActivity(intent);
            }
        });
        tv_tit.setText(city);
    }

    private void getDefailtData(int page) {
        String url = AllUrl.getInstance().getDefailtGoodsUrl(page, limit);
        JsonObjectBuilder builder = new JsonObjectBuilder();
        if (!filter.equals("推荐")) {
            url = AllUrl.getInstance().getGoodsByAreaUrl(page, limit);
            builder.append("area", filter);
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
                            mTitleList.clear();
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
            case R.id.tv_tit:
            case R.id.iv_one:
                selectCity();
                break;
        }
    }

    private void selectCity() {
        final ChooseCityUtil cityUtil = new ChooseCityUtil();
        String difail = "北京-北京-昌平";
        String[] oldCityArray = difail.split("-");//将TextView上的文本分割成数组 当做默认值
        cityUtil.createDialog(mContext, oldCityArray, new ChooseCityUtil.ChooseCityInterface() {
            @Override
            public void sure(String[] newCityArray) {
                //oldCityArray为传入的默认值 newCityArray为返回的结果
                city = newCityArray[1];
                tv_tit.setText(city);
                getTitles();
                Toasty.normal(mContext, newCityArray[0] + "-" + newCityArray[1] + "-" + newCityArray[2]).show();
            }
        });
    }
}
