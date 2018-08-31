package com.app.qingyi.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.qingyi.R;
import com.app.qingyi.http.httputils.AllUrl;
import com.app.qingyi.http.httputils.JsonObjectBuilder;
import com.app.qingyi.models.Device;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by castl on 2017/11/23.
 */

public class DeviceItemManageAdapter extends BaseExpandableListAdapter {

    private List<Device.deviceItem> objects = new ArrayList<Device.deviceItem>();
    private DeviceClickListener mListener;
    private Context context;
    private LayoutInflater layoutInflater;

    //自定义接口，用于回调按钮点击事件到Activity
    public interface DeviceClickListener {
        public void updateNet(Device.deviceItem item, int id, int position);
    }

    public DeviceItemManageAdapter(Context context, List<Device.deviceItem> objects, DeviceClickListener mListener) {
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.objects = objects;
        this.mListener = mListener;
    }

    public void setObjects(List<Device.deviceItem> objects){
        this.objects = objects;
        notifyDataSetChanged();
    }
    public void addObjects(List<Device.deviceItem> objects){
        this.objects.addAll(objects);
        notifyDataSetChanged();
    }
    @Override
    public int getGroupCount() {
        return objects.size();
    }

    @Override
    public int getChildrenCount(int i) {
        return 1;
    }

    @Override
    public Object getGroup(int i) {
        return objects.get(i);
    }

    @Override
    public Object getChild(int i, int i1) {
        return objects.get(i);
    }

    @Override
    public long getGroupId(int i) {
        return i;
    }

    @Override
    public long getChildId(int i, int i1) {
        return i;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    private static class ViewHolderGroup {
        private TextView tvSN;
        private TextView tvName;
        private TextView status;
        private CircleImageView circleImageView;
        private ImageView imageArrow;
        private View lineView;
        private View oneView;
    }

    private static class ViewHolderItem {
        private TextView bandwith;
        private TextView diskAll;
        private TextView todayCoin;

        private TextView jiebang;
        private TextView rename;
        private TextView start;
        private TextView offwifi;
    }

    @Override
    public View getGroupView(int i, boolean b, View convertView, ViewGroup viewGroup) {
        ViewHolderGroup groupHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.device_list_item, viewGroup, false);
            groupHolder = new ViewHolderGroup();
            groupHolder.tvSN = (TextView) convertView.findViewById(R.id.tvSN);
            groupHolder.tvName = (TextView) convertView.findViewById(R.id.tvName);
            groupHolder.status = (TextView) convertView.findViewById(R.id.status);
            groupHolder.imageArrow = (ImageView) convertView.findViewById(R.id.imageArrow);
            groupHolder.circleImageView = (CircleImageView) convertView.findViewById(R.id.circleImageview);
            groupHolder.lineView = (View) convertView.findViewById(R.id.lineView);
            groupHolder.oneView = (View) convertView.findViewById(R.id.oneView);
            AutoUtils.autoSize(convertView);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (ViewHolderGroup) convertView.getTag();
        }
        groupHolder.tvSN.setText(objects.get(i).getBoxSN());
        groupHolder.tvName.setText(objects.get(i).getBoxName());
        groupHolder.lineView.setVisibility(i == 0 ? View.INVISIBLE : View.VISIBLE);
        groupHolder.oneView.setVisibility(i == 0 ? View.VISIBLE : View.GONE);
        groupHolder.imageArrow.setImageResource(objects.get(i).isExpandable() ? R.mipmap.ic_arrow_up : R.mipmap.ic_arrow_down);
        groupHolder.status.setText(objects.get(i).getStatus() == 1 ? "工作中" : objects.get(i).getStatus() == 2 ? "待机中" : "未连接");
        groupHolder.circleImageView.setImageResource(objects.get(i).getStatus() == 1 ? R.color.circleGreen : objects.get(i).getStatus() == 2 ? R.color.circleYellow : R.color.circleGray);
        if((objects.get(i).getStatus() == 1 || objects.get(i).getStatus() == 2) && objects.get(i).getStorageSize() == 0){
            groupHolder.status.setText("\u3000异常");
            groupHolder.circleImageView.setImageResource(R.color.fontRed);
        }

        return convertView;
    }

    @Override
    public View getChildView(int i, int i1, boolean b, View convertView, ViewGroup viewGroup) {
        ViewHolderItem itemHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(
                    R.layout.device_list_item_item, viewGroup, false);
            itemHolder = new ViewHolderItem();
            itemHolder.bandwith = (TextView) convertView.findViewById(R.id.bandwith);
            itemHolder.diskAll = (TextView) convertView.findViewById(R.id.diskAll);
            itemHolder.todayCoin = (TextView) convertView.findViewById(R.id.todayCoin);

            itemHolder.jiebang = (TextView) convertView.findViewById(R.id.jiebang);
            itemHolder.rename = (TextView) convertView.findViewById(R.id.rename);
            itemHolder.start = (TextView) convertView.findViewById(R.id.start);
            itemHolder.offwifi = (TextView) convertView.findViewById(R.id.offwifi);
            AutoUtils.autoSize(convertView);
            convertView.setTag(itemHolder);
        } else {
            itemHolder = (ViewHolderItem) convertView.getTag();
        }
        itemHolder.bandwith.setText(objects.get(i).getUplinkBandwidth() + " Mbps");
        itemHolder.diskAll.setText(objects.get(i).getStorageSize() + " TB");
        itemHolder.todayCoin.setText(objects.get(i).getAllTodayCoins() + " 枚");

        itemHolder.offwifi.setText(objects.get(i).isCurrentWifi() ? "关闭WiFi" : "开启WiFi");
        itemHolder.start.setText(objects.get(i).isMining() ? "关闭" : "开启");

        itemHolder.jiebang.setOnClickListener(new ItemBottomOnClickListener(i, R.id.jiebang));
        itemHolder.rename.setOnClickListener(new ItemBottomOnClickListener(i, R.id.rename));
        itemHolder.start.setOnClickListener(new ItemBottomOnClickListener(i, R.id.start));
        itemHolder.offwifi.setOnClickListener(new ItemBottomOnClickListener(i, R.id.offwifi));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int i, int i1) {
        return false;
    }

    @Override
    public void onGroupExpanded(int groupPosition) {
        objects.get(groupPosition).setExpandable(true);
        notifyDataSetChanged();
        super.onGroupExpanded(groupPosition);
    }

    @Override
    public void onGroupCollapsed(int groupPosition) {
        objects.get(groupPosition).setExpandable(false);
        notifyDataSetChanged();
        super.onGroupCollapsed(groupPosition);
    }


    class ItemBottomOnClickListener implements View.OnClickListener {

        private int position, id;

        public ItemBottomOnClickListener(int position, int id) {
            this.position = position;
            this.id = id;
        }

        @Override
        public void onClick(View view) {
            if (mListener == null) return;
            mListener.updateNet(objects.get(position), id, position);
        }
    }
}

