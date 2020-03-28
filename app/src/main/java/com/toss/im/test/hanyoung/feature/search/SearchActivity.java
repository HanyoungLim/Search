package com.toss.im.test.hanyoung.feature.search;

import android.os.Bundle;

import com.example.lib_api.ApiCaller;
import com.example.lib_api.service.SearchService;
import com.toss.im.test.hanyoung.R;
import com.toss.im.test.hanyoung.base.BaseActivity;
import com.toss.im.test.hanyoung.databinding.ActivitySearchBinding;

import androidx.databinding.DataBindingUtil;

public class SearchActivity extends BaseActivity {

    private SearchService searchService = ApiCaller.getInstance().create(SearchService.class);

    private ActivitySearchBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        initUI();

        getData();
    }

    private void initUI () {
    }

    private void getData () {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }
}
