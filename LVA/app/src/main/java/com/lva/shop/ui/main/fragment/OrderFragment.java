package com.lva.shop.ui.main.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.lva.shop.R;
import com.lva.shop.ui.base.BaseFragment;

import butterknife.ButterKnife;

public class OrderFragment extends BaseFragment {

    public static final String TAG = OrderFragment.class.getSimpleName();


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
}
