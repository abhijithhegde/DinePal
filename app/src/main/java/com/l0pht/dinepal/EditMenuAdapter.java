package com.l0pht.dinepal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EditMenuAdapter extends RecyclerView.Adapter<EditMenuAdapter.ViewHolder> {
    private List<MenuItems> menuItemsList;
    private Context context;
    private OnEditItemClickListener editItemClickListener;

    public EditMenuAdapter(Context context, List<MenuItems> menuItemsList) {
        this.context = context;
        this.menuItemsList = menuItemsList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.edit_item, parent, false);
        return new ViewHolder(view);
    }

    public interface OnEditItemClickListener {
        void onEditItemClicked(MenuItems menuItem, int position);
    }

    public void setEditItemClickListener(OnEditItemClickListener listener) {
        this.editItemClickListener = listener;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MenuItems menuItem = menuItemsList.get(position);
        holder.slNo.setText(String.valueOf(menuItem.getSlNo()));
        holder.itemName.setText(menuItem.getItemName());
        holder.itemPrice.setText(String.valueOf(menuItem.getItemPrice()));
        holder.editItem.setOnClickListener(v -> {
            if (editItemClickListener != null) {
                editItemClickListener.onEditItemClicked(menuItem, position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return menuItemsList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemPrice, slNo;
        ImageButton editItem;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.itemName);
            itemPrice = itemView.findViewById(R.id.itemPrice);
            slNo = itemView.findViewById(R.id.slNo);
            editItem = itemView.findViewById(R.id.editItem);
        }
    }
}
