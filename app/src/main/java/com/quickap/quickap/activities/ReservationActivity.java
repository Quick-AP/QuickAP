package com.quickap.quickap.activities;

import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.quickap.quickap.R;
import com.quickap.quickap.databinding.ActivityReservationBinding;
import com.quickap.quickap.databinding.PopupViewBinding;
import com.quickap.quickap.utils.QueueRegisterThread;

public class ReservationActivity extends AppCompatActivity {
    int count = 1;

    private ActivityReservationBinding reservationBinding;
    private PopupViewBinding popupViewBinding;

    String phoneNumber = "123456";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.reservationBinding = ActivityReservationBinding.inflate(getLayoutInflater());
        this.popupViewBinding = PopupViewBinding.inflate(getLayoutInflater());
        View reservationView = this.reservationBinding.getRoot();
        setContentView(reservationView);
    }

    public void leoClick(View view){
        View V = null;

        V = view;
        QueueRegisterThread queueRegisterThread = new QueueRegisterThread();
        queueRegisterThread.setView(view);
        queueRegisterThread.setPhoneNumber(phoneNumber);
        Thread thread = new Thread(queueRegisterThread);
        thread.start();
        try{
            thread.join();
        }catch (Exception ignore){
            System.out.println();
        }
        String res1 = queueRegisterThread.getRes();
        System.out.println(res1);


        View successView = getLayoutInflater().inflate(R.layout.success, null);
        TextView text = successView.findViewById(R.id.text);
        text.setText(String.format("排号成功：%s", res1));

        PopupWindow sucessWindow = new PopupWindow(successView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
        sucessWindow.showAsDropDown(V,(int)(0.75*V.getWidth()),2*V.getHeight());

        final View popupView = getLayoutInflater().inflate(R.layout.popup_view, null);


//        Button btn1 = popupView.findViewById(R.id.btn1);
//        Button btn2 = popupView.findViewById(R.id.btn2);
//        Button btn3 = popupView.findViewById(R.id.btn3);
//        Button btn4 = popupView.findViewById(R.id.btn4);
//        final PopupWindow popupWindow = new PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
//
//        popupWindow.showAsDropDown(view,800,-100);

//        btn1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                View successView = getLayoutInflater().inflate(R.layout.success, null);
//                TextView text = successView.findViewById(R.id.text);
//                text.setText("排号成功："+count);
//                count++;
//                PopupWindow sucessWindow = new PopupWindow(successView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
////                sucessWindow.showAsDropDown(v,200,-2000);
//                sucessWindow.showAsDropDown(v,(int)(0.75*v.getWidth()),2*v.getHeight());
//                popupWindow.dismiss();
//            }
//        });
//        btn2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        btn3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
//        btn4.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

}
