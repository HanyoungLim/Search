package com.toss.im.test.hanyoung.feature.search.user.viewmodel;

import com.example.lib_api.model.ContactUser;
import com.toss.im.test.hanyoung.custom.view.recycler.BaseViewModelAware;
import com.toss.im.test.hanyoung.feature.search.user.VIEW_TYPE;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.databinding.library.baseAdapters.BR;

public class SearchUserContactViewModel extends BaseObservable implements BaseViewModelAware {

    private ContactUser userModel;

    public SearchUserContactViewModel(ContactUser userModel) {
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
    public String getProfileImageUrl() {
        return userModel.getProfileImageUrl();
    }

    @Bindable
    public String getPhoneNumber() {
        return userModel.getPhoneNumber();
    }

    @Override
    public int getViewType() {
        return VIEW_TYPE.CONTACT.ordinal();
    }
}
