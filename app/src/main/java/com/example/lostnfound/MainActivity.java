package com.example.lostnfound;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnLostItems, btnFoundItems, btnReportItem, btnSearchItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnLostItems = findViewById(R.id.btnLostItems);
        btnFoundItems = findViewById(R.id.btnFoundItems);
        btnReportItem = findViewById(R.id.btnReportItem);
        btnSearchItems = findViewById(R.id.btnSearchItems);

        btnLostItems.setOnClickListener(v -> {
            Intent i = new Intent(
                    MainActivity.this,
                    LostItemsActivity.class
            );
            startActivity(i);
        });

        btnFoundItems.setOnClickListener(v -> {
            Intent i = new Intent(
                    MainActivity.this,
                    FoundItemsActivity.class
            );
            startActivity(i);
        });

        btnReportItem.setOnClickListener(v -> {
            Intent i = new Intent(
                    MainActivity.this,
                    AddItemActivity.class
            );
            startActivity(i);
        });

        btnSearchItems.setOnClickListener(v -> {
            Intent i = new Intent(
                    MainActivity.this,
                    SearchActivity.class
            );
            startActivity(i);
        });
    }
}
