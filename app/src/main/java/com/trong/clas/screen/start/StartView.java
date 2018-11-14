package com.trong.clas.screen.start;

import android.view.View;

import com.trong.clas.model.Transaction;

import java.math.BigInteger;
import java.util.ArrayList;

public interface StartView {
    void showProgress();
    void hideProgress();
    boolean isNetworkConnected();
    void onGetBalanceResult(BigInteger balance);
    void onItemListWalletClick(View view,int pos);
    void onItemListTxClick(View view, int pos);
    void onGetListTx(ArrayList<Transaction> transactions);
}
