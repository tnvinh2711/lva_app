package com.lva.shop.ui.detail.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Paint;
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
import com.lva.shop.ui.customview.ValueSelector;
import com.lva.shop.ui.customview.ValueSelectorMini;
import com.lva.shop.ui.main.model.DataProduct;
import com.lva.shop.utils.AppConstants;
import com.lva.shop.utils.CommonUtils;
import com.lva.shop.utils.Preference;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    private Activity activity;
    private OnItemClickListener listener;
    private List<DataProduct> productList;

    public CartAdapter(Activity activity, List<DataProduct> productList) {
        this.activity = activity;
        this.productList = productList;
    }

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_cart, viewGroup, false);
        return new CartAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        try {
            DataProduct data = productList.get(position);
            viewHolder.bind(data,position, listener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return productList.size();
    }

    public void setData(List<DataProduct> product) {
        productList.clear();
        productList = product;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        ImageView ivHeader;
        TextView tvPrice, tvDiscount;
        ValueSelectorMini selector;
        RelativeLayout rlRemove;


        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_name_product);
            tvPrice = itemView.findViewById(R.id.tv_money_product);
            tvDiscount = itemView.findViewById(R.id.tv_discount_product);
            ivHeader = itemView.findViewById(R.id.iv_product);
            selector = itemView.findViewById(R.id.selector);
            rlRemove = itemView.findViewById(R.id.rl_remove);
        }

        void bind(final DataProduct item,int pos, final OnItemClickListener listener) {
            try {
                tvTitle.setText(item.getName());
                if (item.getPriceDiscount() == null || item.getPriceDiscount().equals("0.00")) {
                    tvPrice.setVisibility(View.GONE);
                    tvDiscount.setText(CommonUtils.convertMoney(item.getPrice(), 1));
                } else {
                    tvPrice.setVisibility(View.VISIBLE);
                    tvPrice.setPaintFlags(tvPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    tvPrice.setText(CommonUtils.convertMoney(item.getPrice(), 1));
                    tvDiscount.setText(CommonUtils.convertMoney(item.getPriceDiscount(), 1));
                }
                Glide.with(activity)
                        .load(item.getLinkImage())
                        .apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.ALL))
                        .into(ivHeader);
                selector.setMinValue(1);
                selector.setValue(item.getQuality());
                selector.setOnValueListener(value -> {
                    item.setQuality(value);
                    String json = new Gson().toJson(productList);
                    if (json.equals("[]")) {
                        Preference.remove(activity, AppConstants.LIST_CART);
                    } else {
                        Preference.save(activity, AppConstants.LIST_CART, json);
                    }
                    if (listener != null)
                        listener.OnItemClick(item, value);
                });
                rlRemove.setOnClickListener(view -> {
                    removeAt(pos);
                    if (listener != null)
                        listener.OnItemClick(item, 0);
                });
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeAt(int position) {
        productList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, productList.size());
        String json = new Gson().toJson(productList);
        if (json.equals("[]")) {
            Preference.remove(activity, AppConstants.LIST_CART);
        } else {
            Preference.save(activity, AppConstants.LIST_CART, json);
        }

    }
    public interface OnItemClickListener {
        void OnItemClick(DataProduct setting, int value);
    }
}
