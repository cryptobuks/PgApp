package com.app.qingyi.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.qingyi.R;
import com.app.qingyi.models.Goods;
import com.app.qingyi.utils.Utils;
import com.app.qingyi.views.AutoHeightImageView;
import com.squareup.picasso.Picasso;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;


public class HistoryAdapter extends BaseAdapter {

    private List<Goods.GoodsItem> allData = new ArrayList<>();
    @SuppressWarnings("unused")
    private Context context;
    private LayoutInflater layoutInflater;

    public HistoryAdapter(Context context) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
    }

    public HistoryAdapter(Context context, List<Goods.GoodsItem> objects) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.allData = objects;
    }

    public void setObjects(List<Goods.GoodsItem> objects) {
        this.allData = objects;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return allData.size();
    }

    @Override
    public Goods.GoodsItem getItem(int position) {
        return allData.get(position);
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
            convertView = layoutInflater.inflate(R.layout.history_list_item, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.imageView = (ImageView) convertView.findViewById(R.id.imageView);
            viewHolder.buyPrice = (TextView) convertView.findViewById(R.id.buyPrice);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.age = (TextView) convertView.findViewById(R.id.age);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.area = (TextView) convertView.findViewById(R.id.area);
            convertView.setTag(viewHolder);
            AutoUtils.autoSize(convertView);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        initializeViews(getItem(position), (ViewHolder) convertView.getTag(), position);
        return convertView;
    }

    private void initializeViews(final Goods.GoodsItem item, ViewHolder holder, final int position) {
        if (item.getPictures() != null && item.getPictures().length > 0) {
            Picasso.with(context)
                    .load(Utils.getRightUrl(item.getPictures()[0]))
                    .error(R.mipmap.ic_default)
                    .fit()
                    .into(holder.imageView);
        }
        holder.buyPrice.setText(item.getSeePrice()+"DO");
        holder.name.setText(item.getName());
        holder.area.setText(item.getProvince()+item.getCity()+item.getArea());
        holder.price.setText(item.getPrice());
        holder.age.setText(item.getAge());
    }

    protected class ViewHolder {
        private ImageView imageView;
        private TextView buyPrice;
        private TextView name;
        private TextView age;
        private TextView price;
        private TextView area;
    }
}

