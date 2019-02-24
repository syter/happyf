package com.sky.happyf.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.sky.happyf.adapter.ArticlePagerAdapter;
import com.sky.happyf.manager.ArticleManager;
import com.sky.happyf.util.Utils;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.ColorTransitionPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class ArticleFragment extends Fragment {
    private View view;
    private ImageView ivMenu;
    private ArticleManager articleManager;
    private int selectIndex = 0;
    private List<String> titleDataList;
    private List<View> viewList = new ArrayList<View>();
    private ViewPager viewPager;
    private ArticlePagerAdapter articlePagerAdapter;
    private PtrClassicFrameLayout ptrLayout1, ptrLayout2;
    private ListView lvArticle1, lvArticle2;
    private ArticleListAdapter adapter1, adapter2;
    private String classId, infoId;

//        lvArticle.setAdapter(adapter);


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_article, container, false);

        initView();

        initListener();

        initData();

        return view;
    }

    private void initView() {
        ivMenu = view.findViewById(R.id.iv_menu);
        viewPager = view.findViewById(R.id.view_pager);
        titleDataList = new ArrayList<>();
        titleDataList.add(getResources().getString(R.string.article_type_class));
        titleDataList.add(getResources().getString(R.string.article_type_info));


        View view1 = getLayoutInflater().inflate(R.layout.pageitem_article, null);
        View view2 = getLayoutInflater().inflate(R.layout.pageitem_article, null);

        TextView tvHidden1 = view1.findViewById(R.id.tv_hidden);
        tvHidden1.setText("0");
        TextView tvHidden2 = view2.findViewById(R.id.tv_hidden);
        tvHidden2.setText("1");

        ptrLayout1 = view1.findViewById(R.id.ptr_layout);
        ptrLayout2 = view2.findViewById(R.id.ptr_layout);
        lvArticle1 = view1.findViewById(R.id.lv_article);
        lvArticle2 = view2.findViewById(R.id.lv_article);
        adapter1 = new ArticleListAdapter(getActivity());
        adapter2 = new ArticleListAdapter(getActivity());

        viewList.add(view1);
        viewList.add(view2);

        articlePagerAdapter = new ArticlePagerAdapter(getActivity(), viewList, titleDataList);
        viewPager.setAdapter(articlePagerAdapter);
    }

    private void initListener() {
        MagicIndicator magicIndicator = view.findViewById(R.id.magic_indicator);
        CommonNavigator commonNavigator = new CommonNavigator(getActivity());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return titleDataList == null ? 0 : titleDataList.size();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, final int index) {
                SimplePagerTitleView simplePagerTitleView = new ColorTransitionPagerTitleView(context);
                simplePagerTitleView.setText(titleDataList.get(index));
                simplePagerTitleView.setTextSize(18);
                simplePagerTitleView.setNormalColor(getActivity().getColor(R.color.black));
                simplePagerTitleView.setSelectedColor(getActivity().getColor(R.color.main_color_blue));
                simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewPager.setCurrentItem(index);
                    }
                });
                return simplePagerTitleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setColors(Color.TRANSPARENT);
                return indicator;
            }
        });
        magicIndicator.setNavigator(commonNavigator);
        LinearLayout titleContainer = commonNavigator.getTitleContainer(); // must after setNavigator
        titleContainer.setShowDividers(LinearLayout.SHOW_DIVIDER_MIDDLE);
        titleContainer.setDividerPadding(Utils.dip2px(getActivity(), 15));
        titleContainer.setDividerDrawable(getResources().getDrawable(R.drawable.simple_splitter));
        ViewPagerHelper.bind(magicIndicator, viewPager);


        ptrLayout1.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrLayout1.refreshComplete();
                articleManager.getArticleList(classId, new ArticleManager.FetchArticleCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                        ptrLayout1.refreshComplete();
                    }

                    @Override
                    public void onFinish(List<Article> data) {
                        ptrLayout1.refreshComplete();
                        adapter1.applyData(data);

                        if (data.size() == 10) {
                            ptrLayout1.setLoadMoreEnable(true);
                        }
                    }
                });
            }
        });


        ptrLayout1.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                articleManager.loadMore(classId, new ArticleManager.FetchArticleCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                        ptrLayout1.refreshComplete();
                    }

                    @Override
                    public void onFinish(List<Article> data) {
                        ptrLayout1.loadMoreComplete(true);

                        adapter1.addData(data);

                        if (data.isEmpty()) {
                            ptrLayout1.setLoadMoreEnable(false);
                        }
                    }
                });
            }
        });

        lvArticle1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", adapter1.getList().get(i).id);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });


        ptrLayout2.setPtrHandler(new PtrDefaultHandler() {
            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                ptrLayout2.refreshComplete();
                articleManager.getArticleList(infoId, new ArticleManager.FetchArticleCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                        ptrLayout2.refreshComplete();
                    }

                    @Override
                    public void onFinish(List<Article> data) {
                        ptrLayout2.refreshComplete();
                        adapter2.applyData(data);

                        if (data.size() == 10) {
                            ptrLayout2.setLoadMoreEnable(true);
                        }
                    }
                });
            }
        });


        ptrLayout2.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                articleManager.loadMore(infoId, new ArticleManager.FetchArticleCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                        ptrLayout2.refreshComplete();
                    }

                    @Override
                    public void onFinish(List<Article> data) {
                        ptrLayout2.loadMoreComplete(true);
                        adapter2.addData(data);

                        if (data.isEmpty()) {
                            ptrLayout2.setLoadMoreEnable(false);
                        }
                    }
                });
            }
        });

        lvArticle2.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), ArticleDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", adapter2.getList().get(i).id);
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
        if (selectIndex == 0) {
            ptrLayout1.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ptrLayout1.autoRefresh(true);
                }
            }, 150);
        } else {
            ptrLayout2.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ptrLayout2.autoRefresh(true);
                }
            }, 150);
        }
    }
}
