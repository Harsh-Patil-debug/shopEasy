package com.example.shopeasy;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.widget.ImageView;
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
        recyclerProducts.setLayoutManager(new LinearLayoutManager(this));

        findViewById(R.id.btnLogout).setOnClickListener(v -> {
            session.logoutUser();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        });

        findViewById(R.id.imgCamera).setOnClickListener(v -> {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivity(intent);
        });

        findViewById(R.id.imgMic).setOnClickListener(v -> {
            Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
            try {
                startActivity(intent);
            } catch (Exception e) {
                Toast.makeText(this, "Speech recognition not supported", Toast.LENGTH_SHORT).show();
            }
        });

        loadProducts();
        adapter = new ProductAdapter(this, productList);
        recyclerProducts.setAdapter(adapter);

        // Only send notification on cold start, not when returning from other activities
        if (savedInstanceState == null) {
            sendNotification("ShopEasy Deals", "Check out the new arrivals for today!");
        }
    }

    private void loadProducts() {
        productList = new ArrayList<>();
        productList.add(new Product("Apple iPhone 14", 79999, "6.1-inch Super Retina XDR display", R.drawable.ic_phone));
        productList.add(new Product("Sony Headphones", 4999, "Noise-cancelling wireless headphones", R.drawable.headphone));
        productList.add(new Product("Amazon Echo Dot", 3499, "Smart speaker with Alexa", R.drawable.echo));
    }

    private void sendNotification(String title, String msg) {
        Intent intent = new Intent(this, NotificationReceiver.class);
        intent.putExtra("TITLE", title);
        intent.putExtra("MESSAGE", msg);
        sendBroadcast(intent);
    }
}