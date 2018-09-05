package com.app.qingyi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
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


public class GradViewAdapter extends BaseAdapter {

    private List<Goods.GoodsItem> objects = new ArrayList<Goods.GoodsItem>();
    private Context context;
    private LayoutInflater layoutInflater;

    public GradViewAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public GradViewAdapter(Context context, GridView mGridView, List<Goods.GoodsItem> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
    }

    public void setObjects(List<Goods.GoodsItem> objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    public void addObjects(List<Goods.GoodsItem> objects) {
        this.objects.addAll(objects);
        notifyDataSetChanged();
    }

    public List<Goods.GoodsItem> getObjects() {
        return objects;
    }


    @Override
    public int getCount() {
        return objects.size();
    }

    @Override
    public Goods.GoodsItem getItem(int position) {
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
            convertView = layoutInflater.inflate(R.layout.gradview_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.tvTitle = (TextView) convertView.findViewById(R.id.tvTitle);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.clicks = (TextView) convertView.findViewById(R.id.clicks);
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
            AutoUtils.autoSize(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        initializeViews((Goods.GoodsItem) getItem(position), (ViewHolder) convertView.getTag(), position);
        return convertView;
    }

    private void initializeViews(final Goods.GoodsItem object, ViewHolder holder, final int position) {
        if (object.getPictures() != null && object.getPictures().length > 0) {
            Picasso.with(context)
                    .load(Utils.getRightUrl(object.getPictures()[0]))
                    .error(R.mipmap.ic_default)
                    .fit()
                    .into(holder.imageView);
        }

        holder.tvTitle.setText(object.getTitle());
        holder.price.setText(object.getPrice());
        holder.clicks.setText(object.getVisitors() + "");
    }

    protected class ViewHolder {
        private TextView tvTitle;
        private TextView price;
        private TextView clicks;
        private ImageView imageView;
    }
}

