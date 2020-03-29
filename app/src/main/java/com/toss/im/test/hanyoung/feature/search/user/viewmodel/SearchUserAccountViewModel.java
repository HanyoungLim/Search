package com.toss.im.test.hanyoung.feature.search.user.viewmodel;


import com.example.lib_api.model.AccountUser;
import com.toss.im.test.hanyoung.custom.view.recycler.BaseViewModelAware;
import com.toss.im.test.hanyoung.feature.base.viewmodel.BooleanViewModel;
import com.toss.im.test.hanyoung.feature.search.user.VIEW_TYPE;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

/**
 * SearchUserAccountViewModel
 *
 * 즐겨찾기 상태만 viewModel을 쓴 이유는 특정 키값(유저이름)으로 데이터를 공유하기위해서
 * 같은이름이라면 (ex, 이소유) account 에서 pin하면 contact에서도 자동 pin된다.
 */

public class SearchUserAccountViewModel extends BaseObservable implements BaseViewModelAware, Observer<Boolean> {

    private AccountUser userModel;

    private BooleanViewModel pinnedViewModel;

    public SearchUserAccountViewModel(Fragment fragment, AccountUser userModel) {
        this.userModel = userModel;
        this.pinnedViewModel = BooleanViewModel.getInstance(fragment, userModel.getId());
        this.pinnedViewModel.setModel(userModel.isPinned());
        this.pinnedViewModel.getModel().observe(fragment, this);
    }

    public AccountUser getUserModel() {
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
    public String getBankLogoUrl() {
        return userModel.getBankLogoUrl();
    }

    @Bindable
    public String getBankInfo () {
        return String.format("%1$s %2$s", userModel.getBankName(), userModel.getBankAccountNo());
    }

    @Override
    public int getViewType() {
        return VIEW_TYPE.ACCOUNT.ordinal();
    }

    @Override
    public void onChanged(Boolean pinned) {
        if (pinned == null) {
            return;
        }

        userModel.setPinned(pinned);
    }
}
