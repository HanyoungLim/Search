package com.toss.im.test.hanyoung.feature.search.user;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lib_api.ApiCaller;
import com.example.lib_api.model.AccountUser;
import com.example.lib_api.model.ContactUser;
import com.example.lib_api.service.SearchService;
import com.toss.im.test.hanyoung.R;
import com.toss.im.test.hanyoung.base.BaseFragment;
import com.toss.im.test.hanyoung.custom.view.recycler.BaseDatabindingViewHolder;
import com.toss.im.test.hanyoung.custom.view.recycler.BaseViewModelAware;
import com.toss.im.test.hanyoung.custom.view.recycler.EmptyViewHolder;
import com.toss.im.test.hanyoung.databinding.FragmentSearchUserBinding;
import com.toss.im.test.hanyoung.databinding.ItemSearchUserAccountBinding;
import com.toss.im.test.hanyoung.databinding.ItemSearchUserContactBinding;
import com.toss.im.test.hanyoung.databinding.ItemSearchUserTitleBinding;
import com.toss.im.test.hanyoung.feature.search.SearchKeywordViewModel;
import com.toss.im.test.hanyoung.feature.search.user.viewmodel.SearchUserAccountViewModel;
import com.toss.im.test.hanyoung.feature.search.user.viewmodel.SearchUserContactViewModel;
import com.toss.im.test.hanyoung.feature.base.viewmodel.TitleViewModel;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SearchUserFragment extends BaseFragment implements Observer<String> {

    private SearchService searchService = ApiCaller.getInstance().create(SearchService.class);

    private FragmentSearchUserBinding binding;

    private SearchKeywordViewModel searchKeywordViewModel;

    private SearchUserRecyclerViewAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_user, container, false);
        binding.setLifecycleOwner(this);

        searchKeywordViewModel = SearchKeywordViewModel.getInstance(getActivity());
        searchKeywordViewModel.getKeywordModel().observe(this, this);

        initUi ();
        initListener ();

        return binding.getRoot();
    }


    private void initUi () {

        initRecyclerView();
    }

    private void initRecyclerView () {
        if (adapter == null) {
            adapter = new SearchUserRecyclerViewAdapter();
            binding.recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            binding.recyclerView.setAdapter(adapter);
        }
    }

    private void initListener () {

    }

    private void getData (String keyword) {
        compositeDisposable.add(searchService.getUsersByKeyword(keyword)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .map(users -> {
                    List<BaseViewModelAware> viewModelList = new ArrayList<>();

                    viewModelList.add(new TitleViewModel(getActivity().getResources().getString(R.string.search_user_contact_title)));
                    if (users.getContactUserList() != null && !users.getContactUserList().isEmpty()) {
                        for (ContactUser contactUser : users.getContactUserList()) {
                            viewModelList.add(new SearchUserContactViewModel(contactUser));
                        }
                    }

                    viewModelList.add(new TitleViewModel(getActivity().getResources().getString(R.string.search_user_account_title)));
                    if (users.getAccountUserList() != null && !users.getAccountUserList().isEmpty()) {
                        for (AccountUser accountUser : users.getAccountUserList()) {
                            viewModelList.add(new SearchUserAccountViewModel(accountUser));
                        }
                    }

                    return viewModelList;
                })
                .subscribe(response -> {
                    adapter.setItems(response);
                    Log.d("SearchActivity", "getData success");
                }, e -> {
                    Log.e("SearchActivity", "getData failed", e);
                }));

    }

    @Override
    public void onChanged(String keyword) {
        Log.d("SearchUserFragment", "onChanged keyword = " + keyword);
        getData(keyword);
    }

    public class SearchUserRecyclerViewAdapter extends RecyclerView.Adapter<BaseDatabindingViewHolder> {

        private List<BaseViewModelAware> items = new ArrayList<>();

        @NonNull
        @Override
        public BaseDatabindingViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            switch (VIEW_TYPE.values()[viewType]) {
                case TITLE:
                    return new SearchUserTitleViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_search_user_title, parent, false));
                case ACCOUNT:
                    return new SearchUserAccountViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_search_user_account, parent, false));
                case CONTACT:
                    return new SearchUserContactViewHolder(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_search_user_contact, parent, false));
                default:
                    return new EmptyViewHolder(parent);
            }
        }

        @Override
        public void onBindViewHolder(@NonNull BaseDatabindingViewHolder holder, int position) {
            BaseViewModelAware item = getItem(position);
            if (item != null) {
                holder.setData(item);
            }
        }

        public void setItems(List<BaseViewModelAware> items) {
            this.items.clear();
            if (items == null || items.isEmpty()) {
                return;
            }

            this.items.addAll(items);
            notifyDataSetChanged();
        }

        public BaseViewModelAware getItem (int position) {
            return items.get(position);
        }

        @Override
        public int getItemCount() {
            return items.size();
        }

        @Override
        public int getItemViewType(int position) {
            return items.get(position).getViewType();
        }

        public class SearchUserTitleViewHolder extends BaseDatabindingViewHolder<ItemSearchUserTitleBinding, TitleViewModel> {

            public SearchUserTitleViewHolder(@NonNull ItemSearchUserTitleBinding vhBinding) {
                super(vhBinding);
            }

            @Override
            public void setData(TitleViewModel viewModel) {
                vhBinding.setViewModel(viewModel);
                vhBinding.executePendingBindings();
            }
        }

        public class SearchUserContactViewHolder extends BaseDatabindingViewHolder<ItemSearchUserContactBinding, SearchUserContactViewModel> {

            public SearchUserContactViewHolder(@NonNull ItemSearchUserContactBinding vhBinding) {
                super(vhBinding);
            }

            @Override
            public void setData(SearchUserContactViewModel viewModel) {
                vhBinding.setViewModel(viewModel);
                vhBinding.executePendingBindings();
            }
        }

        public class SearchUserAccountViewHolder extends BaseDatabindingViewHolder<ItemSearchUserAccountBinding, SearchUserAccountViewModel> {

            public SearchUserAccountViewHolder(@NonNull ItemSearchUserAccountBinding vhBinding) {
                super(vhBinding);
            }

            @Override
            public void setData(SearchUserAccountViewModel viewModel) {
                vhBinding.setViewModel(viewModel);
                vhBinding.executePendingBindings();
            }
        }
    }
}
