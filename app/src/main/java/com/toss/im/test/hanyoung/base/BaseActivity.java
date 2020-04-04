package com.toss.im.test.hanyoung.base;

import android.content.Intent;
import android.util.Log;

import com.toss.im.test.hanyoung.application.BaseApplication;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.HasDefaultViewModelProviderFactory;
import androidx.lifecycle.ViewModelProvider;
import io.reactivex.disposables.CompositeDisposable;

/**
 * BaseActivity
 *
 * onActivityResult 시에 자식 fragment에 이벤트전파를 위한 기본세팅 및
 * androidViewModel 사용하기위한 HasDefaultViewModelProviderFactory 구현한다.
 */
public class BaseActivity extends AppCompatActivity implements HasDefaultViewModelProviderFactory {

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    private List<WeakReference<Fragment>> childFragmentList = new ArrayList<>();

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        childFragmentList.add(new WeakReference(fragment));
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("BaseActivity", "onActivityResult " + requestCode);
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
    protected void onDestroy() {
        super.onDestroy();
        if (!compositeDisposable.isDisposed()) {
            compositeDisposable.dispose();
        }
    }

    @NonNull
    @Override
    public ViewModelProvider.Factory getDefaultViewModelProviderFactory() {
        return new ViewModelProvider.AndroidViewModelFactory(getApplication());
    }
}
