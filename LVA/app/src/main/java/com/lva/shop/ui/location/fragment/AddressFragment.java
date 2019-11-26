package com.lva.shop.ui.location.fragment;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.lva.shop.R;
import com.lva.shop.ui.base.BaseFragment;
import com.lva.shop.ui.location.LocationActivity;
import com.lva.shop.ui.location.model.Address;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddressFragment extends BaseFragment {

    public static final String TAG = AddressFragment.class.getSimpleName();
    @BindView(R.id.tv_tinh)
    TextView tvTinh;
    @BindView(R.id.ll_tinh)
    LinearLayout llTinh;
    @BindView(R.id.tv_huyen)
    TextView tvHuyen;
    @BindView(R.id.ll_huyen)
    LinearLayout llHuyen;
    @BindView(R.id.tv_xa)
    TextView tvXa;
    @BindView(R.id.ll_xa)
    LinearLayout llXa;
    @BindView(R.id.edt_address)
    EditText edtAddress;
    @BindView(R.id.btn_next)
    TextView btnNext;

    private Address cityObj, districtObj, communeObj;

    public static AddressFragment newInstance() {
        Bundle args = new Bundle();
        AddressFragment fragment = new AddressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_address, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        setUpToolbar();
        return view;
    }

    @Override
    protected void setUp(View view) {
        if (cityObj != null) {
            tvTinh.setText(cityObj.getName());
        }
        if (districtObj != null) {
            tvHuyen.setText(districtObj.getName());
        }
        if (communeObj != null) {
            tvXa.setText(communeObj.getName());
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.ll_tinh, R.id.ll_huyen, R.id.ll_xa, R.id.btn_next})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_tinh:
                getFragmentChangedListener().OnFragmentChangedListener(LocationActivity.SCREEN_GET_CITY);
                break;
            case R.id.ll_huyen:
                getFragmentChangedListener().OnFragmentChangedListener(LocationActivity.SCREEN_GET_DISTRICT);
                break;
            case R.id.ll_xa:
                getFragmentChangedListener().OnFragmentChangedListener(LocationActivity.SCREEN_GET_COMMUNE);
                break;
            case R.id.btn_next:
                if (!TextUtils.isEmpty(edtAddress.getText().toString()))
                    getFragmentChangedListener().OnAddressChange(edtAddress.getText().toString());
                getFragmentChangedListener().OnFragmentChangedListener(LocationActivity.UPDATE_ADDRESS);
                break;
        }
    }

    public void setData(Address tinh, Address huyen, Address xa) {
        cityObj = tinh;
        districtObj = huyen;
        communeObj = xa;
    }
}
