package com.l0pht.dinepal;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DashBoard extends AppCompatActivity {
    DatabaseHelper databaseHelper;
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
        databaseHelper = new DatabaseHelper(getApplicationContext());
        List<Order> fetchedOrders = databaseHelper.getAllOrders();
        Set<Order> ordersSet = new HashSet<>(fetchedOrders);

        ordersList.addAll(ordersSet);
        adapter = new OrderListAdapter(this, ordersList);

        newOrder.setOnClickListener(v -> {
            Intent newOrder = new Intent(this, TakeOrder.class);
            startActivityForResult(newOrder, 1);
        });
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent updateOrder = new Intent(DashBoard.this, UpdateOrder.class);
                TextView table = view.findViewById(R.id.tableNo);
                TextView order = view.findViewById(R.id.orderNo);
                updateOrder.putExtra("tableNo", Integer.parseInt(table.getText().toString()));
                updateOrder.putExtra("order", order.getText().toString());
                startActivityForResult(updateOrder, 1);

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Retrieve the order details from the data Intent
            int tableNo = data.getIntExtra("tableNo", 0);
            int orderNo = data.getIntExtra("orderNo", 0);

            Log.e("tableNo", String.valueOf(tableNo));
            Log.e("orderNo", String.valueOf(orderNo));
            // Add the new order to the ordersList
            ordersList.add(new Order(tableNo, orderNo));
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.dashboard_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.editMenu) {
            Intent editMenuIntent = new Intent(DashBoard.this, EditMenu.class);
            startActivity(editMenuIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}