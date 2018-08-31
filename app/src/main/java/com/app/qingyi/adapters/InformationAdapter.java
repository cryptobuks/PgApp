package com.app.qingyi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.qingyi.R;
import com.app.qingyi.models.Information;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;


public class InformationAdapter extends BaseAdapter {

    private List<Information.inforItem> objects = new ArrayList<Information.inforItem>();
    @SuppressWarnings("unused")
    private Context context;
    private LayoutInflater layoutInflater;

    public InformationAdapter(Context context, List<Information.inforItem> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    public void setObjects(List<Information.inforItem> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    public void addObjects(List<Information.inforItem> objects) {
        this.objects.addAll(objects);
        notifyDataSetChanged();
    }
    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Information.inforItem getItem(int position) {
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
            convertView = layoutInflater.inflate(R.layout.infor_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            viewHolder.tvTime = (TextView) convertView.findViewById(R.id.tvTime);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
            AutoUtils.autoSize(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        initializeViews((Information.inforItem) getItem(position), (ViewHolder) convertView.getTag(),position);
        return convertView;
    }

    private void initializeViews(final Information.inforItem object, ViewHolder holder, final int position) {
        Picasso.with(context)
                .load(object.getImgurl())
                .error(R.mipmap.ic_default)
                .fit()
                .centerCrop()
                .into(holder.imageView);
        holder.tvName.setText(object.getName());
        holder.tvTime.setText(object.getCreatedAt()+"");
    }

    protected class ViewHolder {
        private TextView tvName;
        private TextView tvTime;
        private ImageView imageView;
    }
}

