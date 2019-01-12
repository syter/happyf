package com.example.administrator.myapplication;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.io.IOException;
import java.util.logging.Logger;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Thread(new Runnable() {
            @Override
            public void run() {
                OkHttpClient client = new OkHttpClient();
                RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), "");
                Request request = new Request.Builder()
//                        .post(body)
                        .url("http://192.168.0.108:38500/apis/users/register/code/send")
                        .build();

//                RequestBody body = RequestBody.create(MediaType.get("application/json; charset=utf-8"), "1=1");
//                Request request = new Request.Builder()
//                        .url("http://101.37.40.73:80/shop/Api/User/sendMsg")
//                        .post(body)
//                        .build();
                Response response = null;
                try {
                    response = client.newCall(request).execute();
                    String bodyStr = response.body().string();
                    Log.e("aabc", bodyStr);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
