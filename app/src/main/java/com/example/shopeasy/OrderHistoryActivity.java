package com.example.shopeasy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class OrderHistoryActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    GroupedOrderAdapter adapter;
    ImageButton btnHome, btnClear;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        session = new SessionManager(this);

        btnHome = findViewById(R.id.btnHome);
        btnClear = findViewById(R.id.btnClearHistory);
        recyclerView = findViewById(R.id.recyclerOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        AppDatabase.getInstance(this).orderDao().getOrdersByUserLive(session.getUserEmail())
                .observe(this, orderEntities -> {
                    if (orderEntities != null) {
                        List<OrderGroup> groupedOrders = groupOrders(orderEntities);
                        adapter = new GroupedOrderAdapter(groupedOrders, this);
                        recyclerView.setAdapter(adapter);
                    }
                });

        btnClear.setOnClickListener(v -> {
            new android.app.AlertDialog.Builder(this)
                    .setTitle("Clear History")
                    .setMessage("Are you sure you want to delete all orders?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        AppDatabase.getInstance(this).orderDao().deleteAllOrdersByUser(session.getUserEmail());
                        Toast.makeText(this, "History Cleared", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("No", null)
                    .show();
        });

        btnHome.setOnClickListener(v -> {
            Intent intent = new Intent(OrderHistoryActivity.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        });
    }

    private List<OrderGroup> groupOrders(List<OrderEntity> orders) {
        Map<Long, List<OrderEntity>> map = new LinkedHashMap<>();
        for (OrderEntity order : orders) {
            if (!map.containsKey(order.orderGroupId)) {
                map.put(order.orderGroupId, new ArrayList<>());
            }
            map.get(order.orderGroupId).add(order);
        }
        List<OrderGroup> result = new ArrayList<>();
        for (Map.Entry<Long, List<OrderEntity>> entry : map.entrySet()) {
            result.add(new OrderGroup(entry.getKey(), entry.getValue()));
        }
        return result;
    }
}