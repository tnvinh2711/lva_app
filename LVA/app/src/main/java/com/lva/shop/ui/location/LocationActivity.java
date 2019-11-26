package com.lva.shop.ui.location;

import android.content.Intent;
import android.os.Bundle;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.lva.shop.R;
import com.lva.shop.callback.ButtonAlertDialogListener;
import com.lva.shop.callback.FragmentChangedListener;
import com.lva.shop.db.DatabaseOpenHelper;
import com.lva.shop.ui.base.BaseActivity;
import com.lva.shop.ui.location.fragment.AddressFragment;
import com.lva.shop.ui.location.fragment.EditAddressFragment;
import com.lva.shop.ui.location.model.Address;
import com.lva.shop.ui.location.model.AddressReqRes;
import com.lva.shop.utils.AppConstants;
import com.lva.shop.utils.CommonUtils;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationActivity extends BaseActivity implements FragmentChangedListener {
    @BindView(R.id.frame_container)
    FrameLayout frameContainer;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String TAG = LocationActivity.class.getSimpleName();

    public static final int SCREEN_ADDRESS = 1;
    public static final int SCREEN_GET_CITY = 2;
    public static final int SCREEN_GET_DISTRICT = 3;
    public static final int SCREEN_GET_COMMUNE = 4;
    public static final int UPDATE_ADDRESS = 5;

    private String mAddress;
    private Address cityObj;
    private Address districtObj;
    private Address communeObj;
    private AddressFragment addressFragment;
    private EditAddressFragment editAddressFragment;
    private DatabaseOpenHelper databaseOpenHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        setUnBinder(ButterKnife.bind(this));
        databaseOpenHelper = new DatabaseOpenHelper(this);
        setUpView();
    }

    private void setUpView() {
        setUpToolbar();
        initFragments();
        OnFragmentChangedListener(SCREEN_ADDRESS);

    }

    private void setUpToolbar() {
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
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
    public void OnAddressChange(String address) {
        mAddress = address;
    }

    @Override
    public void OnAddressObjChange(int type, Address address) {
        switch (type) {
            case AppConstants.CITY:
                cityObj = address;
                break;
            case AppConstants.DISTRICT:
                districtObj = address;
                break;
            case AppConstants.COMMUNE:
                communeObj = address;
                break;
        }
    }

    @Override
    public void OnFragmentChangedListener(int tag) {
        switch (tag) {
            case SCREEN_ADDRESS:
                addressFragment.setToolbarTitle(getString(R.string.address_ship));
                loadFragment(addressFragment);
                addressFragment.setData(cityObj, districtObj, communeObj);
                break;
            case SCREEN_GET_CITY:
                editAddressFragment.setToolbarTitle(getString(R.string.tinh));
                loadFragment(editAddressFragment);
                setType(AppConstants.CITY, cityObj);
                break;
            case SCREEN_GET_DISTRICT:
                if (cityObj != null) {
                    editAddressFragment.setToolbarTitle(getString(R.string.huyen));
                    loadFragment(editAddressFragment);
                    setType(AppConstants.DISTRICT, cityObj);
                } else {
                    Toast.makeText(this, R.string.please_choose_city, Toast.LENGTH_SHORT).show();
                }
                break;
            case SCREEN_GET_COMMUNE:
                if (cityObj == null) {
                    Toast.makeText(this, R.string.please_choose_city, Toast.LENGTH_SHORT).show();
                } else if (districtObj == null) {
                    Toast.makeText(this, R.string.please_choose_district, Toast.LENGTH_SHORT).show();
                } else {
                    editAddressFragment.setToolbarTitle(getString(R.string.xa));
                    loadFragment(editAddressFragment);
                    setType(AppConstants.COMMUNE, districtObj);
                }
                break;
            case UPDATE_ADDRESS:
                if (cityObj != null && districtObj != null && communeObj != null && mAddress != null) {
                   showDialogConfirm();
                } else {
                    Toast.makeText(this, R.string.please_input_infomation, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    private void showDialogConfirm() {
        AddressReqRes addressReqRes = new AddressReqRes();
        addressReqRes.setAddress(mAddress);
        addressReqRes.setCity(cityObj.getName());
        addressReqRes.setDistrict(districtObj.getName());
        addressReqRes.setWard(communeObj.getName());
        new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(getString(R.string.update_address))
                .setContentText(getString(R.string.message_dialog_update_address, CommonUtils.convertAddress(addressReqRes)))
                .setCancelText(getString(R.string.skip))
                .setConfirmText(getString(R.string.ok))
                .showCancelButton(true)
                .setCancelClickListener(SweetAlertDialog::cancel)
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.cancel();
                    //TODO post address
                })
                .show();
    }

    private void setType(int type, Address address) {
        List<Address> addressList = new ArrayList<>();
        switch (type) {
            case AppConstants.CITY:
                addressList = databaseOpenHelper.getTinh();
                break;
            case AppConstants.DISTRICT:
                addressList = databaseOpenHelper.getHuyen(address.getId());
                break;
            case AppConstants.COMMUNE:
                addressList = databaseOpenHelper.getXa(address.getId());
                break;
        }
        editAddressFragment.setType(type, addressList);
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
}
