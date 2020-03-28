package com.toss.im.test.hanyoung.feature.search.user.viewmodel;

import com.example.lib_api.model.ContactUser;
import com.toss.im.test.hanyoung.custom.view.recycler.BaseViewModelAware;
import com.toss.im.test.hanyoung.feature.base.viewmodel.BooleanViewModel;
import com.toss.im.test.hanyoung.feature.search.user.VIEW_TYPE;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

public class SearchUserContactViewModel extends BaseObservable implements BaseViewModelAware, Observer<Boolean> {

    private ContactUser userModel;

    private BooleanViewModel pinnedViewModel;

    public SearchUserContactViewModel(Fragment fragment, ContactUser userModel) {
        this.userModel = userModel;
        this.pinnedViewModel = BooleanViewModel.getInstance(fragment, userModel.getId());
        this.pinnedViewModel.setModel(userModel.isPinned());
        this.pinnedViewModel.getModel().observe(fragment, this);
    }

    public ContactUser getUserModel() {
        return userModel;
    }

    @Bindable
    public String getName() {
        return userModel.getName();
    }

    @Bindable
    public MutableLiveData<Boolean> getPinned() {
        return pinnedViewModel.getModel();
    }

    public void setPinned(boolean pinned) {
        pinnedViewModel.setModel(pinned);
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

    @Override
    public void onChanged(Boolean pinned) {
        if (pinned == null) {
            return;
        }

        userModel.setPinned(pinned);
    }
}
