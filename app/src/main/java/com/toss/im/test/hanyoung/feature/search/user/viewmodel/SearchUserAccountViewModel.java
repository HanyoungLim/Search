package com.toss.im.test.hanyoung.feature.search.user.viewmodel;

import android.accounts.Account;

import com.example.lib_api.model.AccountUser;
import com.example.lib_api.model.ContactUser;
import com.toss.im.test.hanyoung.custom.view.recycler.BaseViewModelAware;
import com.toss.im.test.hanyoung.feature.search.user.VIEW_TYPE;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class SearchUserAccountViewModel extends BaseObservable implements BaseViewModelAware {

    private AccountUser userModel;

    public SearchUserAccountViewModel(AccountUser userModel) {
        this.userModel = userModel;
    }

    @Bindable
    public String getName() {
        return userModel.getName();
    }

    @Bindable
    public boolean isPinned() {
        return userModel.isPinned();
    }

    public void setPinned(boolean pinned) {
        userModel.setPinned(pinned);
        notifyPropertyChanged(BR.pinned);
    }

    @Bindable
    public String getBankLogoUrl() {
        return userModel.getBankLogoUrl();
    }

    @Bindable
    public String getBankName() {
        return userModel.getBankName();
    }

    @Bindable
    public String getBankAccountNo() {
        return userModel.getBankAccountNo();
    }


    @Override
    public int getViewType() {
        return VIEW_TYPE.ACCOUNT.ordinal();
    }
}
