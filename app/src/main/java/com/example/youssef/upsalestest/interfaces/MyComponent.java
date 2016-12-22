package com.example.youssef.upsalestest.interfaces;


import com.example.youssef.upsalestest.modules.DaggerModule;
import com.example.youssef.upsalestest.presenters.ListPresenter;

import javax.inject.Singleton;

import dagger.Component;

/**
 * Created by Youssef on 12/2/2016.
 */

// The main Dagger2 component for dependency injection
@Singleton
@Component(modules={DaggerModule.class})
public interface MyComponent {

    void inject(ListPresenter listPresenter);
}
