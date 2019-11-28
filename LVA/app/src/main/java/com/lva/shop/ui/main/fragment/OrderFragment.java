package com.lva.shop.ui.main.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager2.widget.ViewPager2;

import com.andremion.counterfab.CounterFab;
import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lva.shop.R;
import com.lva.shop.api.ZipRequest;
import com.lva.shop.ui.base.BaseFragment;
import com.lva.shop.ui.detail.CartActivity;
import com.lva.shop.ui.location.LocationActivity;
import com.lva.shop.ui.main.MainActivity;
import com.lva.shop.ui.main.adapter.ViewPagerAdapter;
import com.lva.shop.ui.main.model.DataProduct;
import com.lva.shop.ui.main.model.Product;
import com.lva.shop.utils.AppConstants;
import com.lva.shop.utils.Preference;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class OrderFragment extends BaseFragment {

    public static final String TAG = OrderFragment.class.getSimpleName();
    @BindView(R.id.tv_address)
    TextView tvAddress;
    @BindView(R.id.appBarLayout)
    ConstraintLayout appBarLayout;
    @BindView(R.id.tabs)
    TabLayout tabLayout;
    @BindView(R.id.view_pager)
    ViewPager2 viewPager;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.tv_change)
    TextView tvChange;
    @BindView(R.id.fab)
    CounterFab fab;
    @BindView(R.id.imageView)
    ImageView imageView;

    private ViewPagerAdapter viewPagerAdapter;
    private List<Product.Data> productList = new ArrayList<>();
    List<DataProduct> cartList = new ArrayList<>();
    private View view;

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
        view = inflater.inflate(R.layout.fragment_order, container, false);
        setUnBinder(ButterKnife.bind(this, view));

        return view;
    }

    @Override
    protected void setUp(View view) {
        setUpFab();
        swipeRefresh.setOnRefreshListener(() -> {
            swipeRefresh.setRefreshing(false);
            getFragmentChangedListener().OnFragmentChangedListener(MainActivity.RELOAD_SCREEN_SHOP);
        });
        viewPagerAdapter = new ViewPagerAdapter(getBaseActivity(), productList);
        viewPager.setAdapter(viewPagerAdapter);
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(productList.get(position).getCategoryName())).attach();
    }

    private void setUpFab() {
        if (Preference.getString(getBaseActivity(), AppConstants.LIST_CART) != null) {
            String arrayListCart = Preference.getString(getBaseActivity(), AppConstants.LIST_CART);
            Gson gson = new Gson();
            Type cartType = new TypeToken<ArrayList<DataProduct>>() {
            }.getType();
            cartList = gson.fromJson(arrayListCart, cartType);
            fab.setVisibility(View.VISIBLE);
            fab.setCount(cartList.size());
        } else {
            fab.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (resultCode){
            case AppConstants.REQUEST_ADDRESS:
                if (data != null) tvAddress.setText(data.getStringExtra(AppConstants.ADDRESS));
                break;
            case AppConstants.CART_RESULT:
                setUpFab();
                break;
        }

    }

    public void setData(ZipRequest zipRequest) {
        try {
            if (zipRequest != null) {
                productList = zipRequest.getResponseProduct().getData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(Boolean isIncrease) {
        YoYo.with(Techniques.Bounce)
                .duration(500)
                .onStart(animator -> fab.setVisibility(View.VISIBLE))
                .onEnd(animator -> {
                    if (isIncrease) {
                        fab.increase();
                    }
                })
                .playOn(fab);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    public void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @OnClick({R.id.appBarLayout, R.id.fab})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.appBarLayout:
                Intent intentAddress = new Intent(getBaseActivity(), LocationActivity.class);
                getBaseActivity().startActivityForResult(intentAddress, AppConstants.REQUEST_ADDRESS);
                break;
            case R.id.fab:
                Intent intentCart = new Intent(getBaseActivity(), CartActivity.class);
                getBaseActivity().startActivityForResult(intentCart, AppConstants.CART_RESULT);
                break;
        }
    }
}