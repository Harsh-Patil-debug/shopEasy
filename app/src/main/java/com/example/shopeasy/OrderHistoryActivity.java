package com.example.shopeasy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
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

        // Observe the database for changes [cite: 1050-1051]
        AppDatabase.getInstance(this).orderDao()
                .getOrdersByUserLive(session.getUserEmail())
                .observe(this, orderEntities -> {
                    if (orderEntities != null) {
                        // Convert the flat list into grouped orders [cite: 1070]
                        List<OrderGroup> groupedList = groupOrders(orderEntities);
                        adapter = new GroupedOrderAdapter(groupedList, this);
                        recyclerView.setAdapter(adapter);
                    }
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