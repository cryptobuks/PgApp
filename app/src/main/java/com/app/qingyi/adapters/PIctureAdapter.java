package com.app.qingyi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.qingyi.R;
import com.app.qingyi.models.Goods;
import com.app.qingyi.utils.Utils;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;


public abstract class PIctureAdapter extends BaseAdapter {

    public abstract void onFirstClick(String bean);

    private List<String> objects = new ArrayList<String>();
    private Context context;
    private LayoutInflater layoutInflater;

    public PIctureAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public PIctureAdapter(Context context, GridView mGridView, List<String> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    public void setObjects(List<String> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    public void addObjects(List<String> objects) {
        this.objects.addAll(objects);
        notifyDataSetChanged();
    }

    public List<String> getObjects() {
        return objects;
    }


    @Override
    public int getCount() {
        return objects.size() + 1;
    }

    @Override
    public String getItem(int position) {
        if (position == 0) return null;
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
            convertView = layoutInflater.inflate(R.layout.picture_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
            AutoUtils.autoSize(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        initializeViews((ViewHolder) convertView.getTag(), position);
        return convertView;
    }

    private void initializeViews(ViewHolder holder, final int position) {
        if (position == 0) {
            holder.imageView.setImageResource(R.mipmap.ic_pic_add);
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onFirstClick("");
                }
            });
        }else {
            if (objects != null) {
                Picasso.with(context)
                        .load(Utils.getRightUrl(objects.get(position-1)))
                        .error(R.mipmap.ic_default)
                        .fit()
                        .into(holder.imageView);
            }
            holder.imageView.setOnClickListener(null);
        }
    }

    protected class ViewHolder {
        private ImageView imageView;
    }
}

