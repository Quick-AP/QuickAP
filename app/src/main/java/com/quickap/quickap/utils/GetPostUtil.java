package com.quickap.quickap.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class GetPostUtil {

    private static HttpURLConnection httpURLConnection = null;

    public static String sendPost(String url, String params) {
        InputStream inputStream = getInputStream(url);
        String result = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "utf-8"));
            result = "";
            String line = "";
            try {
                while((line = reader.readLine()) != null) {
                    result = result + line;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            System.out.println(result);
            httpURLConnection.disconnect();
            } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static InputStream getInputStream(String urlPath) {
        InputStream inputStream = null;
        try{
            URL url = new URL(urlPath);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(3000);
            httpURLConnection.setRequestMethod("GET");
            int responsecode = httpURLConnection.getResponseCode();
            if(responsecode == HttpURLConnection.HTTP_OK){
                inputStream = httpURLConnection.getInputStream();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return inputStream;
    }

}
