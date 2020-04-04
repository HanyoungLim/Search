package com.toss.im.test.hanyoung.base;

import android.content.Intent;
import android.util.Log;

import com.example.lib_commons.ApplicationContextWrapper;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.disposables.CompositeDisposable;

/**
 * BaseFragment
 *
 * onActivityResult 시에 자식 fragment에 이벤트전파를 위한 기본세팅 및
 * androidViewModel 사용하기위한 HasDefaultViewModelProviderFactory 구현한다.
 */
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
        Log.d("BaseFragment", "onActivityResult " + requestCode);
//        for (Fragment activeFragment : getActiveFragmentList()) {
//            activeFragment.onActivityResult(requestCode, resultCode, data);
//        }
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
    public void onDetach() {
        super.onDetach();
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
