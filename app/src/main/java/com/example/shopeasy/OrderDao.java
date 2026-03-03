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

    @Query("SELECT * FROM orders WHERE userEmail = :email ORDER BY orderGroupId DESC")
    LiveData<List<OrderEntity>> getOrdersByUserLive(String email);

    @Query("DELETE FROM orders WHERE orderGroupId = :orderGroupId")
    void deleteOrderGroup(long orderGroupId);

    @Query("DELETE FROM orders WHERE userEmail = :email")
    void deleteAllOrdersByUser(String email);
}