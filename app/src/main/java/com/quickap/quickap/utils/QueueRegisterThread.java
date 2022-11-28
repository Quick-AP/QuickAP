package com.quickap.quickap.utils;

import android.view.View;


/***
 * @Author jianghanchen
 * @Date 21:49 2022/11/18
 ***/


public class QueueRegisterThread implements Runnable{

        private View v;
        private String res;
        private String phoneNumber;

        public void setPhoneNumber(String phoneNumber){
            this.phoneNumber = phoneNumber;
        }
        public void setView(View v){
            this.v = v;
        }
        public String getRes(){
            return res;
        }
        public void run(){
            res = GetPostUtil.sendPost2("http://10.0.2.2:8081/queue/register","{\"phoneNumber\":"+phoneNumber+"}");
        }

}

