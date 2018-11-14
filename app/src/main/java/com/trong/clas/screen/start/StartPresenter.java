package com.trong.clas.screen.start;

import android.os.AsyncTask;

import com.trong.clas.model.ListTxResponse;
import com.trong.clas.model.Transaction;
import com.trong.clas.service.AccountAPIService;
import com.trong.clas.service.AccountApi;
import com.trong.clas.util.Constant;

import org.web3j.protocol.Web3j;
import org.web3j.protocol.Web3jFactory;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;

import java.math.BigInteger;
import java.util.ArrayList;

import javax.security.auth.callback.Callback;

import retrofit2.Call;
import retrofit2.Response;

public class StartPresenter {
    private StartView mView;
    public void attachView(StartView view) {
        mView = view;
    }

    public void getBalance(final String address, final String tokenTestnet) {
        new GetBalanceTask(address).execute(tokenTestnet);
    }
    public void getListTx(String address) {
        mView.showProgress();
        AccountAPIService service = AccountApi.create();
        service.getListTxs(address).enqueue(new retrofit2.Callback<ListTxResponse>() {
            @Override
            public void onResponse(Call<ListTxResponse> call, Response<ListTxResponse> response) {
                mView.hideProgress();
                ListTxResponse listTxResponse = response.body();
                if (listTxResponse != null) {
                    if (listTxResponse.getStatus().equals("1") && listTxResponse.getMessage().equals(Constant.MESSAGE_OK)){
                        ArrayList<Transaction> result = listTxResponse.getResult();
                        mView.onGetListTx(result);
                    }
                    else {
                        mView.onGetListTx(null);
                    }
                }
                else {
                    mView.onGetListTx(null);
                }

            }

            @Override
            public void onFailure(Call<ListTxResponse> call, Throwable t) {
                mView.hideProgress();
                mView.onGetListTx(null);
            }
        });

    }
    class GetBalanceTask extends AsyncTask<String, Void, BigInteger> {
        private String mAddress;
        public GetBalanceTask(String address) {
            mAddress = address;
        }

        @Override
        protected void onPreExecute() {
            mView.showProgress();
            super.onPreExecute();
        }

        @Override
        protected BigInteger doInBackground(String... tokenTestnet) {
            try{
                Web3j web3j = Web3jFactory.build(new HttpService(tokenTestnet[0]));
                EthGetBalance ethGetBalance = web3j.ethGetBalance(mAddress , DefaultBlockParameterName.LATEST)
                        .sendAsync()
                        .get();
                return ethGetBalance.getBalance();
            }
            catch(Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(BigInteger balance) {
            super.onPostExecute(balance);
            mView.onGetBalanceResult(balance);
            if (!isCancelled()) {
                mView.hideProgress();
                cancel(true);

            }
        }
    }
}
