package com.example.shopeasy;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart")
public class CartEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    // ✅ keep price as int for math
    public int price;

    public int quantity;

    // ✅ match OrderEntity
    public int image;
}
