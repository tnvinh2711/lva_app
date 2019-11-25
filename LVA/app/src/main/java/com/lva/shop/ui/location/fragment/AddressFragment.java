package com.lva.shop.ui.location.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.lva.shop.R;
import com.lva.shop.ui.base.BaseFragment;

import butterknife.ButterKnife;

public class AddressFragment extends BaseFragment {

    public static final String TAG = AddressFragment.class.getSimpleName();

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
