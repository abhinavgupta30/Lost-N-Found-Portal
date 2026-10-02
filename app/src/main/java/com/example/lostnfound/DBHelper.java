package com.example.lostnfound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LostFound.db";
    private static final int DATABASE_VERSION = 1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        // Users table
        db.execSQL("CREATE TABLE Users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "email TEXT UNIQUE," +
                "phone TEXT," +
                "password TEXT)");

        // Items table
        db.execSQL("CREATE TABLE Items (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "item_name TEXT," +
                "description TEXT," +
                "category TEXT," +
                "location TEXT," +
                "date TEXT," +
                "type TEXT," +
                "contact TEXT," +
                "status TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS Users");
        db.execSQL("DROP TABLE IF EXISTS Items");

        onCreate(db);
    }

    // Register user
    public boolean registerUser(String name, String email,
                                String phone, String password) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put("name", name);
        values.put("email", email);
        values.put("phone", phone);
        values.put("password", password);

        long result = db.insert("Users", null, values);

        return result != -1;
    }

    // Login user
    public boolean loginUser(String email, String password) {

        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT * FROM Users WHERE email=? AND password=?",
                new String[]{email, password}
        );

        boolean result = cursor.getCount() > 0;

        cursor.close();

        return result;
    }
}