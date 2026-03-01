package com.example.shopeasy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.ViewHolder> {

    private List<OrderEntity> orderList;

    public OrderAdapter(List<OrderEntity> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        OrderEntity order = orderList.get(position);

        // ✅ Product name
        holder.tvName.setText(order.name);

        // ✅ Quantity
        holder.tvQty.setText("Qty: " + order.quantity);

        // ✅ Total price
        int total = order.price * order.quantity;
        holder.tvTotal.setText("Total: ₹" + total);

        // ✅ Product image
        holder.imgProduct.setImageResource(order.image);
    }

    @Override
    public int getItemCount() {
        return orderList == null ? 0 : orderList.size();
    }

    // ================= VIEW HOLDER =================

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgProduct;
        TextView tvName, tvQty, tvTotal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            imgProduct = itemView.findViewById(R.id.imgOrderProduct);
            tvName = itemView.findViewById(R.id.tvOrderName);
            tvQty = itemView.findViewById(R.id.tvOrderQty);
            tvTotal = itemView.findViewById(R.id.tvOrderTotal);
        }
    }
}
