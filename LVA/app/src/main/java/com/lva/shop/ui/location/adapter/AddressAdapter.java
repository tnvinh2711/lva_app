package com.lva.shop.ui.location.adapter;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.lva.shop.R;
import com.lva.shop.ui.location.model.Address;

import java.util.List;

public class AddressAdapter extends RecyclerView.Adapter<AddressAdapter.ViewHolder> {

    private Activity activity;
    private List<Address> addressList;
    private OnItemClickListener onItemClickListener;

    public AddressAdapter(Activity activity, List<Address> addressList) {
        this.activity = activity;
        this.addressList = addressList;
    }

    public void setListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.item_address, viewGroup, false);
        return new AddressAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        try {
            Address data = addressList.get(position);
            viewHolder.bind(data, position, onItemClickListener);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return addressList.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;

        ViewHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_address);
        }

        void bind(final Address item, int position, final OnItemClickListener listener) {
            try {
                tvTitle.setText(item.getName());
                itemView.setOnClickListener(view -> {
                    if (listener != null)
                        listener.OnItemClick(item, position);
                });
            } catch (Resources.NotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public interface OnItemClickListener {
        void OnItemClick(Address address, int position);
    }
}
