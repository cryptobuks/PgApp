package com.app.qingyi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.qingyi.R;
import com.app.qingyi.models.CapitalFlow;
import com.app.qingyi.models.Information;
import com.app.qingyi.utils.Utils;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;


public class CapitalFlowAdapter extends BaseAdapter {

    private List<CapitalFlow.capitalFlowItem> objects = new ArrayList<CapitalFlow.capitalFlowItem>();
    @SuppressWarnings("unused")
    private Context context;
    private LayoutInflater layoutInflater;

    public CapitalFlowAdapter(Context context, List<CapitalFlow.capitalFlowItem> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    public void setObjects(List<CapitalFlow.capitalFlowItem> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    public List<CapitalFlow.capitalFlowItem> getObjects() {
        return objects;
    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public CapitalFlow.capitalFlowItem getItem(int position) {
        return objects.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    /**
     * 数据量过大可能出现错乱，暂时不用缓存策略
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.capitalflow_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.tvStatus = (TextView) convertView.findViewById(R.id.tvStatus);
            viewHolder.tvAmount = (TextView) convertView.findViewById(R.id.tvAmount);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
            AutoUtils.autoSize(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        initializeViews((CapitalFlow.capitalFlowItem) getItem(position), (ViewHolder) convertView.getTag(), position);
        return convertView;
    }

    private void initializeViews(final CapitalFlow.capitalFlowItem object, ViewHolder holder, final int position) {
//        Picasso.with(context)
//                .load(object.getImgurl())
//                .error(R.mipmap.ic_default)
//                .fit()
//                .centerCrop()
//                .into(holder.imageView);
        holder.tvName.setText(object.getInOrOut().equals("in") ? "充值到账" : "提币到" + Utils.getString(object.getCanReceiveAddress(), 11));
        holder.tvTime.setText(object.getCreatedAt() + "");
        holder.tvAmount.setText(object.getAmount() + "");
        if (object.getStatus().equals("wait")) {
            holder.tvStatus.setText("等待");
        } else if (object.getStatus().equals("ok")) {
            holder.tvStatus.setText("已完成");
        } else if (object.getStatus().equals("fail")) {
            holder.tvStatus.setText("失败");
        } else if (object.getStatus().equals("processing")) {
            holder.tvStatus.setText("处理中");
        } else if (object.getStatus().equals("cancel")) {
            holder.tvStatus.setText("已取消");
        }
    }

    protected class ViewHolder {
        private TextView tvName;
        private TextView tvTime;
        private TextView tvAmount;
        private TextView tvStatus;
        private ImageView imageView;
    }
}

