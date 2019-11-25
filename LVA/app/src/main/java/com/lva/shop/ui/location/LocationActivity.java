package com.lva.shop.ui.location;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.lva.shop.R;
import com.lva.shop.callback.ButtonAlertDialogListener;
import com.lva.shop.callback.FragmentChangedListener;
import com.lva.shop.ui.base.BaseActivity;
import com.lva.shop.ui.location.fragment.AddressFragment;
import com.lva.shop.ui.location.fragment.EditAddressFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationActivity extends BaseActivity implements FragmentChangedListener, ButtonAlertDialogListener {
    @BindView(R.id.frame_container)
    FrameLayout frameContainer;
    private String TAG = LocationActivity.class.getSimpleName();

    public static final int SCREEN_ADDRESS = 1;
    public static final int SCREEN_ADD_ADDRESS = 2;

    private AddressFragment addressFragment;
    private EditAddressFragment editAddressFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        setUnBinder(ButterKnife.bind(this));
        setUpView();
    }

    private void setUpView() {
        setButtonAlertDialogListener(this);
        initFragments();
        OnFragmentChangedListener(SCREEN_ADDRESS);

    }

    private void initFragments() {
        addressFragment = AddressFragment.newInstance();
        addressFragment.setFragmentChangedListener(this);

        editAddressFragment = EditAddressFragment.newInstance();
        editAddressFragment.setFragmentChangedListener(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void OnFragmentChangedListener(int tag) {
        switch (tag) {
            case SCREEN_ADDRESS:
                loadFragment(addressFragment);
                break;
            case SCREEN_ADD_ADDRESS:
                loadFragment(editAddressFragment);
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
        if (addressFragment.isVisible()) {
            finish();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void onConfirmClick(String type) {

    }

    @Override
    public void onCancelClick(String type) {

    }
}
