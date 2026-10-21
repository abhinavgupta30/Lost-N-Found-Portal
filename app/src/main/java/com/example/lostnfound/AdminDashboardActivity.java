package com.example.lostnfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        TextView tvAdminWelcome = findViewById(R.id.tvAdminWelcome);
        Button btnManageItems = findViewById(R.id.btnManageItems);
        Button btnManageUsers = findViewById(R.id.btnManageUsers);
        Button btnAdminLogout = findViewById(R.id.btnAdminLogout);

        tvAdminWelcome.setText(R.string.admin_welcome);

        btnManageItems.setOnClickListener(v -> {
            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    MainActivity.class
            );
            intent.putExtra("isAdmin", true);
            startActivity(intent);
        });

        btnManageUsers.setOnClickListener(v -> {
            DBHelper dbHelper = new DBHelper(this);
            int userCount = dbHelper.getUserCount();
            Toast.makeText(this,
                    "Total registered users: " + userCount,
                    Toast.LENGTH_SHORT).show();
        });

        btnAdminLogout.setOnClickListener(v -> {
            Intent intent = new Intent(
                    AdminDashboardActivity.this,
                    LoginActivity.class
            );
            intent.setFlags(
                    Intent.FLAG_ACTIVITY_NEW_TASK |
                            Intent.FLAG_ACTIVITY_CLEAR_TASK
            );
            startActivity(intent);
            finish();
        });
    }
}
