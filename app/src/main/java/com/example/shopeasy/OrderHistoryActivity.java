package com.example.shopeasy;

import android.app.AlertDialog;
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
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);
        session = new SessionManager(this);

        recyclerView = findViewById(R.id.recyclerOrders);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Observation logic for grouped history [cite: 1048-1051]
        AppDatabase.getInstance(this).orderDao()
                .getOrdersByUserLive(session.getUserEmail())
                .observe(this, orders -> {
                    if (orders != null) {
                        List<OrderGroup> groupedList = groupOrders(orders);
                        adapter = new GroupedOrderAdapter(groupedList, this);
                        recyclerView.setAdapter(adapter);
                    }
                });

        // Fixed: Delete All Button with confirmation [cite: 1055-1079]
        ImageButton btnClear = findViewById(R.id.btnClearHistory);
        btnClear.setOnClickListener(v -> {
            new AlertDialog.Builder(this)
                    .setTitle("Clear History")
                    .setMessage("Delete all of your order history?")
                    .setPositiveButton("Clear All", (dialog, which) -> {
                        AppDatabase.getInstance(this).orderDao().deleteAllOrdersByUser(session.getUserEmail());
                        Toast.makeText(this, "History Wiped", Toast.LENGTH_SHORT).show();
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });

        findViewById(R.id.btnHome).setOnClickListener(v -> finish());
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