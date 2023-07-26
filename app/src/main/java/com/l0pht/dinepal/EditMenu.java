package com.l0pht.dinepal;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class EditMenu extends AppCompatActivity implements EditMenuAdapter.OnEditItemClickListener {
    DatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    List<MenuItems> menuItemsList;
    EditMenuAdapter editMenuAdapter;
    FloatingActionButton newItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);
        recyclerView = findViewById(R.id.recyclerView);
        newItem = findViewById(R.id.newItem);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        menuItemsList = databaseHelper.getAllMenuItems();
        editMenuAdapter = new EditMenuAdapter(this, menuItemsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(editMenuAdapter);

        newItem.setOnClickListener(v -> {
            EditMenuDialog editMenuDialog = new EditMenuDialog(this, 0, null, null, databaseHelper);
            editMenuDialog.setTitle("Add New Item");
            editMenuDialog.setDeleteButtonVisibility(false);
            editMenuDialog.setOnDialogButtonClickListener(new EditMenuDialog.OnDialogButtonClickListener() {
                @Override
                public void onDeleteButtonClick() {
                    editMenuDialog.dismiss();
                }

                @Override
                public void onUpdateButtonClick(String text1, String text2, String text3) {
                    if (!text2.isEmpty() && !text3.isEmpty()) {
                        double price = Double.parseDouble(text3);
                        long newRowId = databaseHelper.insertItems(text2, price);
                        if (newRowId != -1) {
                            Toast.makeText(EditMenu.this, "New Item Added!", Toast.LENGTH_SHORT).show();
                            MenuItems newItem = new MenuItems((int) newRowId, text2, price);
                            menuItemsList.add(newItem);
                            editMenuDialog.dismiss();
                            editMenuAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(EditMenu.this, "Failed to Add item", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(EditMenu.this, "Please enter item name and price", Toast.LENGTH_SHORT).show();
                    }
                }
            });
            editMenuDialog.show();
        });
        editMenuAdapter.setEditItemClickListener(this);

    }

    @Override
    public void onEditItemClicked(MenuItems selectedItem, int position) {
        EditMenuDialog editMenuDialog = new EditMenuDialog(this, selectedItem.getSlNo(), selectedItem.getItemName(), String.valueOf(selectedItem.getItemPrice()), databaseHelper);

        editMenuDialog.setTitle("Edit Item");
        editMenuDialog.setOnDialogButtonClickListener(new EditMenuDialog.OnDialogButtonClickListener() {
            @Override
            public void onDeleteButtonClick() {
                boolean deleted = databaseHelper.deleteItem(selectedItem.getSlNo());
                if (deleted) {
                    Toast.makeText(EditMenu.this, "Item Deleted", Toast.LENGTH_SHORT).show();
                    menuItemsList.remove(position);
                    editMenuAdapter.notifyItemRemoved(position);
                } else {
                    Toast.makeText(EditMenu.this, "Failed to delete item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onUpdateButtonClick(String text1, String text2, String text3) {
                if (!text1.isEmpty() && !text2.isEmpty()) {
                    int slNo = Integer.parseInt(text1);
                    double price = Double.parseDouble(text3);
                    selectedItem.setItemName(text2);
                    selectedItem.setItemPrice(price);

                    boolean isUpdated = databaseHelper.updateMenuItem(slNo, text2, price);
                    if (isUpdated) {
                        Toast.makeText(EditMenu.this, "Item Updated", Toast.LENGTH_SHORT).show();
//                        editMenuAdapter.notifyDataSetChanged();
                        editMenuAdapter.notifyItemChanged(position);
                    } else {
                        Toast.makeText(EditMenu.this, "Failed to update item", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(EditMenu.this, "Please enter item name and price", Toast.LENGTH_SHORT).show();
                }
            }
        });
        editMenuDialog.show();
    }
}