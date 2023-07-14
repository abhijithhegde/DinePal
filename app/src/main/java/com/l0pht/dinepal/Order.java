package com.l0pht.dinepal;

public class Order {
    private int tableNo;
    private int orderNo;

    public Order(int tableNo, int orderNo) {
        this.tableNo = tableNo;
        this.orderNo = orderNo;
    }

    public int getTableNo() {
        return tableNo;
    }

    public void setTableNo(int tableNo) {
        this.tableNo = tableNo;
    }

    public int getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(int orderNo) {
        this.orderNo = orderNo;
    }
}
