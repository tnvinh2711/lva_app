package com.lva.shop.ui.main.fragment;

import android.os.Bundle;
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
import com.lva.shop.ui.main.adapter.HomeImageAdapter;
import com.lva.shop.ui.main.model.Knowledge;
import com.lva.shop.ui.main.model.News;

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

    private List<News.Data> knowledgeList = new ArrayList<>();
    private HomeImageAdapter homeImageAdapter;


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
        homeImageAdapter = new HomeImageAdapter(getBaseActivity(), knowledgeList);
        rcvContentKnowledge.setAdapter(homeImageAdapter);
        rcvContentTutorial.setAdapter(homeImageAdapter);
        rcvContentNews.setAdapter(homeImageAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @OnClick({R.id.appBarLayout, R.id.ll_history, R.id.ll_order, R.id.ll_facebook})
    public void onViewClicked(View view) {
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
            knowledgeList = zipRequest.getResponseNews().getData();
            if (homeImageAdapter != null) homeImageAdapter.setNews(knowledgeList);
        }

    }
}
