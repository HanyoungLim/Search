package com.toss.im.test.hanyoung.feature.search;

import android.os.Bundle;
import android.util.Log;

import com.example.lib_commons.util.ParameterContants;
import com.example.lib_commons.util.StringUtility;
import com.toss.im.test.hanyoung.custom.view.text.SimpleTextWatcher;
import com.toss.im.test.hanyoung.R;
import com.toss.im.test.hanyoung.base.BaseActivity;
import com.toss.im.test.hanyoung.databinding.ActivitySearchBinding;
import com.toss.im.test.hanyoung.feature.search.user.SearchUserFragment;

import java.util.concurrent.TimeUnit;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchActivity extends BaseActivity implements Observer<String> {

    private ActivitySearchBinding binding;

    private SearchKeywordViewModel searchKeywordViewModel;

    private SimpleTextWatcher keywordTextWatcher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_search);

        String defaultKeyword = null;
        if (getIntent() == null) {
            //savedInstantState
        } else {
            defaultKeyword = getIntent().getStringExtra(ParameterContants.PARAM_SEARCH_DEFAULT_KEYWORD);
        }

        searchKeywordViewModel = SearchKeywordViewModel.getInstance(this);
        searchKeywordViewModel.getKeywordModel().observe(this, this);

        initUI();
        initListener();

        if (StringUtility.isNotNullOrEmpty(defaultKeyword)) {
            binding.actSearchEt.setText(defaultKeyword);
        }
    }

    private void initUI () {
        SearchUserFragment searchUserFragment = new SearchUserFragment();
        searchUserFragment.setArguments(null);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, searchUserFragment).commit();
    }

    private void initListener () {

        compositeDisposable.add(Observable.create(new ObservableOnSubscribe<String>() {

            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                if (keywordTextWatcher == null) {
                    keywordTextWatcher = new SimpleTextWatcher() {
                        @Override
                        public void onTextChanged(CharSequence s, int start, int before, int count) {
                            super.onTextChanged(s, start, before, count);
                            String keyword = s.toString();
                            emitter.onNext(keyword);
                            Log.d("SearchActivity", "onTextChanged keyword = " + keyword);
                        }
                    };

                    binding.actSearchEt.addTextChangedListener(keywordTextWatcher);
                }
            }

        })
        .debounce(1500, TimeUnit.MILLISECONDS)
        .subscribeOn(Schedulers.trampoline())
        .observeOn(AndroidSchedulers.mainThread())
        .subscribe(response -> {
            searchKeywordViewModel.setKeyword(response);
        }, e -> {
            Log.e("SearchActivity", "failed debounce keyword", e);
        }));

    }

    @Override
    public void onChanged(String keyword) {
        Log.d("SearchActivity", "onChanged keyword = " + keyword);
    }
}
