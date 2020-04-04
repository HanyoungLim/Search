package com.toss.im.test.hanyoung.feature.search.user;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.lib_api.ApiCaller;
import com.example.lib_api.model.AccountUser;
import com.example.lib_api.model.ContactUser;
import com.example.lib_api.service.SearchService;
import com.example.lib_commons.util.StringUtility;
import com.toss.im.test.hanyoung.R;
import com.toss.im.test.hanyoung.base.BaseFragment;
import com.toss.im.test.hanyoung.custom.view.recycler.BaseDatabindingViewHolder;
import com.toss.im.test.hanyoung.custom.view.recycler.BaseViewModelAware;
import com.toss.im.test.hanyoung.custom.view.recycler.EmptyViewHolder;
import com.toss.im.test.hanyoung.databinding.FragmentSearchUserBinding;
import com.toss.im.test.hanyoung.databinding.ItemSearchUserAccountBinding;
import com.toss.im.test.hanyoung.databinding.ItemSearchUserContactBinding;
import com.toss.im.test.hanyoung.databinding.ItemSearchUserTitleBinding;
import com.toss.im.test.hanyoung.feature.TestActivity;
import com.toss.im.test.hanyoung.feature.search.SearchKeywordViewModel;
import com.toss.im.test.hanyoung.feature.search.user.db.PinnedUsersDB;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 * SearchUserFragment
 *
 * rx의 zip을 사용하여, 로컬pinned유저, 및 api response를 통해받은유저정보를 동시에 response함
 */
public class SearchUserFragment extends BaseFragment implements Observer<String> {

    private SearchService searchService = ApiCaller.getInstance().create(SearchService.class);

    private FragmentSearchUserBinding binding;

    private SearchKeywordViewModel searchKeywordViewModel;
    private PinnedUsersDB pinnedUserDB;

