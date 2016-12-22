package com.example.youssef.upsalestest.modules;

import android.content.Context;
import android.content.SharedPreferences;


import com.example.youssef.upsalestest.UpsalesTestApplication;
import javax.inject.Singleton;
import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

/**
 * Created by Youssef on 12/2/2016.
 *
 * The main Dagger2 Module for Dependency Injection
 *
 */

@Module
public class DaggerModule {

    private static final String SERVER_URL = "https://integration.upsales.com/api/v2/";
    private UpsalesTestApplication app;

    public DaggerModule(UpsalesTestApplication app){
        this.app= app;
    }
    @Provides
    @Singleton
    UpsalesTestApplication provideApplicationContext(){
        return app;
    }

    @Provides
    @Singleton
    Retrofit providesRetrofit(UpsalesTestApplication application){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit;
    }
    @Provides
    @Singleton
    SharedPreferences providesSharedPreferences(UpsalesTestApplication application) {
        return application.getSharedPreferences("account", Context.MODE_PRIVATE);
    }
}
