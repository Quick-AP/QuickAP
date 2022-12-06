package com.quickap.quickap.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.quickap.quickap.controller.MenuController;
import com.quickap.quickap.databinding.ActivityMenuBinding;
import com.quickap.quickap.design.MenuGridViewAdapter;
import com.quickap.quickap.model.FoodArrayListModel;
import com.quickap.quickap.utils.RegisterTableThread;
import com.quickap.quickap.utils.Settings;

import org.json.JSONException;
import org.json.JSONObject;

public class MenuActivity extends AppCompatActivity {

    private ActivityMenuBinding menuBinding;
    private MenuController menuController;

    private int tableID;
    private String phoneNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.menuController = new MenuController();

        // Establish UI Bindings
        this.menuBinding = ActivityMenuBinding.inflate(getLayoutInflater());
        View menuView = this.menuBinding.getRoot();
        setContentView(menuView);

        Bundle extras = getIntent().getExtras();
        this.tableID = extras.getInt("tableId");
        this.phoneNumber = extras.getString("phoneNumber");
        Log.d("DEBUG: tableId passing", String.valueOf(this.tableID));
        Log.d("DEBUG: phoneNumber passing", String.valueOf(this.phoneNumber));

        // TODO: UNCOMMENT THIS
        if(registerTable(this.phoneNumber, this.tableID) == -1) {
            Log.d("DEBUG: FAILED TO REGISTER TABLE", String.valueOf(this.tableID));
            throw new RuntimeException("Failed to register table " + this.tableID);
        }

        // TODO: Load Food Array List from json
        MenuGridViewAdapter menuGridViewAdapter = new MenuGridViewAdapter(
                MenuActivity.this,
                new FoodArrayListModel().getFoodArrayListFromURL("http://"+ Settings.HOST+":8081/queryFoodMenu"),
//                new FoodArrayListModel().generateDummyData(),
                this.menuController
        );

        this.menuBinding.menuGridView.setAdapter(menuGridViewAdapter);

        // Initialize Search View
        this.menuBinding.menuSearchView.setActivated(true);
        this.menuBinding.menuSearchView.setQueryHint("Type to search for food!");
        this.menuBinding.menuSearchView.onActionViewExpanded();
        this.menuBinding.menuSearchView.setIconified(false);
        this.menuBinding.menuSearchView.clearFocus();
        this.menuBinding.menuSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                menuGridViewAdapter.getFilter().filter(newText);
                return false;
            }
        });

        // Order Button Logic
        this.menuBinding.menuOrderButton.setOnClickListener(v -> {
            if (this.menuController.getTotalPrice() > 0.0) {
                Bundle menuOrderBundle = new Bundle();
                // Parcel menuController and place into Bundle
                menuOrderBundle.putParcelable("MENU_CONTROLLER", this.menuController);
                menuOrderBundle.putInt("tableId", this.tableID);
                Intent menuOrderIntent = new Intent(getApplicationContext(), ConfirmationActivity.class);
                menuOrderIntent.putExtras(menuOrderBundle);
                startActivity(menuOrderIntent);
            }
            else {
                Toast.makeText(getApplicationContext(),
                        "Please add food to the order before confirming order!",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    public void updateTotalPrice(String newText) {
        menuBinding.menuTotalPrice.setText(newText);
    }

    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this).setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Return to table selection").setMessage(
                        "Are you sure you want to return to table selection?" +
                        "\n(You will lose the current orders if Yes are selected.)")
                .setPositiveButton("Yes", (dialog, which) -> {
                    finish();
                    Toast.makeText(MenuActivity.this, "Order closed",Toast.LENGTH_SHORT).show();
                }).setNegativeButton("No", null).show();
    }


    private int registerTable(String phoneNumber, int tableId){
        RegisterTableThread tableStateThread = new RegisterTableThread();
        tableStateThread.setPhoneNumber(phoneNumber);
        tableStateThread.setTableId(tableId);
        Thread thread = new Thread(tableStateThread);
        thread.start();

        while(tableStateThread.getResponse() == null){}

        JSONObject res = tableStateThread.getResponse();
        try {
            return res.getInt("status");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1;
    }

}