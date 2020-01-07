package com.lva.shop.ui.main.fragment;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.lva.shop.R;
import com.lva.shop.api.ZipRequest;
import com.lva.shop.ui.base.BaseFragment;
import com.lva.shop.ui.detail.HistoryActivity;
import com.lva.shop.ui.detail.NotificationActivity;
import com.lva.shop.ui.detail.ProfileActivity;
import com.lva.shop.ui.detail.WebActivity;
import com.lva.shop.ui.detail.model.NtfModel;
import com.lva.shop.ui.login.LoginActivity;
import com.lva.shop.ui.login.model.UserInfo;
import com.lva.shop.ui.main.MainActivity;
import com.lva.shop.ui.main.adapter.HomeImageKnowledgeAdapter;
import com.lva.shop.ui.main.adapter.HomeImageNewsAdapter;
import com.lva.shop.ui.main.adapter.HomeImageTutorialAdapter;
import com.lva.shop.ui.main.model.Knowledge;
import com.lva.shop.ui.main.model.News;
import com.lva.shop.ui.main.model.Tutorial;
import com.lva.shop.utils.AppConstants;
import com.lva.shop.utils.CommonUtils;
import com.lva.shop.utils.Preference;
import com.ontbee.legacyforks.cn.pedant.SweetAlert.SweetAlertDialog;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class HomeFragment extends BaseFragment {

    public static final String TAG = HomeFragment.class.getSimpleName();
    @BindView(R.id.appBarLayout)
    ConstraintLayout appBarLayout;
    @BindView(R.id.ll_history)
    LinearLayout llHistory;
    @BindView(R.id.ll_order)
    LinearLayout llOrder;
    @BindView(R.id.ll_facebook)
    LinearLayout llFacebook;
    @BindView(R.id.rcv_content_knowledge)
    RecyclerView rcvContentKnowledge;
    @BindView(R.id.rcv_content_tutorial)
    RecyclerView rcvContentTutorial;
    @BindView(R.id.rcv_content_news)
    RecyclerView rcvContentNews;
    @BindView(R.id.tv_login)
    TextView tvLogin;
    @BindView(R.id.swipeRefresh)
    SwipeRefreshLayout swipeRefresh;
    @BindView(R.id.iv_ava)
    ImageView ivAva;
    @BindView(R.id.iv_history)
    ImageView ivHistory;
    @BindView(R.id.tv_history)
    TextView tvHistory;
    @BindView(R.id.iv_order)
    ImageView ivOrder;
    @BindView(R.id.tv_order)
    TextView tvOrder;
    @BindView(R.id.iv_facebook)
    ImageView ivFacebook;
    @BindView(R.id.tv_facebook)
    TextView tvFacebook;
    @BindView(R.id.tv_title_knowledge)
    TextView tvTitleKnowledge;
    @BindView(R.id.tv_title_1)
    TextView tvTitle1;
    @BindView(R.id.tv_point)
    TextView tvPoint;
    @BindView(R.id.tv_count_noti)
    TextView tvCountNoti;
    @BindView(R.id.rl_noti)
    RelativeLayout rlNoti;

    private List<News.Data> newsList = new ArrayList<>();
    private List<Tutorial.Data> tutorialList = new ArrayList<>();
    private List<Knowledge.Data> knowledgeList = new ArrayList<>();
    private HomeImageKnowledgeAdapter homeImageKnowledgeAdapter;
    private HomeImageNewsAdapter homeImageNewsAdapter;
    private HomeImageTutorialAdapter homeImageTutorialAdapter;
    private View view;
    private UserInfo userInfo;


    public static HomeFragment newInstance() {
        Bundle args = new Bundle();
        HomeFragment fragment = new HomeFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_home, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        return view;
    }

    @Override
    protected void setUp(View view) {
        try {
            swipeRefresh.setOnRefreshListener(() -> {
                swipeRefresh.setRefreshing(false);
                getFragmentChangedListener().OnFragmentChangedListener(MainActivity.RELOAD_SCREEN_SHOP);
            });
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(getBaseActivity(), LinearLayoutManager.HORIZONTAL, false);
            LinearLayoutManager layoutManager2
                    = new LinearLayoutManager(getBaseActivity(), LinearLayoutManager.HORIZONTAL, false);
            LinearLayoutManager layoutManager3
                    = new LinearLayoutManager(getBaseActivity(), LinearLayoutManager.HORIZONTAL, false);

            rcvContentKnowledge.setLayoutManager(layoutManager);
            rcvContentTutorial.setLayoutManager(layoutManager2);
            rcvContentNews.setLayoutManager(layoutManager3);
            homeImageKnowledgeAdapter = new HomeImageKnowledgeAdapter(getBaseActivity(), knowledgeList);
            homeImageNewsAdapter = new HomeImageNewsAdapter(getBaseActivity(), newsList);
            homeImageTutorialAdapter = new HomeImageTutorialAdapter(getBaseActivity(), tutorialList);
            rcvContentKnowledge.setAdapter(homeImageKnowledgeAdapter);
            rcvContentTutorial.setAdapter(homeImageTutorialAdapter);
            rcvContentNews.setAdapter(homeImageNewsAdapter);
            homeImageKnowledgeAdapter.setListener((item, position) -> goToWebActivity(item.getNewsTitle(), item.getLinkDetail()));
            homeImageNewsAdapter.setListener((item, position) -> goToWebActivity(item.getNewsTitle(), item.getLinkDetail()));
            if (Preference.getString(getBaseActivity(), AppConstants.ACCESS_TOKEN) != null) {
                String jsonUser = Preference.getString(getBaseActivity(), AppConstants.USER_INFO);
                Log.e(TAG, "setUp: " + jsonUser);
                Gson gson = new Gson();
                userInfo = gson.fromJson(jsonUser, UserInfo.class);
                if (userInfo.getName() != null && !userInfo.getName().equals("")) {
                    tvLogin.setText(userInfo.getName());
                } else {
                    tvLogin.setText(getString(R.string.hello));
                }
                tvPoint.setVisibility(View.VISIBLE);
                tvPoint.setText(userInfo.getRank()+" | "+ userInfo.getPoint());
                Glide.with(this)
                        .load(userInfo.getUrlAvatar())
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop().placeholder(getResources().getDrawable(R.mipmap.ic_profile_unselected)))
                        .into(ivAva);
            } else {
                tvLogin.setText(getString(R.string.login));
                tvPoint.setVisibility(View.GONE);
                Glide.with(this)
                        .load(getResources().getDrawable(R.mipmap.ic_profile_unselected))
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL).circleCrop().placeholder(getResources().getDrawable(R.mipmap.ic_profile_unselected)))
                        .into(ivAva);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToWebActivity(String newsTitle, String linkDetail) {
        if (linkDetail != null && !TextUtils.isEmpty(linkDetail)) {
            Intent intent = new Intent(getBaseActivity(), WebActivity.class);
            intent.putExtra(AppConstants.TITLE, newsTitle != null ? newsTitle : getString(R.string.news));
            intent.putExtra(AppConstants.URL, linkDetail);
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getBaseActivity())
                .registerReceiver(mGlobalMessageReceiver, new IntentFilter(AppConstants.INTENT_FILTER_FROM_NOTI));
        checkBadge();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(getBaseActivity()).unregisterReceiver(mGlobalMessageReceiver);
    }

    private BroadcastReceiver mGlobalMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                checkBadge();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };

    private void checkBadge() {
        int count = 0;
        List<NtfModel> ntfModelList = new ArrayList<>();
        if (Preference.getString(getBaseActivity(), AppConstants.NOTI) != null) {
            String arrayListNoti = Preference.getString(getBaseActivity(), AppConstants.NOTI);
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<NtfModel>>() {
            }.getType();
            if (gson.fromJson(arrayListNoti, type) != null) {
                ntfModelList.addAll(gson.fromJson(arrayListNoti, type));
                if (ntfModelList.size() > 0) {

                    for (NtfModel ntfModel : ntfModelList) {
                        if (!ntfModel.isClick()) count++;
                    }
                    if (count > 0) {
                        tvCountNoti.setVisibility(View.VISIBLE);
                        tvCountNoti.setText(String.valueOf(count));
                    } else {
                        tvCountNoti.setVisibility(View.GONE);
                    }
                } else {
                    tvCountNoti.setVisibility(View.GONE);
                }
            }
        } else {
            tvCountNoti.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    public void setData(ZipRequest zipRequest) {
        if (zipRequest != null) {
            knowledgeList = zipRequest.getResponseKnowledge().getData();
            tutorialList = zipRequest.getResponseTutorial().getData();
            newsList = zipRequest.getResponseNews().getData();
            if (homeImageNewsAdapter != null) homeImageNewsAdapter.setNews(newsList);
            if (homeImageKnowledgeAdapter != null)
                homeImageKnowledgeAdapter.setKnowledge(knowledgeList);
            if (homeImageTutorialAdapter != null)
                homeImageTutorialAdapter.setTutorial(tutorialList);
        }
    }

    @OnClick({R.id.ll_history, R.id.ll_order, R.id.ll_facebook, R.id.appBarLayout, R.id.rl_noti})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.appBarLayout:
                if (Preference.getString(getBaseActivity(), AppConstants.ACCESS_TOKEN) != null) {
                    Intent intentProfile = new Intent(getBaseActivity(), ProfileActivity.class);
                    startActivity(intentProfile);
                } else {
                    Intent intentLogin = new Intent(getBaseActivity(), LoginActivity.class);
                    intentLogin.putExtra(AppConstants.LAUNCH_APP, false);
                    startActivityForResult(intentLogin, AppConstants.REQ_LOGIN_FROM_HOME);
                }
                break;
            case R.id.ll_history:
                if (Preference.getString(getBaseActivity(), AppConstants.ACCESS_TOKEN) != null) {
                    Intent intentHistory = new Intent(getBaseActivity(), HistoryActivity.class);
                    startActivity(intentHistory);
                } else {
                    showDialogError(getString(R.string.you_need_login), 1);
                }
                break;
            case R.id.ll_order:
                getFragmentChangedListener().OnFragmentChangedListener(MainActivity.SCREEN_ORDER);
                break;
            case R.id.ll_facebook:
                CommonUtils.directToFacebook(getBaseActivity(), "109490687202082");
                break;
            case R.id.rl_noti:
                startActivity(new Intent(getBaseActivity(), NotificationActivity.class));
                break;
        }
    }

    private void showDialogError(String string, int type) {
        new SweetAlertDialog(getBaseActivity(), SweetAlertDialog.WARNING_TYPE)
                .setTitleText(getString(R.string.attention))
                .setContentText(string)
                .setCancelText(getString(R.string.skip))
                .setConfirmText(getString(R.string.ok))
                .showCancelButton(true)
                .setCancelClickListener(SweetAlertDialog::cancel)
                .setConfirmClickListener(sweetAlertDialog -> {
                    sweetAlertDialog.cancel();
                    Intent intentLogin = new Intent(getBaseActivity(), LoginActivity.class);
                    intentLogin.putExtra(AppConstants.LAUNCH_APP, false);
                    startActivityForResult(intentLogin, AppConstants.REQ_LOGIN_FROM_HOME);

                })
                .show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        setUp(view);
    }

    @OnClick(R.id.rl_noti)
    public void onViewClicked() {
    }
}