    private SearchUserRecyclerViewAdapter adapter;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        pinnedUserDB = new PinnedUsersDB(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (pinnedUserDB != null) {
            pinnedUserDB.close();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_user, container, false);
        binding.setLifecycleOwner(this);

        if (savedInstanceState == null) {

        } else {

        }

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
        binding.drawerSwipeRefresh.setOnChildScrollUpCallback(new SwipeRefreshLayout.OnChildScrollUpCallback() {
            @Override
            public boolean canChildScrollUp(SwipeRefreshLayout parent, @Nullable View child) {
                return binding.recyclerView.getScrollY() != 0;
            }
        });

        binding.drawerSwipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (searchKeywordViewModel.getKeywordModel() != null) {
                    searchKeywordViewModel.setKeyword(searchKeywordViewModel.getKeywordModel().getValue());
                }
            }
        });
    }

    private void getData (String keyword) {
        compositeDisposable.add(Single.zip(
                Single.just(pinnedUserDB.getPinnedUserList(keyword)),    //로컬 db pinned user 검색
                searchService.getUsersByKeyword(keyword)    //api 검색
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread()), (pinnedUserIdList, users) -> {

                            List<BaseViewModelAware> viewModelList = new ArrayList<>();

                            if (users.getContactUserList() != null && !users.getContactUserList().isEmpty()) {
                                viewModelList.add(new TitleViewModel(getActivity().getResources().getString(R.string.search_user_contact_title)));
                                for (ContactUser contactUser : users.getContactUserList()) {
                                    if (pinnedUserIdList.contains(contactUser.getId())) {
                                        contactUser.setPinned(true);
                                    }
                                    viewModelList.add(new SearchUserContactViewModel(SearchUserFragment.this, contactUser));
                                }
                            }

                            if (users.getAccountUserList() != null && !users.getAccountUserList().isEmpty()) {
                                viewModelList.add(new TitleViewModel(getActivity().getResources().getString(R.string.search_user_account_title)));
                                for (AccountUser accountUser : users.getAccountUserList()) {
                                    if (pinnedUserIdList.contains(accountUser.getId())) {
                                        accountUser.setPinned(true);
                                    }
                                    viewModelList.add(new SearchUserAccountViewModel(SearchUserFragment.this, accountUser));
                                }
                            }

                            return viewModelList;
                        })
                .doOnSubscribe(disposable -> {
                    binding.drawerSwipeRefresh.setRefreshing(true);
                })
                .doFinally(() -> {
                    binding.drawerSwipeRefresh.setRefreshing(false);
                })
                .subscribe(response -> {
                    adapter.setItems(response);
                    if (response.isEmpty() && StringUtility.isNotNullOrEmpty(keyword)) {
                        binding.recyclerView.setVisibility(View.GONE);
                        binding.fragSearchUserNotFountTv.setVisibility(View.VISIBLE);
                        binding.fragSearchUserNotFountTv.setText(Html.fromHtml(getActivity().getResources().getString(R.string.search_user_not_found, keyword)));
                    } else {
                        binding.recyclerView.setVisibility(View.VISIBLE);
                        binding.fragSearchUserNotFountTv.setVisibility(View.GONE);
                    }
                    Log.d("SearchUserFragment", "getData success");
                }, e -> {
                    Log.e("SearchUserFragment", "getData failed", e);
                }));
    }

    @Override
    public void onChanged(String keyword) {
        Log.d("SearchUserFragment", "onChanged keyword = " + keyword);
        getData(keyword);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    public class SearchUserRecyclerViewAdapter extends RecyclerView.Adapter<BaseDatabindingViewHolder> {

        private List<BaseViewModelAware> items = new ArrayList<>();

        private SearchUserPresenter presenter = new SearchUserPresenter() {
            @Override
            public void onClickContact(View view, SearchUserContactViewModel viewModel) {
                boolean newPinned = !viewModel.getUserModel().isPinned();
                viewModel.setPinned(newPinned);

                if (newPinned) {
                    pinnedUserDB.addUser(viewModel.getUserModel());
                } else {
                    pinnedUserDB.deleteUser(viewModel.getUserModel());
                }

                String message = String.format("%1$s " + viewModel.getName(), newPinned ? "pin" : "unpin");
                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

                Log.d("SearchUserFragment", "onClickContact " + message);

                Intent intent = new Intent(getActivity(), TestActivity.class);
                startActivityForResult(intent, 1234);
            }

            @Override
            public void onClickAccount(View view, SearchUserAccountViewModel viewModel) {
                boolean newPinned = !viewModel.getUserModel().isPinned();
                viewModel.setPinned(newPinned);

                if (newPinned) {
                    pinnedUserDB.addUser(viewModel.getUserModel());
                } else {
                    pinnedUserDB.deleteUser(viewModel.getUserModel());
                }

                String message = String.format("%1$s " + viewModel.getName(), newPinned ? "pin" : "unpin");
                Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();

                Log.d("SearchUserFragment", "onClickContact " + message);
            }
        };

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
            holder.setData(getItem(position));
            holder.getVhBinding().setLifecycleOwner(SearchUserFragment.this);
        }

        public void setItems(List<BaseViewModelAware> items) {
            this.items.clear();
            if (items != null && !items.isEmpty()) {
                this.items.addAll(items);
            }
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

        public class SearchUserTitleViewHolder extends BaseDatabindingViewHolder<ItemSearchUserTitleBinding, BaseViewModelAware> {

            public SearchUserTitleViewHolder(@NonNull ItemSearchUserTitleBinding vhBinding) {
                super(vhBinding);
            }

            @Override
            public void setData(BaseViewModelAware viewModel) {
                if (viewModel instanceof TitleViewModel) {
                    vhBinding.setViewModel((TitleViewModel) viewModel);
                    vhBinding.executePendingBindings();
                }
            }
        }

        public class SearchUserContactViewHolder extends BaseDatabindingViewHolder<ItemSearchUserContactBinding, BaseViewModelAware> {

            public SearchUserContactViewHolder(@NonNull ItemSearchUserContactBinding vhBinding) {
                super(vhBinding);
            }

            @Override
            public void setData(BaseViewModelAware viewModel) {
                if (viewModel instanceof SearchUserContactViewModel) {
                    vhBinding.setViewModel((SearchUserContactViewModel)viewModel);
                    vhBinding.setPresenter(presenter);
                    vhBinding.executePendingBindings();
                }
            }
        }

        public class SearchUserAccountViewHolder extends BaseDatabindingViewHolder<ItemSearchUserAccountBinding, BaseViewModelAware> {

            public SearchUserAccountViewHolder(@NonNull ItemSearchUserAccountBinding vhBinding) {
                super(vhBinding);
            }

            @Override
            public void setData(BaseViewModelAware viewModel) {
                if (viewModel instanceof SearchUserAccountViewModel) {
                    vhBinding.setViewModel((SearchUserAccountViewModel)viewModel);
                    vhBinding.setPresenter(presenter);
                    vhBinding.executePendingBindings();
                }
            }
        }
    }

    public interface SearchUserPresenter {
        void onClickContact (View view, SearchUserContactViewModel viewModel);
        void onClickAccount (View view, SearchUserAccountViewModel viewModel);
    }
}
