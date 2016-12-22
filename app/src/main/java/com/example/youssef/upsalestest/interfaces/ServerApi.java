package com.example.youssef.upsalestest.interfaces;

import java.util.Map;

import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import rx.Observable;

/**
 * Created by Youssef on 10/16/2016.
 *
 * The main Retrofit server API that communicates with the RESTful API
 *
 * Asynchronous communication is handled using RxAndroid with the Observable object
 *
 */

public interface ServerApi {

    // Getting accounts from the server API with filters as a Map
    @GET("accounts")
    Observable<String> getClientsList(@QueryMap Map<String, String> params);


}
