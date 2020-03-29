package com.toss.im.test.hanyoung.custom.view.recycler;


import androidx.annotation.NonNull;
import androidx.databinding.ViewDataBinding;
import androidx.recyclerview.widget.RecyclerView;

/**
 * recycler view에서 databinding을 사용하기위한 기본 viewHolder
 *
 */
public abstract class BaseDatabindingViewHolder<B extends ViewDataBinding, VM> extends RecyclerView.ViewHolder {

    protected B vhBinding;

    public BaseDatabindingViewHolder(@NonNull B vhBinding) {
        super(vhBinding.getRoot());
        this.vhBinding = vhBinding;
    }

    public B getVhBinding() {
        return vhBinding;
    }

    public abstract void setData (VM viewModel);
}
