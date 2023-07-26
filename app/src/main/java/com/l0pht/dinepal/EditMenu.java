package com.l0pht.dinepal;

import android.app.AlertDialog;
import android.os.Bundle;
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
        String message = "Item: " + selectedItem.getItemName()
                + "\nPrice: " + selectedItem.getItemPrice()
                + "\nSL No: " + selectedItem.getSlNo();

        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }
}