package com.sky.happyf.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sky.happyf.R;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

public class PayActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private EditText etName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        getSupportActionBar().hide();//隐藏标题栏

        initView();

        initListener();


    }

    private void initView() {
        etName = (EditText) findViewById(R.id.et_name);
        titleBar = (CommonTitleBar) findViewById(R.id.titlebar);
    }

    private void initListener() {
//        titleBar.setBackgroundResource(R.drawable.shape_gradient);
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    finish();
                } else if (action == CommonTitleBar.ACTION_RIGHT_TEXT) {
                    // TODO save
                    finish();
                }
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
