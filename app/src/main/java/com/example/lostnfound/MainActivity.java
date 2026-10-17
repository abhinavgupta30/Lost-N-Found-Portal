package com.example.lostnfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button btnReportItem = findViewById(R.id.btnReportItem);
        Button btnAcknowledgeFound = findViewById(R.id.btnAcknowledgeFound);

        // Open Report Lost Item screen
        btnReportItem.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    AddItemActivity.class
            );
            startActivity(intent);
        });

        // Open Found Items screen
        btnAcknowledgeFound.setOnClickListener(v -> {
            Intent intent = new Intent(
                    MainActivity.this,
                    FoundItemsActivity.class
            );
            startActivity(intent);
        });
    }
}
