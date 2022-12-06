package com.quickap.quickap.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;


public class PlaceOrderThread implements Runnable{
    private String menuJson;
    private String response;

    public void setMenuJson(String menuJson) {
        this.menuJson = menuJson;
    }

    public JSONObject getResponse() {
        try {
            while (response == null) {}
            JSONObject json = new JSONObject(response);
            return json;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public void run() {
        Log.d("Json", this.menuJson);
        this.response = GetPostUtil.postJson("http://"+Settings.HOST+":8081/orderFood", this.menuJson);
    }

}