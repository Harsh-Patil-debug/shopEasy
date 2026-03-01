package com.example.shopeasy;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.annotation.NonNull;
import android.os.Build;
import android.Manifest;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer; // For the Observer interface
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerProducts;
    ProductAdapter productAdapter;
    List<Product> productList;

    EditText searchEditText;

    // 🔴 Badges and Containers
    FrameLayout cartContainer;
    TextView tvCartCount;
    TextView tvNotificationCount;
    ImageView notificationButton;

    ImageView logoOrderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // ================== INIT VIEWS ==================
        recyclerProducts = findViewById(R.id.recyclerProducts);
        cartContainer = findViewById(R.id.cartContainer);
        tvCartCount = findViewById(R.id.tvCartCount);
        tvNotificationCount = findViewById(R.id.tvNotificationCount);
        notificationButton = findViewById(R.id.notificationButton);
        logoOrderHistory = findViewById(R.id.logoOrderHistory);
        searchEditText = findViewById(R.id.searchEditText);

        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));

        // ================== SEARCH FILTER LOGIC ==================
        // This listens to every character typed in the search bar
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString()); // ✅ Call the filter method
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // ================== REAL-TIME OBSERVERS (CO4) ==================
        // This listens to the database and updates the red badge INSTANTLY
        AppDatabase.getInstance(this).notificationDao().getAllNotificationsLive()
                .observe(this, new Observer<List<NotificationEntity>>() {
                    @Override
                    public void onChanged(List<NotificationEntity> notifications) {
                        if (notifications != null && !notifications.isEmpty()) {
                            tvNotificationCount.setText(String.valueOf(notifications.size()));
                            tvNotificationCount.setVisibility(android.view.View.VISIBLE);
                        } else {
                            tvNotificationCount.setVisibility(android.view.View.GONE);
                        }
                    }
                });

        // ================== CLICK LISTENERS ==================
        cartContainer.setOnClickListener(v ->
                startActivity(new Intent(MainActivity.this, CartActivity.class))
        );

        notificationButton.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, NotificationActivity.class));
            // Trigger the permission check/alarm when they check notifications
            checkAndScheduleNotifications();
        });

        logoOrderHistory.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, OrderHistoryActivity.class);
            startActivity(intent);
        });

        // ================== PRODUCT LIST (CO3) ==================
        loadProducts();
        productAdapter = new ProductAdapter(this, productList);
        recyclerProducts.setAdapter(productAdapter);
    }

    private void loadProducts() {
        productList = new ArrayList<>();
        productList.add(new Product("Apple iPhone 14", 79999, "6.1-inch Super Retina XDR display...", R.drawable.ic_phone));
        productList.add(new Product("Samsung Galaxy S23", 69999, "Dynamic AMOLED display, Snapdragon 8 Gen 2...", R.drawable.s));
        productList.add(new Product("Sony Headphones", 4999, "Noise-cancelling over-ear headphones...", R.drawable.headphone));
        productList.add(new Product("Amazon Echo Dot", 3499, "Smart speaker with Alexa...", R.drawable.echo));
        productList.add(new Product("Apple Watch Series 9", 39999, "Always-On Retina display...", R.drawable.apple_wtch));
    }

    private void filter(String text) {
        List<Product> filteredList = new ArrayList<>();
        for (Product item : productList) {
            // Check if name contains the search text (case-insensitive)
            if (item.name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        productAdapter.filterList(filteredList);
    }

    // ================== NOTIFICATION LOGIC (CO1 & CO5) ==================
    private void checkAndScheduleNotifications() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS)
                    == PackageManager.PERMISSION_GRANTED) {
                scheduleNotifications();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        } else {
            scheduleNotifications();
        }
    }

    private void scheduleNotifications() {
        Intent intent = new Intent(this, NotificationReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            long triggerTime = System.currentTimeMillis() + (5 * 1000); // 5-second test trigger

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            } else {
                alarmManager.set(AlarmManager.RTC_WAKEUP, triggerTime, pendingIntent);
            }
            Toast.makeText(this, "Deals starting in 5 seconds!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 101 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            scheduleNotifications();
        }
    }

    // ================== LIFECYCLE REFRESH ==================
    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
        // Note: Notification badge is handled by the LiveData Observer in onCreate!
    }

    private void updateCartBadge() {
        int count = AppDatabase.getInstance(this).cartDao().getAll().size();
        tvCartCount.setText(String.valueOf(count));
        tvCartCount.setVisibility(count == 0 ? android.view.View.GONE : android.view.View.VISIBLE);
    }
}