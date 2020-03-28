package com.toss.im.test.hanyoung.base;

import android.content.Intent;

import com.example.lib_commons.ApplicationContextWrapper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.disposables.CompositeDisposable;

public class BaseFragment extends Fragment implements HasDefaultViewModelProviderFactory {

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    private List<WeakReference<Fragment>> childFragmentList = new ArrayList<>();

    @Override
    public void onAttachFragment(@NonNull Fragment childFragment) {
        super.onAttachFragment(childFragment);
        childFragmentList.add(new WeakReference(childFragment));
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment activeFragment : getActiveFragmentList()) {
            activeFragment.onActivityResult(requestCode, resultCode, data);
        }
    }

    protected List<Fragment> getActiveFragmentList() {
        ArrayList<Fragment> activeFragmentList = new ArrayList<>();
        for(WeakReference<Fragment> childFragmentRef : childFragmentList) {
            Fragment childFragment = childFragmentRef.get();
            if(childFragment != null) {
                if(childFragment.isVisible()) {
                    activeFragmentList.add(childFragment);
                }
            }
        }

        return activeFragmentList;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    @NonNull
    @Override
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        return new ViewModelProvider.AndroidViewModelFactory(ApplicationContextWrapper.getApplication());
    }
}
