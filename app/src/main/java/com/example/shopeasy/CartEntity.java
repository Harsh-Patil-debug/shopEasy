package com.example.shopeasy;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "cart")
public class CartEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String userEmail;
    public String name;
    public int price;
    public int quantity;
    public int image;
}