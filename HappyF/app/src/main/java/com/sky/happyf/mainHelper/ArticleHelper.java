package com.sky.happyf.mainHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.orhanobut.logger.Logger;
import com.sky.happyf.Model.Article;
import com.sky.happyf.Model.Goods;
import com.sky.happyf.R;
import com.sky.happyf.activity.ArticleDetailActivity;
import com.sky.happyf.activity.GoodsDetailActivity;
import com.sky.happyf.adapter.ArticleListAdapter;
import com.sky.happyf.adapter.GoodsListAdapter;
import com.sky.happyf.manager.ArticleManager;
import com.sky.happyf.manager.GoodsManager;

import java.util.ArrayList;
import java.util.List;

public class ArticleHelper {
    private View view;
    private Activity act;
    private PtrClassicFrameLayout ptrLayout;
    private ListView lvArticle;
    private ArticleListAdapter adapter;
    private ArticleManager articleManager;
    private TextView tvClass, tvInfo;
    private int selectIndex = 0;

    public ArticleHelper(Activity act, View view) {
        this.act = act;
        this.view = view;
    }

    public void init() {
        initView();

        initListener();

        initData();
    }

    private void initView() {
        tvClass = view.findViewById(R.id.tv_class);
        tvInfo = view.findViewById(R.id.tv_info);

        ptrLayout = view.findViewById(R.id.ptr_layout);
        lvArticle = view.findViewById(R.id.lv_article);
        adapter = new ArticleListAdapter(act);
        lvArticle.setAdapter(adapter);

    }

    private void initListener() {
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrLayout.refreshComplete();
//                articleManager.getArticleList(selectIndex, new ArticleManager.FetchArticleCallback() {
//                    @Override
//                    public void onFailure(String errorMsg) {
//                        ptrLayout.refreshComplete();
//                    }
//
//                    @Override
//                    public void onFinish(List<Article> data) {
//                        ptrLayout.refreshComplete();
//
//                        adapter.applyData(data);
//                        ptrLayout.refreshComplete();
//
//                        if (!ptrLayout.isLoadMoreEnable()) {
//                            ptrLayout.setLoadMoreEnable(true);
//                        }
//                    }
//                });
            }
        });


        ptrLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
//                articleManager.loadMore(selectIndex, new ArticleManager.FetchArticleCallback() {
//                    @Override
//                    public void onFailure(String errorMsg) {
//                        ptrLayout.refreshComplete();
//                    }
//
//                    @Override
//                    public void onFinish(List<Article> data) {
//                        ptrLayout.loadMoreComplete(true);
//
//                        adapter.applyData(data);
//                        ptrLayout.refreshComplete();
//
//                        if (data.isEmpty()) {
//                            ptrLayout.setLoadMoreEnable(false);
//                        }
//                    }
//                });
            }
        });


        tvClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIndex = 0;
                processSelectIndexView();
                initArticleData();
            }
        });
        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIndex = 1;
                processSelectIndexView();
                initArticleData();
            }
        });


        lvArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(act, ArticleDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", adapter.getList().get(i).id);
                intent.putExtras(bundle);
                act.startActivity(intent);
                act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });
    }

    private void initData() {
        articleManager = new ArticleManager(act);
        processSelectIndexView();

        initArticleData();
    }

    private void processSelectIndexView() {
        if (selectIndex == 0) {
            tvClass.setTextColor(act.getColor(R.color.main_color_blue));
            tvInfo.setTextColor(act.getColor(R.color.black));
        } else if (selectIndex == 1) {
            tvClass.setTextColor(act.getColor(R.color.black));
            tvInfo.setTextColor(act.getColor(R.color.main_color_blue));
        }
    }

    private void initArticleData() {
        ptrLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrLayout.autoRefresh(true);
            }
        }, 150);
    }
}
