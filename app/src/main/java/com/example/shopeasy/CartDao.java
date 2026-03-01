package com.example.shopeasy;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface CartDao {

    @Insert
    void insert(CartEntity cart);

    @Query("SELECT * FROM cart")
    List<CartEntity> getAll();

    @Query("DELETE FROM cart")
    void clearCart();
}
