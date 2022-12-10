package com.quickap.quickap.activities;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
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

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import org.json.JSONException;
import org.json.JSONObject;
import com.quickap.quickap.R;
import com.quickap.quickap.databinding.ActivityReservationBinding;
import com.quickap.quickap.databinding.PopupViewBinding;
import com.quickap.quickap.utils.QueueRegisterThread;
import com.quickap.quickap.utils.QueuequeueThread;
import com.quickap.quickap.utils.QueueAskThread;


import com.quickap.quickap.design.NotificationClickReceiver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ReservationActivity extends AppCompatActivity implements View.OnClickListener{
    int count = 1;

    private ActivityReservationBinding reservationBinding;
    private PopupViewBinding popupViewBinding;
    View V = null;
//    String phoneNumber1;
    String[] phoneNumber;



    /**
     * Utility to confirm if a input Stirng is a valid mobile number in China using Regular Expression.
     * Regex refered from: https://blog.csdn.net/cm519096/article/details/126370622.
     * @param mobile input string.
     * @return boolean identifying if the input string is a valid mobile number.
     */
    public static boolean isMobile(String mobile) {
        if (mobile == null) return false;
        String regex = "^((13[0-9])|(14[0,1,4-9])|(15[0-3,5-9])|(16[2,5,6,7])|(17[0-8])|(18[0-9])|(19[0-3,5-9]))\\d{8}$";
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }

    /**
     * Get Phone number from the system as a String, returns null if failed.
     * @return phone number of the user's device, of null when failed.
     */
    private String getPhoneNumberFromSystem() {
        TelephonyManager telephonyManager =
                (TelephonyManager) getApplicationContext()
                        .getSystemService(TELEPHONY_SERVICE);

        boolean permissionsNotGranted = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_SMS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_NUMBERS) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED;

        if (permissionsNotGranted) {
            return null;
        }

        String phoneNumber = telephonyManager.getLine1Number();
        return isMobile(phoneNumber) ? phoneNumber : null;
    }

    /**
     * Display pop-up and save obtained phone number in a given String array.
     * @param phoneNumber Single element String array to save the user inputted phone number
     */
    public void getPhoneNumberFromUser(String[] phoneNumber){


        final androidx.appcompat.app.AlertDialog alertDialog = new androidx.appcompat.app.AlertDialog.Builder(this).create();
        alertDialog.setTitle("Phone number not found");
        alertDialog.setMessage("Please input your phone number to begin using!");

        final EditText input = new EditText(this);
        input.setMaxLines(1);

        // Deal with keyboard enter IME action
        input.setImeOptions(EditorInfo.IME_ACTION_DONE);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);

        input.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (input.getText() != null && isMobile(input.getText().toString())) {
                    // call alertDialog's on click
                    alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).callOnClick();
                }
            }
            return false;
        });

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s == null || s.length() == 0 || !isMobile(s.toString())) {
                    alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setEnabled(false);
                    alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.main_style_sandDlooar));
                } else {
                    alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setEnabled(true);
                    alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.main_style_carafe));
                }
            }
        });

        alertDialog.setView(input);

        alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, "OK",
                (dialog, which) -> {
                    phoneNumber[0] = input.getText().toString();
                    Log.d("DEBUG", phoneNumber[0]);
                    alertDialog.dismiss();
                });

        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        alertDialog.show();
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.reservationBinding = ActivityReservationBinding.inflate(getLayoutInflater());
        this.popupViewBinding = PopupViewBinding.inflate(getLayoutInflater());
        View reservationView = this.reservationBinding.getRoot();
        setContentView(reservationView);

        // Try getting phone number from system
        this.phoneNumber = new String[]{getPhoneNumberFromSystem()};
        // if failed to obtai, then get from user
        if (phoneNumber[0] == null || !isMobile(phoneNumber[0])) {
            getPhoneNumberFromUser(phoneNumber);

        }

        Button queueButton = reservationBinding.queuingButton;
        queueButton.setOnClickListener(this);
//        this.phoneNumber1 = phoneNumber[0];

    }

    @Override
    public void onClick(View view){
        V = view;

        register(V, phoneNumber[0]);
        int res = queue(V, phoneNumber[0]);

        if(res != 0){
            View successView = getLayoutInflater().inflate(R.layout.success, null);
            TextView text = successView.findViewById(R.id.QueueSuccessHint);
            int rank = queue(V, phoneNumber[0]);
            text.setText("Your rank is:"+rank);
            PopupWindow sucessWindow = new PopupWindow(successView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
            sucessWindow.showAsDropDown(V,(int)(0.5*V.getWidth()),(int)0.5*V.getHeight());

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

               try{
                   Intent intent =new Intent (ReservationActivity.this, NotificationClickReceiver.class);
                   PendingIntent pendingIntent;
                   if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.S) {
                       pendingIntent = PendingIntent.getActivity(ReservationActivity.this, 123, intent, PendingIntent.FLAG_IMMUTABLE);
                   } else {
                       pendingIntent = PendingIntent.getActivity(ReservationActivity.this, 123, intent, PendingIntent.FLAG_ONE_SHOT);
                   }
                   builder.setContentIntent(pendingIntent);
                   notification = builder.build();
               }catch (Exception e){
                   System.out.println();
               }
            }else{
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                        .setContentTitle("Your can register table now")
                        .setContentText("you are O")
                        .setWhen(System.currentTimeMillis())
                        .setSmallIcon(R.mipmap.ic_launcher)
                        .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
                Intent intent =new Intent (ReservationActivity.this,NotificationClickReceiver.class);
                PendingIntent pendingIntent = PendingIntent.getBroadcast(ReservationActivity.this, 0, intent, 0);
//                builder.setContentIntent(pendingIntent);
//                notification = builder.build();
            }
            ask(manager,notification, phoneNumber[0]);

        }
        else {

            Intent intent = new Intent(view.getContext(), FirstFloor.class);
            Bundle bundle = new Bundle();
            bundle.putString("phoneNumber", this.phoneNumber[0]);
            intent.putExtras(bundle);
            startActivity(intent);

//            View successView = getLayoutInflater().inflate(R.layout.success, null);
//            TextView text = successView.findViewById(R.id.QueueSuccessHint);
//            text.setText("You can register:"+res);
//            PopupWindow sucessWindow = new PopupWindow(successView, ViewGroup.LayoutParams.WRAP_CONTENT,ViewGroup.LayoutParams.WRAP_CONTENT,true);
//            sucessWindow.showAsDropDown(V,(int)(0.75*V.getWidth()),2*V.getHeight());
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
