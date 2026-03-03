package com.example.shopeasy;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
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
    TextView tvCartCount, tvNotificationCount;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(this);
        setContentView(R.layout.activity_main);

        tvCartCount = findViewById(R.id.tvCartCount);
        tvNotificationCount = findViewById(R.id.tvNotificationCount);

        // Fixed: Microphone Button [cite: 451-519]
        findViewById(R.id.imgMic).setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Voice search not supported on this device", Toast.LENGTH_SHORT).show();
            }
        });

        // Search Bar TextWatcher [cite: 502-518]
        EditText searchBar = findViewById(R.id.searchEditText);
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                filter(s.toString());
            }
            @Override public void afterTextChanged(Editable s) {}
        });

        // Real-time Notification Observer [cite: 521-535]
        AppDatabase.getInstance(this).notificationDao()
                .getNotificationsByUserLive(session.getUserEmail())
                .observe(this, list -> {
                    int count = (list != null) ? list.size() : 0;
                    tvNotificationCount.setText(String.valueOf(count));
                    tvNotificationCount.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
                });

        loadProducts(); // [cite: 568-574]
        recyclerProducts = findViewById(R.id.recyclerProducts);
        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ProductAdapter(this, productList);
        recyclerProducts.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge(); // [cite: 646-649]
    }

    private void updateCartBadge() {
        int count = AppDatabase.getInstance(this).cartDao().getCartByUser(session.getUserEmail()).size();
        tvCartCount.setText(String.valueOf(count));
        // Fixed: Only show badge if there are items [cite: 653-660]
        tvCartCount.setVisibility(count > 0 ? View.VISIBLE : View.GONE);
    }

    private void filter(String text) {
        List<Product> filtered = new ArrayList<>();
        for (Product p : productList) {
            if (p.name.toLowerCase().contains(text.toLowerCase())) filtered.add(p);
        }
        adapter.filterList(filtered); // [cite: 586]
    }

    private void loadProducts() {
        productList = new ArrayList<>();
        productList.add(new Product("Apple iPhone 14", 79999, "6.1-inch Super Retina XDR display...", R.drawable.ic_phone));
        productList.add(new Product("Samsung Galaxy S23", 69999, "Dynamic AMOLED display...", R.drawable.s));
        productList.add(new Product("Sony Headphones", 4999, "Noise-cancelling headphones...", R.drawable.headphone));
        productList.add(new Product("Amazon Echo Dot", 3499, "Smart speaker with Alexa...", R.drawable.echo));
        productList.add(new Product("Apple Watch Series 9", 39999, "Always-On Retina display...", R.drawable.apple_wtch));
    }
}