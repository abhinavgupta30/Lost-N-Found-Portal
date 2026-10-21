package com.example.lostnfound;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ItemDetailsActivity extends AppCompatActivity {

    TextView tvItemName, tvDescription, tvCategory, tvLocation;
    TextView tvDate, tvType, tvContact, tvStatus;
    ImageView imgItemPhoto;
    Button btnBack, btnResolve;

    DBHelper db;
    int itemId;
    boolean isAdmin;

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

        imgItemPhoto = findViewById(R.id.imgItemPhoto);

        btnBack = findViewById(R.id.btnBack);
        btnResolve = findViewById(R.id.btnResolve);

        db = new DBHelper(this);

        itemId = getIntent().getIntExtra("itemId", -1);
        isAdmin = getIntent().getBooleanExtra("isAdmin", false);

        if (itemId != -1) {
            loadItemDetails(itemId);
        } else {
            Toast.makeText(this, "Item not found",
                    Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        btnBack.setOnClickListener(v -> finish());

        btnResolve.setOnClickListener(v -> markResolved());
    }

    private void loadItemDetails(int id) {

        SQLiteDatabase database = db.getReadableDatabase();

        Cursor cursor = database.rawQuery(
                "SELECT item_name, description, category, location, " +
                        "date, type, contact, status, image_uri " +
                        "FROM Items WHERE id = ?",
                new String[]{String.valueOf(id)}
        );

        try {
            if (cursor.moveToFirst()) {

                tvItemName.setText("Item Name: " + cursor.getString(0));
                tvDescription.setText("Description: " + cursor.getString(1));
                tvCategory.setText("Category: " + cursor.getString(2));
                tvLocation.setText("Location: " + cursor.getString(3));
                tvDate.setText("Date: " + cursor.getString(4));
                tvType.setText("Type: " + cursor.getString(5));
                tvContact.setText("Contact: " + cursor.getString(6));

                String status = cursor.getString(7);

                if (status == null || status.isEmpty()) {
                    status = "Pending";
                }

                tvStatus.setText("Status: " + status);

                String imageUriString = cursor.getString(8);

                if (imageUriString != null && !imageUriString.isEmpty()) {
                    try {
                        imgItemPhoto.setImageURI(Uri.parse(imageUriString));
                        imgItemPhoto.setVisibility(ImageView.VISIBLE);
                    } catch (Exception e) {
                        imgItemPhoto.setVisibility(ImageView.GONE);
                    }
                } else {
                    imgItemPhoto.setVisibility(ImageView.GONE);
                }

                if (isAdmin) {
                    btnResolve.setVisibility(View.VISIBLE);
                    if (status.equalsIgnoreCase("Resolved")) {
                        btnResolve.setEnabled(false);
                        btnResolve.setText("Already Resolved");
                    } else {
                        btnResolve.setEnabled(true);
                        btnResolve.setText("Mark Resolved");
                    }
                } else {
                    btnResolve.setVisibility(View.GONE);
                }

            } else {
                Toast.makeText(this, "Item details not available",
                        Toast.LENGTH_SHORT).show();
                finish();
            }
        } finally {
            cursor.close();
            database.close();
        }
    }

    private void markResolved() {

        SQLiteDatabase database = db.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("status", "Resolved");

        int result;

        try {
            result = database.update(
                    "Items",
                    values,
                    "id = ?",
                    new String[]{String.valueOf(itemId)}
            );
        } finally {
            database.close();
        }

        if (result > 0) {
            Toast.makeText(this, "Item marked as resolved",
                    Toast.LENGTH_SHORT).show();

            loadItemDetails(itemId);
        } else {
            Toast.makeText(this, "Unable to update item status",
                    Toast.LENGTH_SHORT).show();
        }
    }
}
