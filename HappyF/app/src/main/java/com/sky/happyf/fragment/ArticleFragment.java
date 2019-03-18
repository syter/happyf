package com.sky.happyf.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.sky.happyf.Model.Article;
import com.sky.happyf.R;
import com.sky.happyf.activity.ArticleDetailActivity;
import com.sky.happyf.adapter.ArticleListAdapter;
import com.sky.happyf.manager.ArticleManager;
import com.sky.happyf.util.Utils;

import java.util.List;

public class ArticleFragment extends Fragment {
    private View view;
    private ArticleManager articleManager;
    private int selectIndex = 0;
    private TextView tvClass, tvInfo;
    private PtrClassicFrameLayout ptrLayout;
    private ListView lvArticle;
    private ArticleListAdapter adapter;
    private String classId, infoId;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_article, container, false);

        initView();

        initListener();

        initData();

        return view;
    }

    private void initView() {
        tvClass = view.findViewById(R.id.tv_article_class);
        tvInfo = view.findViewById(R.id.tv_article_info);
        ptrLayout = view.findViewById(R.id.ptr_layout);
        lvArticle = view.findViewById(R.id.lv_article);
        adapter = new ArticleListAdapter(getActivity());
        lvArticle.setAdapter(adapter);
    }

    private void initListener() {
        tvClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectIndex == 1) {
                    tvClass.setTextColor(getActivity().getColor(R.color.main_color_blue));
                    tvInfo.setTextColor(getActivity().getColor(R.color.gray_text_1));
                    selectIndex = 0;
                    initArticleData();
                }
            }
        });
        tvInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (selectIndex == 0) {
                    tvClass.setTextColor(getActivity().getColor(R.color.gray_text_1));
                    tvInfo.setTextColor(getActivity().getColor(R.color.main_color_blue));
                    selectIndex = 1;
                    initArticleData();
                }
            }
        });


        ptrLayout.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrLayout.refreshComplete();
                String cateId = "";
                if (selectIndex == 0) {
                    cateId = classId;
                } else {
                    cateId = infoId;
                }
                articleManager.getArticleList(cateId, new ArticleManager.FetchArticleCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                        ptrLayout.refreshComplete();
                    }

                    @Override
                    public void onFinish(List<Article> data) {
                        ptrLayout.refreshComplete();
                        adapter.applyData(data);

                        if (data.size() == 10) {
                            ptrLayout.setLoadMoreEnable(true);
                        }
                    }
                });
            }
        });


        ptrLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                String cateId = "";
                if (selectIndex == 0) {
                    cateId = classId;
                } else {
                    cateId = infoId;
                }
                articleManager.loadMore(cateId, new ArticleManager.FetchArticleCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                        ptrLayout.refreshComplete();
                    }

                    @Override
                    public void onFinish(List<Article> data) {
                        ptrLayout.loadMoreComplete(true);

                        adapter.addData(data);

                        if (data.isEmpty()) {
                            ptrLayout.setLoadMoreEnable(false);
                        }
                    }
                });
            }
        });

        lvArticle.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", adapter.getList().get(i).id);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });
    }

    private void initData() {
        articleManager = new ArticleManager(getActivity());

        initCategory();
    }

    private void initCategory() {
        if (Utils.isEmptyString(classId)) {
            articleManager.getCategory(new ArticleManager.FetchCategoryCallback() {
                @Override
                public void onFailure(String errorMsg) {
                    Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinish(List<String> data) {
                    if (data.size() >= 2) {
                        classId = data.get(0);
                        infoId = data.get(1);
                        initArticleData();
                    } else {
                        Toast.makeText(getActivity(), getResources().getString(R.string.common_error), Toast.LENGTH_LONG).show();
                    }
                }
            });
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
