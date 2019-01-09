package com.sky.happyf.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

import com.sky.happyf.R;
import com.sky.happyf.mainHelper.ArticleHelper;
import com.sky.happyf.mainHelper.HappyHelper;
import com.sky.happyf.mainHelper.MainHelper;
import com.sky.happyf.mainHelper.MineHelper;
import com.sky.happyf.mainHelper.ShopHelper;
import com.wuhenzhizao.titlebar.statusbar.StatusBarUtils;

import java.util.ArrayList;
import java.util.List;

import devlight.io.library.ntb.NavigationTabBar;

public class MainActivity extends AppCompatActivity {

    private NavigationTabBar navigationTabBar;
    private ViewPager viewPager;
    private List<View> viewList;
    private ShopHelper shopHelper;
    private HappyHelper happyHelper;
    private MainHelper mainHelper;
    private ArticleHelper articleHelper;
    private MineHelper mineHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //设置标题栏和状态栏
        getSupportActionBar().hide();
        StatusBarUtils.setDarkMode(getWindow());
        StatusBarUtils.setStatusBarColor(getWindow(), getColor(R.color.white), 0);

        initView();

        initData();
    }

    private void initView() {
        navigationTabBar = (NavigationTabBar) findViewById(R.id.ntb_horizontal);
        viewPager = (ViewPager) findViewById(R.id.vp_horizontal_ntb);

        viewList = new ArrayList<View>();
        View shopView = LayoutInflater.from(
                getBaseContext()).inflate(R.layout.item_shop, null, false);
        View happyView = LayoutInflater.from(
                getBaseContext()).inflate(R.layout.item_happy, null, false);
        View mainView = LayoutInflater.from(
                getBaseContext()).inflate(R.layout.item_main, null, false);
        View articleView = LayoutInflater.from(
                getBaseContext()).inflate(R.layout.item_article, null, false);
        View mineView = LayoutInflater.from(
                getBaseContext()).inflate(R.layout.item_mine, null, false);

        viewList.add(shopView);
        viewList.add(happyView);
        viewList.add(mainView);
        viewList.add(articleView);
        viewList.add(mineView);

        shopHelper = new ShopHelper(MainActivity.this, shopView);
        shopHelper.init();
        happyHelper = new HappyHelper(MainActivity.this, happyView);
        happyHelper.init();
        mainHelper = new MainHelper(MainActivity.this, mainView);
        mainHelper.init();
        articleHelper = new ArticleHelper(MainActivity.this, articleView);
        articleHelper.init();
        mineHelper = new MineHelper(MainActivity.this, mineView);
        mineHelper.init();

    }


    @Override
    protected void onResume() {
        super.onResume();
    }

    private void initData() {
        viewPager.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return 5;
            }

            @Override
            public boolean isViewFromObject(final View view, final Object object) {
                return view.equals(object);
            }

            @Override
            public void destroyItem(final View container, final int position, final Object object) {
                ((ViewPager) container).removeView((View) object);
            }

            @Override
            public Object instantiateItem(final ViewGroup container, final int position) {
                View view = viewList.get(position);
                container.addView(view);
                return view;
            }
        });

        ArrayList<NavigationTabBar.Model> models = new ArrayList<>();
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_first),
                        getColor(R.color.mediumslateblue)
                ).title(getResources().getString(R.string.main_first))
//                        .badgeTitle("NTB")
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_second),
                        getColor(R.color.mediumslateblue)
                ).title(getResources().getString(R.string.main_second))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_third),
                        getColor(R.color.mediumslateblue)
                ).title(getResources().getString(R.string.main_third))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_fourth),
                        getColor(R.color.mediumslateblue)
                ).title(getResources().getString(R.string.main_forth))
                        .build()
        );
        models.add(
                new NavigationTabBar.Model.Builder(
                        getResources().getDrawable(R.drawable.ic_fifth),
                        getColor(R.color.mediumslateblue)
                ).title(getResources().getString(R.string.main_fifth))
                        .build()
        );

        navigationTabBar.setIsBadged(false);
        navigationTabBar.setModels(models);
        navigationTabBar.setInactiveColor(Color.WHITE);
        navigationTabBar.setActiveColor(getColor(R.color.skyblue));
        navigationTabBar.setBgColor(getColor(R.color.deepskyblue));
        navigationTabBar.setViewPager(viewPager, 2);
        navigationTabBar.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(final int position, final float positionOffset, final int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(final int position) {
                navigationTabBar.getModels().get(position).hideBadge();
            }

            @Override
            public void onPageScrollStateChanged(final int state) {

            }
        });
    }
}
