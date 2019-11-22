package com.lva.shop.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.lva.shop.R;
import com.lva.shop.api.RestfulManager;
import com.lva.shop.api.ZipRequest;
import com.lva.shop.callback.FragmentChangedListener;
import com.lva.shop.ui.base.BaseActivity;
import com.lva.shop.ui.main.fragment.HomeFragment;
import com.lva.shop.ui.main.fragment.OrderFragment;
import com.lva.shop.ui.main.fragment.ProfileFragment;
import com.lva.shop.utils.NoConnectException;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements FragmentChangedListener {
    @BindView(R.id.frame_container)
    FrameLayout frameContainer;
    @BindView(R.id.navigation)
    BottomNavigationView navigationBottomView;
    @BindView(R.id.container)
    CoordinatorLayout container;
    private String TAG = MainActivity.class.getSimpleName();

    public static final int SCREEN_SHOP = 1;
    public static final int SCREEN_ORDER = 2;
    public static final int SCREEN_PROFILE = 3;

    private HomeFragment homeFragment;
    private ProfileFragment profileFragment;
    private OrderFragment orderFragment;
    private ZipRequest zipRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUnBinder(ButterKnife.bind(this));
        setUpView();
    }

    private void setUpView() {
        navigationBottomView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigationBottomView.setItemIconTintList(null);
        initFragments();
        OnFragmentChangedListener(SCREEN_SHOP);
        getData();
    }

    private void getData() {
        showLoading();
        RestfulManager.getInstance(MainActivity.this).getHome(new RestfulManager.OnChangeListener() {
            @Override
            public void onGetZipSuccess(ZipRequest zipRequest) {
                MainActivity.this.zipRequest = zipRequest;
                hideLoading();
                homeFragment.setData(zipRequest);
            }

            @Override
            public void onError(Throwable e) {
                MainActivity.this.onError(e);
                hideLoading();
            }
        });
    }

    private void initFragments() {
        homeFragment = HomeFragment.newInstance();
        homeFragment.setFragmentChangedListener(this);

        profileFragment = ProfileFragment.newInstance();
        profileFragment.setFragmentChangedListener(this);

        orderFragment = OrderFragment.newInstance();
        orderFragment.setFragmentChangedListener(this);

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = item -> {
        switch (item.getItemId()) {
            case R.id.navigation_shop:
                OnFragmentChangedListener(SCREEN_SHOP);
                return true;
            case R.id.navigation_order:
                OnFragmentChangedListener(SCREEN_ORDER);
                return true;
            case R.id.navigation_profile:
                OnFragmentChangedListener(SCREEN_PROFILE);
                return true;
        }
        return false;
    };

    @Override
    public void OnFragmentChangedListener(int tag) {
        switch (tag) {
            case SCREEN_SHOP:
                loadFragment(homeFragment);
                break;
            case SCREEN_ORDER:
                loadFragment(orderFragment);
                break;
            case SCREEN_PROFILE:
                loadFragment(profileFragment);
                break;
        }
    }

    private void loadFragment(Fragment fragment) {
        try {
            if (fragment == null) return;
            if (fragment.isAdded() || fragment.isVisible()) {
                return;
            }
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frame_container, fragment);
            ft.addToBackStack(fragment.getClass().getSimpleName());
            ft.commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        showDialogExit(getString(R.string.do_you_want_quit));
    }
}
