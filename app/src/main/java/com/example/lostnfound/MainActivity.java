package com.example.lostnfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private Button btnLostItems, btnFoundItems, btnReportItem, btnSearchItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLostItems = findViewById(R.id.btnLostItems);
        btnFoundItems = findViewById(R.id.btnFoundItems);
        btnReportItem = findViewById(R.id.btnReportItem);
        btnSearchItems = findViewById(R.id.btnSearchItems);

        btnLostItems.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    LostItemsActivity.class
            );
            startActivity(intent);
        });

        btnFoundItems.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    FoundItemsActivity.class
            );
            startActivity(intent);
        });

        btnReportItem.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddItemActivity.class
            );
            startActivity(intent);
        });

        btnSearchItems.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    SearchActivity.class
            );
            startActivity(intent);
        });
    }
}
