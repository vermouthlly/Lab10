package com.example.dell.lab10_code;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by dell on 2017/12/7.
 */

public class MyDB extends SQLiteOpenHelper {

    private static final String DB_NAME = "Contacts.db";
    private static final String TABLE_NAME = "Contacts";
    private static final int DB_VERSION = 1;


    public MyDB(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE= "create table "
                + TABLE_NAME
                + " (_id integer primary key , "
                + "name text , "
                + "birth text , "
                + "gift text);";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

    public void insert(String name, String birthday, String gift) {
        SQLiteDatabase db= getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("birth", birthday);
        values.put("gift", gift);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public void delete(String name) {
        SQLiteDatabase db = getWritableDatabase();
        String whereClause = "name = ?";
        String[] whereArgs = { name + "" };
       db.delete(TABLE_NAME, whereClause, whereArgs);
        db.close();
    }

    public void update(String name, String birthday, String gift) {
        SQLiteDatabase db= getWritableDatabase();
        String whereClause = "name = ?";
        String[] whereArgs = { name + "" };
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("birth", birthday);
        values.put("gift", gift);
        db.update(TABLE_NAME, values, whereClause, whereArgs);
        db.close();
    }

    public Cursor getAll() {
        SQLiteDatabase db= getWritableDatabase();
        Cursor cursor= db.query(TABLE_NAME, new String[]{"_id", "name", "birth", "gift"}, null, null, null, null, null);
        return cursor;
    }

    public boolean query(String name) {
        SQLiteDatabase db= getWritableDatabase();
        Cursor cursor= getAll();
        while (cursor.moveToNext()) {
            if (cursor.getString(cursor.getColumnIndex("name")).equals(name)) {
                return true;
            }
        }
        return false;
    }
}