package com.toss.im.test.hanyoung.feature;

import android.content.Intent;
import android.os.Bundle;

import com.toss.im.test.hanyoung.R;
import com.toss.im.test.hanyoung.base.BaseActivity;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

public class TestActivity extends BaseActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_test);
    }

    @Override
    public void finish() {
        super.finish();
        setResult(RESULT_OK, new Intent());
    }
}
