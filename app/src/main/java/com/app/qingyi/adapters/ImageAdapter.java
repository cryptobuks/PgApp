package com.app.qingyi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.qingyi.R;
import com.app.qingyi.views.AutoHeightImageView;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;


public class ImageAdapter extends BaseAdapter {

    private String[] objects = new String[]{};
    @SuppressWarnings("unused")
    private Context context;
    private LayoutInflater layoutInflater;

    public ImageAdapter(Context context, String[] objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    public void setObjects(String[] objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return objects.length;
    }

    @Override
    public String getItem(int position) {
        return objects[position];
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
            convertView = layoutInflater.inflate(R.layout.img_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (AutoHeightImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
            AutoUtils.autoSize(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag(),position);
        return convertView;
    }

    private void initializeViews(final String url, ViewHolder holder, final int position) {
        Picasso.with(context)
                .load(url)
                .error(R.mipmap.ic_default)
                .fit()
                .into(holder.imageView);
    }

    protected class ViewHolder {
        private AutoHeightImageView imageView;
    }
}

