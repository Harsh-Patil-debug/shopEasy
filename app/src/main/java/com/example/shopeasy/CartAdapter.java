package com.example.shopeasy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    List<CartEntity> cartList;

    public CartAdapter(List<CartEntity> cartList) {
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {

        CartEntity item = cartList.get(position);

        holder.name.setText(item.name);
        int totalPriceForItem = item.price * item.quantity;

        holder.price.setText("₹ " + totalPriceForItem);
        holder.qty.setText("Qty: " + item.quantity);
        holder.image.setImageResource(item.image);
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder {

        ImageView image;
        TextView name, price, qty;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.imgCart);
            name = itemView.findViewById(R.id.tvCartName);
            price = itemView.findViewById(R.id.tvCartPrice);
            qty = itemView.findViewById(R.id.tvCartQty);
        }
    }
}
