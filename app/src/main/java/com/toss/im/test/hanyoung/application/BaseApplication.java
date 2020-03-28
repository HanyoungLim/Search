package com.toss.im.test.hanyoung.application;

import android.app.Application;

import com.example.lib_commons.ApplicationContextWrapper;

public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationContextWrapper.initializing(this);
    }
}
