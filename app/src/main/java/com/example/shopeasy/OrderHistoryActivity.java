package com.example.shopeasy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.lifecycle.Observer;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderHistoryActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    OrderAdapter adapter;
    ImageButton btnHome, btnClear; // Declare here
    List<OrderEntity> orders;      // Class-level list

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        // ✅ 1. Initialize Buttons AFTER setContentView
        btnHome = findViewById(R.id.btnHome);
        btnClear = findViewById(R.id.btnClearHistory);

        // ✅ 2. Initialize the Data List and RecyclerView
        recyclerView = findViewById(R.id.recyclerOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Fetch orders and save to the class-level 'orders' variable
        AppDatabase.getInstance(this).orderDao().getAllOrdersLive()
                .observe(this, new Observer<List<OrderEntity>>() {
                    @Override
                    public void onChanged(List<OrderEntity> orderEntities) {
                        if (orderEntities != null) {
                            // Update the adapter with the fresh list from DB
                            adapter = new OrderAdapter(orderEntities);
                            recyclerView.setAdapter(adapter);
                        }
                    }
                });

        // ✅ 3. Home Navigation Logic
        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(OrderHistoryActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });

        // ✅ 4. Clear History Logic (CO4 & CO5)
        btnClear.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Clear History")
                    .setMessage("Are you sure you want to delete all orders?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Delete from DB
                        AppDatabase.getInstance(this).orderDao().deleteAllOrders();

                        // Clear the list used by adapter and refresh
                        orders.clear();
                        adapter.notifyDataSetChanged();

                        Toast.makeText(this, "History Cleared", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });
    }
}