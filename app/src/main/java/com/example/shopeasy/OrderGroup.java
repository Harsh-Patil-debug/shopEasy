package com.example.shopeasy;

import java.util.List;

public class OrderGroup {
    public long orderGroupId;
    public List<OrderEntity> items;

    public OrderGroup(long orderGroupId, List<OrderEntity> items) {
        this.orderGroupId = orderGroupId;
        this.items = items;
    }
}