package com.example.lib_commons;

import android.app.Application;
import android.content.ContextWrapper;

import androidx.annotation.NonNull;

public class ApplicationContextWrapper {

    private static ApplicationContextWrapper instance;

    public static Application getApplication () {
        if (instance == null) {
            throw new RuntimeException("ApplicationContextWrapper::getApplication, you have to initializing in application!!");
        }

        return instance.application;
    }

    public static void initializing (@NonNull Application application) {
        if (instance == null) {
            instance = new ApplicationContextWrapper(application);
        }
    }

    private Application application;

    private ApplicationContextWrapper(Application application) {
        this.application = application;
    }
}
