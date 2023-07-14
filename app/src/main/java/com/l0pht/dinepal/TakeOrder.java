package com.l0pht.dinepal;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class TakeOrder extends AppCompatActivity {
    EditText search;
    TextView order;
    AutoCompleteTextView selectTableNo;
    Button placeOrder, editOrder, deleteOrder;
    int tableNo;
    static int orderNo = 1;
    String[] tables = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10"};
    ArrayAdapter<String> tableAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_order);


        search = findViewById(R.id.search);
        selectTableNo = findViewById(R.id.selectTableNo);
        placeOrder = findViewById(R.id.placeOrder);
        editOrder = findViewById(R.id.editOrder);
        order = findViewById(R.id.orderNo);
        order.setText(String.valueOf(orderNo));

        tableAdapter = new ArrayAdapter<>(this, R.layout.tables_list, tables);
        selectTableNo.setAdapter(tableAdapter);


        placeOrder.setOnClickListener(v -> {
            tableNo = Integer.parseInt(selectTableNo.getText().toString());
            Intent resultIntent = new Intent(this, DashBoard.class);
            resultIntent.putExtra("tableNo", tableNo);
            resultIntent.putExtra("orderNo", orderNo);
            orderNo += 1;
            setResult(RESULT_OK, resultIntent);
            finish();
        });
    }
}