package com.lva.shop.ui.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lva.shop.R;
import com.lva.shop.api.RestfulManager;
import com.lva.shop.ui.base.BaseActivity;
import com.lva.shop.ui.detail.adapter.CartAdapter;
import com.lva.shop.ui.detail.model.ProductOrder;
import com.lva.shop.ui.location.LocationActivity;
import com.lva.shop.ui.location.model.AddressReqRes;
import com.lva.shop.ui.login.LoginActivity;
import com.lva.shop.ui.login.model.UserInfo;
import com.lva.shop.ui.main.model.DataProduct;
import com.lva.shop.utils.AppConstants;
import com.lva.shop.utils.CommonUtils;
import com.lva.shop.utils.Preference;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CartActivity extends BaseActivity {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_phone)
    TextView tvPhone;
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.rcv_product)
    RecyclerView rcvProduct;
    @BindView(R.id.btn_order)
    TextView btnOrder;
    @BindView(R.id.rl_order)
    RelativeLayout rlOrder;
    @BindView(R.id.tv_sum)
    TextView tvSum;
    private String TAG = CartActivity.class.getSimpleName();
    private List<DataProduct> cartList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        setUnBinder(ButterKnife.bind(this));
        setUpView();
        setUpAddress();
    }

    private void setUpAddress() {
        if (Preference.getString(this, AppConstants.ACCESS_TOKEN) != null) {
            setUpAddressUserInfo();
        } else {
            setUpAddressLocal();
        }
    }

    private void setUpAddressUserInfo() {
        String jsonUser = Preference.getString(this, AppConstants.USER_INFO);
        Gson gson = new Gson();
        UserInfo userInfo = gson.fromJson(jsonUser, UserInfo.class);
        if (userInfo != null) {
            if (userInfo.getAddress() != null && !userInfo.getAddress().equals("")) {
                AddressReqRes addressReqRes = new AddressReqRes();
                addressReqRes.setAddress(userInfo.getAddress());
                addressReqRes.setCity(userInfo.getProvince());
                addressReqRes.setDistrict(userInfo.getDistrict());
                addressReqRes.setWard(userInfo.getWard());
                tvAddress.setText(CommonUtils.convertAddress(addressReqRes));
                tvName.setText(userInfo.getNameDelivery());
                tvPhone.setText(userInfo.getPhoneDelivery());
            }
        }
    }

    private void setUpAddressLocal() {
        if (Preference.getString(this, AppConstants.ADDRESS_LOCAL) != null) {
            Gson gson = new Gson();
            AddressReqRes addressReqRes = gson.fromJson(Preference.getString(this, AppConstants.ADDRESS_LOCAL), AddressReqRes.class);
            tvAddress.setText(CommonUtils.convertAddress(addressReqRes));
            tvName.setText(addressReqRes.getName());
            tvPhone.setText(addressReqRes.getPhone());
        }
    }

    private void setUpView() {
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcvProduct.setLayoutManager(layoutManager);
        cartList.clear();
        if (Preference.getString(this, AppConstants.LIST_CART) != null) {
            String arrayListCart = Preference.getString(this, AppConstants.LIST_CART);
            Gson gson = new Gson();
            Type cartType = new TypeToken<ArrayList<DataProduct>>() {
            }.getType();
            cartList = gson.fromJson(arrayListCart, cartType);
            calculatorSum();
            CartAdapter cartAdapter = new CartAdapter(this, cartList);
            cartAdapter.setListener((setting, value) -> calculatorSum());
            rcvProduct.setAdapter(cartAdapter);
        } else {
        }
    }

    private void calculatorSum() {
        int totalPrice = 0;
        for (int i = 0; i < cartList.size(); i++) {
            int price;
            if (cartList.get(i).getPriceDiscount() == null || cartList.get(i).getPriceDiscount().equals("0.00")) {
                price = (int) Double.parseDouble(cartList.get(i).getPrice());
            } else {
                price = (int) Double.parseDouble(cartList.get(i).getPriceDiscount());
            }
            int quality = cartList.get(i).getQuality();
            totalPrice += price * quality;
        }
        tvSum.setText(getText(R.string.sum) + " " + CommonUtils.convertMoney(String.valueOf(totalPrice), 1));
        if (cartList.size() == 0) {
            btnOrder.setEnabled(false);
            btnOrder.setBackground(getResources().getDrawable(R.drawable.bg_btn_login_gray));
        } else {
            btnOrder.setEnabled(true);
            btnOrder.setBackground(getResources().getDrawable(R.drawable.bg_btn_login));
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        CartActivity.this.setResult(Activity.RESULT_OK);
        finish();
    }

    @OnClick({R.id.tv_change, R.id.btn_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_change:
                Intent intentAddress = new Intent(CartActivity.this, LocationActivity.class);
                startActivityForResult(intentAddress, AppConstants.REQUEST_ADDRESS);
                break;
            case R.id.btn_order:
                if (Preference.getString(this, AppConstants.ACCESS_TOKEN) != null) {
                    List<ProductOrder> productOrders = new ArrayList<>();
                    for (DataProduct dataProduct : cartList) {
                        productOrders.add(new ProductOrder(dataProduct.getId(), dataProduct.getName(), dataProduct.getQuality()));
                    }
                    showLoading();
                    RestfulManager.getInstance(this, 1).postOrder(productOrders, new RestfulManager.OnPostOrderListener() {
                        @Override
                        public void onPostOrderSuccess() {
                            try {
                                hideLoading();
                                new SweetAlertDialog(CartActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText(getString(R.string.order_success))
                                        .setContentText(getString(R.string.order_success_text))
                                        .setConfirmText(getString(R.string.ok))
                                        .showCancelButton(false)
                                        .setConfirmClickListener(sweetAlertDialog -> {
                                            sweetAlertDialog.cancel();
                                            CartActivity.this.setResult(Activity.RESULT_OK);
                                            finish();
                                        })
                                        .show();
                                Preference.remove(CartActivity.this, AppConstants.LIST_CART);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                        @Override
                        public void onError(Throwable e) {
                            hideLoading();
                        }
                    });
                } else {
                    showDialog(getString(R.string.you_need_login));
                }
                break;
        }
    }

    private void showDialog(String string) {
        new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.attention))
                .setContentText(string)
                .setCancelText(getString(R.string.skip))
                .setConfirmText(getString(R.string.ok))
                .showCancelButton(true)
                .setCancelClickListener(SweetAlertDialog::cancel)
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.cancel();
                    Intent intentLogin = new Intent(this, LoginActivity.class);
                    intentLogin.putExtra(AppConstants.LAUNCH_APP, false);
                    startActivityForResult(intentLogin, AppConstants.REQ_LOGIN_FROM_CART);
                })
                .show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpAddress();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppConstants.REQ_LOGIN_FROM_CART && resultCode == AppConstants.LOGIN_RESULT) {
            Log.e(TAG, "onActivityResult: " + 1);
            setUpAddress();
        }

        if (requestCode == AppConstants.REQUEST_ADDRESS && resultCode == Activity.RESULT_OK) {
            Log.e(TAG, "onActivityResult: " + 1);
            setUpAddress();
        }
    }
}
