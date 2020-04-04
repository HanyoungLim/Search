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
 *
 *
 * onAcitivityResult에 명시적으로 fragment onActivityResult에 이벤트를 전파해야하는이유
 *
 * 1. fragment에서 startActivityResult를 실행하면 결과값을 fragment가 받지만 **** activity도 임의의난수 req코드로도 받는다 ****. 예상하지못한이슈가발생가능성있음. 중요!!
 * 2. getActivity.startActivityResult를 하면 fragment의 부모인 activity가 요청한 req로 받아서 유연하게 다음 작업을 처리가능하다.
 */
public class BaseActivity extends AppCompatActivity implements HasDefaultViewModelProviderFactory {

    protected CompositeDisposable compositeDisposable = new CompositeDisposable();

    private List<WeakReference<Fragment>> childFragmentList = new ArrayList<>();

    @Override
    public void onAttachFragment(@NonNull Fragment fragment) {
        super.onAttachFragment(fragment);
        childFragmentList.add(new WeakReference(fragment));
    }

    /**
     *
     * @param requestCode
     * @param resultCode   activity finish에서 해당로직처리할경우 super.finish 뒤에 setResult 넣으면 resultcode는 항상0 (cancel)로 오니 주의!!
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("BaseActivity", "onActivityResult req=" + requestCode + ", res=" + resultCode);
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
