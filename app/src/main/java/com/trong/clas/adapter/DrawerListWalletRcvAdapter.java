package com.trong.clas.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.trong.clas.doan_web3j.R;
import com.trong.clas.model.SavedInfor;
import com.trong.clas.screen.start.StartView;

import java.util.List;

public class DrawerListWalletRcvAdapter extends RecyclerView.Adapter<DrawerListWalletRcvAdapter.ItemWalletVH> {
    private List<SavedInfor> mData;
    private Context mContext;
    private StartView mListener;
    public DrawerListWalletRcvAdapter(Context context, List<SavedInfor> data, StartView listener ) {
        mData = data;
        mContext = context;
        mListener = listener;
    }
    @NonNull
    @Override
    public ItemWalletVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  v = LayoutInflater.from(mContext).inflate(R.layout.item_list_wallet, parent, false);
        return new ItemWalletVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemWalletVH holder, final int position) {
        final SavedInfor infor = mData.get(position);
        if (infor!=null) {
            holder.mTvAddress.setText(infor.getAddress());
            holder.mIvDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setTag(infor);
                    mListener.onItemListWalletClick(view,position);
                }
            });
            if (infor.ismIsCurrentWallet()) {
                holder.mTvAddress.setBackgroundResource(R.color.blue_300);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class ItemWalletVH extends RecyclerView.ViewHolder {
        private TextView mTvAddress;
        private ImageView mIvDelete;
        public ItemWalletVH(View itemView) {
            super(itemView);
            mTvAddress = itemView.findViewById(R.id.tv_item_wallet_address);
            mIvDelete = itemView.findViewById(R.id.iv_delete_wallet);
        }
    }

    public void setData(List<SavedInfor> data) {
        if (mData != null) {
            mData = null;
            mData.addAll(data);
        }
        else {
            mData.addAll(data);
        }
        notifyDataSetChanged();
    }
}
