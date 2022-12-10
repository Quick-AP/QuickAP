package com.quickap.quickap.design;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/***
 * @Author jianghanchen
 * @Date 17:10 2022/11/28
 ***/
public class NotificationClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newIntent = new Intent(context, com.quickap.quickap.activities.FirstFloor.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String phoneNumber =  intent.getExtras().getString("phoneNumber");
        Log.d("DEBUG", String.valueOf(phoneNumber == null));
        newIntent.putExtra("phoneNumber", phoneNumber);
        context.startActivity(newIntent);
    }

}