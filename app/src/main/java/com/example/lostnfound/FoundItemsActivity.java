package com.example.lostnfound;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase; 
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FoundItemsActivity extends AppCompatActivity {

    private ListView lv1;
    private TextView tv3;

    private DBHelper db;
    private ArrayList<String> itemList;
    private ArrayList<Integer> itemIds;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_items);

        lv1 = findViewById(R.id.lv1);
        tv3 = findViewById(R.id.tv3);
        Button bt1 = findViewById(R.id.bt1);

        db = new DBHelper(this);

        itemList = new ArrayList<>();
        itemIds = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                itemList
        );

        lv1.setAdapter(adapter);

        bt1.setOnClickListener(v -> finish());

        lv1.setOnItemClickListener((parent, view, position, id) -> {
            int itemId = itemIds.get(position);

            Toast.makeText(
                    FoundItemsActivity.this,
                    "Selected Item ID: " + itemId,
                    Toast.LENGTH_SHORT
            ).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (db != null && itemList != null) {
            loadFoundItems();
        }
    }

    private void loadFoundItems() {

        itemList.clear();
        itemIds.clear();

        SQLiteDatabase database = db.getReadableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT id, item_name, category, location, status " +
                        "FROM Items WHERE type = ? ORDER BY id DESC",
                new String[]{"Found"}
        );

        while (cursor.moveToNext()) {

            int itemId = cursor.getInt(0);
            String name = cursor.getString(1);
            String category = cursor.getString(2);
            String location = cursor.getString(3);
            String status = cursor.getString(4);

            itemIds.add(itemId);

            itemList.add(
                    name +
                            "\nCategory: " + category +
                            "\nLocation: " + location +
                            "\nStatus: " + status
            );
        }

        cursor.close();

        adapter.notifyDataSetChanged();

        if (itemList.isEmpty()) {
            tv3.setText(R.string.no_found_items);
            tv3.setVisibility(View.VISIBLE);
            lv1.setVisibility(View.GONE);
        } else {
            tv3.setVisibility(View.GONE);
            lv1.setVisibility(View.VISIBLE);
        }
    }
}
