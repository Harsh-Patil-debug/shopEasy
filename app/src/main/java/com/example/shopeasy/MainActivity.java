package com.example.shopeasy;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    RecyclerView recyclerProducts;
    ProductAdapter adapter;
    List<Product> productList;
    EditText searchEditText;
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

        recyclerProducts = findViewById(R.id.recyclerProducts);
        searchEditText = findViewById(R.id.searchEditText);
        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));

        // Setup Search Logic [cite: 502, 509]
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Navigation [cite: 541, 544, 553]
        findViewById(R.id.cartContainer).setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
        findViewById(R.id.notificationButton).setOnClickListener(v -> startActivity(new Intent(this, NotificationActivity.class)));
        findViewById(R.id.logoOrderHistory).setOnClickListener(v -> startActivity(new Intent(this, OrderHistoryActivity.class)));
        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            session.logoutUser();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        // Hardware [cite: 106, 144, 1663]
        findViewById(R.id.imgCamera).setOnClickListener(v -> startActivity(new Intent(MediaStore.ACTION_IMAGE_CAPTURE)));
        findViewById(R.id.imgMic).setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            startActivity(intent);
        });

        loadProducts(); // Restoring all your products [cite: 568]
        adapter = new ProductAdapter(this, productList);
        recyclerProducts.setAdapter(adapter);

        if (savedInstanceState == null) {
            sendNotification("Welcome to ShopEasy", "Explore our latest collection!");
        }
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
        adapter.filterList(filteredList);
    }

    private void sendNotification(String title, String msg) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("TITLE", title);
        intent.putExtra("MESSAGE", msg);
        sendBroadcast(intent);
    }
}