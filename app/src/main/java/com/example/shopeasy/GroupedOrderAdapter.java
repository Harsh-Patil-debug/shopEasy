package com.example.shopeasy;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class GroupedOrderAdapter extends RecyclerView.Adapter<GroupedOrderAdapter.GroupViewHolder> {
    private List<OrderGroup> groupList;
    private Context context;

    public GroupedOrderAdapter(List<OrderGroup> groupList, Context context) {
        this.groupList = groupList;
        this.context = context;
    }

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GroupViewHolder(LayoutInflater.from(context).inflate(R.layout.order_group_item, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        OrderGroup group = groupList.get(position);
        holder.tvOrderId.setText("Order ID: " + group.orderGroupId);

        holder.btnDeleteGroup.setOnClickListener(v -> {
            new AlertDialog.Builder(context)
                    .setTitle("Delete Order")
                    .setMessage("Remove this order from history?")
                    .setPositiveButton("Delete", (dialog, which) -> {
                        AppDatabase.getInstance(context).orderDao().deleteOrderGroup(group.orderGroupId);
                    })
                    .setNegativeButton("Cancel", null)
                    .show();
        });
    }

    @Override
    public int getItemCount() { return groupList.size(); }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId;
        ImageButton btnDeleteGroup;
        public GroupViewHolder(View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            btnDeleteGroup = itemView.findViewById(R.id.btnDeleteGroup);
        }
    }
}