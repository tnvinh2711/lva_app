package com.lva.shop.ui.main.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.lva.shop.ui.main.fragment.OrderContentFragment;
import com.lva.shop.ui.main.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentStateAdapter {
    private List<Product.Data> dataProducts = new ArrayList<>();

    public ViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, List<Product.Data> data) {
        super(fragmentActivity);
        this.dataProducts.clear();
        this.dataProducts.addAll(data);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        OrderContentFragment orderContentFragment = OrderContentFragment.newInstance();
        orderContentFragment.setProductList(dataProducts.get(position));
        return orderContentFragment;
    }

    @Override
    public int getItemCount() {
        return dataProducts.size();
    }

    public void setProductList(List<Product.Data> data) {
        dataProducts.clear();
        dataProducts .addAll(data);
        notifyDataSetChanged();
    }
}
