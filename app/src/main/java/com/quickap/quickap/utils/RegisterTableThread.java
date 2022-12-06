package com.quickap.quickap.utils;

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterTableThread implements Runnable {
    private int tableId;
    private String phoneNumber;
    private String response;

    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
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
        String paramJson = "{\"tableId\":" + this.tableId + ",\"phoneNumber\":" + this.phoneNumber + "}";
        Log.d("Json", paramJson);
        this.response = GetPostUtil.postJson("http://"+Settings.HOST+":8081/table/register", paramJson);
    }



}
