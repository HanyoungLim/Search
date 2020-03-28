package com.toss.im.test.hanyoung.feature.base.viewmodel;

import com.toss.im.test.hanyoung.custom.view.recycler.BaseViewModelAware;
import com.toss.im.test.hanyoung.feature.search.user.VIEW_TYPE;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

public class TitleViewModel extends BaseObservable implements BaseViewModelAware {

    private String title;

    public TitleViewModel(String title) {
        this.title = title;
    }

    @Bindable
    public String getTitle() {
        return title;
    }

    @Override
    public int getViewType() {
        return VIEW_TYPE.TITLE.ordinal();
    }
}
