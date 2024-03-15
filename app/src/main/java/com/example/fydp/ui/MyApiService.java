package com.example.fydp.ui;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface MyApiService {
    @POST("addData")
    Call<Void> addData(@Body DataModel data);
}
