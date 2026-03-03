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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder> {
    private List<Product> list;
    private Context context;

    public ProductAdapter(Context context, List<Product> list) {
        this.context = context;
        this.list = list;
    }

    public void filterList(List<Product> filteredList) {
        this.list = filteredList;
        notifyDataSetChanged();
    }

    @NonNull @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_product, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Product p = list.get(position);
        holder.title.setText(p.name);
        holder.price.setText("₹" + p.price);
        holder.img.setImageResource(p.imageResId);

        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, BuyActivity.class);
            intent.putExtra("name", p.name);
            intent.putExtra("price", p.price);
            intent.putExtra("description", p.description);
            intent.putExtra("image", p.imageResId);
            context.startActivity(intent);
        });
    }

    @Override public int getItemCount() { return list.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView title, price;
        ImageView img;
        public ViewHolder(View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.txtTitle);
            price = itemView.findViewById(R.id.txtPrice);
            img = itemView.findViewById(R.id.imgProduct);
        }
    }
}