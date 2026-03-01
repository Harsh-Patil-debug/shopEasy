package com.example.shopeasy;

import java.util.ArrayList;
import java.util.List;

public class CartManager {

    private static final List<CartItem> cartList = new ArrayList<>();

    public static void addToCart(Product product, int quantity) {

        for (CartItem item : cartList) {
            if (item.name.equals(product.name)) {
                item.quantity += quantity;
                return;
            }
        }

        cartList.add(new CartItem(
                product.name,
                product.price,
                product.imageResId,
                quantity
        ));
    }

    public static List<CartItem> getCartItems() {
        return cartList;
    }

    public static int getCartCount() {
        int count = 0;
        for (CartItem item : cartList) {
            count += item.quantity;
        }
        return count;
    }

    public static boolean isCartEmpty() {
        return cartList.isEmpty();
    }

    public static void clearCart() {
        cartList.clear();
    }
}
