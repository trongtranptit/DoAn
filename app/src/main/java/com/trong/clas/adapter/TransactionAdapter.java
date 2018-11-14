package com.trong.clas.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.trong.clas.doan_web3j.R;
import com.trong.clas.model.Transaction;
import com.trong.clas.screen.start.StartView;

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TxVH> {
    private List<Transaction> mData;
    private StartView mListener;
    private Context mContext;
    public TransactionAdapter(Context context, ArrayList<Transaction> data, StartView listener){
        mListener = listener;
        mContext = context;
        mData = data;
    }


    @NonNull
    @Override
    public TxVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_list_tx, parent, false);
        return new TxVH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull TxVH holder, final int position) {
        final Transaction tx = mData.get(position);
        if (tx != null) {
            holder.mTvTxHash.setText(tx.getHash());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setTag(tx);
                    mListener.onItemListTxClick(view,position);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class TxVH extends RecyclerView.ViewHolder {
        private TextView mTvTxHash;
        public TxVH(View itemView) {
            super(itemView);
            mTvTxHash = itemView.findViewById(R.id.item_tx_tv_tx_hash);
        }
    }
}
