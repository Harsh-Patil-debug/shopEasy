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
    int image;
    int unitPrice; // renaming 'price' to 'unitPrice' for clarity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buy);

        // ✅ Connect views
        productImage = findViewById(R.id.imgBuyProduct);
        productName = findViewById(R.id.tvBuyName);
        productPrice = findViewById(R.id.tvBuyPrice);
        productDescription = findViewById(R.id.tvBuyDescription);
        tvQuantity = findViewById(R.id.tvQuantity);
        btnIncrease = findViewById(R.id.btnIncrease);
        btnDecrease = findViewById(R.id.btnDecrease);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        // ✅ Receive data
        name = getIntent().getStringExtra("name");
        unitPrice = getIntent().getIntExtra("price", 0);
        description = getIntent().getStringExtra("description");
        image = getIntent().getIntExtra("image", 0);

        if (description == null) description = "No description available";

        // ✅ Set Initial Data
        productName.setText(name);
        productDescription.setText(description);
        productImage.setImageResource(image);

        // Initial Price Display
        updatePriceAndQuantityUI();

        // 🔥 INCREASE
        btnIncrease.setOnClickListener(v -> {
            quantity++;
            updatePriceAndQuantityUI();
        });

        // 🔥 DECREASE
        btnDecrease.setOnClickListener(v -> {
            if (quantity > 1) {
                quantity--;
                updatePriceAndQuantityUI();
            }
        });

        // 🛒 ADD TO CART (ROOM DATABASE)
        btnAddToCart.setOnClickListener(v -> {
            CartEntity item = new CartEntity();
            item.name = name;

            // Note: We save the unitPrice in the DB.
            // The CartActivity or OrderHistory will handle (unitPrice * quantity)
            item.price = unitPrice;
            item.image = image;
            item.quantity = quantity;

            AppDatabase.getInstance(this)
                    .cartDao()
                    .insert(item);

            Toast.makeText(this,
                    name + " added to cart",
                    Toast.LENGTH_SHORT).show();

            finish(); // Optional: Close activity and return to list
        });
    }

    /**
     * Helper method to update the UI based on current quantity.
     * Hits CO3: Demonstrating interactive UI updates.
     */
    private void updatePriceAndQuantityUI() {
        int totalDisplayPrice = unitPrice * quantity;
        productPrice.setText("Total: ₹ " + totalDisplayPrice);
        tvQuantity.setText(String.valueOf(quantity));
    }
}