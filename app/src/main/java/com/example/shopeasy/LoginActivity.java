package com.example.shopeasy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    SessionManager sessionManager;
    EditText etEmail, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);

        if (sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_login);

        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        Button btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show();
                return;
            }

            UserEntity user = AppDatabase.getInstance(this).userDao().login(email, password);
            if (user == null) {
                UserEntity newUser = new UserEntity();
                newUser.email = email;
                newUser.password = password;
                AppDatabase.getInstance(this).userDao().insertUser(newUser);
                Toast.makeText(this, "New account created", Toast.LENGTH_SHORT).show();
            }

            sessionManager.loginUser(email);
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        });
    }
}