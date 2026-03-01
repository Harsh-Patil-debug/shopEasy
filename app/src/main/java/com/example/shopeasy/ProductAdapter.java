package com.example.shopeasy;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> productList;
    private final Context context;

    public ProductAdapter(Context context, List<Product> productList) {
        this.context = context;
        this.productList = productList;
    }

    public void filterList(List<Product> filteredList) {
        this.productList = filteredList;
        notifyDataSetChanged(); // Refreshes the RecyclerView with the new list
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context)
                .inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {

        Product product = productList.get(position);

        // ✅ Bind data safely
        holder.name.setText(product.name);
        holder.price.setText("₹ " + product.price);
        holder.image.setImageResource(product.imageResId);

        // ✅ Click → Open BuyActivity with FULL data
        holder.itemView.setOnClickListener(v -> {

            Intent intent = new Intent(context, BuyActivity.class);
            intent.putExtra("name", product.name);
            intent.putExtra("price", product.price);
            intent.putExtra("description", product.description);
            intent.putExtra("image", product.imageResId);

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return productList == null ? 0 : productList.size();
    }

    // ✅ ViewHolder
    static class ProductViewHolder extends RecyclerView.ViewHolder {

        TextView name, price;
        ImageView image;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.tvProductName);
            price = itemView.findViewById(R.id.tvProductPrice);
            image = itemView.findViewById(R.id.imgProduct);
        }
    }
}
