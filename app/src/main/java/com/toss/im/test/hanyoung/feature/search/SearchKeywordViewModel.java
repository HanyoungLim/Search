package com.toss.im.test.hanyoung.feature.search;

import android.app.Application;
import android.util.Log;

import com.example.lib_commons.ApplicationContextWrapper;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;

public class SearchKeywordViewModel extends AndroidViewModel {

    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    private MutableLiveData<String> keywordModel = new MutableLiveData<>();

    public SearchKeywordViewModel(@NonNull Application application) {
        super(application);
    }

    public static SearchKeywordViewModel getInstance(FragmentActivity activity) {
        try {
            return new ViewModelProvider(activity).get(activity.getClass().getSimpleName(), SearchKeywordViewModel.class);
        } catch (Exception e) {
            Log.e("SearchKeywordViewModel", "failed initializing", e);
            return new SearchKeywordViewModel(ApplicationContextWrapper.getApplication());
        }
    }

    public MutableLiveData<String> getKeywordModel() {
        return keywordModel;
    }

    public void setKeyword(String keyword) {
        if (keyword == null) {
            keyword = "";
        }

        compositeDisposable.add(Single.just(keyword)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    keywordModel.setValue(response);
                }, e -> {
                    Log.e("SearchKeywordViewModel", "failed setKeyword", e);
                }));
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}
