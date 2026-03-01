package com.example.shopeasy;

public class Product {

    public String name;
    public int price;
    public String description;
    public int imageResId;

    public Product(String name, int price, String description, int imageResId) {
        this.name = name;
        this.price = price;
        this.description = description;
        this.imageResId = imageResId;
    }
}
