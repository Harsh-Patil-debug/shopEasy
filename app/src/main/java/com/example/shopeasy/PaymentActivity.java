package com.example.shopeasy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class PaymentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);

        Button pay = findViewById(R.id.btnPay);

        pay.setOnClickListener(v -> {

            List<CartEntity> cart =
                    AppDatabase.getInstance(this).cartDao().getAll();

            for (CartEntity c : cart) {
                OrderEntity order = new OrderEntity();
                order.name = c.name;

                // 🔥 if Cart price is String
                order.price = c.price;

                order.quantity = c.quantity;
                order.image = c.image;

                AppDatabase.getInstance(this).orderDao().insert(order);
            }

            AppDatabase.getInstance(this).cartDao().clearCart();

            Toast.makeText(this, "Payment Successful", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, OrderHistoryActivity.class));
        });
    }
}
