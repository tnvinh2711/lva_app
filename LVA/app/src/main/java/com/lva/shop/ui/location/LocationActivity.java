package com.lva.shop.ui.location;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.gson.Gson;
import com.lva.shop.R;
import com.lva.shop.api.RestfulManager;
import com.lva.shop.callback.ButtonAlertDialogListener;
import com.lva.shop.callback.FragmentChangedListener;
import com.lva.shop.db.DatabaseOpenHelper;
import com.lva.shop.ui.base.BaseActivity;
import com.lva.shop.ui.location.fragment.AddressFragment;
import com.lva.shop.ui.location.fragment.EditAddressFragment;
import com.lva.shop.ui.location.model.Address;
import com.lva.shop.ui.location.model.AddressReqRes;
import com.lva.shop.ui.login.LoginActivity;
import com.lva.shop.ui.login.model.ResponseUser;
import com.lva.shop.ui.login.model.UserInfo;
import com.lva.shop.utils.AppConstants;
import com.lva.shop.utils.CommonUtils;
import com.lva.shop.utils.Preference;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class LocationActivity extends BaseActivity implements FragmentChangedListener, ButtonAlertDialogListener {
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
    private final String TYPE_ERROR_LOCATION = "error_location";

    private String mAddress, mName, mPhone;
    private Address cityObj = new Address();
    private Address districtObj = new Address();
    private Address communeObj = new Address();
    private AddressFragment addressFragment;
    private EditAddressFragment editAddressFragment;
    private DatabaseOpenHelper databaseOpenHelper;
    private UserInfo userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        setUnBinder(ButterKnife.bind(this));
        databaseOpenHelper = new DatabaseOpenHelper(this);
        setUpView();
    }

    private void setUpView() {
        if (Preference.getString(this, AppConstants.ACCESS_TOKEN) != null) {
            String jsonUser = Preference.getString(this, AppConstants.USER_INFO);
            Log.e(TAG, "setUp: " + jsonUser);
            Gson gson = new Gson();
            userInfo = gson.fromJson(jsonUser, UserInfo.class);
            if (userInfo.getNameDelivery() != null) {
                mName = userInfo.getNameDelivery();
            } else {
                mName = userInfo.getName() != null ? userInfo.getName() : "";
            }
            if (userInfo.getPhoneDelivery() != null) {
                mPhone = userInfo.getPhoneDelivery();
            } else {
                mPhone = Preference.getString(this, AppConstants.PHONE);
            }
            mAddress = userInfo.getAddress() != null ? userInfo.getAddress() : "";
            cityObj.setName(userInfo.getProvince() != null ? userInfo.getProvince() : "");
            districtObj.setName(userInfo.getDistrict() != null ? userInfo.getDistrict() : "");
            communeObj.setName(userInfo.getWard() != null ? userInfo.getWard() : "");
        }
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
    public void OnAddressChange(String address, String name, String phone) {
        mAddress = address;
        mName = name;
        mPhone = phone;
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
                addressFragment.setData(mAddress, mName, mPhone, cityObj, districtObj, communeObj);
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
                if (cityObj.getName() != null && districtObj.getName() != null && communeObj.getName() != null && mAddress != null && mName != null && mPhone != null
                        && !cityObj.getName().equals("") && !districtObj.getName().equals("") && !communeObj.getName().equals("") && !mAddress.equals("") && !mName.equals("") && !mPhone.equals("")) {
                    showDialogConfirm();
                    mAddress = null;
                    mName = null;
                    mPhone = null;
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
                    postUserInfo();
                })
                .show();
    }

    private void postUserInfo() {
        showLoading();
        RestfulManager.getInstance(LocationActivity.this, 1).postUpdateUser(mName, mPhone, mAddress, cityObj.getName(), districtObj.getName(), communeObj.getName(), null, null, null, null, null, new RestfulManager.OnGetUserListener() {
            @Override
            public void onGetUserSuccess(ResponseUser responseUser) {
                Gson gson = new Gson();
                String jsonUser = gson.toJson(responseUser.getUserInfo());
                Preference.save(LocationActivity.this, AppConstants.USER_INFO, jsonUser);
                setUpView();
                hideLoading();
            }

            @Override
            public void onError(Throwable e) {
                hideLoading();
                LocationActivity.this.onError(e, TYPE_ERROR_LOCATION);
            }
        });
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

    @Override
    public void onConfirmClick(String type) {
        postUserInfo();
    }

    @Override
    public void onCancelClick(String type) {

    }
}
