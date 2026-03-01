package com.example.shopeasy;

import android.os.Bundle;
import android.widget.ImageButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.lifecycle.Observer;
import java.util.List;

public class NotificationActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    NotificationAdapter adapter;
    ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        // 1. Setup Back Button
        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        // 2. Setup RecyclerView
        recyclerView = findViewById(R.id.recyclerNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // 3. Fetch from Room Database (CO4)
        AppDatabase.getInstance(this).notificationDao().getAllNotificationsLive()
                .observe(this, new Observer<List<NotificationEntity>>() {
                    @Override
                    public void onChanged(List<NotificationEntity> notifications) {
                        if (notifications != null) {
                            // 4. Bind to Adapter
                            // This updates the list automatically whenever the DB changes
                            adapter = new NotificationAdapter(notifications);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });
    }
}