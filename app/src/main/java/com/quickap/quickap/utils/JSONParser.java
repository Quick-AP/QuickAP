package com.quickap.quickap.utils;

import android.util.Log;

import com.quickap.quickap.model.FoodModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

public class JSONParser {

    public static JSONObject getJsonFromURL(String urlString) {
        try {
            URL url = new URL(urlString);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(url.openConnection().getInputStream()));
            return getJsonFromBufferedReader(bufferedReader);
        } catch (IOException | JSONException e) {
            Log.e("JSONParser",
                    "Failed to JSON from "+urlString+":"+e.getMessage());

            return null;
        }
    }

    public static JSONObject getJsonFromBufferedReader(BufferedReader bufferedReader) throws IOException, JSONException {
        StringBuilder stringBuffer = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null)
            stringBuffer.append(line);
        return new JSONObject(stringBuffer.toString());
    }

    public static FoodModel convertJSONObjectToFoodModel(JSONObject food) throws JSONException {
        Log.d("convertJSONObjectToFoodModel", food.toString());
        String foodID = food.getString("foodId");
        String foodType = food.getString("foodType");
        String foodName = food.getString("name");
        double foodPrice = food.getDouble("price");
        String foodImageURL = food.getString("imageUrl");
        return new FoodModel(foodID, foodType, foodImageURL, foodName, foodPrice);
    }


}
