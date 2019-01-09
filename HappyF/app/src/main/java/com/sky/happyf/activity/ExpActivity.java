package com.sky.happyf.activity;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.sky.happyf.R;
import com.sky.happyf.util.Constants;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

public class ExpActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private WebView webview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exp);

        getSupportActionBar().hide();//隐藏标题栏

        initView();

        initListener();
    }

    private void initView() {
        titleBar = (CommonTitleBar) findViewById(R.id.titlebar);

        webview = (WebView) findViewById(R.id.webview);
        webview.loadUrl(Constants.EXP_URL);

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
