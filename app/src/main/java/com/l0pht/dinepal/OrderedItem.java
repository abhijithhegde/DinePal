package com.l0pht.dinepal;

import java.util.Objects;

public class OrderedItem {
    private String itemName;
    private double itemPrice;
    private int quantity;

    public OrderedItem(String itemName, double itemPrice, int quantity) {
        this.itemName = itemName;
        this.itemPrice = itemPrice;
        this.quantity = quantity;
    }

    public String getItemName() {
        return itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof OrderedItem)) return false;
        OrderedItem that = (OrderedItem) o;
        return Double.compare(that.itemPrice, itemPrice) == 0 &&
                itemName.equals(that.itemName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(itemName, itemPrice);
    }

    public void setQuantity(int i) {
        this.quantity = i;
    }
}
