package com.app.qingyi.adapters;//package com.app.mobipromo.adapters;
//
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.app.mobipromo.R;
//import com.app.mobipromo.models.TransferHistoryItem;
//import com.app.mobipromo.views.RecyclerViewItemClickListener;
//import com.zhy.autolayout.utils.AutoUtils;
//
//import java.util.List;
//
///**
// * Created by ylkjcjq on 2017/8/28.
// */
//
//public class RecyclerViewDetialAdapter extends RecyclerView.Adapter<RecyclerViewDetialAdapter.ViewHolder> {
//    List<TransferHistoryItem> datas = null;
//    CoinObject mCoinObject = null;
//
//    public RecyclerViewDetialAdapter(List<TransferHistoryItem> datas) {
//        this.datas = datas;
//    }
//
//    public RecyclerViewItemClickListener mRecyclerViewItemClickListener;
//
//    public void setCoinObject(CoinObject mCoinObject) {
//        this.mCoinObject = mCoinObject;
//    }
//
//    public void setmRecyclerViewItemClickListener(RecyclerViewItemClickListener mRecyclerViewItemClickListener) {
//        this.mRecyclerViewItemClickListener = mRecyclerViewItemClickListener;
//    }
//
//    //创建新View，被LayoutManager所调用
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.record_recyclerview_item_, viewGroup, false);
//        ViewHolder vh = new ViewHolder(view);
//        return vh;
//    }
//
//    //将数据与界面进行绑定的操作
//    @Override
//    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
//        if (mRecyclerViewItemClickListener != null) {
//            viewHolder.view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mRecyclerViewItemClickListener.onItemClick(viewHolder.view, position);
//                }
//            });
//        }
//
//        viewHolder.tv_hash.setText(datas.get(position).getTransactionHash());
//        if (mCoinObject != null) {
//            if (mCoinObject.getTokenWalletAddress().toLowerCase().equals(datas.get(position).getTransactionFrom())) {
//                viewHolder.img.setBackgroundResource(R.mipmap.icon_5);
//                viewHolder.tv_num.setText("-" + datas.get(position).getTransactionValue());
//            } else {
//                viewHolder.img.setBackgroundResource(R.mipmap.icon_6);
//                viewHolder.tv_num.setText("+" + datas.get(position).getTransactionValue());
//            }
//        }
//    }
//
//    //获取数据的数量
//    @Override
//    public int getItemCount() {
//        return datas.size();
//    }
//
//    //自定义的ViewHolder，持有每个Item的的所有界面元素
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        public ImageView img;
//        public TextView tv_hash;
//        public View view;
//        public TextView tv_num;
//
//        public ViewHolder(View view) {
//            super(view);
//            //对于listview，注意添加这一行，即可在item上使用高度
//            AutoUtils.autoSize(view);
//            this.view = view;
//            img = (ImageView) view.findViewById(R.id.img);
//            tv_hash = (TextView) view.findViewById(R.id.tv_hash);
//            tv_num = (TextView) view.findViewById(R.id.tv_num);
//        }
//    }
//}
