package com.sky.happyf.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.bumptech.glide.Glide;
import com.sky.happyf.Model.Article;
import com.sky.happyf.R;
import com.sky.happyf.manager.ArticleManager;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.Utils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.util.List;

public class ArticleDetailActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private TextView tvTitle, tvAuthorName, tvDate;
    private LinearLayout llContent;
    private ArticleManager articleManager;
    private Article currentArticle;

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
        tvDate = findViewById(R.id.tv_date);
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

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String id = bundle.getString("id");

        initArticleData(id);
    }

    private void initArticleData(String id) {
        articleManager.getArticleDetail(id, new ArticleManager.FetchArticleCallback() {
            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(List<Article> data) {
                currentArticle = data.get(0);
                tvTitle.setText(currentArticle.title);
                tvAuthorName.setText(currentArticle.authorName);
                tvDate.setText(currentArticle.date);
                for (String content : currentArticle.contents) {
                    if (content.indexOf(Constants.CONTENT_TEXT) != -1) {
                        content = content.replaceAll(Constants.CONTENT_TEXT, "");
                        TextView tvContent = new TextView(ArticleDetailActivity.this);
                        tvContent.setText(content);
                        tvContent.setTextColor(getColor(R.color.gray_text_2));
                        tvContent.setTextSize(14);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.topMargin = Utils.dip2px(ArticleDetailActivity.this, 12);
                        llContent.addView(tvContent, params);
                    } else if (content.indexOf(Constants.CONTENT_IMAGE) != -1) {
                        content = content.replaceAll(Constants.CONTENT_IMAGE, "");
                        content = currentArticle.contentUrl + content;
                        ImageView ivContent = new ImageView(ArticleDetailActivity.this);
                        Glide.with(ArticleDetailActivity.this).load(content).into(ivContent);
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.topMargin = Utils.dip2px(ArticleDetailActivity.this, 12);
                        llContent.addView(ivContent, params);
                    } else if (content.indexOf(Constants.CONTENT_VIDEO) != -1) {
                        content = content.replaceAll(Constants.CONTENT_VIDEO, "");
                        content = currentArticle.contentUrl + content;
                        VideoView vvContent = new VideoView(ArticleDetailActivity.this);
                        vvContent.setVideoURI(Uri.parse(content));
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT);
                        params.topMargin = Utils.dip2px(ArticleDetailActivity.this, 12);
                        llContent.addView(vvContent, params);
                    }
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.anim_exit);
    }
}
