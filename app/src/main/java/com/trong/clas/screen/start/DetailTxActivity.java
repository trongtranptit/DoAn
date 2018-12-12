package com.trong.clas.screen.start;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.trong.clas.doan_web3j.R;
import com.trong.clas.model.Transaction;
import com.trong.clas.util.Constant;

import java.sql.Time;

public class DetailTxActivity extends AppCompatActivity {
    private TextView TxHash;
    private TextView TxReceiptStatus;
    private TextView TimeStamp;
    private TextView From;
    private TextView To;
    private TextView Nonce;
    private TextView InputData;
    private Transaction mTx;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_tx);
        mTx = (Transaction) getIntent().getSerializableExtra(Constant.TX);
        TxHash = findViewById(R.id.detail_tx_tx_hash);
        TxReceiptStatus = findViewById(R.id.detail_tx_tx_receipt_status);
        TimeStamp = findViewById(R.id.detail_tx_time_stamp);
        From = findViewById(R.id.detail_tx_from);
        To = findViewById(R.id.detail_tx_to);
        Nonce = findViewById(R.id.detail_tx_nonce);
        InputData = findViewById(R.id.detail_tx_input_data);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
        if (mTx != null) {
            try{
                TxHash.setText(mTx.getHash());
                TxReceiptStatus.setText(mTx.getTxreceipt_status());
                TimeStamp.setText(mTx.getTimeStamp());
                From.setText(mTx.getFrom());
                To.setText(mTx.getTo());
                Nonce.setText(mTx.getNonce());
                InputData.setText(mTx.getInput());
            }
            catch(Exception e){
                Log.e("TAG",e.getMessage());
            }
        }
    }
}
