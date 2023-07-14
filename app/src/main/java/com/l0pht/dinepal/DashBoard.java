package com.l0pht.dinepal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class DashBoard extends AppCompatActivity {
    FloatingActionButton newOrder;
    ListView listView;
    private List<Order> ordersList;
    private OrderListAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        newOrder = findViewById(R.id.newOrder);
        listView = findViewById(R.id.ordersList);


        ordersList = new ArrayList<>();
        adapter = new OrderListAdapter(this, ordersList);

        newOrder.setOnClickListener(v -> {
            Intent newOrder = new Intent(this, TakeOrder.class);
            startActivityForResult(newOrder, 1);
        });
        listView.setAdapter(adapter);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Retrieve the order details from the data Intent
            int tableNo = data.getIntExtra("tableNo", 0);
            int orderNo = data.getIntExtra("orderNo", 0);

            Log.e("tableNo",String.valueOf(tableNo));
            Log.e("orderNo",String.valueOf(orderNo));
            // Add the new order to the ordersList
            ordersList.add(new Order(tableNo, orderNo));
            adapter.notifyDataSetChanged();
        }
    }
}