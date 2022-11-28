package com.quickap.quickap.activities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.app.*;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupWindow;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.core.app.NotificationCompat;
import org.json.JSONException;
import org.json.JSONObject;
import com.quickap.quickap.R;
import com.quickap.quickap.databinding.ActivityReservationBinding;
import com.quickap.quickap.databinding.PopupViewBinding;
import com.quickap.quickap.utils.QueueRegisterThread;
import org.json.JSONException;
import org.json.JSONObject;

import java.com.quickap.quickap.design.NotificationClickReceiver;

public class ReservationActivity extends AppCompatActivity {
    int count = 1;

    private ActivityReservationBinding reservationBinding;
    private PopupViewBinding popupViewBinding;
    View V = null;
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



        V = view;
        register(V, phoneNumber1);
        int res = queue(V,phoneNumber1);
        if(res != 0){
            View successView = getLayoutInflater().inflate(R.layout.success, null);
            TextView text = successView.findViewById(R.id.text);
            int rank = queue(V, phoneNumber1);
            text.setText("Your rank is:"+rank);
            PopupWindow sucessWindow = new PopupWindow(successView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
            sucessWindow.showAsDropDown(V,(int)(0.75*V.getWidth()),2*V.getHeight());



            Notification notification = null;
            NotificationManager manager = (NotificationManager)getSystemService(NOTIFICATION_SERVICE);
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                String id = "ID";
                String name = "Name";
                NotificationChannel channel = new NotificationChannel(id,name,manager.IMPORTANCE_LOW);
                manager.createNotificationChannel(channel);
                Notification.Builder builder = new Notification.Builder(this)
                        .setChannelId(id)
                        .setContentTitle("Your can register table now")
                        .setContentText("you are O")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));

                Intent intent =new Intent (ReservationActivity.this, NotificationClickReceiver.class);
                PendingIntent pendingIntent =PendingIntent.getBroadcast(ReservationActivity.this, 0, intent, 0);
                builder.setContentIntent(pendingIntent);
                notification = builder.build();
            }else{
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        .setContentTitle("Your can register table now")
                        .setContentText("you are O")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                Intent intent =new Intent (ReservationActivity.this,NotificationClickReceiver.class);
                PendingIntent pendingIntent =PendingIntent.getBroadcast(ReservationActivity.this, 0, intent, 0);
                builder.setContentIntent(pendingIntent);
                notification = builder.build();
            }
            ask(manager,notification, phoneNumber1);

        }else{
            View successView = getLayoutInflater().inflate(R.layout.success, null);
            TextView text = successView.findViewById(R.id.text);
            text.setText("You can register:"+res);
            PopupWindow sucessWindow = new PopupWindow(successView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
            sucessWindow.showAsDropDown(V,(int)(0.75*V.getWidth()),2*V.getHeight());
        }

    }
    public int register(View view, String phoneNumber){
        QueueRegisterThread queueRegisterThread = new QueueRegisterThread();
        queueRegisterThread.setView(view);
        queueRegisterThread.setPhoneNumber(phoneNumber);
        Thread thread = new Thread(queueRegisterThread);
        thread.start();
        while(queueRegisterThread.getRes() == null){}
        String res = queueRegisterThread.getRes();
        try {
            JSONObject obj = new JSONObject(res);
            return obj.getInt("obj");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return -3;
    }
    public int queue(View view, String phoneNumber){
        QueuequeueThread queueRegisterThread = new QueuequeueThread();
        queueRegisterThread.setView(view);
        queueRegisterThread.setPhoneNumber(phoneNumber);
        Thread thread = new Thread(queueRegisterThread);
        thread.start();
        while(queueRegisterThread.getRes() == null){}
        String res = queueRegisterThread.getRes();
        try {
            JSONObject obj = new JSONObject(res);
            int rank = obj.getJSONObject("obj").getInt("rank");
            return rank;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -3;
    }

    public void ask(NotificationManager manager,Notification notification, String phoneNumber){
        QueueAskThread t = new QueueAskThread(this);
        t.setNotification(notification);
        t.setNotificationManager(manager);
        t.setPhoneNumber(phoneNumber);
        Thread thread = new Thread(t);
        thread.start();
    }

}
