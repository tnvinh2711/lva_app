package com.lva.shop.ui.main.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.lva.shop.R;
import com.lva.shop.api.ZipRequest;
import com.lva.shop.ui.base.BaseFragment;
import com.lva.shop.ui.main.adapter.HomeImageKnowledgeAdapter;
import com.lva.shop.ui.main.adapter.HomeImageNewsAdapter;
import com.lva.shop.ui.main.adapter.HomeImageTutorialAdapter;
import com.lva.shop.ui.main.model.Knowledge;
import com.lva.shop.ui.main.model.News;
import com.lva.shop.ui.main.model.Tutorial;
import com.lva.shop.ui.webview.WebActivity;
import com.lva.shop.utils.AppConstants;

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

    private List<News.Data> newsList = new ArrayList<>();
    private List<Tutorial.Data> tutorialList = new ArrayList<>();
    private List<Knowledge.Data> knowledgeList = new ArrayList<>();
    private HomeImageKnowledgeAdapter homeImageKnowledgeAdapter;
    private HomeImageNewsAdapter homeImageNewsAdapter;
    private HomeImageTutorialAdapter homeImageTutorialAdapter;


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
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        setUnBinder(ButterKnife.bind(this, view));
        return view;
    }

    @Override
    protected void setUp(View view) {
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
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.appBarLayout, R.id.ll_history, R.id.ll_order, R.id.ll_facebook})
    void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.appBarLayout:
                break;
            case R.id.ll_history:
                break;
            case R.id.ll_order:
                break;
            case R.id.ll_facebook:
                break;
        }
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
}
