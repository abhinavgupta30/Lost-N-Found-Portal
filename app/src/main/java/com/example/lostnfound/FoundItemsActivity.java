
package com.example.lostnfound;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class FoundItemsActivity extends AppCompatActivity {

    ListView lv1;
    TextView tv3;
    Button bt1;
    DBHelper db;
    ArrayList<String> list;
    ArrayList<Integer> ids;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_found_items);

        lv1 = findViewById(R.id.lv1);
        tv3 = findViewById(R.id.tv3);
        bt1 = findViewById(R.id.bt1);

        db = new DBHelper(this);
        list = new ArrayList<>();
        ids = new ArrayList<>();

        adapter = new ArrayAdapter<>(
                this, android.R.layout.simple_list_item_1, list);

        lv1.setAdapter(adapter);

        loadItems();

        bt1.setOnClickListener(v -> finish());

        lv1.setOnItemClickListener((parent, view, position, id) -> {
            int itemId = ids.get(position);
            android.widget.Toast.makeText(this,
                    "Selected Item ID: " + itemId,
                    android.widget.Toast.LENGTH_SHORT).show();
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (db != null && list != null) {
            loadItems();
        }
    }

    private void loadItems() {
        list.clear();
        ids.clear();

        SQLiteDatabase database = db.getReadableDatabase();

        Cursor c = database.rawQuery(
                "SELECT id, item_name, category, location, status " +
                        "FROM Items WHERE type=? ORDER BY id DESC",
                new String[]{"Found"});

        while (c.moveToNext()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            String category = c.getString(2);
            String location = c.getString(3);
            String status = c.getString(4);

            ids.add(id);
            list.add(name + "\nCategory: " + category
                    + "\nLocation: " + location
                    + "\nStatus: " + status);
        }

        c.close();
        adapter.notifyDataSetChanged();

        if (list.isEmpty()) {
            tv3.setVisibility(TextView.VISIBLE);
            lv1.setVisibility(ListView.GONE);
        } else {
            tv3.setVisibility(TextView.GONE);
            lv1.setVisibility(ListView.VISIBLE);
        }
    }
}
