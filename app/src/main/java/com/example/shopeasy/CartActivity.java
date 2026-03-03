package com.example.shopeasy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class CartActivity extends AppCompatActivity {
    RecyclerView recyclerCart;
    Button checkout;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);
        session = new SessionManager(this);

        recyclerCart = findViewById(R.id.recyclerCart);
        checkout = findViewById(R.id.btnCheckout);

        recyclerCart.setLayoutManager(new LinearLayoutManager(this));

        List<CartEntity> cartList = AppDatabase.getInstance(this).cartDao().getCartByUser(session.getUserEmail());
        CartAdapter adapter = new CartAdapter(cartList);
        recyclerCart.setAdapter(adapter);

        checkout.setOnClickListener(v -> startActivity(new Intent(this, PaymentActivity.class)));
    }
}