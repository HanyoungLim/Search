package com.toss.im.test.hanyoung;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import com.toss.im.test.hanyoung.base.BaseActivity;

import java.util.concurrent.TimeUnit;

import androidx.databinding.DataBindingUtil;
import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * SplashActivity
 *
 * deeplink로 들어온 url을 parse 하여 다음으로 이동할 activity를 start한다.
 */
public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setStatusBarColor(Color.WHITE);

        DataBindingUtil.setContentView(this, R.layout.activity_main);

        /**
         * Completable을 쓴이유는 발행할 데이터는 없지만 발행 이벤트를 받기위해
         */
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
