package com.example.shopeasy;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface OrderDao {

    @Insert
    void insert(OrderEntity order);

    @Query("SELECT * FROM orders ORDER BY id DESC")
    LiveData<List<OrderEntity>> getAllOrdersLive();

    @Query("DELETE FROM orders")
    void deleteAllOrders();
}
