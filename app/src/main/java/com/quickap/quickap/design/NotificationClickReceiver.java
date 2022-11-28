package com.quickap.quickap.design;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/***
 * @Author jianghanchen
 * @Date 17:10 2022/11/28
 ***/
public class NotificationClickReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent newIntent = new Intent(context, com.quickap.quickap.activities.FirstFloor.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(newIntent);
    }
}