package com.sky.happyf.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.happyf.R;
import com.sky.happyf.manager.ArticleManager;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import org.w3c.dom.Text;

public class ArticleDetailActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private TextView tvTitle, tvAuthorName, tvdate;
    private LinearLayout llContent;
    private ArticleManager articleManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_article_detail);

        getSupportActionBar().hide();//隐藏标题栏

        initView();

        initListener();

        initData();
    }

    private void initView() {
        tvTitle = findViewById(R.id.tv_title);
        tvAuthorName = findViewById(R.id.tv_author_name);
        tvdate = findViewById(R.id.tv_date);
        llContent = findViewById(R.id.ll_content);
        titleBar = findViewById(R.id.titlebar);
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

    private void initData() {
        articleManager = new ArticleManager(this);
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
