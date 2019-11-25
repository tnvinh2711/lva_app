package com.lva.shop.ui.location.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.lva.shop.R;
import com.lva.shop.ui.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class EditAddressFragment extends BaseFragment {

    public static final String TAG = EditAddressFragment.class.getSimpleName();

    public static EditAddressFragment newInstance() {
        Bundle args = new Bundle();
        EditAddressFragment fragment = new EditAddressFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_address, container, false);
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
