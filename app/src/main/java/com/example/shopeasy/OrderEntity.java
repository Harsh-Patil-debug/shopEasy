package com.example.shopeasy;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class OrderEntity {

    @PrimaryKey(autoGenerate = true)
    public int id;

    public String name;

    // 🔥 MUST be int for calculation
    public int price;

    public int quantity;

    // 🔥 for image display
    public int image;
}
