package com.quickap.quickap.utils;


import org.json.JSONException;
import org.json.JSONObject;

public class GetMenuThread implements Runnable{

    private JSONObject res;

    public JSONObject getRes(){
        return res;
    }
    public void run(){
        JSONObject menu_info;
        try {
            menu_info = new JSONObject(GetPostUtil.sendPost("http://"+Settings.HOST+":8081/queryFoodMenu", null));
            res = menu_info;
        } catch (JSONException e) {
            e.printStackTrace();

        } catch (NullPointerException e) {
            // DEBUG: ONLY FOR OFFLINE TESTING
            res = null;
        }
    }

}