package com.lva.shop.ui.detail;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lva.shop.R;
import com.lva.shop.ui.base.BaseActivity;
import com.lva.shop.ui.detail.adapter.CartAdapter;
import com.lva.shop.ui.location.LocationActivity;
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
            int price = (int) Double.parseDouble(cartList.get(i).getPrice());
            int quality = cartList.get(i).getQuality();
            totalPrice += price * quality;
        }
        tvSum.setText(getText(R.string.sum)+" "+ CommonUtils.convertMoney(String.valueOf(totalPrice),1));
        if(cartList.size() == 0){
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
        } catch (Exception e) {
        }
    }

    @OnClick({R.id.tv_change, R.id.btn_order})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_change:
                Intent intentAddress = new Intent(CartActivity.this, LocationActivity.class);
                startActivityForResult(intentAddress, AppConstants.REQUEST_ADDRESS);
                break;
            case R.id.btn_order:
                new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText(getString(R.string.order_success))
                        .setContentText(getString(R.string.order_success_text))
                        .setConfirmText(getString(R.string.ok))
                        .showCancelButton(false)
                        .setConfirmClickListener(sweetAlertDialog -> {
                            sweetAlertDialog.cancel();
                            Preference.remove(CartActivity.this, AppConstants.LIST_CART);
                            CartActivity.this.setResult(Activity.RESULT_OK);
                            finish();
                        })
                        .show();
                break;
        }
    }
}
