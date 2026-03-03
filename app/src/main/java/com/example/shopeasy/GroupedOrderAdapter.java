package com.example.shopeasy;

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

    @NonNull
    @Override
    public GroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_group_item, parent, false);
        return new GroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GroupViewHolder holder, int position) {
        OrderGroup group = groupList.get(position);
        holder.tvOrderId.setText("Order ID: " + group.orderGroupId);

        int grandTotal = 0;
        for (OrderEntity item : group.items) {
            grandTotal += (item.price * item.quantity);
        }
        holder.tvGroupTotal.setText("Total: ₹" + grandTotal);

        OrderAdapter childAdapter = new OrderAdapter(group.items);
        holder.childRecyclerView.setLayoutManager(new LinearLayoutManager(context));
        holder.childRecyclerView.setAdapter(childAdapter);

        holder.btnDeleteGroup.setOnClickListener(v -> {
            AppDatabase.getInstance(context).orderDao().deleteOrderGroup(group.orderGroupId);
        });
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }

    public static class GroupViewHolder extends RecyclerView.ViewHolder {
        TextView tvOrderId, tvGroupTotal;
        RecyclerView childRecyclerView;
        ImageButton btnDeleteGroup;

        public GroupViewHolder(@NonNull View itemView) {
            super(itemView);
            tvOrderId = itemView.findViewById(R.id.tvOrderId);
            tvGroupTotal = itemView.findViewById(R.id.tvGroupTotal);
            childRecyclerView = itemView.findViewById(R.id.childRecyclerView);
            btnDeleteGroup = itemView.findViewById(R.id.btnDeleteGroup);
        }
    }
}