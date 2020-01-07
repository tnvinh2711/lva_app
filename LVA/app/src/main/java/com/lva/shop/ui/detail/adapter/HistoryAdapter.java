package com.lva.shop.ui.detail.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.lva.shop.R;
import com.lva.shop.ui.customview.ValueSelectorMini;
import com.lva.shop.ui.detail.model.History;
import com.lva.shop.ui.main.model.DataProduct;
import com.lva.shop.utils.AppConstants;
import com.lva.shop.utils.CommonUtils;
import com.lva.shop.utils.Preference;

import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {

    private Activity activity;
    private OnItemClickListener listener;
    private List<History.Data> historyList;

    public HistoryAdapter(Activity activity, List<History.Data> historyList) {
        this.activity = activity;
        this.historyList = historyList;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_history, viewGroup, false);
        return new HistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        try {
            History.Data data = historyList.get(position);
            viewHolder.bind(data, position, listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return historyList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime, tvStatus, tvname, tvCount, tvSum;
        ImageView ivAva;


        ViewHolder(View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvStatus = itemView.findViewById(R.id.tv_status);
            tvname = itemView.findViewById(R.id.tv_name_first_product);
            tvCount = itemView.findViewById(R.id.tv_discount_product);
            tvSum = itemView.findViewById(R.id.tv_sum);
            ivAva = itemView.findViewById(R.id.iv_product);
        }

        void bind(final History.Data item, int pos, final OnItemClickListener listener) {
            try {
                tvTime.setText(item.getDateCreated());
                tvStatus.setText(item.getDilivery());
                StringBuilder name = new StringBuilder();
                for (int i = 0; i < item.getProductName().size(); i++) {
                   if(i < item.getProductName().size()-1){
                       name.append(item.getProductName().get(i).getProductName()).append(", ");
                   } else {
                       name.append(item.getProductName().get(i).getProductName());
                   }
                }
                tvname.setText(name);
                tvCount.setText(activity.getString(R.string.discount, CommonUtils.convertMoney(String.valueOf(item.getInvoiceDiscount()), 1)));
                tvSum.setText(CommonUtils.convertMoney(String.valueOf(item.getInvoicePrice()), 1));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public interface OnItemClickListener {
        void OnItemClick(DataProduct setting, int value);
    }
}
