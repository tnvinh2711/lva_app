package com.lva.shop.ui.detail.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.lva.shop.R;
import com.lva.shop.ui.detail.model.NtfModel;
import com.lva.shop.utils.AppConstants;
import com.lva.shop.utils.Preference;

import java.util.List;

public class NotiAdapter extends RecyclerView.Adapter<NotiAdapter.ViewHolder> {

    private Activity activity;
    private OnItemClickListener listener;
    private List<NtfModel> ntfModelList;

    public NotiAdapter(Activity activity, List<NtfModel> ntfModelList) {
        this.activity = activity;
        this.ntfModelList = ntfModelList;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_noti, viewGroup, false);
        return new NotiAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        try {
            NtfModel data = ntfModelList.get(position);
            viewHolder.bind(data, position, listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return ntfModelList.size();
    }

    public void setData(List<NtfModel> ntfModelList) {
        this.ntfModelList = ntfModelList;
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvTitle, tvContent;

        ViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
        }

        void bind(final NtfModel item, int pos, final OnItemClickListener listener) {
            try {
                tvTime.setText(item.getTime());
                tvTitle.setText(item.getTitle());
                tvContent.setText(item.getContent());
                if (item.isClick()) {
                    tvTime.setTextColor(activity.getResources().getColor(R.color.color_text_gray));
                    tvContent.setTextColor(activity.getResources().getColor(R.color.color_text_gray));
                    tvTitle.setTextColor(activity.getResources().getColor(R.color.color_text_gray));
                } else {
                    tvTime.setTextColor(activity.getResources().getColor(R.color.color_text_gray));
                    tvContent.setTextColor(activity.getResources().getColor(R.color.color_text));
                    tvTitle.setTextColor(activity.getResources().getColor(R.color.black));
                }
                itemView.setOnClickListener(view -> {
                    item.setClick(true);
                    Gson gson = new Gson();
                    String ntfModelJson = gson.toJson(ntfModelList);
                    Preference.save(activity, AppConstants.NOTI, ntfModelJson);
                    notifyDataSetChanged();
                    listener.OnItemClick(item, pos);
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(NtfModel setting, int value);
    }
}
