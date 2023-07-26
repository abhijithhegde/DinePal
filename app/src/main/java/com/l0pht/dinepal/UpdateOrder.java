package com.l0pht.dinepal;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Objects;

public class UpdateOrder extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    TextView order, totalPrice;
    AutoCompleteTextView selectTableNo, search;
    Button editOrder, cancel;
    int tableNo, orderNo;
    String[] tables = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    ArrayAdapter<String> tableAdapter;
    RecyclerView recyclerView;
    List<OrderedItem> orderedItemsList;
    OrderedItemsAdapter orderedItemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_order);
        Objects.requireNonNull(getSupportActionBar()).hide();

        search = findViewById(R.id.search);
        selectTableNo = findViewById(R.id.selectTableNo);
        selectTableNo.setEnabled(false);
        selectTableNo.setClickable(false);
        editOrder = findViewById(R.id.editOrder);
        order = findViewById(R.id.orderNo);
        recyclerView = findViewById(R.id.recyclerView);
        totalPrice = findViewById(R.id.totalPrice);
        cancel = findViewById(R.id.cancel);


        tableAdapter = new ArrayAdapter<>(this, R.layout.tables_list, tables);
        selectTableNo.setAdapter(tableAdapter);

        order.setText(getIntent().getStringExtra("order"));

        orderNo = Integer.parseInt(order.getText().toString());

        //log orderNo
        Log.d("Order NO:", String.valueOf(orderNo));
        selectTableNo.setText(tables[getIntent().getIntExtra("tableNo", 0) - 1]);


        databaseHelper = new DatabaseHelper(getApplicationContext());
        orderedItemsList = databaseHelper.getOrderedItems(orderNo);
        orderedItemsAdapter = new OrderedItemsAdapter(this, orderedItemsList);


        totalPrice.setText(String.valueOf(orderedItemsAdapter.calculateTotalPrice(1)));

        setUpSearchAdapter();

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(orderedItemsAdapter);

        orderedItemsAdapter.setOnItemClickListener(position -> {
            OrderedItem orderedItem = orderedItemsList.get(position);
            orderedItem.setQuantity(orderedItem.getQuantity() + 1);
            orderedItemsAdapter.notifyDataSetChanged();
            totalPrice.setText(String.valueOf(orderedItemsAdapter.calculateTotalPrice(1)));
        });

        orderedItemsAdapter.setOnItemLongClickListener(position -> {
            OrderedItem orderedItem = orderedItemsList.get(position);
            orderedItem.setQuantity(orderedItem.getQuantity() - 1);
            if (orderedItem.getQuantity() == 0) {
                orderedItemsList.remove(position);
                orderedItemsAdapter.notifyItemRemoved(position);
                databaseHelper.deleteOrderedItem(orderNo, databaseHelper.getIntItemID(orderNo));
            } else {
                orderedItemsAdapter.notifyItemChanged(position);
            }
            totalPrice.setText(String.valueOf(orderedItemsAdapter.calculateTotalPrice(0)));
        });

        editOrder.setOnClickListener(v -> {
            if (isValid()) {
                // Get the selected table number
                tableNo = Integer.parseInt(selectTableNo.getText().toString());
                String TrDate = databaseHelper.getDate(orderNo);
                // Check if any existing item's quantity has become zero, and remove it from the list
                for (int i = orderedItemsList.size() - 1; i >= 0; i--) {
                    OrderedItem orderedItem = orderedItemsList.get(i);
                    if (orderedItem.getQuantity() == 0) {
                        orderedItemsList.remove(i);
                        orderedItemsAdapter.notifyDataSetChanged();
                    }
                }
                for (OrderedItem orderedItem : orderedItemsList) {
                    int PrdtID = databaseHelper.getItemId(orderedItem.getItemName());
                    int Qty = orderedItem.getQuantity();
                    double Rate = orderedItem.getItemPrice();
                    double ItemAmt = Qty * Rate;
                    // Check if the item already exists for the given order
                    if (databaseHelper.isItemExist(orderNo, PrdtID)) {
                        // Item already exists, update its quantity
                        databaseHelper.updateOrderDetails(orderNo, PrdtID, Qty, ItemAmt);
                    } else {
                        // Item doesn't exist, insert a new entry
                        int slNo = databaseHelper.getLatestSlNo(orderNo);
                        slNo++;
                        databaseHelper.insertOrderDetails(TrDate, orderNo, tableNo, slNo, PrdtID, Qty, Rate, ItemAmt);
                    }
                }
                Toast.makeText(this, "Order updated successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

        cancel.setOnClickListener(v -> onBackPressed());

    }

    private void setUpSearchAdapter() {
        String[] from = {DatabaseHelper.COLUMN_NAME};
        int[] to = {android.R.id.text1};

        SimpleCursorAdapter adapter = new SimpleCursorAdapter(this, android.R.layout.simple_dropdown_item_1line, null, new String[]{DatabaseHelper.COLUMN_NAME}, new int[]{android.R.id.text1}, 0);


        search.setAdapter(adapter);
        search.setOnItemClickListener((parent, view, position, id) -> {
            //log position;
            TextView textView = view.findViewById(android.R.id.text1);
            String selectedItem = textView.getText().toString();
            search.setText(selectedItem);

            double selectedItemPrice = databaseHelper.getItemPrice(selectedItem);

            boolean itemExists = false;
            for (OrderedItem existingItem : orderedItemsList) {
                if (existingItem.getItemName().equals(selectedItem)) {
                    // Item already exists, update its quantity
                    existingItem.setQuantity(existingItem.getQuantity() + 1);
                    orderedItemsAdapter.notifyDataSetChanged();
                    totalPrice.setText(String.valueOf(orderedItemsAdapter.calculateTotalPrice(1)));
                    itemExists = true;
                    break;
                }
            }
            if (!itemExists) {
                // Item does not exist, add it as a new item to the list
                OrderedItem orderedItem = new OrderedItem(selectedItem, selectedItemPrice, 1);
                orderedItemsList.add(orderedItem);
                orderedItemsAdapter.notifyDataSetChanged();
                totalPrice.setText(String.valueOf(orderedItemsAdapter.calculateTotalPrice(1)));
            }
            double totalPriceValue = orderedItemsAdapter.calculateTotalPrice(1);
            totalPrice.setText(String.valueOf(totalPriceValue));

            search.setText("");
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Fetch suggestions from the database based on the current text in the AutoCompleteTextView
                try {
                    String searchText = s.toString();
                    Cursor cursor = databaseHelper.getSuggestions(searchText);
                    adapter.changeCursor(cursor);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private boolean isValid() {
        if (selectTableNo.getText().toString().isEmpty()) {
            Toast.makeText(this, "Select Table Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (orderedItemsList.isEmpty()) {
            Toast.makeText(this, "Add items to the order", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}