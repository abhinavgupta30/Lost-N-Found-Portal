package com.example.lostnfound;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailsActivity extends AppCompatActivity {

    TextView tvItemName, tvDescription, tvCategory, tvLocation;
    TextView tvDate, tvType, tvContact, tvStatus;
    Button btnBack;

    DBHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        tvItemName = findViewById(R.id.tvItemName);
        tvDescription = findViewById(R.id.tvDescription);
        tvCategory = findViewById(R.id.tvCategory);
        tvLocation = findViewById(R.id.tvLocation);
        tvDate = findViewById(R.id.tvDate);
        tvType = findViewById(R.id.tvType);
        tvContact = findViewById(R.id.tvContact);
        tvStatus = findViewById(R.id.tvStatus);
        btnBack = findViewById(R.id.btnBack);

        db = new DBHelper(this);

        int itemId = getIntent().getIntExtra("itemId", -1);

        if (itemId != -1) {
            loadItemDetails(itemId);
        } else {
            Toast.makeText(this, "Item not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnBack.setOnClickListener(v -> finish());
    }

    private void loadItemDetails(int itemId) {

        SQLiteDatabase database = db.getReadableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT item_name, description, category, location, date, type, contact, status " +
                        "FROM Items WHERE id = ?",
                new String[]{String.valueOf(itemId)}
        );

        if (cursor.moveToFirst()) {

            tvItemName.setText("Item Name: " + cursor.getString(0));
            tvDescription.setText("Description: " + cursor.getString(1));
            tvCategory.setText("Category: " + cursor.getString(2));
            tvLocation.setText("Location: " + cursor.getString(3));
            tvDate.setText("Date: " + cursor.getString(4));
            tvType.setText("Type: " + cursor.getString(5));
            tvContact.setText("Contact: " + cursor.getString(6));
            tvStatus.setText("Status: " + cursor.getString(7));

        } else {
            Toast.makeText(this, "Item details not available", Toast.LENGTH_SHORT).show();
            finish();
        }

        cursor.close();
    }
}
