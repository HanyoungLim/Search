package com.toss.im.test.hanyoung.feature.base.viewmodel;

import android.app.Application;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.databinding.BaseObservable;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;
import io.reactivex.Observable;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.schedulers.Schedulers;

public abstract class BaseLiveDataViewModel<M> extends AndroidViewModel {

    private androidx.databinding.Observable.OnPropertyChangedCallback callback = new androidx.databinding.Observable.OnPropertyChangedCallback() {

        @Override
        public void onPropertyChanged(androidx.databinding.Observable sender, int propertyId) {
            compositeDisposable.add(Observable.just(getModel())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(model -> {
                        if (model != null) {
                            getModel().setValue(getModel().getValue());
                        }
                    }, e -> {
                        Log.e("BaseLiveDataViewModel", "failed refresh model", e);
                    }));
        }
    };

    private CompositeDisposable compositeDisposable = new CompositeDisposable();
    private MutableLiveData<M> model;

    public BaseLiveDataViewModel(@NonNull Application application) {
        super(application);
    }

    public void setModel(M model) {
        if (model == null) {
            if (this.model == null) {
                return;
            }

            if (Looper.myLooper() == Looper.getMainLooper()) {
                this.model.setValue(null);
            } else {
                this.model.postValue(null);
            }
            return;
        }

        compositeDisposable.add(Single.just(model)
                .subscribeOn(Schedulers.trampoline())
                .map(data -> {
                    if (data instanceof BaseObservable) {
                        ((BaseObservable) data).removeOnPropertyChangedCallback(callback);
                        ((BaseObservable) data).addOnPropertyChangedCallback(callback);
                    }

                    if (this.model == null) {
                        this.model = new MutableLiveData<>(data);
                    }

                    return data;
                })
                .observeOn(Looper.myLooper() == Looper.getMainLooper() ? Schedulers.trampoline() : AndroidSchedulers.mainThread())
                .subscribe(data -> this.model.setValue(data), e -> {
                    Log.e("BaseLiveDataViewModel", "failed setModel", e);
                }));
    }

    public MutableLiveData<M> getModel() {
        if (model == null) {
            model = new MutableLiveData<>();
        }
        return model;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}
