package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.sky.happyf.R;
import com.sky.happyf.message.MessageEvent;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.SpfHelper;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import org.greenrobot.eventbus.EventBus;

public class SetActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private RelativeLayout rlUpdatePwd, rlRemove, rlAbout, rlQuit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set);

        getSupportActionBar().hide();//隐藏标题栏

        initView();

        initListener();


    }

    private void initView() {
        titleBar = findViewById(R.id.titlebar);
        rlUpdatePwd = findViewById(R.id.rl_updatepwd);
        rlRemove = findViewById(R.id.rl_remove);
        rlAbout = findViewById(R.id.rl_about);
        rlQuit = findViewById(R.id.rl_quit);
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

        rlUpdatePwd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SetActivity.this, UpdatePwdActivity.class));
                overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });

        rlRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.d("remove cache!");
                Toast.makeText(getApplicationContext(), "清除缓存成功", Toast.LENGTH_LONG).show();
            }
        });

        rlAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(SetActivity.this, AboutActivity.class));
                overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });

        rlQuit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SpfHelper.getInstance(SetActivity.this).clearUserInfo();
                Toast.makeText(getApplicationContext(), "已退出登录", Toast.LENGTH_LONG).show();
                EventBus.getDefault().post(new MessageEvent(Constants.EVENT_MESSAGE_LOGIN));
                finish();
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
