package com.toss.im.test.hanyoung;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;

import com.toss.im.test.hanyoung.base.BaseActivity;
import com.toss.im.test.hanyoung.feature.search.SearchActivity;

import java.util.concurrent.TimeUnit;

import androidx.databinding.DataBindingUtil;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.WHITE);

        DataBindingUtil.setContentView(this, R.layout.activity_main);

        compositeDisposable.add(Completable
                .timer(1500, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(() -> {
                    Intent intent = UriParser.parseUri(this);
                    startActivity(intent);
                    finish();
                }));
    }
}
