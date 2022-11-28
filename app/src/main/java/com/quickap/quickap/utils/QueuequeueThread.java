package java.com.quickap.quickap.utils;

import android.view.View;
import org.json.JSONException;
import org.json.JSONObject;

/***
 * @Author jianghanchen
 * @Date 13:43 2022/11/28
 ***/
public class QueuequeueThread implements Runnable{
    private View v;
    private String res;
    private String phoneNumber;

    public void setPhoneNumber(String phoneNumber){
        this.phoneNumber = phoneNumber;
    }
    public void setView(View v){
        this.v = v;
    }
    public String getRes(){
        return res;
    }
    public void run(){
        res = GetPostUtil.sendPost("http://10.0.2.2:8081/queue/query?phoneNumber=" + phoneNumber, null);
    }
    public int queue(String phoneNumber){
        QueuequeueThread queueRegisterThread = new QueuequeueThread();
        queueRegisterThread.setPhoneNumber(phoneNumber);
        Thread thread = new Thread(queueRegisterThread);
        thread.start();
        while(queueRegisterThread.getRes() == null){}
        String res = queueRegisterThread.getRes();
        try {
            JSONObject obj = new JSONObject(res);
            int rank = obj.getJSONObject("obj").getInt("rank");
            return rank;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return -3;
    }
}
