package com.trong.clas.service;

import com.trong.clas.model.ListTxResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface AccountAPIService {
    @GET("api?module=account&action=txlist&startblock=0&endblock=99999999&sort=asc&apikey=YourApiKeyToken")
    Call<ListTxResponse> getListTxs(@Query("address") String address);
}
