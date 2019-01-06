package com.sky.happyf.mainHelper;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.Button;

import com.sky.happyf.R;
import com.sky.happyf.activity.CartListActivity;
import com.sky.happyf.activity.WannaHappyActivity;

public class HappyHelper {
    private View view;
    private Activity act;
    private Button btnWanna;

    public HappyHelper(Activity act, View view) {
        this.act = act;
        this.view = view;
    }

    public void init() {
        initView();

        initListener();

        initData();
    }

    private void initView() {
        btnWanna = (Button) view.findViewById(R.id.btn_go);
    }

    private void initListener() {
        btnWanna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.startActivity(new Intent(act, WannaHappyActivity.class));
                act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });
    }

    private void initData() {

    }
}
