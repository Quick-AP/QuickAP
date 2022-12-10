package com.quickap.quickap.utils;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.view.View;

/***
 * @Author jianghanchen
 * @Date 13:43 2022/11/28
 ***/
public class QueueAskThread implements Runnable{
    private View v;
    private Notification notification;
    private NotificationManager manager;
    private String res;
    private String phoneNumber;
    private Context mContext;

    public QueueAskThread(Context mContext) {
        this.mContext = mContext;
    }

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public void setView(View v){
        this.v = v;
    }
    public void setNotification(Notification notification){
        this.notification = notification;
    }
    public void setNotificationManager(NotificationManager manager){
        this.manager = manager;
    }
    public String getRes(){
        return res;
    }
    public void run(){
        QueuequeueThread thread = new QueuequeueThread();
        int rank = -3;
        do{
            try {
                Thread.sleep(1000*30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            rank = thread.queue(phoneNumber);
        }while(rank != 0 && rank != -1);

        manager.notify(1,notification);
        Intent intent= new Intent();
        intent.putExtra("phoneNumber", this.phoneNumber);
        intent.setClass(mContext, com.quickap.quickap.activities.FirstFloor.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        mContext.startActivity(intent);

        res = "ok";
    }
}
