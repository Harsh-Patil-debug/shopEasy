package com.example.shopeasy;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText etEmail, etPassword;
    SessionManager sessionManager;

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
            String pass = etPassword.getText().toString().trim();

            if (TextUtils.isEmpty(email) || TextUtils.isEmpty(pass)) {
                Toast.makeText(this, "Fields cannot be empty", Toast.LENGTH_SHORT).show();
                return;
            }

            UserDao dao = AppDatabase.getInstance(this).userDao();
            UserEntity existingUser = dao.getUserByEmail(email);

            if (existingUser == null) {
                UserEntity newUser = new UserEntity();
                newUser.email = email;
                newUser.password = pass;
                dao.insertUser(newUser);
                Toast.makeText(this, "New account created", Toast.LENGTH_SHORT).show();
                proceed(email);
            } else {
                if (existingUser.password.equals(pass)) {
                    proceed(email);
                } else {
                    Toast.makeText(this, "Incorrect password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void proceed(String email) {
        sessionManager.loginUser(email);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}