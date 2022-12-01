package com.quickap.quickap.activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.quickap.quickap.controller.MenuController;
import com.quickap.quickap.databinding.ActivityConfirmationBinding;

import java.util.Locale;
import java.util.Map;

public class ConfirmationActivity extends AppCompatActivity {

    private ActivityConfirmationBinding binding;
    private int tableId;
    private MenuController menuController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.menuController = extras.getParcelable("MENU_CONTROLLER");
            this.tableId = extras.getInt("tableId");
            Toast.makeText(
                    getApplicationContext(),
                    String.format(Locale.ENGLISH, "%.2f", menuController.getTotalPrice()),
                    Toast.LENGTH_SHORT).show();
            Log.d("CONFIRMATION", getOrderJsonSummary());
        }
        else {
            throw new RuntimeException("Failed to parse extra bundles");
        }

        binding.confirmationTextView.setText(getOrderSummary());
    }

    private String getOrderSummary() {
        StringBuilder orderSummary = new StringBuilder();

        for (MenuController.MenuItem item : this.menuController.getOrderMap().values()) {
            orderSummary.append(
                    String.format(
                            Locale.ENGLISH, "%d X\t%-30s\t= %.2f RMB\n",
                            item.amount, item.name, item.amount  *item.price)
            );
        }
        orderSummary.append(String.format(Locale.ENGLISH, "Total Price: %.2f", this.menuController.getTotalPrice()));
        return orderSummary.toString();
    }


    private String getOrderJsonSummary() {
        return String.format(Locale.ENGLISH, "{\"tableId\":%d,\"foodIDList\":%s}",
                this.tableId,
                this.menuController.getFoodListJson());
    }
}