package com.example.shopeasy;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class BuyActivity extends AppCompatActivity {
    ImageView productImage;
    TextView productName, productPrice, productDescription, tvQuantity;
    ImageButton btnIncrease, btnDecrease;
    Button btnAddToCart;
    int quantity = 1;
    String name, description;
    int image, unitPrice;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);
        session = new SessionManager(this);

        productImage = findViewById(R.id.imgBuyProduct);
        productName = findViewById(R.id.tvBuyName);
        productPrice = findViewById(R.id.tvBuyPrice);
        productDescription = findViewById(R.id.tvBuyDescription);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        name = getIntent().getStringExtra("name");
        unitPrice = getIntent().getIntExtra("price", 0);
        description = getIntent().getStringExtra("description");
        image = getIntent().getIntExtra("image", 0);

        if (description == null) description = "No description available";

        productName.setText(name);
        productDescription.setText(description);
        productImage.setImageResource(image);

        updatePriceAndQuantityUI();

        btnIncrease.setOnClickListener(v -> {
            quantity++;
            updatePriceAndQuantityUI();
        });

        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updatePriceAndQuantityUI();
            }
        });

        btnAddToCart.setOnClickListener(v -> {
            CartEntity item = new CartEntity();
            item.userEmail = session.getUserEmail();
            item.name = name;
            item.price = unitPrice;
            item.image = image;
            item.quantity = quantity;

            AppDatabase.getInstance(this).cartDao().insert(item);
            Toast.makeText(this, name + " added to cart", Toast.LENGTH_SHORT).show();
            finish();
        });
    }

    private void updatePriceAndQuantityUI() {
        int totalDisplayPrice = unitPrice * quantity;
        productPrice.setText("Total: ₹" + totalDisplayPrice);
        tvQuantity.setText(String.valueOf(quantity));
    }
}