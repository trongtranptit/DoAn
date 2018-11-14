package com.trong.clas.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.trong.clas.service.APIConstant.BASE_URL;

public class AccountApi {
    public static AccountAPIService create(){
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        AccountAPIService service = retrofit.create(AccountAPIService.class);
        return  service;
    }
}
