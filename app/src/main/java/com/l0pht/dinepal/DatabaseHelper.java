package com.l0pht.dinepal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "DinePalDb";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "item_mast";
    public static final String COLUMN_ITEM_CODE = "_id";
    public static final String COLUMN_NAME = "itemName";
    public static final String COLUMN_PRICE = "price";
    private static final String CREATE_TABLE = "create table " + TABLE_NAME +
            "(" + COLUMN_ITEM_CODE + " INTEGER PRIMARY KEY AUTOINCREMENT," +
            COLUMN_NAME + " TEXT NOT NULL," +
            COLUMN_PRICE + " real NOT NULL)";
    private static final String CREATE_ORDER_MAST_TABLE = "CREATE TABLE order_mast (" +
            "TrDate TEXT," +
            "OrderNo INTEGER," +
            "TableNo INTEGER," +
            "OrderSL INTEGER," +
            "PrdtID INTEGER," +
            "Qty INTEGER," +
            "Rate REAL," +
            "ItemAmt REAL)";


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
        db.execSQL(CREATE_ORDER_MAST_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS order_mast");
        onCreate(db);
    }

    public long insertItems(String itemName, double price) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("itemName", itemName);
        values.put("price", price);
        long newRowId = db.insert(TABLE_NAME, null, values);
        Log.d("insertItems: ", "inserted");
        return newRowId;
    }


    public Cursor getSuggestions(String searchText) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT _id," + COLUMN_NAME + " AS itemName, _id FROM " + TABLE_NAME +
                " WHERE itemName LIKE '%" + searchText + "%'";
        Cursor cursor = db.rawQuery(query, null);
        return cursor;
    }

    public double getItemPrice(String itemName) {
        double itemPrice = -1; // Default value if the item is not found or there is an error

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Define the query
            String query = "SELECT " + COLUMN_PRICE + " FROM " + TABLE_NAME +
                    " WHERE " + COLUMN_NAME + " = ?";

            // Execute the query
            cursor = db.rawQuery(query, new String[]{itemName});

            // Check if the cursor contains any data
            if (cursor != null && cursor.moveToFirst()) {
                // Get the price value from the cursor
                itemPrice = cursor.getDouble(0); // The price is in the first column
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the cursor and database
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return itemPrice;
    }

    public int getItemId(String itemName) {
        int productId = -1; // Default value in case the item is not found

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Define the columns to retrieve from the table
            String[] columns = {"_id"};

            // Define the selection criteria (WHERE clause)
            String selection = "ItemName = ?";

            // Define the selection arguments (values to replace ? in the selection criteria)
            String[] selectionArgs = {itemName};

            // Execute the query
            cursor = db.query("item_mast", columns, selection, selectionArgs, null, null, null);

            // Check if a matching item is found in the table
            if (cursor != null && cursor.moveToFirst()) {
                // Extract the product ID from the cursor
                productId = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close the cursor and the database connection
            if (cursor != null) {
                cursor.close();
            }
            db.close();
        }

        return productId;
    }

    public String getItemName(int itemID) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT itemName FROM item_mast WHERE _id = ?";
        String[] selectionArgs = {String.valueOf(itemID)};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        if (cursor != null && cursor.moveToFirst()) {
            String itemName = cursor.getString(0); // The column index is 0 because there is only one column in the result
            cursor.close();
            return itemName;
        }

        return null; // Return null if the item ID is not found
    }

    public long insertOrderDetails(String trDate, int orderNo, int tableNo, int orderSL, int prdtID, int qty, double rate, double itemAmt) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("TrDate", trDate);
        values.put("OrderNo", orderNo);
        values.put("TableNo", tableNo);
        values.put("OrderSL", orderSL);
        values.put("PrdtID", prdtID);
        values.put("Qty", qty);
        values.put("Rate", rate);
        values.put("ItemAmt", itemAmt);
        return db.insert("Order_mast", null, values);
    }

    public List<OrderedItem> getOrderedItems(int orderNo) {
        List<OrderedItem> orderedItemsList = new ArrayList<>();
        double totalAmount = 0.0;

        SQLiteDatabase db = this.getReadableDatabase();
        String[] columns = {"PrdtID", "Qty", "Rate", "ItemAmt"};
        String selection = "OrderNo=?";
        String[] selectionArgs = {String.valueOf(orderNo)};

        Cursor cursor = db.query("order_mast", columns, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int prdtID = cursor.getInt(cursor.getColumnIndex("PrdtID"));
                int qty = cursor.getInt(cursor.getColumnIndex("Qty"));
                double rate = cursor.getDouble(cursor.getColumnIndex("Rate"));
                double itemAmt = cursor.getDouble(cursor.getColumnIndex("ItemAmt"));

                // Fetch the item name using prdtID from another table (e.g., item_mast)
                String itemName = this.getItemName(prdtID);

                // Create OrderedItem object and add it to the list
                OrderedItem orderedItem = new OrderedItem(itemName, rate, qty);
                orderedItemsList.add(orderedItem);

                // Update the total amount
                totalAmount += itemAmt;
            } while (cursor.moveToNext());
        }

        // Close the cursor
        if (cursor != null) {
            cursor.close();
        }
        return orderedItemsList;
    }

    public List<Order> getAllOrders() {
        List<Order> ordersList = new ArrayList<>();

        // Assuming you have a SQLiteDatabase instance named 'database' initialized in your DatabaseHelper class
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database to fetch all the orders
        Cursor cursor = db.query("Order_mast", new String[]{"TableNo", "OrderNo"}, null, null, null, null, null);

        if (cursor != null) {
            // Loop through the cursor to retrieve the orders and their table numbers
            while (cursor.moveToNext()) {
                int tableNo = cursor.getInt(cursor.getColumnIndexOrThrow("TableNo"));
                int orderNo = cursor.getInt(cursor.getColumnIndexOrThrow("OrderNo"));

                // Create an Order object and add it to the list
                Order order = new Order(tableNo, orderNo);
                ordersList.add(order);
            }
            cursor.close();
        }

        return ordersList;
    }

    public int getCurrentOrderNO() {
        int latestOrderNo = 0;

        // Assuming you have a SQLiteDatabase instance named 'database' initialized in your DatabaseHelper class
        SQLiteDatabase db = this.getReadableDatabase();

        // Query the database to fetch the maximum orderNo
        Cursor cursor = db.rawQuery("SELECT MAX(OrderNo) AS maxOrderNo FROM Order_mast", null);

        if (cursor != null && cursor.moveToFirst()) {
            latestOrderNo = cursor.getInt(cursor.getColumnIndex("maxOrderNo"));
            cursor.close();
        }

        return latestOrderNo;
    }

    public List<MenuItems> getAllMenuItems() {
        List<MenuItems> menuItemsList = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = null;

        try {
            // Query the item_mast table to get all the values
            cursor = db.query("item_mast", null, null, null, null, null, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    // Extract the values from the cursor
                    int itemId = cursor.getInt(cursor.getColumnIndex("_id"));
                    String itemName = cursor.getString(cursor.getColumnIndex("itemName"));
                    double itemPrice = cursor.getDouble(cursor.getColumnIndex("price"));

                    // Create a MenuItems object and add it to the list
                    MenuItems menuItem = new MenuItems(itemId, itemName, itemPrice);
                    menuItemsList.add(menuItem);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return menuItemsList;
    }

    public void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }


}
