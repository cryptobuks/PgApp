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
import com.app.qingyi.http.requestparams.BaseRequestParm;
import com.app.qingyi.http.responsebeans.BaseResponseBean;
import com.app.qingyi.http.responsebeans.RequestListener;
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
    private MiningStatus mMiningStatus = null;
    private TabLayout mTabLayout;
    private GridView mGridView;
    private RefreshLayout refreshLayout;
    private List<Goods.GoodsItem> all7 = new ArrayList<>();
    private GradViewAdapter mGradViewAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case GlobleValue.SUCCESS:
                    break;
                case GlobleValue.SUCCESS2:
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
        return view;
    }

    private List<String> mTitleList = new ArrayList<String>();//页卡标题集合

    private void initView(View view) {
        mTitleList.add("头条");
        mTitleList.add("热点");
        mTitleList.add("本地");
        mTitleList.add("财经");
        mTitleList.add("科技");
        mTitleList.add("教育");
        mTitleList.add("体育");
        mTitleList.add("笑话");
        mTabLayout = (TabLayout) view.findViewById(R.id.tabs);
        mGridView = (GridView) view.findViewById(R.id.mGridView);
        refreshLayout = (RefreshLayout) view.findViewById(R.id.refreshLayout);
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(0)), true);
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(2)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(3)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(4)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(5)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(6)));
        mTabLayout.addTab(mTabLayout.newTab().setText(mTitleList.get(7)));
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                int position = tab.getPosition();
                Toasty.normal(mContext,mTitleList.get(position)).show();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        String [] pictures =  {"http://pic26.photophoto.cn/20130324/0036036837895295_b.jpg"};
        all7.add(new Goods.GoodsItem(1,"美女会所嫩模学生白领护士御姐肤白貌美姐…",pictures,1000,"300起"));
        all7.add(new Goods.GoodsItem(1,"美女会所嫩模学生白领护士御姐肤白貌美姐…",pictures,1000,"300起"));
        all7.add(new Goods.GoodsItem(1,"美女会所嫩模学生白领护士御姐肤白貌美姐…",pictures,1000,"300起"));
        mGradViewAdapter = new GradViewAdapter(mContext);
        mGradViewAdapter.setObjects(all7);
        mGridView.setAdapter(mGradViewAdapter);

        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
//                curPage = 1;
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(RefreshLayout refreshlayout) {
//                getDevicesList(++curPage, filter);
            }
        });
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    private void getStatistics() {
        String url = AllUrl.getInstance().getStatistics();
        if (HttpUtil.isNetworkAvailable(mContext)) {
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(mContext, url, null,
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
                            mMiningStatus = GsonUtils.JsonObjectToBean(GsonUtils.getRootJsonObject(bean.getResult()), MiningStatus.class);
                            handler.sendEmptyMessage(GlobleValue.SUCCESS);
                        }
                    }, mContext);
        } else {
            Snackbar.make(mTabLayout, "网络未连接", Snackbar.LENGTH_LONG).show();
        }
    }

    private void getChart() {
        if (ACache.get(mContext).getAsString(LoginConfig.getAccount() + Utils.getToday()) != null) {
            all7 = GsonUtils.JsonArrayToListBean(GsonUtils.getRootJsonObject(ACache.get(mContext).getAsString(LoginConfig.getAccount() + Utils.getToday())).getAsJsonArray("data"), Goods.GoodsItem.class);
            handler.sendEmptyMessage(GlobleValue.SUCCESS2);
            return;
        }
        String url = AllUrl.getInstance().get7days();
        if (HttpUtil.isNetworkAvailable(mContext)) {
            AsyncTaskManager.getInstance().StartHttp(new BaseRequestParm(mContext, url, null,
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
                            all7 = GsonUtils.JsonArrayToListBean(GsonUtils.getRootJsonObject(bean.getResult()).getAsJsonArray("data"), Goods.GoodsItem.class);
                            ACache.get(mContext).put(LoginConfig.getAccount() + Utils.getToday(), bean.getResult());
                            handler.sendEmptyMessage(GlobleValue.SUCCESS2);
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
