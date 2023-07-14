package com.l0pht.dinepal;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class OrderListAdapter extends BaseAdapter {

    private List<Order> orderList;
    private LayoutInflater inflater;

    public OrderListAdapter(Context context, List<Order> orderList) {
        this.orderList = orderList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Object getItem(int position) {
        return orderList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = inflater.inflate(R.layout.orders_list, parent, false);
        }

        TextView tableNoTextView = view.findViewById(R.id.tableNo);
        TextView orderNoTextView = view.findViewById(R.id.orderNo);

        Order order = orderList.get(position);

        tableNoTextView.setText(String.valueOf(order.getTableNo()));
        orderNoTextView.setText(String.valueOf(order.getOrderNo()));

        return view;
    }
}
