package com.example.youssef.upsalestest;

import android.app.Application;


import com.example.youssef.upsalestest.interfaces.DaggerMyComponent;
import com.example.youssef.upsalestest.interfaces.MyComponent;
import com.example.youssef.upsalestest.modules.DaggerModule;


//import com.example.youssef.list.interfaces.DaggerMyComponent;

/**
 * Created by Youssef on 12/1/2016.
 *
 * The custom App Class for handling Dependency Injection with Dagger2
 *
 */

public class UpsalesTestApplication extends Application {

    MyComponent dagger;
    @Override
    public void onCreate() {
        super.onCreate();
        dagger = DaggerMyComponent.builder()
                .daggerModule(new DaggerModule(this))
                .build();
    }
    public MyComponent getComponent() {
        return dagger;
    }
}
