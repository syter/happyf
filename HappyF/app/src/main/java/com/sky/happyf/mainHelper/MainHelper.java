package com.sky.happyf.mainHelper;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.sky.happyf.R;
import com.sky.happyf.activity.LoginActivity;

public class MainHelper {
    private View view;
    private Activity act;
    public Button btnLogin;

    public MainHelper(Activity act, View view) {
        this.act = act;
        this.view = view;
    }

    public void init() {
        initView();

        initListener();

        initData();
    }

    private void initView() {
        btnLogin = (Button) view.findViewById(R.id.btn_login);
    }

    private void initListener() {
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.startActivity(new Intent(act, LoginActivity.class));
                act.overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
            }
        });
    }

    private void initData() {

    }
}
