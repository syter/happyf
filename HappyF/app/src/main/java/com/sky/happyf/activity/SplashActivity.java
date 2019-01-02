package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.sky.happyf.R;

public class SplashActivity extends AppCompatActivity {

    private Button btnJump;
    private boolean isJump = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);//隐藏状态栏
        getSupportActionBar().hide();//隐藏标题栏
        setContentView(R.layout.activity_splash);

        initView();

        initListener();


    }

    private void initView() {
        btnJump = (Button) findViewById(R.id.btn_jump);
    }

    private void initListener() {
        btnJump.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isJump = true;
                Intent it = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(it);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        Thread myThread = new Thread() {//创建子线程
            @Override
            public void run() {
                try {
                    sleep(5000);//使程序休眠五秒
                    if (isJump) {
                        return;
                    }
                    Intent it = new Intent(getApplicationContext(), MainActivity.class);//启动MainActivity
                    startActivity(it);
                    finish();//关闭当前活动
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        myThread.start();//启动线程
    }
}
