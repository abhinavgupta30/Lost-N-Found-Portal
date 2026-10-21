package com.example.lostnfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnGoRegister = findViewById(R.id.btnGoRegister);

        DBHelper dbHelper = new DBHelper(this);


        dbHelper.createAdminAccount();

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this,
                        "Please enter email and password",
                        Toast.LENGTH_SHORT).show();
                return;
            }

            String role = dbHelper.getUserRole(email, password);

            if ("admin".equals(role)) {
                Toast.makeText(this, "Admin login successful",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(
                        LoginActivity.this,
                        AdminDashboardActivity.class
                );
                startActivity(intent);
                finish();

            } else if ("user".equals(role)) {
                Toast.makeText(this, "Login successful",
                        Toast.LENGTH_SHORT).show();

                Intent intent = new Intent(
                        LoginActivity.this,
                        MainActivity.class
                );
                startActivity(intent);
                finish();

            } else {
                Toast.makeText(this, "Invalid email or password",
                        Toast.LENGTH_SHORT).show();
            }
        });

        btnGoRegister.setOnClickListener(v -> {
            Intent intent = new Intent(
                    LoginActivity.this,
                    RegisterActivity.class
            );
            startActivity(intent);
        });
    }
}
