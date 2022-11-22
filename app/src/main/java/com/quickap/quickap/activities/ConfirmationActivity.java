package com.quickap.quickap.activities;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.quickap.quickap.controller.MenuController;
import com.quickap.quickap.databinding.ActivityConfirmationBinding;

import java.util.Locale;

public class ConfirmationActivity extends AppCompatActivity {

    private ActivityConfirmationBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            MenuController menuController = extras.getParcelable("MENU_CONTROLLER");
            Toast.makeText(
                    getApplicationContext(),
                    String.format(Locale.ENGLISH, "%.2f", menuController.getTotalPrice()),
                    Toast.LENGTH_SHORT).show();
        }

    }
}