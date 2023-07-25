package com.l0pht.dinepal;

public class OrderedItem {
    private String itemName;
    private double itemPrice;

    public OrderedItem(String itemName, double itemPrice) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }
}
