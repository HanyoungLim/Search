package com.toss.im.test.hanyoung.custom.view.recycler;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.toss.im.test.hanyoung.R;
import com.toss.im.test.hanyoung.databinding.ItemEmptyBinding;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;

public class EmptyViewHolder extends BaseDatabindingViewHolder<ItemEmptyBinding, Object> {

    public EmptyViewHolder(@NonNull ViewGroup parent) {
        super(DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.item_empty, parent, false));
    }

    @Override
    public void setData(Object viewModel) {
        vhBinding.setViewModel(viewModel);
        vhBinding.executePendingBindings();
    }
}
