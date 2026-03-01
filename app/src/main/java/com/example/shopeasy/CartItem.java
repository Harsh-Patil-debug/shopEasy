package com.example.shopeasy;

public class CartItem {

    public String name;
    public int price;
    public int imageResId;
    public int quantity;

    public CartItem(String name, int price, int imageResId, int quantity) {
        this.name = name;
        this.price = price;
        this.imageResId = imageResId;
        this.quantity = quantity;
    }
}
