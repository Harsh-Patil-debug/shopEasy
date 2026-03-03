package com.example.shopeasy;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GroupedOrderAdapter extends RecyclerView.Adapter<GroupedOrderAdapter.GroupViewHolder> {
    private List<OrderGroup> groupList;
    private Context context;

    public GroupedOrderAdapter(List<OrderGroup> groupList, Context context) {
        this.groupList = groupList;
        this.context = context;
    }

    @NonNull @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.order_group_item, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        OrderGroup group = groupList.get(position);
        holder.tvOrderId.setText("Order ID: " + group.orderGroupId);

        // Setup the nested RecyclerView for items in THIS specific order [cite: 914, 944]
        OrderAdapter childAdapter = new OrderAdapter(group.items);
        holder.childRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.childRecyclerView.setAdapter(childAdapter);

        // Confirmation warning before deleting [cite: 1056-1059]
        holder.btnDeleteGroup.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Order")
                    .setMessage("Remove this order from your history?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        AppDatabase.getInstance(context).orderDao().deleteOrderGroup(group.orderGroupId);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override public int getItemCount() { return groupList.size(); }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId;
        RecyclerView childRecyclerView;
        ImageButton btnDeleteGroup;

        public GroupViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            childRecyclerView = itemView.findViewById(R.id.childRecyclerView);
            btnDeleteGroup = itemView.findViewById(R.id.btnDeleteGroup);
        }
    }
}