package com.trong.clas.screen.sendtx;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.trong.clas.doan_web3j.R;
import com.trong.clas.model.MyRawTx;
import com.trong.clas.model.SavedInfor;
import com.trong.clas.screen.start.StartActivity;
import com.trong.clas.util.Constant;
import com.trong.clas.util.Utilize;


import org.web3j.crypto.Hash;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigInteger;

public class TransactionActivity extends AppCompatActivity implements TransactionView {
    private EditText mEdtToAddress;
    private EditText mEdtAmountEth;
    private TextView mTvGasPrice;
    private TextView mTvGasLimit;
    private SeekBar mSeekbarGasPrice;
    private SeekBar mSeekbarGasLimit;
    private ImageView mIvNext;
    private TextView mTvNetworkFee;
    private TextView mTvInvalidAddress;
    private String mBalance;
    private ProgressDialog mDialog;
    private TransactionPresenter mPresenter;
    private SavedInfor mCurrentInfor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction);
        mPresenter = new TransactionPresenter();
        mPresenter.attachView(this);
        mDialog = new ProgressDialog(this);
        mBalance = (String) getIntent().getSerializableExtra(Constant.BALANCE);
        mCurrentInfor = (SavedInfor) getIntent().getSerializableExtra(Constant.SAVED_INFOR);
        mIvNext = findViewById(R.id.send_tx_ic_next);
        mEdtToAddress = findViewById(R.id.send_tx_edt_to_address);
        mTvGasPrice = findViewById(R.id.send_tx_tv_gas_price);
        mSeekbarGasPrice = findViewById(R.id.send_tx_seekbar_gas_price);
        mTvGasLimit = findViewById(R.id.send_tx_tv_gas_limit);
        mSeekbarGasLimit = findViewById(R.id.send_tx_seekbar_gas_limit);
        mEdtAmountEth = findViewById(R.id.send_tx_edt_amount_eth);
        mTvNetworkFee = findViewById(R.id.send_tx_tv_network_fee);
        mTvInvalidAddress = findViewById(R.id.send_tx_tv_invalid_address);
        mEdtToAddress.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (!isValidAddress(editable.toString())) {
                    mTvInvalidAddress.setVisibility(View.VISIBLE);
                }
                else {
                    mTvInvalidAddress.setVisibility(View.GONE);
                }
            }
        });
        mSeekbarGasLimit.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mTvGasLimit.setText(i*1000+"");
                mTvNetworkFee.setText(getNetworkFee());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mSeekbarGasPrice.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                mTvGasPrice.setText(i+" Gwei");
                mTvNetworkFee.setText(getNetworkFee());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mIvNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String amount = mEdtAmountEth.getText().toString();
                if (isValidAddress(mEdtToAddress.getText().toString()) && amount!=null && !amount.equals("")) {
                    final MyRawTx rawTx = new MyRawTx(
                            null,
                            mCurrentInfor.getAddress(),
                            mEdtToAddress.getText().toString(),
                            new BigInteger(mSeekbarGasLimit.getProgress()*1000+""),
                            new BigInteger(mSeekbarGasPrice.getProgress()+"000000000"),
                            Convert.toWei(amount, Convert.Unit.ETHER).toBigInteger()
                    );

                    View v = LayoutInflater.from(TransactionActivity.this).inflate(R.layout.dialog_confirm_send_tx, null, false);
                    TextView TvContent = v.findViewById(R.id.confirm_dialog_tv_detail_tx);
                    Button BtnConfirm = v.findViewById(R.id.confirm_dialog_btn_confirm);
                    final AlertDialog dialog = new AlertDialog.Builder(TransactionActivity.this)
                            .setView(v)
                            .create();

                    BigInteger res = new BigInteger(mBalance).subtract(
                            Convert.toWei(amount,Convert.Unit.ETHER).toBigInteger()
                    );
                    //wei -> eth and get float value
//                    res = Convert.fromWei(res.toString(), Convert.Unit.ETHER).toBigInteger();
                    String content = Utilize.getDisplayedBalance(TransactionActivity.this,mBalance ) +" - "+ amount+" = "+Utilize.getDisplayedBalance(TransactionActivity.this, res.toString())+"\n"
                            +"From: "+rawTx.getFrom()+"\n"
                            +"To:" + rawTx.getTo()+"\n"
                            +"Gas Price: "+rawTx.getGasPrice()+"\n"
                            +"Gas Limit: "+rawTx.getGasLimit()+"\n"
                            +"Network Fee:"+mTvNetworkFee.getText();
                    TvContent.setText(content);
                    BtnConfirm.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            dialog.dismiss();
                            mPresenter.getNonce(rawTx ,  getResources().getString(R.string.infura_rinkeby_token));
                        }
                    });
                    dialog.show();

                }
            }
        });
    }

    private String getNetworkFee() {
        float divide = (float) mSeekbarGasPrice.getProgress()*mSeekbarGasLimit.getProgress()/((float)1000000.0);
        return divide+" ETH";
    }

    public static String checkedAddress(final String address) {
        final String cleanAddress = Numeric.cleanHexPrefix(address).toLowerCase();
        //
        StringBuilder o = new StringBuilder();
        String keccak = Hash.sha3String(cleanAddress);
        char[] checkChars = keccak.substring(2).toCharArray();

        char[] cs = cleanAddress.toLowerCase().toCharArray();
        for (int i = 0; i < cs.length; i++) {
            char c = cs[i];
            c = (Character.digit(checkChars[i], 16) & 0xFF) > 7 ? Character.toUpperCase(c) : Character.toLowerCase(c);
            o.append(c);
        }
        return Numeric.prependHexPrefix(o.toString());
    }

    public static boolean isValidAddress(final String address) {
        return true;
//        return Numeric.prependHexPrefix(address).equals(checkedAddress(address));
    }

    @Override
    public void showProgress() {
        mDialog.setMessage(getResources().getString(R.string.loading));
        mDialog.setCancelable(false);
        mDialog.show();
    }

    @Override
    public void hideProgress() {
        if (mDialog.isShowing()) {
            mDialog.dismiss();
        }
    }

    @Override
    public boolean isNetworkConnected() {
        final ConnectivityManager conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo activeNetwork = conMgr.getActiveNetworkInfo();
        if (activeNetwork != null && activeNetwork.isConnected()) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void onSendedTx(boolean result) {
        hideProgress();
        if (!result) {
            setResult(RESULT_CANCELED);
        }
        else{
            setResult(RESULT_OK);
        }
        finish();
    }

    @Override
    public void onGetNonce(MyRawTx result) {
        if (result != null) {
            mPresenter.sendTx(
                    result, mCurrentInfor, getResources().getString(R.string.infura_rinkeby_token)
            );
        }
        else {
            setResult(RESULT_CANCELED);
            finish();
        }
    }
}
