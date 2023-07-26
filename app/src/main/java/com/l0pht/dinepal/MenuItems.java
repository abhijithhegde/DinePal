package com.l0pht.dinepal;

public class MenuItems {
    private String itemName;
    private double itemPrice;
    private int slNo;

    public MenuItems(int slNo, String itemName, double itemPrice) {
        this.slNo = slNo;
        this.itemName = itemName;
        this.itemPrice = itemPrice;

    }

    public String getItemName() {
        return itemName;
    }

    public double getItemPrice() {
        return itemPrice;
    }

    public int getSlNo() {
        return slNo;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public void setItemPrice(double itemPrice) {
        this.itemPrice = itemPrice;
    }

    public void setSlNo(int slNo) {
        this.slNo = slNo;
    }
}
