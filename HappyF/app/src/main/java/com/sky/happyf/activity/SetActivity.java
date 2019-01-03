package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;

import com.orhanobut.logger.Logger;
import com.sky.happyf.R;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

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
        titleBar = (CommonTitleBar) findViewById(R.id.titlebar);
        rlUpdatePwd = (RelativeLayout) findViewById(R.id.rl_updatepwd);
        rlRemove = (RelativeLayout) findViewById(R.id.rl_remove);
        rlAbout = (RelativeLayout) findViewById(R.id.rl_about);
        rlQuit = (RelativeLayout) findViewById(R.id.rl_quit);
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
