package com.quickap.quickap.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.quickap.quickap.R;
import com.quickap.quickap.databinding.FloorFirstBinding;
import com.quickap.quickap.utils.TableStateThread;
import com.google.android.material.snackbar.Snackbar;

public class FirstFloor extends AppCompatActivity implements View.OnClickListener {

    private FloorFirstBinding binding;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = FloorFirstBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        ImageView stair_up = binding.stairUp;
        stair_up.setOnClickListener(view -> {
            Intent intent = new Intent(view.getContext(), SecondFloor.class);
            startActivity(intent);
        });

        ImageView[] seats = {
                binding.seatInside0,
                binding.seatInside1,
                binding.seatInside2,
                binding.seatOutside3,
                binding.seatOutside4,
                binding.seatOutside5,
                binding.seatOutside6,
                binding.seatOutside7,
                binding.seatOutside8,
                binding.seatOutside9,
                binding.seatOutside10
        };

        for (ImageView seat : seats){
            tableStateChecker(seat);
        }

//        Expensive but automatic method
//        int id = getResources().getIdentifier("R.id.imageView" + i, "id", null);
//        seats[i] = (ImageView) findViewById(id);

//        seat_0.setOnClickListener(this);
//        seat_1.setOnClickListener(this);
//        seat_2.setOnClickListener(this);
//        seat_3.setOnClickListener(this);
//        seat_4.setOnClickListener(this);
//        seat_5.setOnClickListener(this);
//        seat_6.setOnClickListener(this);
//        seat_7.setOnClickListener(this);
//        seat_8.setOnClickListener(this);
//        seat_9.setOnClickListener(this);
//        seat_10.setOnClickListener(this);
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

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(findViewById(R.id.FirstFloorContent),
                            "This " + table_info[1] + " table has already been booked" + table_info[2], Snackbar.LENGTH_SHORT).show();
                }
            });
        }
    }

    @Override
    public void onClick(View view){
        String[] item_id = getResources().getResourceName(view.getId()).split("_");
        int table_id = Integer.parseInt(item_id[2]);
        Snackbar.make(findViewById(R.id.FirstFloorContent),
                "You have choosed " + item_id[1] + " table, number:" + item_id[2], Snackbar.LENGTH_SHORT).show();
        ImageView v = (ImageView) view;
        if(item_id[1].equals("outside")){
            v.setImageResource(R.drawable.seat_outside_selected);
        } else {
            v.setImageResource(R.drawable.seat_inside_selected);
        }
        Intent menu = new Intent(this, MenuActivity.class);
        Bundle extras = getIntent().getExtras();
        Bundle bundle = new Bundle();
        bundle.putInt("tableId", table_id);
        bundle.putString("phoneNumber", extras.getString("phoneNumber"));
        menu.putExtras(bundle);
        startActivity(menu);
    }

}