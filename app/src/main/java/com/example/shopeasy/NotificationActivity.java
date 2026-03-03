package com.example.shopeasy;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class NotificationActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    NotificationAdapter adapter;
    ImageButton btnBack;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);
        session = new SessionManager(this);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase.getInstance(this).notificationDao().getNotificationsByUserLive(session.getUserEmail())
                .observe(this, notifications -> {
                    if (notifications != null) {
                        adapter = new NotificationAdapter(notifications);
                        recyclerView.setAdapter(adapter);
                    }
                });
    }
}