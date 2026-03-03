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
                Toast.makeText(this, "Empty fields", Toast.LENGTH_SHORT).show();
                return;
            }

            UserDao dao = AppDatabase.getInstance(this).userDao();
            UserEntity user = dao.getUserByEmail(email);

            if (user == null) {
                UserEntity newUser = new UserEntity();
                newUser.email = email;
                newUser.password = pass;
                dao.insertUser(newUser);
                Toast.makeText(this, "New Account Created", Toast.LENGTH_SHORT).show();
                loginAndProceed(email);
            } else if (user.password.equals(pass)) {
                loginAndProceed(email);
            } else {
                Toast.makeText(this, "Invalid Password", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loginAndProceed(String email) {
        sessionManager.loginUser(email);
        startActivity(new Intent(this, MainActivity.class));
        finish();
    }
}