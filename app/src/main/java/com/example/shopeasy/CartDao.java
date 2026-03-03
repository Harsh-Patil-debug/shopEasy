package com.example.shopeasy;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import java.util.List;

@Dao
public interface CartDao {
    @Insert
    void insert(CartEntity cart);

    @Query("SELECT * FROM cart WHERE userEmail = :email")
    List<CartEntity> getCartByUser(String email);

    @Query("DELETE FROM cart WHERE userEmail = :email")
    void clearCartByUser(String email);
}