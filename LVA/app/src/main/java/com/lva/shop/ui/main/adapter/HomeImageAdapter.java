package com.lva.shop.ui.main.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lva.shop.R;
import com.lva.shop.ui.main.model.Knowledge;
import com.lva.shop.ui.main.model.News;
import com.lva.shop.utils.ViewUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class HomeImageAdapter extends RecyclerView.Adapter<HomeImageAdapter.ViewHolder> {

    private Activity activity;
    private OnItemClickListener listener;
    private List<News.Data> knowledgeList;

    public HomeImageAdapter(Activity activity, List<News.Data> knowledgeList) {
        this.activity = activity;
        this.knowledgeList = knowledgeList;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_rcv_home_img, viewGroup, false);
        return new HomeImageAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        try {
            News.Data data = knowledgeList.get(position);
            viewHolder.bind(data, position, listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return knowledgeList.size();
    }

    public void setNews(List<News.Data> knowledgeList) {
        this.knowledgeList.clear();
        this.knowledgeList = knowledgeList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvContent;
        RoundedImageView ivHeader;
        LinearLayout llContent;


        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
            ivHeader = itemView.findViewById(R.id.iv_header);
            llContent = itemView.findViewById(R.id.ll_content);
        }

        void bind(final News.Data item, int position, final OnItemClickListener listener) {
            if (knowledgeList.size() > 0 && position == 0) {
                ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(itemView.getLayoutParams());
                marginLayoutParams.setMargins(ViewUtils.dpToPx(6), 0, ViewUtils.dpToPx(6), 0);
                itemView.setLayoutParams(marginLayoutParams);
            } else {
                ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(itemView.getLayoutParams());
                marginLayoutParams.setMargins(0, 0, ViewUtils.dpToPx(6), 0);
                itemView.setLayoutParams(marginLayoutParams);
            }
            tvTitle.setText(item.getNewsTitle());
            tvContent.setText(item.getNewsSortdesc());
            Glide.with(activity)
                    .load(item.getLinkImage())
                    .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(ivHeader);
            llContent.setOnClickListener(view -> {
                if (listener != null)
                    listener.OnItemClick(item, position);
            });
        }
    }


    public interface OnItemClickListener {
        void OnItemClick(News.Data item, int position);

        void OnButtonClick(News.Data item, int position);
    }
}
