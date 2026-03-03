package com.example.shopeasy;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerProducts;
    ProductAdapter adapter;
    List<Product> productList;
    TextView tvCartCount, tvNotificationCount;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(this);

        if (!session.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        tvCartCount = findViewById(R.id.tvCartCount);
        tvNotificationCount = findViewById(R.id.tvNotificationCount);
        recyclerProducts = findViewById(R.id.recyclerProducts);
        EditText searchBar = findViewById(R.id.searchEditText);

        // Functional Navigation Listeners [cite: 541-558]
        findViewById(R.id.cartContainer).setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        findViewById(R.id.notificationButton).setOnClickListener(v -> startActivity(new Intent(this, NotificationActivity.class)));
        findViewById(R.id.logoOrderHistory).setOnClickListener(v -> startActivity(new Intent(this, OrderHistoryActivity.class)));

        // Logout with Notification
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            sendNotification("Session Ended", "You have successfully logged out of ShopEasy.");
            session.logoutUser();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Fixed Hardware Integration
        findViewById(R.id.imgCamera).setOnClickListener(v -> startActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)));
        findViewById(R.id.imgMic).setOnClickListener(v -> startVoiceSearch());

        checkNotificationPermission();

        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        loadProducts(); // Restored all your original products
        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, productList);
        recyclerProducts.setAdapter(adapter);

        // Notification Badge Observer [cite: 521-535]
        AppDatabase.getInstance(this).notificationDao()
                .getNotificationsByUserLive(session.getUserEmail())
                .observe(this, list -> {
                    int count = (list != null) ? list.size() : 0;
                    tvNotificationCount.setText(String.valueOf(count));
                    tvNotificationCount.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
                });

        // App Entry Notification
        if (savedInstanceState == null) {
            sendNotification("App Entered", "Welcome back! Start exploring the best deals today.");
        }
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    private void startVoiceSearch() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        try {
            startActivity(intent);
        } catch (Exception e) {
            Toast.makeText(this, "Voice recognition not available", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
    }

    private void updateCartBadge() {
        int count = AppDatabase.getInstance(this).cartDao().getCartByUser(session.getUserEmail()).size();
        tvCartCount.setText(String.valueOf(count));
        tvCartCount.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }

    private void filter(String text) {
        List<Product> filtered = new ArrayList<>();
        for (Product p : productList) {
            if (p.name.toLowerCase().contains(text.toLowerCase())) filtered.add(p);
        }
        adapter.filterList(filtered);
    }

    private void loadProducts() {
        productList = new ArrayList<>();
        productList.add(new Product("Apple iPhone 14", 79999, "6.1-inch display", R.drawable.ic_phone));
        productList.add(new Product("Samsung Galaxy S23", 69999, "AMOLED display", R.drawable.s));
        productList.add(new Product("Sony Headphones", 4999, "Noise-cancelling", R.drawable.headphone));
        productList.add(new Product("Amazon Echo Dot", 3499, "Smart speaker", R.drawable.echo));
        productList.add(new Product("Apple Watch Series 9", 39999, "Retina display", R.drawable.apple_wtch));
    }

    private void sendNotification(String title, String msg) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("TITLE", title);
        intent.putExtra("MESSAGE", msg);
        sendBroadcast(intent);
    }
}