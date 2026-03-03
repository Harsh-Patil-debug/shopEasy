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
            String currentUserEmail = session.getUserEmail();
            AppDatabase db = AppDatabase.getInstance(this);

            // Get the cart items for the specific user [cite: 1113-1114]
            List<CartEntity> cartItems = db.cartDao().getCartByUser(currentUserEmail);

            if (cartItems.isEmpty()) {
                Toast.makeText(this, "Your cart is empty", Toast.LENGTH_SHORT).show();
                return;
            }

            // Create a single unique ID for this entire purchase [cite: 1116]
            long groupId = System.currentTimeMillis();

            for (CartEntity item : cartItems) {
                OrderEntity order = new OrderEntity();
                order.userEmail = currentUserEmail; // Tagging the user
                order.orderGroupId = groupId;       // Grouping items together
                order.name = item.name;
                order.price = item.price;
                order.quantity = item.quantity;
                order.image = item.image;
                db.orderDao().insert(order);
            }

            // Clear ONLY this user's cart after successful purchase [cite: 1123]
            db.cartDao().clearCartByUser(currentUserEmail);

            Toast.makeText(this, "Payment Successful!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, OrderHistoryActivity.class));
            finish();
        });
    }
}