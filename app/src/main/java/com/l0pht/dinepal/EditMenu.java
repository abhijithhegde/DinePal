package com.l0pht.dinepal;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class EditMenu extends AppCompatActivity implements EditMenuAdapter.OnEditItemClickListener {
    DatabaseHelper databaseHelper;
    RecyclerView recyclerView;
    List<MenuItems> menuItemsList;
    EditMenuAdapter editMenuAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_menu);
        recyclerView = findViewById(R.id.recyclerView);

        databaseHelper = new DatabaseHelper(getApplicationContext());

        menuItemsList = databaseHelper.getAllMenuItems();
        editMenuAdapter = new EditMenuAdapter(this, menuItemsList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(editMenuAdapter);

        editMenuAdapter.setEditItemClickListener(this);
    }

    @Override
    public void onEditItemClicked(MenuItems selectedItem, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_edit_item, null);
        builder.setView(dialogView);

        EditText editItemName = dialogView.findViewById(R.id.editItemName);
        EditText editItemPrice = dialogView.findViewById(R.id.editItemPrice);

        // Set initial values to the EditTexts
        editItemName.setText(selectedItem.getItemName());
        editItemPrice.setText(String.valueOf(selectedItem.getItemPrice()));

        builder.setTitle("Edit Item");
        builder.setPositiveButton("Save", (dialog, which) -> {
            // Get edited values from EditTexts
            String editedItemName = editItemName.getText().toString().trim();
            double editedItemPrice = Double.parseDouble(editItemPrice.getText().toString().trim());

            // Update the selected item's values
            selectedItem.setItemName(editedItemName);
            selectedItem.setItemPrice(editedItemPrice);

            // Update the database with the new values
            databaseHelper.updateMenuItem(selectedItem);

            // Update the RecyclerView with the new list
            menuItemsList.set(position, selectedItem);
            editMenuAdapter.notifyItemChanged(position);

            Toast.makeText(this, "Item updated!", Toast.LENGTH_SHORT).show();
        });

        builder.setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.show();
    }
}