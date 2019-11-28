/*
 * Copyright (C) 2017 MINDORKS NEXTGEN PRIVATE LIMITED
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://mindorks.com/license/apache-v2
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License
 */

package com.lva.shop.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lva.shop.R;
import com.lva.shop.ui.base.BaseDialog;
import com.lva.shop.ui.customview.ValueSelector;
import com.lva.shop.ui.detail.WebActivity;
import com.lva.shop.ui.main.model.DataProduct;
import com.lva.shop.utils.AppConstants;
import com.lva.shop.utils.CommonUtils;
import com.lva.shop.utils.Preference;
import com.makeramen.roundedimageview.RoundedImageView;

import org.greenrobot.eventbus.EventBus;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DialogProductFragment extends BaseDialog {

    private static final String TAG = DialogProductFragment.class.getSimpleName();
    @BindView(R.id.iv_img_product)
    RoundedImageView ivImgProduct;
    @BindView(R.id.tv_title_product)
    TextView tvTitleProduct;
    @BindView(R.id.tv_money_product)
    TextView tvMoneyProduct;
    @BindView(R.id.tv_des)
    TextView tvDes;
    @BindView(R.id.ll_feature)
    LinearLayout llFeature;
    @BindView(R.id.btn_add_to_cart)
    TextView btnAddToCart;
    @BindView(R.id.selector)
    ValueSelector selector;

    private DataProduct dataProduct;
    private List<DataProduct> cartList = new ArrayList<>();

    public static DialogProductFragment newInstance() {
        DialogProductFragment fragment = new DialogProductFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.dialog_product, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        return view;
    }

    void show(FragmentManager fragmentManager) {
        super.show(fragmentManager, TAG);
    }

    @Override
    protected void setUp(View view) {
        selector.setMaxValue(99);
        selector.setMinValue(1);
        selector.setValue(1);
        dataProduct.setQuality(1);
        selector.setOnValueListener(value -> {
//            tvMoneyProduct.setText(CommonUtils.convertMoney(dataProduct.getPrice(), value));
            dataProduct.setQuality(value);
        });
        Glide.with(getBaseActivity())
                .load(dataProduct.getLinkImage())
                .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(ivImgProduct);
        tvDes.setText(dataProduct.getProductSortdesc());
        tvMoneyProduct.setText(CommonUtils.convertMoney(dataProduct.getPrice(), 1));
        tvTitleProduct.setText(dataProduct.getName());
    }


    public void dismissDialog() {
        dismissDialog(TAG);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.ll_feature, R.id.btn_add_to_cart})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_feature:
                if (dataProduct.getLinkDetail() != null && !TextUtils.isEmpty(dataProduct.getLinkDetail())) {
                    Intent intent = new Intent(getBaseActivity(), WebActivity.class);
                    intent.putExtra(AppConstants.TITLE, dataProduct.getName() != null ? dataProduct.getName() : getString(R.string.news));
                    intent.putExtra(AppConstants.URL, dataProduct.getLinkDetail());
                    startActivity(intent);
                }
                break;
            case R.id.btn_add_to_cart:
                dismissDialog();
                handleListCart();
                break;
        }
    }

    private void handleListCart() {
        boolean isAdd = true;
        if (Preference.getString(getBaseActivity(), AppConstants.LIST_CART) != null) {
            String arrayListCart = Preference.getString(getBaseActivity(), AppConstants.LIST_CART);
            Gson gson = new Gson();
            Type cartType = new TypeToken<ArrayList<DataProduct>>() {
            }.getType();
            cartList = gson.fromJson(arrayListCart, cartType);
            for (int i = 0; i < cartList.size(); i++) {
                if (cartList.get(i).getId().equals(dataProduct.getId())) {
                    int oldQuality = cartList.get(i).getQuality();
                    cartList.get(i).setQuality(dataProduct.getQuality() + oldQuality);
                    isAdd = false;
                }
            }
            if (isAdd) {
                cartList.add(dataProduct);
            }
            EventBus.getDefault().post(isAdd);
        } else {
            cartList.add(dataProduct);
            EventBus.getDefault().post(true);
        }
        String json = new Gson().toJson(cartList);
        if (json.equals("[]")) {
            Preference.remove(getBaseActivity(), AppConstants.LIST_CART);
        } else {
            Preference.save(getBaseActivity(), AppConstants.LIST_CART, json);
        }
        cartList.clear();
        Log.e(TAG, "handleListCart: " + json);
    }

    public void setData(DataProduct product) {
        dataProduct = product;
    }
}