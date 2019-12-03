package com.lva.shop.ui.detail;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.lva.shop.R;
import com.lva.shop.api.RestfulManager;
import com.lva.shop.callback.ButtonAlertDialogListener;
import com.lva.shop.ui.base.BaseActivity;
import com.lva.shop.ui.detail.adapter.HistoryAdapter;
import com.lva.shop.ui.detail.model.History;
import com.lva.shop.utils.AppConstants;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryActivity extends BaseActivity implements ButtonAlertDialogListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.appbar)
    AppBarLayout appbar;
    @BindView(R.id.rc_history)
    RecyclerView rcHistory;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;

    private String TAG = HistoryActivity.class.getSimpleName();
    private List<History.Data> historyList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        setUnBinder(ButterKnife.bind(this));
        setSupportActionBar(toolbar);
        try {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
        toolbar.setNavigationOnClickListener(v -> onBackPressed());
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rcHistory.setLayoutManager(layoutManager);
        swipeRefresh.setOnRefreshListener(() -> {
            swipeRefresh.setRefreshing(false);
            getData();
        });
        getData();
    }

    private void getData() {
        showLoading();
        //TODO fake phone
        RestfulManager.getInstance(this, 1).getHistory("0986002032", new RestfulManager.OnHistoryListener() {
            @Override
            public void onGetHistorySuccess(History history) {
                hideLoading();
                historyList.clear();
                historyList.addAll(history.getData());
                setUpView();
            }

            @Override
            public void onError(Throwable e) {
                HistoryActivity.this.onError(e, AppConstants.LOAD_DATA_HOME_FAIL);
                hideLoading();
            }
        });
    }

    private void setUpView() {
        HistoryAdapter historyAdapter = new HistoryAdapter(this, historyList);
        historyAdapter.setListener((setting, value) -> {
        });
        rcHistory.setAdapter(historyAdapter);
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
}
