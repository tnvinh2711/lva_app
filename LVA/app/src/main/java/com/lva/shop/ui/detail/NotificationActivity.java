package com.lva.shop.ui.detail;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.appbar.AppBarLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lva.shop.R;
import com.lva.shop.callback.ButtonAlertDialogListener;
import com.lva.shop.ui.base.BaseActivity;
import com.lva.shop.ui.detail.adapter.NotiAdapter;
import com.lva.shop.ui.detail.model.NtfModel;
import com.lva.shop.utils.AppConstants;
import com.lva.shop.utils.Preference;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class NotificationActivity extends BaseActivity implements ButtonAlertDialogListener {


    @BindView(R.id.rc_noti)
    RecyclerView rcNoti;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_read_all)
    ImageView ivReadAll;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    private String TAG = NotificationActivity.class.getSimpleName();
    private List<NtfModel> ntfModelList = new ArrayList<>();
    private NotiAdapter notiAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setUnBinder(ButterKnife.bind(this));
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        getData();
        setUpView();
    }

    private void getData() {
        if (Preference.getString(this, AppConstants.NOTI) != null) {
            String arrayListNoti = Preference.getString(this, AppConstants.NOTI);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<NtfModel>>() {
            }.getType();
            if (gson.fromJson(arrayListNoti, type) != null) {
                ntfModelList.addAll(gson.fromJson(arrayListNoti, type));
            }
        }
    }

    private void setUpView() {
        if (ntfModelList.size() > 0) {
            ivReadAll.setVisibility(View.VISIBLE);
        } else {
            ivReadAll.setVisibility(View.GONE);
        }
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcNoti.setLayoutManager(layoutManager);
        notiAdapter = new NotiAdapter(this, ntfModelList);
        notiAdapter.setListener((item, value) -> {
            if(item.getUrl()!= null){
                Intent intent = new Intent(this, WebActivity.class);
                intent.putExtra(AppConstants.TITLE, item.getTitle());
                intent.putExtra(AppConstants.URL, item.getUrl());
                startActivity(intent);
            }
        });
        rcNoti.setAdapter(notiAdapter);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onConfirmClick(String type) {
        getData();
    }

    @Override
    public void onCancelClick(String type) {

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @OnClick(R.id.iv_read_all)
    public void onViewClicked() {
        for (NtfModel ntfModel : ntfModelList) {
            ntfModel.setClick(true);
        }
        notiAdapter.setData(ntfModelList);
        Gson gson = new Gson();
        String ntfModelJson = gson.toJson(ntfModelList);
        Preference.save(getApplicationContext(), AppConstants.NOTI, ntfModelJson);
    }
}
