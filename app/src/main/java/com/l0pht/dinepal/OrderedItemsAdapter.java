package com.l0pht.dinepal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderedItemsAdapter extends RecyclerView.Adapter<OrderedItemsAdapter.ViewHolder> {
    private List<OrderedItem> orderedItemsList;
    private Context context;

    public OrderedItemsAdapter(Context context, List<OrderedItem> orderedItemsList) {
        this.context = context;
        this.orderedItemsList = orderedItemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ordered_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderedItem orderedItem = orderedItemsList.get(position);
        holder.itemName.setText(orderedItem.getItemName());
        holder.itemPrice.setText(String.valueOf(orderedItem.getItemPrice()));
    }

    @Override
    public int getItemCount() {
        return orderedItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemNameTextView);
            itemPrice = itemView.findViewById(R.id.itemPriceTextView);
        }
    }
}
