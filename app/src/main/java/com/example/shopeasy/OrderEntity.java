package com.example.shopeasy;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "orders")
public class OrderEntity {
    @PrimaryKey(autoGenerate = true)
    public int id;
    public String userEmail;
    public long orderGroupId;
    public String name;
    public int price;
    public int quantity;
    public int image;
}