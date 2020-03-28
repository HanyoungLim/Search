package com.toss.im.test.hanyoung;

import android.content.Intent;
import android.os.Bundle;

import com.toss.im.test.hanyoung.base.BaseActivity;
import com.toss.im.test.hanyoung.feature.search.SearchActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = new Intent(this, SearchActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }
}
