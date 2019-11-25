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
import com.lva.shop.callback.ButtonAlertDialogListener;
import com.lva.shop.callback.FragmentChangedListener;
import com.lva.shop.ui.base.BaseActivity;
import com.lva.shop.ui.main.fragment.HomeFragment;
import com.lva.shop.ui.main.fragment.OrderFragment;
import com.lva.shop.ui.main.fragment.SettingFragment;
import com.lva.shop.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements FragmentChangedListener, ButtonAlertDialogListener {
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
    public static final int RELOAD_SCREEN_SHOP = 4;

    private HomeFragment homeFragment;
    private SettingFragment settingFragment;
    private OrderFragment orderFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setUnBinder(ButterKnife.bind(this));
        setUpView();
    }

    private void setUpView() {
        setButtonAlertDialogListener(this);
        navigationBottomView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigationBottomView.setItemIconTintList(null);
        initFragments();
        getData();
        OnFragmentChangedListener(SCREEN_SHOP);

    }

    private void getData() {
        showLoading();
        RestfulManager.getInstance(MainActivity.this).getHome(new RestfulManager.OnChangeListener() {
            @Override
            public void onGetZipSuccess(ZipRequest zipRequest) {
                hideLoading();
                homeFragment.setData(zipRequest);
            }

            @Override
            public void onError(Throwable e) {
                MainActivity.this.onError(e, AppConstants.LOAD_DATA_HOME_FAIL);
                hideLoading();
            }
        });
    }

    private void initFragments() {
        homeFragment = HomeFragment.newInstance();
        homeFragment.setFragmentChangedListener(this);

        settingFragment = SettingFragment.newInstance();
        settingFragment.setFragmentChangedListener(this);

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
                loadFragment(homeFragment);
                return true;
            case R.id.navigation_order:
                loadFragment(orderFragment);
                return true;
            case R.id.navigation_profile:
                loadFragment(settingFragment);
                return true;
        }
        return false;
    };

    @Override
    public void OnFragmentChangedListener(int tag) {
        switch (tag) {
            case SCREEN_SHOP:
                navigationBottomView.setSelectedItemId(R.id.navigation_shop);
                break;
            case SCREEN_ORDER:
                navigationBottomView.setSelectedItemId(R.id.navigation_order);
                break;
            case SCREEN_PROFILE:
                navigationBottomView.setSelectedItemId(R.id.navigation_profile);
                break;
            case RELOAD_SCREEN_SHOP:
                getData();
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

    @Override
    public void onConfirmClick(String type) {
        switch (type) {
            case AppConstants.LOAD_DATA_HOME_FAIL:
                getData();
                break;
        }
    }

    @Override
    public void onCancelClick(String type) {

    }
}
