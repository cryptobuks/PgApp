package com.app.qingyi.adapters;//package com.app.mobipromo.adapters;
//
//import android.content.Context;
//import android.support.v7.widget.RecyclerView;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.app.mobipromo.R;
//import com.app.mobipromo.models.MiningStatus;
//import com.app.mobipromo.views.RecyclerViewItemClickListener;
//import com.zhy.autolayout.utils.AutoUtils;
//
//import java.math.BigDecimal;
//import java.util.ArrayList;
//import java.util.List;
//
///**
// * Created by ylkjcjq on 2017/8/28.
// */
//
//public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
//    List<CoinObject> datas = new ArrayList<>();
//    private List<MiningStatus> mMiningStatuses = new ArrayList<>();
//    private Context mContext;
//
//    public RecyclerViewAdapter(List<CoinObject> datas) {
//        this.datas.clear();
//        this.datas.addAll(datas);
//    }
//
//    public RecyclerViewItemClickListener mRecyclerViewItemClickListener;
//
//    public void setmRecyclerViewItemClickListener(RecyclerViewItemClickListener mRecyclerViewItemClickListener) {
//        this.mRecyclerViewItemClickListener = mRecyclerViewItemClickListener;
//    }
//
//    public void setExxQc(List<MiningStatus> mMiningStatuses) {
//        this.mMiningStatuses.clear();
//        this.mMiningStatuses.addAll(mMiningStatuses);
//        notifyDataSetChanged();
//    }
//
//    //创建新View，被LayoutManager所调用
//    @Override
//    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
//        mContext = viewGroup.getContext();
//        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.recyclerview_item_, viewGroup, false);
//        ViewHolder vh = new ViewHolder(view);
//        return vh;
//    }
//
//    private String getStringOutE(String str){
//        BigDecimal bd = new BigDecimal(str);
//        return bd.toPlainString();
//    }
//
//    //将数据与界面进行绑定的操作
//    @Override
//    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
//        if(datas.size() == 0){
//            return;
//        }
//        if (mRecyclerViewItemClickListener != null) {
//            viewHolder.view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mRecyclerViewItemClickListener.onItemClick(viewHolder.view, position);
//                }
//            });
//        }
//        viewHolder.mTextViewName.setText(datas.get(position).getTokenName());
//        viewHolder.mTextViewAmount.setText(getStringOutE(datas.get(position).getTokenWalletValue()+""));
//        viewHolder.price.setText("~");
////        for (MiningStatus mExxQc : mMiningStatuses) {
////            if (datas.get(position).getTokenName().equals(mExxQc.getName()) && mExxQc.getTicker() != null) {
////                viewHolder.price.setText("最新价格：" + mExxQc.getTicker().getLast());
////            }
////        }
//        if (datas.get(position).getTokenName().equals("CAN")) {
//            viewHolder.img.setImageResource(R.mipmap.market_can);
//        } else if (datas.get(position).getTokenName().equals("ETH")) {
//            viewHolder.img.setImageResource(R.mipmap.market_eth);
//        } else if (datas.get(position).getTokenName().equals("EOS")) {
//            viewHolder.img.setImageResource(R.mipmap.market_eos);
//        } else {
//            viewHolder.img.setImageResource(R.mipmap.ico_coin_btc_dark);
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
//        public TextView mTextViewName;
//        public View view;
//        public TextView mTextViewAmount;
//        public TextView price;
//        public ImageView img;
//
//        public ViewHolder(View view) {
//            super(view);
//            //对于listview，注意添加这一行，即可在item上使用高度
//            AutoUtils.autoSize(view);
//            this.view = view;
//            mTextViewName = (TextView) view.findViewById(R.id.name);
//            mTextViewAmount = (TextView) view.findViewById(R.id.tvNUM);
//            price = (TextView) view.findViewById(R.id.price);
//            img = (ImageView) view.findViewById(R.id.img);
//        }
//    }
//}
