package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sky.happyf.R;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

public class WannaHappyActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private Button btnWanna;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wanna_happy);

        getSupportActionBar().hide();//隐藏标题栏

        initView();

        initListener();


    }

    private void initView() {
        btnWanna = (Button) findViewById(R.id.btn_wanna);
        titleBar = (CommonTitleBar) findViewById(R.id.titlebar);
    }

    private void initListener() {
//        titleBar.setBackgroundResource(R.drawable.shape_gradient);
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    finish();
                }
            }
        });

        btnWanna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(WannaHappyActivity.this, PayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("price", "100");
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });
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
