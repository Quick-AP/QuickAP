package com.quickap.quickap.utils;

import org.json.JSONException;
import org.json.JSONObject;

public class CheckOutThread implements Runnable {
    private String response;
    private int tableId;

    public void setTableId(int tableId) {
        this.tableId = tableId;
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

        this.response = GetPostUtil.postJson("http://"+Settings.HOST+":8081/checkOut", String.valueOf(this.tableId));
    }

}
