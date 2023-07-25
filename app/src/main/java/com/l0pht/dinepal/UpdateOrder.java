package com.l0pht.dinepal;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class UpdateOrder extends AppCompatActivity {
    EditText search;
    TextView order;
    AutoCompleteTextView selectTableNo;
    Button placeOrder, editOrder;
    int tableNo;
    static int orderNo = 1;
    String[] tables = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    ArrayAdapter<String> tableAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_order);
        Objects.requireNonNull(getSupportActionBar()).hide();

        search = findViewById(R.id.search);
        selectTableNo = findViewById(R.id.selectTableNo);
        placeOrder = findViewById(R.id.placeOrder);
        editOrder = findViewById(R.id.editOrder);
        order = findViewById(R.id.orderNo);
        order.setText(String.valueOf(orderNo));


        tableAdapter = new ArrayAdapter<>(this, R.layout.tables_list, tables);
        selectTableNo.setAdapter(tableAdapter);

        order.setText(getIntent().getStringExtra("order"));
//        Log.d("onCreate: ", "selected: " + getIntent().getIntExtra("tableNo", 0));
        selectTableNo.setText(tables[getIntent().getIntExtra("tableNo", 0) - 1]);

        placeOrder.setOnClickListener(v -> {
            onBackPressed();
        });
    }
}