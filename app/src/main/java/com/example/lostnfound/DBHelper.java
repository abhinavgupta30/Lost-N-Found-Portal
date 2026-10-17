package com.example.lostnfound;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "LostFound.db";
    private static final int DATABASE_VERSION = 2;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Users (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "name TEXT," +
                "email TEXT UNIQUE," +
                "phone TEXT," +
                "password TEXT," +
                "role TEXT DEFAULT 'user')");

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
    public void onUpgrade(SQLiteDatabase db,
                          int oldVersion, int newVersion) {
        if (oldVersion < 2) {
            db.execSQL(
                    "ALTER TABLE Users ADD COLUMN role TEXT DEFAULT 'user'"
            );
        }
    }

    public boolean registerUser(String name, String email,
                                String phone, String password) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("email", email);
        values.put("phone", phone);
        values.put("password", password);
        values.put("role", "user");

        long result = db.insert("Users", null, values);
        return result != -1;
    }

    public boolean addItem(String name, String description, String category,
                           String location, String date, String type, String contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("item_name", name);
        values.put("description", description);
        values.put("category", category);
        values.put("location", location);
        values.put("date", date);
        values.put("type", type);
        values.put("contact", contact);
        values.put("status", "Pending");

        long result = db.insert("Items", null, values);
        return result != -1;
    }

    public boolean loginUser(String email, String password) {
        return getUserRole(email, password) != null;
    }

    public void createAdminAccount() {
        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT id FROM Users WHERE email = ?",
                new String[]{"admin@nmims.edu"}
        );

        boolean adminExists = cursor.moveToFirst();
        cursor.close();

        if (!adminExists) {
            ContentValues values = new ContentValues();
            values.put("name", "Administrator");
            values.put("email", "admin@nmims.edu");
            values.put("phone", "");
            values.put("password", "Admin@123");
            values.put("role", "admin");

            db.insert("Users", null, values);
        }
    }

    public String getUserRole(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.rawQuery(
                "SELECT role FROM Users WHERE email = ? AND password = ?",
                new String[]{email, password}
        );

        String role = cursor.moveToFirst() ? cursor.getString(0) : null;
        cursor.close();
        return role;
    }
}
