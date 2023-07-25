package com.l0pht.dinepal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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


    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
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




    public void closeCursor(Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }


}
