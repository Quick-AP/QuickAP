package com.quickap.quickap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.quickap.quickap.R;
import com.quickap.quickap.databinding.FloorSecondBinding;
import com.quickap.quickap.utils.TableStateThread;
import com.google.android.material.snackbar.Snackbar;

public class SecondFloor extends AppCompatActivity implements View.OnClickListener {

    private FloorSecondBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FloorSecondBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageView stair_up = binding.stairDown;
        stair_up.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), FirstFloor.class);
            startActivity(intent);
        });

        ImageView[] seats = {
                binding.seatInside11,
                binding.seatInside12,
                binding.seatInside13,
                binding.seatInside14,
                binding.seatInside15,
                binding.seatOutside16,
                binding.seatOutside17,
                binding.seatOutside18,
                binding.seatOutside19,
        };

        // Set table states before start up
        for (ImageView seat : seats){
            tableStateChecker(seat);
        }
    }

    public void tableStateChecker(ImageView view){
        String[] table_info = getResources().getResourceName(view.getId()).split("_");
        int table_id = Integer.parseInt(table_info[2]);

        TableStateThread table_state = new TableStateThread();
        table_state.setTableID(table_id);

        Thread thread = new Thread(table_state);
        thread.start();
        try{
            thread.join();
        }catch (Exception ignore){
            System.out.println("Ignore a mistake");
        }
        boolean res = table_state.getRes();
        if (res) {
            view.setOnClickListener(this);
            return;
        } else {
            if (table_info[1].equals("outside"))
                view.setImageResource(R.drawable.seat_outside_selected);
            else
                view.setImageResource(R.drawable.seat_inside_selected);

            view.setOnClickListener(view1 -> Snackbar.make(findViewById(R.id.SecondFloorContent),
                    "This " + table_info[1] + " table has already been booked" + table_info[2], Snackbar.LENGTH_SHORT).show());
        }
    }

    @Override
    public void onClick(View view){
        String[] item_id = getResources().getResourceName(view.getId()).split("_");
        int table_id = Integer.parseInt(item_id[2]);
        Snackbar.make(findViewById(R.id.SecondFloorContent),
                "You have choosed " + item_id[1] + " table, number:" + item_id[2], Snackbar.LENGTH_SHORT).show();
        ImageView v = (ImageView) view;
        if(item_id[1].equals("outside")){
            v.setImageResource(R.drawable.seat_outside_selected);
        } else {
            v.setImageResource(R.drawable.seat_inside_selected);
        }
        Intent menu = new Intent(this, MenuActivity.class); // Replace MainActivity.class with menu.class
        Bundle bundle = new Bundle();
        bundle.putInt("tableId", table_id);
        menu.putExtras(bundle);
        startActivity(menu);
    }
}