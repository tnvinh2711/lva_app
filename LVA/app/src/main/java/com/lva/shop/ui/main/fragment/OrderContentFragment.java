package com.lva.shop.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lva.shop.R;
import com.lva.shop.ui.base.BaseFragment;
import com.lva.shop.ui.main.adapter.ProductAdapter;
import com.lva.shop.ui.main.model.DataProduct;
import com.lva.shop.ui.main.model.Product;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OrderContentFragment extends BaseFragment {

    public static final String TAG = OrderContentFragment.class.getSimpleName();
    @BindView(R.id.rcv_product)
    RecyclerView rcvProduct;
    private List<DataProduct> productList = new ArrayList<>();
    private ProductAdapter productAdapter;
    private DialogProductFragment dialogProductFragment;


    public static OrderContentFragment newInstance() {
        Bundle args = new Bundle();
        OrderContentFragment fragment = new OrderContentFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_content_product, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        return view;
    }

    @Override
    protected void setUp(View view) {
        dialogProductFragment = DialogProductFragment.newInstance();
        rcvProduct.setLayoutManager(new GridLayoutManager(getBaseActivity(), 2));
        productAdapter = new ProductAdapter(getBaseActivity(), productList);
        productAdapter.setListener((product, position) -> {
            dialogProductFragment.setData(product);
            dialogProductFragment.show(getBaseActivity().getSupportFragmentManager());
        });
        rcvProduct.setAdapter(productAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

    }

    public void setProductList(Product.Data data) {
        productList.clear();
        productList = data.getData();
        if (productAdapter != null) productAdapter.setData(productList);
    }
}
