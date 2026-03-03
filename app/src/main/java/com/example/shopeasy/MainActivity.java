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
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerProducts;
    ProductAdapter productAdapter;
    List<Product> productList;
    EditText searchEditText;
    FrameLayout cartContainer;
    TextView tvCartCount, tvNotificationCount;
    ImageView notificationButton, logoOrderHistory, imgCamera, imgMic, btnLogout;
    SessionManager sessionManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sessionManager = new SessionManager(this);

        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        setContentView(R.layout.activity_main);

        checkNotificationPermission();

        recyclerProducts = findViewById(R.id.recyclerProducts);
        cartContainer = findViewById(R.id.cartContainer);
        tvCartCount = findViewById(R.id.tvCartCount);
        tvNotificationCount = findViewById(R.id.tvNotificationCount);
        notificationButton = findViewById(R.id.notificationButton);
        logoOrderHistory = findViewById(R.id.logoOrderHistory);
        searchEditText = findViewById(R.id.searchEditText);
        imgCamera = findViewById(R.id.imgCamera);
        imgMic = findViewById(R.id.imgMic);
        btnLogout = findViewById(R.id.btnLogout);

        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        AppDatabase.getInstance(this).notificationDao()
                .getNotificationsByUserLive(sessionManager.getUserEmail())
                .observe(this, notifications -> {
                    if (notifications != null && !notifications.isEmpty()) {
                        tvNotificationCount.setText(String.valueOf(notifications.size()));
                        tvNotificationCount.setVisibility(android.view.View.VISIBLE);
                    } else {
                        tvNotificationCount.setVisibility(android.view.View.GONE);
                    }
                });

        cartContainer.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
        notificationButton.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, NotificationActivity.class)));
        logoOrderHistory.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, OrderHistoryActivity.class)));

        btnLogout.setOnClickListener(v -> {
            sessionManager.logoutUser();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
            finish();
        });

        imgCamera.setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "Camera not available", Toast.LENGTH_SHORT).show();
            }
        });

        imgMic.setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            if (intent.resolveActivity(getPackageManager()) != null) {
                startActivity(intent);
            } else {
                Toast.makeText(this, "Microphone not available", Toast.LENGTH_SHORT).show();
            }
        });

        loadProducts();
        productAdapter = new ProductAdapter(this, productList);
        recyclerProducts.setAdapter(productAdapter);

        sendAppLifecycleNotification("Welcome Back!", "Check out the latest deals today.");
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
            if (item.name.toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        productAdapter.filterList(filteredList);
    }

    private void checkNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }
    }

    private void sendAppLifecycleNotification(String title, String msg) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("TITLE", title);
        intent.putExtra("MESSAGE", msg);
        sendBroadcast(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
    }

    @Override
    protected void onStop() {
        super.onStop();
        sendAppLifecycleNotification("We miss you!", "Come back soon for more shopping deals.");
    }

    private void updateCartBadge() {
        int count = AppDatabase.getInstance(this).cartDao().getCartByUser(sessionManager.getUserEmail()).size();
        tvCartCount.setText(String.valueOf(count));
        tvCartCount.setVisibility(count == 0 ? android.view.View.GONE : android.view.View.VISIBLE);
    }
}