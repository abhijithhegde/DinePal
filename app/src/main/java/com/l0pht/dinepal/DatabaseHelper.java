package com.l0pht.dinepal;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "DinePalDb";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "item_mast";

    private static final String CREATE_TABLE = "create table " + TABLE_NAME +
            "(id integer primary key autoincrement," +
            "itemName text not null," +
            "price real not null)";

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

    public List<String> getAll() {
        List<String> list = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String select = "SELECT itemName FROM " + TABLE_NAME;
        Cursor cursor = db.rawQuery(select, null);

        if (cursor.moveToFirst()) {
            do {
                String name = cursor.getString(0);
                assert false;
                list.add(name);
                Log.d("getAll: ", name);
            } while (cursor.moveToNext());
        }
        db.close();
        return list;
    }
}
