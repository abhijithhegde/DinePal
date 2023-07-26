package com.l0pht.dinepal;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class TakeOrder extends AppCompatActivity {
    DatabaseHelper databaseHelper;
    TextView order, totalPrice;
    AutoCompleteTextView selectTableNo, search;
    Button placeOrder;
    int tableNo, orderNo = 0;
    String[] tables = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    ArrayAdapter<String> tableAdapter;
    RecyclerView recyclerView;
    List<OrderedItem> orderedItemsList;
    OrderedItemsAdapter orderedItemsAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);
        Objects.requireNonNull(getSupportActionBar()).hide();

        search = findViewById(R.id.search);
        selectTableNo = findViewById(R.id.selectTableNo);
        placeOrder = findViewById(R.id.placeOrder);
        order = findViewById(R.id.orderNo);
        recyclerView = findViewById(R.id.recyclerView);
        totalPrice = findViewById(R.id.totalPrice);

        tableAdapter = new ArrayAdapter<>(this, R.layout.tables_list, tables);
        selectTableNo.setAdapter(tableAdapter);

        databaseHelper = new DatabaseHelper(getApplicationContext());
        orderNo = databaseHelper.getCurrentOrderNO() + 1;
        order.setText(String.valueOf(orderNo));

        databaseHelper.insertItems("Chicken Momo", 150);
        setUpSearchAdapter();


        orderedItemsList = new ArrayList<>();
        orderedItemsAdapter = new OrderedItemsAdapter(this, orderedItemsList);
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
            orderedItemsAdapter.notifyDataSetChanged();
            totalPrice.setText(String.valueOf(orderedItemsAdapter.calculateTotalPrice(0)));
        });


        placeOrder.setOnClickListener(v -> {
            if (isValid()) {
                tableNo = Integer.parseInt(selectTableNo.getText().toString());
                int OrderSL = 1;
                String TrDate = getCurrentDateAndTime();
                for (OrderedItem orderedItem : orderedItemsList) {
                    int PrdtID = databaseHelper.getItemId(orderedItem.getItemName());
                    int Qty = orderedItem.getQuantity();
                    double Rate = orderedItem.getItemPrice();
                    double ItemAmt = Qty * Rate;
                    databaseHelper.insertOrderDetails(TrDate, orderNo, tableNo, OrderSL, PrdtID, Qty, Rate, ItemAmt);
                    OrderSL++;
                }
                Intent resultIntent = new Intent(this, DashBoard.class);

                resultIntent.putExtra("tableNo", tableNo);
                resultIntent.putExtra("orderNo", orderNo);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
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

            OrderedItem orderedItem = new OrderedItem(selectedItem, selectedItemPrice, 1);
            if (!orderedItemsList.contains(orderedItem)) {
                orderedItemsList.add(orderedItem);
                orderedItemsAdapter.notifyDataSetChanged();
                totalPrice.setText(String.valueOf(orderedItemsAdapter.calculateTotalPrice(1)));
            }
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

    private String getCurrentDateAndTime() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(calendar.getTime());
    }

    private boolean isValid() {
        if (selectTableNo.getText().toString().isEmpty()) {
            Toast.makeText(this, "Select Table Number", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}

