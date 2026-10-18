
package com.example.lostnfound;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
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
    ArrayList<String> imageUris;

    ItemListAdapter adapter;

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
        imageUris = new ArrayList<>();

        adapter = new ItemListAdapter(this, list, imageUris);
        lv1.setAdapter(adapter);

        bt1.setOnClickListener(v -> finish());

        lv1.setOnItemClickListener((parent, view, position, id) -> {
            int itemId = ids.get(position);

            Intent intent = new Intent(
                    FoundItemsActivity.this,
                    ItemDetailsActivity.class
            );

            intent.putExtra("itemId", itemId);
            startActivity(intent);
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
        imageUris.clear();

        SQLiteDatabase database = db.getReadableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT id, item_name, category, location, status, image_uri " +
                        "FROM Items WHERE type=? ORDER BY id DESC",
                new String[]{"Found"}
        );

        try {
            while (cursor.moveToNext()) {
                int itemId = cursor.getInt(0);
                String name = cursor.getString(1);
                String category = cursor.getString(2);
                String location = cursor.getString(3);
                String status = cursor.getString(4);
                String imageUri = cursor.getString(5);

                if (status == null || status.isEmpty()) {
                    status = "Pending";
                }

                ids.add(itemId);

                list.add(name
                        + "\nCategory: " + category
                        + "\nLocation: " + location
                        + "\nStatus: " + status);

                imageUris.add(imageUri);
            }
        } finally {
            cursor.close();
            database.close();
        }

        adapter.notifyDataSetChanged();

        if (list.isEmpty()) {
            tv3.setText(R.string.no_found_items);
            tv3.setVisibility(View.VISIBLE);
            lv1.setVisibility(View.GONE);
        } else {
            tv3.setVisibility(View.GONE);
            lv1.setVisibility(View.VISIBLE);
        }
    }
}
