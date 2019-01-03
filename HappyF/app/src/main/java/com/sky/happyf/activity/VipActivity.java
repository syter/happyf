package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.happyf.R;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

public class VipActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private LinearLayout llOne, llTwo, llThree;
    private Button btnBevip;
    private int selectIndex = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);

        getSupportActionBar().hide();//隐藏标题栏

        initView();

        initListener();


    }

    private void initView() {
        titleBar = (CommonTitleBar) findViewById(R.id.titlebar);
        llOne = (LinearLayout) findViewById(R.id.ll_one);
        llTwo = (LinearLayout) findViewById(R.id.ll_two);
        llThree = (LinearLayout) findViewById(R.id.ll_three);
        btnBevip = (Button) findViewById(R.id.btn_bevip);
    }

    private void initListener() {
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    finish();
                }
            }
        });
        llOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIndex = 0;
                processSelectIndexView();
            }
        });
        llTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIndex = 1;
                processSelectIndexView();
            }
        });
        llThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIndex = 2;
                processSelectIndexView();
            }
        });
        btnBevip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(VipActivity.this, PayActivity.class));
                overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });
    }

    private void processSelectIndexView() {
        if (selectIndex == 0) {
            llOne.setBackgroundColor(getColor(R.color.skyblue));
            llTwo.setBackgroundColor(getColor(R.color.white));
            llThree.setBackgroundColor(getColor(R.color.white));
        } else if (selectIndex == 1) {
            llOne.setBackgroundColor(getColor(R.color.white));
            llTwo.setBackgroundColor(getColor(R.color.skyblue));
            llThree.setBackgroundColor(getColor(R.color.white));
        } else if (selectIndex == 2) {
            llOne.setBackgroundColor(getColor(R.color.white));
            llTwo.setBackgroundColor(getColor(R.color.white));
            llThree.setBackgroundColor(getColor(R.color.skyblue));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.anim_exit);
    }
}
