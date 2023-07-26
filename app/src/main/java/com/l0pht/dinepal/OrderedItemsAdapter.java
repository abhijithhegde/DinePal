package com.l0pht.dinepal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class OrderedItemsAdapter extends RecyclerView.Adapter<OrderedItemsAdapter.ViewHolder> {
    private List<OrderedItem> orderedItemsList;
    private Context context;
    private OnItemClickListener clickListener;
    private OnItemLongClickListener longClickListener;

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

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public interface OnItemLongClickListener {
        void onItemLongClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.clickListener = listener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener listener) {
        this.longClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        OrderedItem orderedItem = orderedItemsList.get(position);
        holder.itemName.setText(orderedItem.getItemName());
        holder.itemPrice.setText(String.valueOf(orderedItem.getItemPrice()));
        holder.quantity.setText(String.valueOf(orderedItem.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return orderedItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, quantity;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemNameTextView);
            itemPrice = itemView.findViewById(R.id.itemPriceTextView);
            quantity = itemView.findViewById(R.id.itemQuantityTextView);
            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        clickListener.onItemClick(position);
                    }
                }
            });
            itemView.setOnLongClickListener(v -> {
                if (longClickListener != null) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        longClickListener.onItemLongClick(position);
                    }
                }
                return true;
            });
        }
    }

    public double calculateTotalPrice(int operation) {
        double total = 0;
        List<OrderedItem> orderedItems = new ArrayList<>(orderedItemsList); // Convert HashSet to List
        for (OrderedItem orderedItem : orderedItems) {
            total += orderedItem.getItemPrice() * orderedItem.getQuantity();
        }
        if (operation == 0) {
            Iterator<OrderedItem> iterator = orderedItemsList.iterator();
            while (iterator.hasNext()) {
                OrderedItem orderedItem = iterator.next();
                if (orderedItem.getQuantity() == 0) {
                    total -= orderedItem.getItemPrice() * orderedItem.getQuantity();
                    iterator.remove(); // Safely remove the item from the list
                }
            }
        }
        return total;
    }
}
