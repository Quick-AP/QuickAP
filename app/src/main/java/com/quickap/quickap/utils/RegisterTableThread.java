package com.quickap.quickap.utils;

import org.json.JSONObject;

public class RegisterTableThread implements Runnable {
    private int tableId;
    private String phoneNumber;
    private JSONObject response;


    public void setTableId(int tableId) {
        this.tableId = tableId;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public JSONObject getResponse() {
        return response;
    }

    @Override
    public void run() {
        String paramJson = "{\"tableId\":" + this.tableId + "\"phoneNumber\":" + this.phoneNumber + "}";
        this.response = GetPostUtil.sendPostJSON("http://10.0.2.2:8081/table/register", paramJson);
    }



}
