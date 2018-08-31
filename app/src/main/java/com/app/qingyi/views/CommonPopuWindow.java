package com.app.qingyi.views;

/**
 * Created by ylkjcjq on 2017/11/21.
 */


import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.ScaleAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.app.qingyi.R;
import com.app.qingyi.utils.Utils;

public class CommonPopuWindow extends PopupWindow
         {//OnDismissListener

    private Activity mActivity;
    private View rootView;
    private ListView mListView;
    private Resources mResources;
    private DataAdapter mAdapter;

    public enum AnimStyle {
        LEFTANIM, RIGHTANIM
    }

    private ScaleAnimation leftShowAnim, rightShowAnim, leftExitAnim,
            rightExitAnim;
    private ItemClickCallBack mCallBack;

    public CommonPopuWindow(Activity activity,
                            ItemClickCallBack callBack) {
        this.mActivity = activity;
        this.mResources = activity.getResources();
        this.mCallBack = callBack;
        init();
    }

    private void init() {
        this.rootView = LayoutInflater.from(mActivity).inflate(
                R.layout.popupwindow_layout, null);
        this.mListView = (ListView) rootView.findViewById(R.id.lv_popup_list);

        this.setContentView(rootView);
        this.setWidth(Utils.getWindowWidth(mActivity)/3+60);
        this.setHeight(LayoutParams.WRAP_CONTENT);
        this.setBackgroundDrawable(new BitmapDrawable());
        this.setOutsideTouchable(true);
        this.setTouchable(true);
        this.setFocusable(true); //设置点击menu以外其他地方以及返回键退出

        this.mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                if (mCallBack != null) {
                    mCallBack.callBack(position);
                }
            }
        });
    }

    /**
     * 获取数据
     */
    public void initData(int stringArrayId) {
        String[] arrays = mResources.getStringArray(stringArrayId);
        mAdapter = new DataAdapter(arrays);
        mListView.setAdapter(mAdapter);
    }

    private class DataAdapter extends BaseAdapter {

        private String[] arrays;

        class ViewHolder {
            TextView dataView;
        }

        public DataAdapter(String[] arrays) {
            this.arrays = arrays;
        }

        @Override
        public int getCount() {
            if (arrays != null) {
                return arrays.length;
            }
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (arrays != null) {
                return arrays[position];
            }
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(mActivity).inflate(
                        R.layout.list_item_popupwindow, null);
                viewHolder.dataView = (TextView) convertView
                        .findViewById(R.id.tv_list_item);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.dataView.setText(arrays[position]);
            return convertView;
        }
    }

    public interface ItemClickCallBack {
        void callBack(int position);
    }

    private int dp2Px(Context context, float dp) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
