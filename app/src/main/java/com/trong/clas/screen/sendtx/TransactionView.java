package com.trong.clas.screen.sendtx;

import com.trong.clas.model.MyRawTx;

public interface TransactionView {
    void showProgress();
    void hideProgress();
    boolean isNetworkConnected();
    void  onSendedTx(boolean result);
    void onGetNonce(MyRawTx result);
}
