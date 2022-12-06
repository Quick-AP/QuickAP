package com.quickap.quickap.model;


import android.util.Log;

import com.quickap.quickap.utils.GetMenuThread;
import com.quickap.quickap.utils.JSONParser;
import com.quickap.quickap.utils.TableStateThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class FoodArrayListModel {


    public ArrayList<FoodModel> generateDummyData() {
        ArrayList<FoodModel> foodList = new ArrayList<>();

        try {
            String DEBUG_JSON = "{\n" +
                    "    \"obj\": [\n" +
                    "        {\n" +
                    "            \"id\": 1,\n" +
                    "            \"foodId\": \"PR01\",\n" +
                    "            \"name\": \"Bolognese Spaghetti\",\n" +
                    "            \"foodType\": \"Pasta & Risotto\",\n" +
                    "            \"price\": 36.0,\n" +
                    "            \"imageUrl\": \"https://5b0988e595225.cdn.sohucs.com/images/20180723/5c9272dc8a28431ebea05e028ccaecf1.jpeg\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": 2,\n" +
                    "            \"foodId\": \"PR02\",\n" +
                    "            \"name\": \"Seafood Pasta\",\n" +
                    "            \"foodType\": \"Pasta & Risotto\",\n" +
                    "            \"price\": 24.0,\n" +
                    "            \"imageUrl\": \"https://pic.quanjing.com/7a/az/QJ9130379340.jpg@!350h\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": 3,\n" +
                    "            \"foodId\": \"PZ01\",\n" +
                    "            \"name\": \"Random Generic Pizza\",\n" +
                    "            \"foodType\": \"Pizza\",\n" +
                    "            \"price\": 68.0,\n" +
                    "            \"imageUrl\": \"https://pic.quanjing.com/m9/97/QJ6116758155.jpg@!350h\"\n" +
                    "        },\n" +
                    "        {\n" +
                    "            \"id\": 4,\n" +
                    "            \"foodId\": \"PZ02\",\n" +
                    "            \"name\": \"Random Generic Pizza2\",\n" +
                    "            \"foodType\": \"Pizza\",\n" +
                    "            \"price\": 74.0,\n" +
                    "            \"imageUrl\": \"https://pic.quanjing.com/m9/97/QJ6116758155.jpg@!350h\"\n" +
                    "        }\n" +
                    "    ],\n" +
                    "    \"comment\": null,\n" +
                    "    \"status\": 1\n" +
                    "}";
            JSONObject json = new JSONObject(DEBUG_JSON);
            FoodArrayListModel.populateFoodArrayListWithJson(json, foodList);
        }
        catch (JSONException jsonException) {
            Log.e("ERROR PARSING DUMMY DATA",
                    jsonException.getMessage());
        }

        return foodList;
    }


    public ArrayList<FoodModel> getFoodArrayListFromURL(String url) {
        ArrayList<FoodModel> foodList = new ArrayList<>();
        //TODO: Populate this with JSON response
        Log.d("DEBUG", url);

        GetMenuThread menu_state = new GetMenuThread();

        Thread thread = new Thread(menu_state);
        thread.start();
        try{
            thread.join();
        }catch (Exception ignore){
            System.out.println("Ignore a mistake");
        }
        FoodArrayListModel.populateFoodArrayListWithJson(menu_state.getRes(), foodList);
        return foodList;
    }

    private static void populateFoodArrayListWithJson(JSONObject root, List<FoodModel> foodList) {

        try {
            JSONArray responseArray = root.getJSONArray("obj");
            for (int i = 0; i < responseArray.length(); i++) {
                JSONObject food = responseArray.getJSONObject(i);
                foodList.add(JSONParser.convertJSONObjectToFoodModel(food));
            }

        }
        catch (JSONException jsonException) {
            Log.e("FoodArrayListModel",
                    jsonException.getMessage());
        }

    }

}
