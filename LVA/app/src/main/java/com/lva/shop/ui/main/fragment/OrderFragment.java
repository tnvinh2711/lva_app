package com.lva.shop.ui.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.lva.shop.R;
import com.lva.shop.ui.base.BaseFragment;
import com.lva.shop.ui.location.LocationActivity;
import com.lva.shop.utils.AppConstants;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderFragment extends BaseFragment {

    public static final String TAG = OrderFragment.class.getSimpleName();
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.appBarLayout)
    ConstraintLayout appBarLayout;


    public static OrderFragment newInstance() {
        Bundle args = new Bundle();
        OrderFragment fragment = new OrderFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        setUnBinder(ButterKnife.bind(this, view));

        return view;
    }

    @Override
    protected void setUp(View view) {
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick(R.id.appBarLayout)
    public void onViewClicked() {
        Intent intentAddress = new Intent(getBaseActivity(), LocationActivity.class);
        getBaseActivity().startActivityForResult(intentAddress, AppConstants.REQUEST_ADDRESS);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == AppConstants.REQUEST_ADDRESS) {
            if (data != null) tvAddress.setText(data.getStringExtra(AppConstants.ADDRESS));
        }

    }
}
