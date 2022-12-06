package com.quickap.quickap.utils;

import android.util.Log;

import com.quickap.quickap.model.FoodModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class JSONParser {

    public static JSONObject getJsonFromURL(String urlString) {
        try {
            URL url = new URL(urlString);
            Log.d("DEBUG", String.valueOf(url));
            URLConnection conn =  url.openConnection();
            Log.d("DEBUG", String.valueOf(conn));
            InputStream is = conn.getInputStream();
            Log.d("DEBUG", String.valueOf(is));
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
            Log.d("DEBUG", String.valueOf(bufferedReader));
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
        Log.d("DEBUG", stringBuffer.toString());
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
