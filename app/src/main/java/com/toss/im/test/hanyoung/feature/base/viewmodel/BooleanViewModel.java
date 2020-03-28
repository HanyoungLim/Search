package com.toss.im.test.hanyoung.feature.base.viewmodel;

import android.app.Application;
import android.util.Log;

import com.example.lib_commons.ApplicationContextWrapper;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

public class BooleanViewModel extends BaseLiveDataViewModel<Boolean> {

    public BooleanViewModel(@NonNull Application application) {
        super(application);
    }

    public static BooleanViewModel getInstance (@NonNull FragmentActivity activity, @NonNull String key) {
        try {
            return new ViewModelProvider(activity).get(key, BooleanViewModel.class);
        } catch (Exception e){
            Log.e("BooleanViewModel", "failed initializing BooleanViewModel", e);
            return new BooleanViewModel(ApplicationContextWrapper.getApplication());
        }
    }

    public static BooleanViewModel getInstance (@NonNull Fragment fragment, @NonNull String key) {
        try {
            return new ViewModelProvider(fragment).get(key, BooleanViewModel.class);
        } catch (Exception e){
            Log.e("BooleanViewModel", "failed initializing BooleanViewModel", e);
            return new BooleanViewModel(ApplicationContextWrapper.getApplication());
        }
    }
}
