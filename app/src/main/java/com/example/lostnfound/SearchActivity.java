package com.example.lostnfound;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class SearchActivity extends AppCompatActivity {

    EditText etSearch;
    Spinner spCategory;
    Button btSearch, btBack;
    ListView lv1;
    TextView tvMessage;

    DBHelper db;
    ArrayList<String> list;
    ArrayList<Integer> ids;
    ArrayAdapter<String> adapter;

    String[] categories = {
            "All Categories",
            "Electronics",
            "Bags",
            "Documents",
            "Keys",
            "Clothing",
            "Accessories",
            "Other"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        etSearch = findViewById(R.id.etSearch);
        spCategory = findViewById(R.id.spCategory);
        btSearch = findViewById(R.id.btSearch);
        btBack = findViewById(R.id.btBack);
        lv1 = findViewById(R.id.lv1);
        tvMessage = findViewById(R.id.tvMessage);

        db = new DBHelper(this);

        list = new ArrayList<>();
        ids = new ArrayList<>();

        ArrayAdapter<String> categoryAdapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_spinner_item,
                categories
        );

        categoryAdapter.setDropDownViewResource(
                android.R.layout.simple_spinner_dropdown_item
        );

        spCategory.setAdapter(categoryAdapter);

        adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                list
        );

        lv1.setAdapter(adapter);

        btSearch.setOnClickListener(v -> searchItems());

        btBack.setOnClickListener(v -> finish());

        lv1.setOnItemClickListener((parent, view, position, id) -> {
            int itemId = ids.get(position);

            Intent intent = new Intent(
                    SearchActivity.this,
                    ItemDetailsActivity.class
            );

            intent.putExtra("itemId", itemId);
            startActivity(intent);
        });

        searchItems();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (db != null && list != null) {
            searchItems();
        }
    }

    private void searchItems() {
        list.clear();
        ids.clear();

        String searchText = etSearch.getText().toString().trim();
        String category = spCategory.getSelectedItem().toString();

        SQLiteDatabase database = db.getReadableDatabase();

        String query;
        String[] arguments;

        if (category.equals("All Categories")) {
            query = "SELECT id, item_name, category, location, status " +
                    "FROM Items WHERE item_name LIKE ? ORDER BY id DESC";

            arguments = new String[]{"%" + searchText + "%"};
        } else {
            query = "SELECT id, item_name, category, location, status " +
                    "FROM Items WHERE item_name LIKE ? AND category = ? " +
                    "ORDER BY id DESC";

            arguments = new String[]{
                    "%" + searchText + "%",
                    category
            };
        }

        Cursor c = database.rawQuery(query, arguments);

        while (c.moveToNext()) {
            int id = c.getInt(0);
            String name = c.getString(1);
            String itemCategory = c.getString(2);
            String location = c.getString(3);
            String status = c.getString(4);

            ids.add(id);

            list.add(name
                    + "\nCategory: " + itemCategory
                    + "\nLocation: " + location
                    + "\nStatus: " + status);
        }

        c.close();
        adapter.notifyDataSetChanged();

        if (list.isEmpty()) {
            tvMessage.setText(R.string.no_matching_items);
            tvMessage.setVisibility(View.VISIBLE);
            lv1.setVisibility(View.GONE);
        } else {
            tvMessage.setVisibility(View.GONE);
            lv1.setVisibility(View.VISIBLE);
        }
    }
}
