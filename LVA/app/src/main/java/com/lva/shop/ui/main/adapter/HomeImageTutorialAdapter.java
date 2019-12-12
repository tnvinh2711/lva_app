package com.lva.shop.ui.main.adapter;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.lva.shop.R;
import com.lva.shop.ui.detail.VideoActivity;
import com.lva.shop.ui.main.model.News;
import com.lva.shop.ui.main.model.Tutorial;
import com.lva.shop.utils.AppConstants;
import com.lva.shop.utils.ScreenUtils;
import com.lva.shop.utils.ViewUtils;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.List;

public class HomeImageTutorialAdapter extends RecyclerView.Adapter<HomeImageTutorialAdapter.ViewHolder> {

    private Activity activity;
    private OnItemClickListener listener;
    private List<Tutorial.Data> tutorialList;

    public HomeImageTutorialAdapter(Activity activity, List<Tutorial.Data> tutorialList) {
        this.activity = activity;
        this.tutorialList = tutorialList;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_rcv_home_video, viewGroup, false);
        return new HomeImageTutorialAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        try {
            Tutorial.Data data = tutorialList.get(position);
            viewHolder.bind(data, position, listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return tutorialList.size();
    }

    public void setTutorial(List<Tutorial.Data> knowledgeList) {
        this.tutorialList.clear();
        this.tutorialList = knowledgeList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        RoundedImageView ivHeader;
        RelativeLayout llContent;


        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            ivHeader = itemView.findViewById(R.id.iv_header);
            llContent = itemView.findViewById(R.id.ll_content);
        }

        void bind(final Tutorial.Data item, int position, final OnItemClickListener listener) {
            try {
                itemView.getLayoutParams().width = (ScreenUtils.getScreenWidth(activity) / 2 - ViewUtils.dpToPx(10));
                if (tutorialList.size() > 0 && position == 0) {
                    ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(itemView.getLayoutParams());
                    marginLayoutParams.setMargins(ViewUtils.dpToPx(6), 0, ViewUtils.dpToPx(6), 0);
                    itemView.setLayoutParams(marginLayoutParams);
                } else {
                    ViewGroup.MarginLayoutParams marginLayoutParams = new ViewGroup.MarginLayoutParams(itemView.getLayoutParams());
                    marginLayoutParams.setMargins(0, 0, ViewUtils.dpToPx(6), 0);
                    itemView.setLayoutParams(marginLayoutParams);
                }
                tvTitle.setText(item.getNewsTitle());
                Glide.with(activity)
                        .load(item.getLinkImage())
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(ivHeader);
                llContent.setOnClickListener(view -> {
                    if (listener != null)
                        listener.OnItemClick(item, position);
                    if(item.getLinkVideo()!= null && !TextUtils.isEmpty(item.getLinkVideo())){
                        Intent intentVideo = new Intent(activity, VideoActivity.class);
                        intentVideo.putExtra(AppConstants.VIDEO_URL, item.getLinkVideo());
                        activity.startActivity(intentVideo);
                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public interface OnItemClickListener {
        void OnItemClick(Tutorial.Data item, int position);
    }
}
