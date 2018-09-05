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


public class ImageAdapter extends BaseAdapter {

    private String[] objects = new String[]{};
    private Goods.GoodsItem goodsItem;

    private Context context;
    private LayoutInflater layoutInflater;

    public ImageAdapter(Context context, String[] objects, Goods.GoodsItem goodsItem) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
        this.goodsItem = goodsItem;
    }

    public void setObjects(String[] objects) {
        this.objects = objects;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return objects.length + 1;
    }

    @Override
    public String getItem(int position) {
        if (position == 0) {
            return null;
        }
        return objects[position - 1];
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
        if (position == 0) {
            ViewHolder viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.layout, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.area = (TextView) convertView.findViewById(R.id.area);
            viewHolder.price = (TextView) convertView.findViewById(R.id.price);
            viewHolder.age = (TextView) convertView.findViewById(R.id.age);
            viewHolder.like = (TextView) convertView.findViewById(R.id.like);
            viewHolder.describe = (TextView) convertView.findViewById(R.id.describe);
            viewHolder.service = (TextView) convertView.findViewById(R.id.service);
            viewHolder.face = (TextView) convertView.findViewById(R.id.face);
            viewHolder.topImg = (ImageView) convertView.findViewById(R.id.topImg);
            setText(viewHolder);
        } else {
            ViewHolder viewHolder;
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.img_list_item, parent, false);
            viewHolder.imageView = (AutoHeightImageView) convertView.findViewById(R.id.imageView);
            convertView.setTag(viewHolder);
            initializeViews(getItem(position), (ViewHolder) convertView.getTag(), position);
        }
        AutoUtils.autoSize(convertView);
        return convertView;
    }

    private void initializeViews(final String url, ViewHolder holder, final int position) {
        if (position != 0) {
            Picasso.with(context)
                    .load(Utils.getRightUrl(url))
                    .error(R.mipmap.ic_default)
                    .into(holder.imageView);
        }
    }

    private void setText(ViewHolder viewHolder) {
        if(goodsItem == null) return;
        viewHolder.service.setText(goodsItem.getService());
        viewHolder.face.setText(goodsItem.getBrief());
        viewHolder.describe.setText(goodsItem.getDescribe());

        viewHolder.like.setText(goodsItem.getVisitors() + "");
        viewHolder.name.setText(goodsItem.getName());
        viewHolder.age.setText(goodsItem.getAge());
        viewHolder.price.setText(goodsItem.getPrice());
        viewHolder.area.setText(goodsItem.getProvince() + goodsItem.getCity() + goodsItem.getArea());
        if (goodsItem.getPictures() != null && goodsItem.getPictures().length > 0) {
            Picasso.with(context)
                    .load(Utils.getRightUrl(goodsItem.getPictures()[0]))
                    .error(R.mipmap.ic_default)
                    .fit()
                    .into(viewHolder.topImg);
        }
    }

    protected class ViewHolder {
        private AutoHeightImageView imageView;
        private TextView name, area, price, age, like, describe, service, face, buy;
        private ImageView topImg;
    }
}

