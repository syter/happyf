package com.sky.happyf.mainHelper;

import android.app.Activity;
import android.view.View;

public class MineHelper {
    private View view;
    private Activity act;

    public MineHelper(Activity act, View view) {
        this.act = act;
        this.view = view;
    }

    public void init() {
        initView();

        initListener();

        initData();
    }

    private void initView() {
    }

    private void initListener() {

    }

    private void initData() {

    }
}
