package com.quickap.quickap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.quickap.quickap.R;
import com.quickap.quickap.controller.MenuController;
import com.quickap.quickap.databinding.ActivityConfirmationBinding;
import com.quickap.quickap.utils.CheckOutThread;
import com.quickap.quickap.utils.PlaceOrderThread;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class ConfirmationActivity extends AppCompatActivity {

    private ActivityConfirmationBinding binding;
    private int tableId;
    private MenuController menuController;
    private List<Map<String,?>> itemList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmationBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            this.menuController = extras.getParcelable("MENU_CONTROLLER");
            for(String foodId: menuController.getOrderMap().keySet()){
                Map<String,String> map = new HashMap<>();
                map.put("firstLine",String.format("%s x %d", menuController.getOrderMap().get(foodId).name, menuController.getOrderMap().get(foodId).amount) );
                map.put("secondLine",String.format("item total price: %.2f x %d = %.2f",
                        menuController.getOrderMap().get(foodId).price,
                        menuController.getOrderMap().get(foodId).amount,
                        menuController.getOrderMap().get(foodId).amount * menuController.getOrderMap().get(foodId).price));
                itemList.add(map);
                Log.d("checkout", "checkout "+"x:"+foodId+" name:"+menuController.getOrderMap().get(foodId).name+" amount:"+menuController.getOrderMap().get(foodId).amount+" price:"+menuController.getOrderMap().get(foodId).price);
            }


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

        binding.listViewCheckOut.setAdapter(new SimpleAdapter(this,itemList,R.layout.item_check_out,new String[]{"firstLine","secondLine"},new int[]{R.id.item_nums,R.id.item_prices}));
        binding.totalCheckOutPrice.setText(String.format(Locale.ENGLISH, "Total Price: %.2f", this.menuController.getTotalPrice()));
        binding.checkout.setOnClickListener(view1 -> {
            checkOut();
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
        });


        if(placeOrder() == -1) {
            Log.d("ERROR", "Failed to get response for menu ordering");
        }
    }


    private double checkOut() {
        CheckOutThread checkOutThread = new CheckOutThread();
        checkOutThread.setTableId(this.tableId);
        Thread thread = new Thread(checkOutThread);
        thread.start();

        while (checkOutThread.getResponse() == null);

        try {
            return checkOutThread.getResponse().getDouble("obj");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -1;
    }

    private int placeOrder() {
        String orderSummary = getOrderSummary();
        Log.d("DEBUG", orderSummary);
        PlaceOrderThread placeOrderThread = new PlaceOrderThread();
        placeOrderThread.setMenuJson(getOrderJsonSummary());
        Thread thread = new Thread(placeOrderThread);
        thread.start();

        while(placeOrderThread.getResponse() == null){}

        JSONObject res = placeOrderThread.getResponse();

        try {
            return res.getInt("status");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -1;
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
        return this.menuController.getDTOListJson(this.tableId);
    }
}