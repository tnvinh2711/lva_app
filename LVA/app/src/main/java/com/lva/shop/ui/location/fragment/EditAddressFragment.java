package com.lva.shop.ui.location.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lva.shop.R;
import com.lva.shop.ui.base.BaseFragment;
import com.lva.shop.ui.location.LocationActivity;
import com.lva.shop.ui.location.adapter.AddressAdapter;
import com.lva.shop.ui.location.model.Address;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class EditAddressFragment extends BaseFragment {

    public static final String TAG = EditAddressFragment.class.getSimpleName();
    @BindView(R.id.rcv_address)
    RecyclerView rcvAddress;
    private int type = 0;
    private List<Address> addressList = new ArrayList<>();
    private AddressAdapter addressAdapter;

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
        setUpToolbar();
        return view;
    }

    @Override
    protected void setUp(View view) {
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getBaseActivity(), LinearLayoutManager.VERTICAL, false);
        rcvAddress.setLayoutManager(layoutManager);
        addressAdapter = new AddressAdapter(getBaseActivity(), addressList);
        addressAdapter.setListener((address, position) -> {
            getFragmentChangedListener().OnAddressObjChange(type, address);
            getFragmentChangedListener().OnFragmentChangedListener(LocationActivity.SCREEN_ADDRESS);
        });
        rcvAddress.setAdapter(addressAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setType(int typeAddress, List<Address> address) {
        type = typeAddress;
        addressList.clear();
        addressList = address;
    }
}
