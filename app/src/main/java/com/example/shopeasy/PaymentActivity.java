package com.example.shopeasy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.List;

public class PaymentActivity extends AppCompatActivity {
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        session = new SessionManager(this);

        Button pay = findViewById(R.id.btnPay);

        pay.setOnClickListener(v -> {
            String email = session.getUserEmail();
            List<CartEntity> cart = AppDatabase.getInstance(this).cartDao().getCartByUser(email);

            if (cart.isEmpty()) {
                Toast.makeText(this, "Cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            long groupId = System.currentTimeMillis();

            for (CartEntity c : cart) {
                OrderEntity order = new OrderEntity();
                order.userEmail = email;
                order.orderGroupId = groupId;
                order.name = c.name;
                order.price = c.price;
                order.quantity = c.quantity;
                order.image = c.image;
                AppDatabase.getInstance(this).orderDao().insert(order);
            }

            AppDatabase.getInstance(this).cartDao().clearCartByUser(email);
            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, OrderHistoryActivity.class));
            finish();
        });
    }
}