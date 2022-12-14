package com.quickap.quickap.utils;


import android.text.TextUtils;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.*;
import java.net.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
/***
 * @Author jianghanchen
 * @Date 13:53 2022/11/13
 ***/
public class GetPostUtil {


    //    private static String URL_PATH="http://www.baidu.com";
    private static HttpURLConnection httpURLConnection = null;
    private static HttpURLConnection httpURLConnection2 = null;
    public GetPostUtil(){}



    public static String sendPost(String url, String params){
        InputStream inputStream = getInputStream(url);
        String result= null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
            result = "";
            String line = "";
            try {
                while((line = reader.readLine())!= null){
                    result = result+ line;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(result);
            httpURLConnection2.disconnect();
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;

    }
    public static String sendPost2(String url, String params){
        InputStream inputStream = getInputStreamPost(url,params);
        String result = null;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
            result = "";
            String line = "";
            try {
                while((line = reader.readLine())!= null){
                    result = result+ line;
                }
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            System.out.println(result);
            httpURLConnection.disconnect();
        } catch (UnsupportedEncodingException | NullPointerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return result;
    }


    public static String postJson(String urlPath, String Json) {
        String result = "";
        BufferedReader reader = null;
        try {
            URL url = new URL(urlPath);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("Charset", "UTF-8");
            // 设置文件类型:
            conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            // 设置接收类型否则返回415错误
            //conn.setRequestProperty("accept","*/*")此处为暴力方法设置接受所有类型，以此来防范返回415;
            conn.setRequestProperty("accept", "application/json");
            // 往服务器里面发送数据
            if (Json != null && !TextUtils.isEmpty(Json)) {
                byte[] writebytes = Json.getBytes();
                // 设置文件长度
                conn.setRequestProperty("Content-Length", String.valueOf(writebytes.length));
                OutputStream outwritestream = conn.getOutputStream();
                outwritestream.write(Json.getBytes());
                outwritestream.flush();
                outwritestream.close();
                Log.d("hlhupload", "doJsonPost: conn" + conn.getResponseCode());
            }
            if (conn.getResponseCode() == 200) {
                reader = new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));
                result = reader.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }


    public static JSONObject sendPostJSON(String url, String params) {
        InputStream inputStream = getInputStreamPost(url,params);
        JSONObject resultJson = null;

        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream,"utf-8"));
            resultJson = JSONParser.getJsonFromBufferedReader(bufferedReader);
            httpURLConnection.disconnect();
        } catch (NullPointerException | IOException | JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return resultJson;
    }
    /**
     * 获取服务端的数据，以InputStream返回
     * @return
     */
    public static InputStream getInputStream(String urlPath){
        InputStream inputStream = null;

        try {
            URL url = new URL(urlPath);
            if(url != null){
                try {
                    httpURLConnection2 = (HttpURLConnection) url.openConnection();
                    //设置超时时间
                    httpURLConnection2.setConnectTimeout(3000);
                    //设置请求方式
                    httpURLConnection2.setRequestMethod("GET");
                    int responsecode = httpURLConnection2.getResponseCode();
                    if(responsecode == HttpURLConnection.HTTP_OK){
                        inputStream = httpURLConnection2.getInputStream();
                    }
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return inputStream;
    }

    public static InputStream getInputStreamPost(String urlPath,String param){
        InputStream inputStream = null;

        try {
            URL url = new URL(urlPath);
            if(url != null){
                try {
                    httpURLConnection = (HttpURLConnection) url.openConnection();
                    //设置超时时间
                    httpURLConnection.setConnectTimeout(3000);
                    //设置请求方式
                    httpURLConnection.setRequestMethod("POST");
                    httpURLConnection.setDoOutput(true);
                    httpURLConnection.setDoInput(true);
                    httpURLConnection.setRequestProperty("accept", "*/*");
                    httpURLConnection.setRequestProperty("connection", "Keep-Alive");
                    httpURLConnection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
                    // 设置传入参数的格式:请求参数应该是 name1=value1&name2=value2 的形式
                    httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                    OutputStream os = httpURLConnection.getOutputStream();
                    Log.d("DEBUG", "HTTP Connection" + httpURLConnection);
                    if(param.length() != 0){
                        os.write(param.getBytes());
                    }
                    Log.d("DEBUG", "Output stream" + os);
                    int responsecode = httpURLConnection.getResponseCode();
                    Log.d("DEBUG", "Response code" + responsecode);
                    if(responsecode == HttpURLConnection.HTTP_OK){
                        inputStream = httpURLConnection.getInputStream();
                    }
                } catch (Exception e) {
                    Log.d("DEBUG", "HTTP");
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return inputStream;
    }


}
