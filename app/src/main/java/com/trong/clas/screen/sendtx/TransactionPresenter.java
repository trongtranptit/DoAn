package com.trong.clas.screen.sendtx;

import android.os.AsyncTask;

import com.trong.clas.model.MyRawTx;
import com.trong.clas.model.SavedInfor;

import org.web3j.crypto.Credentials;
import org.web3j.crypto.ECKeyPair;
import org.web3j.crypto.RawTransaction;
import org.web3j.crypto.TransactionEncoder;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.Request;
import org.web3j.protocol.core.methods.response.EthGetTransactionCount;
import org.web3j.protocol.core.methods.response.EthSendTransaction;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;
import org.web3j.utils.Numeric;

import java.math.BigDecimal;
import java.math.BigInteger;

public class TransactionPresenter {
    private TransactionView mView;

    public void attachView(TransactionView view) {
        mView = view;
    }

    public void detachView() {
        if (mView !=null) {
            mView = null;
        }
    }

    public void getNonce(MyRawTx rawTx, String tokenTestnet) {
        new GetNonceTask(rawTx).execute(tokenTestnet);
    }

    public void sendTx(MyRawTx rawTx, SavedInfor infor, String tokenTestnet) {
        mView.showProgress();
        new SendTxTask(rawTx,infor).execute(tokenTestnet);
    }

    class GetNonceTask extends AsyncTask<String, Void, BigInteger> {
        private MyRawTx mRawTx;
        public GetNonceTask(MyRawTx rawTx) {
            mRawTx = rawTx;
        }
        @Override
        protected BigInteger doInBackground(String... tokens) {
            try {

                Web3j web3j = Web3jFactory.build(new HttpService(tokens[0]));
                EthGetTransactionCount ethGetTransactionCount = web3j.ethGetTransactionCount(
                        mRawTx.getFrom() , DefaultBlockParameterName.LATEST).sendAsync().get();

                BigInteger nonce = ethGetTransactionCount.getTransactionCount();

                return nonce;
            }
            catch(Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(BigInteger bigInteger) {
            super.onPostExecute(bigInteger);
            if (bigInteger == null) {
                mRawTx.setNonce(null);
                mView.onGetNonce(null);
            }
            else {
                mRawTx.setNonce(bigInteger);
                mView.onGetNonce(mRawTx);
            }
            if (!isCancelled()) {
                cancel(true);
            }
        }
    }

    class SendTxTask extends AsyncTask<String, Void, Boolean> {
        private MyRawTx mRawTx;
        private SavedInfor mInfor;
        public SendTxTask(MyRawTx rawTx, SavedInfor infor) {
            mRawTx = rawTx;
            mInfor = infor;
        }
        @Override
        protected Boolean doInBackground(String... tokens) {
            try{
                Web3j web3j = Web3jFactory.build(new HttpService(tokens[0]));
                ECKeyPair keyPair = ECKeyPair.create(new BigInteger(mInfor.getPrivatekey()));
                Credentials credentials = Credentials.create(keyPair);
//                RawTransaction rtx = RawTransaction.createEtherTransaction(
//                        mRawTx.getNonce(),
//                        mRawTx.getGasPrice(),
//                        mRawTx.getGasLimit(),
//                        mRawTx.getTo(),
//                        mRawTx.getValue()
//                );
//                byte[] signedMessage = TransactionEncoder.signMessage(rtx, credentials);
//                String hexValue = Numeric.toHexString(signedMessage);
//                EthSendTransaction ethSendTransaction = web3j.ethSendRawTransaction(hexValue).sendAsync().get();


//                if (ethSendTransaction.hasError()) {
//                    return false;
//                }
//                String transactionHash = ethSendTransaction.getTransactionHash();
                Transfer transfer = new Transfer(web3j, new RawTransactionManager(web3j, credentials));
                TransactionReceipt transactionReceipt = transfer.sendFunds(
                         mRawTx.getTo(), new BigDecimal(mRawTx.getValue()), Convert.Unit.WEI, mRawTx.getGasPrice(), mRawTx.getGasLimit()
                ).send();
//                String st = transactionReceipt.getStatus();
//                Status.FINISHED.toString();
//                while (!transactionReceipt.getStatus().equals(Status.FINISHED.toString())) {
//
//                };
                while (transactionReceipt.getBlockNumber() == null){};
                return true;
            }
            catch(Exception e) {
                return false;
            }
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            mView.onSendedTx(aBoolean);
            if (!isCancelled()) {
                mView.hideProgress();
                cancel(true);
            }
        }
    }
}
