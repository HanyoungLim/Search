package com.toss.im.test.hanyoung.application;

import android.app.Application;

import com.example.lib_commons.ApplicationContextWrapper;


/**
 * BaseApplication
 *
 * 앱에 필요한 각종 환결설정 및 기본값 세팅한다..
 */
public class BaseApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        ApplicationContextWrapper.initializing(this);
    }
}
