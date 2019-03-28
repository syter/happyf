package com.sky.happyf.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.happyf.R;
import com.sky.happyf.fragment.ArticleFragment;
import com.sky.happyf.fragment.HappyFragment;
import com.sky.happyf.fragment.MainFragment;
import com.sky.happyf.fragment.MineFragment;
import com.sky.happyf.fragment.ShopFragment;
import com.sky.happyf.message.MessageEvent;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.Utils;
import com.wuhenzhizao.titlebar.statusbar.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {
    private LinearLayout llShop, llHappy, llMain, llArticle, llMine;
    private ImageView ivShopBar, ivHappyBar, ivMainBar, ivArticleBar, ivMineBar;
    private TextView tvShopBar, tvHappyBar, tvMainBar, tvArticleBar, tvMineBar;

    public static final int PAGE_SHOP = 0;
    public static final int PAGE_HAPPY = 1;
    public static final int PAGE_MAIN = 2;
    public static final int PAGE_ARTICLE = 3;
    public static final int PAGE_MINE = 4;

    private HashMap<Integer, Fragment> fragments = new HashMap<>();
    private int fragmentContentId = R.id.fragment_content;
    private int currentTab;

//    private SlidingMenu slidingMenu;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        EventBus.getDefault().register(this);

        //设置标题栏和状态栏
        getSupportActionBar().hide();
        StatusBarUtils.setDarkMode(getWindow());
        StatusBarUtils.setStatusBarColor(getWindow(), getColor(R.color.white), 0);

        initView();

        initListener();

        initData();
    }

    private void initView() {
        llShop = findViewById(R.id.ll_shop);
        llHappy = findViewById(R.id.ll_happy);
        llMain = findViewById(R.id.ll_main);
        llArticle = findViewById(R.id.ll_article);
        llMine = findViewById(R.id.ll_mine);

        ivShopBar = findViewById(R.id.iv_shop_bar);
        ivHappyBar = findViewById(R.id.iv_happy_bar);
        ivMainBar = findViewById(R.id.iv_main_bar);
        ivArticleBar = findViewById(R.id.iv_article_bar);
        ivMineBar = findViewById(R.id.iv_mine_bar);

        tvShopBar = findViewById(R.id.tv_shop_bar);
        tvHappyBar = findViewById(R.id.tv_happy_bar);
        tvMainBar = findViewById(R.id.tv_main_bar);
        tvArticleBar = findViewById(R.id.tv_article_bar);
        tvMineBar = findViewById(R.id.tv_mine_bar);

        fragments.put(PAGE_SHOP, new ShopFragment());
        fragments.put(PAGE_HAPPY, new HappyFragment());
        fragments.put(PAGE_MAIN, new MainFragment());
        fragments.put(PAGE_ARTICLE, new ArticleFragment());
        fragments.put(PAGE_MINE, new MineFragment());

        // 设置默认的Fragment
        defaultFragment();
    }

    private void defaultFragment() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(fragmentContentId, fragments.get(PAGE_MAIN));
        currentTab = PAGE_MAIN;
        ft.commit();
    }

    private void initListener() {
        llShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTab(PAGE_SHOP);
            }
        });
        llHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTab(PAGE_HAPPY);
            }
        });
        llMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTab(PAGE_MAIN);
            }
        });
        llArticle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTab(PAGE_ARTICLE);
            }
        });
        llMine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTab(PAGE_MINE);
            }
        });
    }

    /**
     * 点击切换下部按钮
     *
     * @param page
     */
    private void changeTab(int page) {
        //默认的currentTab == 当前的页码，不做任何处理
        if (currentTab == page) {
            return;
        }

        if (currentTab == 0) {
            ivShopBar.setImageDrawable(getDrawable(R.drawable.shop_default));
            tvShopBar.setTextColor(getColor(R.color.gray_text_2));
        } else if (currentTab == 1) {
            ivHappyBar.setImageDrawable(getDrawable(R.drawable.happy_default));
            tvHappyBar.setTextColor(getColor(R.color.gray_text_2));
        } else if (currentTab == 2) {
            ivMainBar.setImageDrawable(getDrawable(R.drawable.main_default));
            tvMainBar.setTextColor(getColor(R.color.gray_text_2));
        } else if (currentTab == 3) {
            ivArticleBar.setImageDrawable(getDrawable(R.drawable.article_default));
            tvArticleBar.setTextColor(getColor(R.color.gray_text_2));
        } else if (currentTab == 4) {
            ivMineBar.setImageDrawable(getDrawable(R.drawable.mine_default));
            tvMineBar.setTextColor(getColor(R.color.gray_text_2));
        }

        //获取fragment的页码
        Fragment fragment = fragments.get(page);
        //fragment事务
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        //如果该Fragment对象被添加到了它的Activity中，那么它返回true，否则返回false。
        //当前activity中添加的不是这个fragment
        if (!fragment.isAdded()) {
            //所以将他加进去
            ft.add(fragmentContentId, fragment);
        }
        //隐藏当前currentTab的
        ft.hide(fragments.get(currentTab));
        //显示现在page的
        ft.show(fragments.get(page));
        //设置当前currentTab底部的状态
//        SelectColor(currentTab);
        //当前显示的赋值给currentTab
        currentTab = page;
        //设置当前currentTab底部的状态
//        SelectColor(currentTab);
        //activity被销毁？  ！否
        if (!this.isFinishing()) {
            //允许状态丢失
            ft.commitAllowingStateLoss();
        }


        if (page == 0) {
            ivShopBar.setImageDrawable(getDrawable(R.drawable.shop_active));
            tvShopBar.setTextColor(getColor(R.color.main_color_blue));
        } else if (currentTab == 1) {
            ivHappyBar.setImageDrawable(getDrawable(R.drawable.happy_active));
            tvHappyBar.setTextColor(getColor(R.color.main_color_blue));
        } else if (currentTab == 2) {
            ivMainBar.setImageDrawable(getDrawable(R.drawable.main_active));
            tvMainBar.setTextColor(getColor(R.color.main_color_blue));
        } else if (currentTab == 3) {
            ivArticleBar.setImageDrawable(getDrawable(R.drawable.article_active));
            tvArticleBar.setTextColor(getColor(R.color.main_color_blue));
        } else if (currentTab == 4) {
            ivMineBar.setImageDrawable(getDrawable(R.drawable.mine_active));
            tvMineBar.setTextColor(getColor(R.color.main_color_blue));
        }

        Utils.hideKeyboard(llShop);
    }


    private void initData() {

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().unregister(this);
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(MessageEvent messageEvent) {
        if (Constants.EVENT_MESSAGE_EDIT_USER.equals(messageEvent.getMessage()) ||
                Constants.EVENT_MESSAGE_LOGIN.equals(messageEvent.getMessage())) {
            ((MineFragment) fragments.get(PAGE_MINE)).initData();
        } else {
            ((ShopFragment) fragments.get(PAGE_SHOP)).initCart();
        }
    }
}
