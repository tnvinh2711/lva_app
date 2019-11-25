package com.lva.shop.ui.main.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lva.shop.R;
import com.lva.shop.ui.main.model.Setting;

import java.util.List;

public class SettingAdapter extends RecyclerView.Adapter<SettingAdapter.ViewHolder> {

    private Activity activity;
    private OnItemClickListener listener;
    private List<Setting> profilesList;

    public SettingAdapter(Activity activity, OnItemClickListener listener, List<Setting> profilesList) {
        this.activity = activity;
        this.listener = listener;
        this.profilesList = profilesList;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_setting, viewGroup, false);
        return new SettingAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        try {
            Setting data = profilesList.get(position);
            viewHolder.bind(data, position, listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return profilesList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivIcon;
        LinearLayout llContent;


        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_feature_profile);
            ivIcon = itemView.findViewById(R.id.iv_icon_profile);
            llContent = itemView.findViewById(R.id.ll_content);
        }

        void bind(final Setting item, int position, final OnItemClickListener listener) {
            tvTitle.setText(item.getTitle());
            ivIcon.setImageDrawable(activity.getResources().getDrawable(item.getIcon()));
            itemView.setOnClickListener(view -> {
                if (listener != null)
                    listener.OnItemClick(item, position);
            });
        }
    }


    public interface OnItemClickListener {
        void OnItemClick(Setting setting, int position);
    }
}
