package com.example.lostnfound;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class RegisterActivity extends AppCompatActivity {

    private EditText etName, etEmail, etPhone;
    private EditText etPassword, etConfirmPassword;
    private DBHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etName = findViewById(R.id.etRegisterName);
        etEmail = findViewById(R.id.etRegisterEmail);
        etPhone = findViewById(R.id.etRegisterPhone);
        etPassword = findViewById(R.id.etRegisterPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);

        Button btnRegister = findViewById(R.id.btnRegister);
        Button btnGoLogin = findViewById(R.id.btnGoLogin);

        dbHelper = new DBHelper(this);

        btnRegister.setOnClickListener(v -> {

            String name = etName.getText().toString().trim();
            String email = etEmail.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            String password = etPassword.getText().toString();
            String confirmPassword = etConfirmPassword.getText().toString();

            if (name.isEmpty() || email.isEmpty()
                    || phone.isEmpty() || password.isEmpty()
                    || confirmPassword.isEmpty()) {

                Toast.makeText(this,
                        "Please fill all fields",
                        Toast.LENGTH_SHORT).show();

            } else if (!Objects.equals(password, confirmPassword)) {

                Toast.makeText(this,
                        "Passwords do not match",
                        Toast.LENGTH_SHORT).show();

            } else {

                boolean registered = dbHelper.registerUser(
                        name, email, phone, password
                );

                if (registered) {

                    Toast.makeText(this,
                            "Registration successful",
                            Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(
                            RegisterActivity.this,
                            LoginActivity.class
                    );

                    startActivity(intent);
                    finish();

                } else {

                    Toast.makeText(this,
                            "Registration failed. Email may already exist.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnGoLogin.setOnClickListener(v -> {
            Intent intent = new Intent(
                    RegisterActivity.this,
                    LoginActivity.class
            );

            startActivity(intent);
            finish();
        });
    }
}
