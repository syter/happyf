package com.sky.happyf.mainHelper;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.orhanobut.logger.Logger;
import com.sky.happyf.Model.Goods;
import com.sky.happyf.Model.SmallType;
import com.sky.happyf.R;
import com.sky.happyf.activity.CartListActivity;
import com.sky.happyf.activity.GoodsDetailActivity;
import com.sky.happyf.activity.SearchGoodsActivity;
import com.sky.happyf.adapter.GoodsListAdapter;
import com.sky.happyf.adapter.ShopSmallTypeAdapter;
import com.sky.happyf.manager.GoodsManager;
import com.sky.happyf.util.GlideImageLoader;
import com.sky.happyf.util.Utils;
import com.sky.happyf.view.HorizontalListView;
import com.sky.happyf.view.MyListView;
import com.youth.banner.Banner;

import java.util.ArrayList;
import java.util.List;

public class ShopHelper {
    private View view;
    private Activity act;
    private PtrClassicFrameLayout ptrLayout;
    private MyListView lvGoods;
    private GoodsListAdapter adapter;
    private GoodsManager goodsManager;
    private Banner banner;
    private RelativeLayout rlBanner, rlCart;
    private LinearLayout llLeftMenu, llRightClose, llTitleSearch, llQuickway, llLoadMore;
    private ImageView ivMenu;
    private ScrollView svMain;
    private boolean isSearchGone = false;
    private boolean isSmallTypeGone = false;
    private HorizontalListView smallTypeListView;
    private ShopSmallTypeAdapter shopSmallTypeAdapter;
    private List<SmallType> typeList = new ArrayList<>();
    private int showSmallTypeHeight;
    private EditText etSearch1, etSearch2;

    public ShopHelper(Activity act, View view) {
        this.act = act;
        this.view = view;
    }

    public void init() {
        initView();

        initListener();

        initData();
    }

    private void initView() {
        etSearch1 = view.findViewById(R.id.et_search1);
        etSearch2 = view.findViewById(R.id.et_search2);
        ivMenu = view.findViewById(R.id.iv_menu);
        llLeftMenu = view.findViewById(R.id.ll_left_menu);
        llRightClose = view.findViewById(R.id.ll_right_close);
        llTitleSearch = view.findViewById(R.id.ll_title_search);
        llQuickway = view.findViewById(R.id.ll_quickway);
        llLoadMore = view.findViewById(R.id.ll_load_more);
        rlCart = view.findViewById(R.id.rl_cart);

//        ptrLayout = view.findViewById(R.id.ptr_layout);
        lvGoods = view.findViewById(R.id.lv_goods);
        adapter = new GoodsListAdapter(act);
        lvGoods.setAdapter(adapter);
        svMain = view.findViewById(R.id.sv_main);

        rlBanner = view.findViewById(R.id.rl_banner);
        banner = view.findViewById(R.id.banner);
        DisplayMetrics dm = act.getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int realBannerHeight = 182 * screenWidth / 375;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlBanner.getLayoutParams();
        params.height = realBannerHeight;
        rlBanner.setLayoutParams(params);

        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) llQuickway.getLayoutParams();
        showSmallTypeHeight += realBannerHeight + params2.height - Utils.dip2px(act, 30);

        smallTypeListView = view.findViewById(R.id.horizontal_lv);
        shopSmallTypeAdapter = new ShopSmallTypeAdapter(act);
        smallTypeListView.setAdapter(shopSmallTypeAdapter);
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        rlCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.startActivity(new Intent(act, CartListActivity.class));
                act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
            }
        });

        llRightClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMenu();
            }
        });

        svMain.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                // TODO Auto-generated method stub
                switch (event.getAction()) {
                    case MotionEvent.ACTION_MOVE: {

                        break;
                    }
                    case MotionEvent.ACTION_DOWN: {
                        break;
                    }
                    case MotionEvent.ACTION_UP: {
                        // 滑动到底部，加载更多商品
                        if (svMain.getChildAt(0).getMeasuredHeight() <= svMain.getScrollY() + svMain.getHeight()) {
                            llLoadMore.setVisibility(View.VISIBLE);
                            goodsManager.loadMore(0, new GoodsManager.FetchGoodsCallback() {
                                @Override
                                public void onFailure(String errorMsg) {
                                    llLoadMore.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFinish(List<Goods> data) {
                                    llLoadMore.setVisibility(View.GONE);

                                    adapter.applyData(data);
                                    adapter.notifyDataSetChanged();

                                }
                            });
                        } else {

                        }
                        break;
                    }
                }
                return false;
            }
        });

        svMain.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int x, int y, int oldx, int oldy) {
                Logger.e("now y = " + y);
                if (y >= Utils.dip2px(act, 35)) {
                    if (isSearchGone) {
                        llTitleSearch.setVisibility(View.VISIBLE);
                        TranslateAnimation visibleAnim = new TranslateAnimation(0, 0, 150, 0);
                        visibleAnim.setDuration(200);
                        visibleAnim.setFillAfter(true);
                        llTitleSearch.setAnimation(visibleAnim);
                        isSearchGone = false;
                    }
                } else {
                    if (!isSearchGone) {
                        llTitleSearch.setVisibility(View.GONE);
                        TranslateAnimation goneAnim = new TranslateAnimation(0, 0, 0, 150);
                        goneAnim.setDuration(200);
                        goneAnim.setFillAfter(true);
                        llTitleSearch.setAnimation(goneAnim);
                        isSearchGone = true;
                    }
                }

                if (isSmallTypeGone) {
                    if (y > showSmallTypeHeight) {
                        smallTypeListView.setVisibility(View.VISIBLE);
                        isSmallTypeGone = false;
                    }
                } else {
                    if (y <= showSmallTypeHeight + Utils.dip2px(act, 30)) {
                        smallTypeListView.setVisibility(View.GONE);
                        isSmallTypeGone = true;
                    }
                }


            }
        });

        lvGoods.setOnLoadMoreListener(new MyListView.OnLoadMoreListener() {
            @Override
            public void onloadMore() {

            }
        });


        lvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(act, GoodsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", adapter.getList().get(i).id);
                intent.putExtras(bundle);
                act.startActivity(intent);
                act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });

        smallTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                for (SmallType st : typeList) {
                    st.isSelected = false;
                }
                SmallType st = typeList.get(i);
                st.isSelected = true;
                shopSmallTypeAdapter.applyData(typeList);
                shopSmallTypeAdapter.notifyDataSetChanged();
            }
        });

        etSearch1.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    // 点击搜索要做的操作
                    Intent intent = new Intent(act, SearchGoodsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("content", etSearch1.getText().toString());
                    intent.putExtras(bundle);
                    act.startActivity(intent);
                    act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                }
                return false;
            }
        });
        etSearch2.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    // 点击搜索要做的操作
                    Intent intent = new Intent(act, SearchGoodsActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("content", etSearch2.getText().toString());
                    intent.putExtras(bundle);
                    act.startActivity(intent);
                    act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                }
                return false;
            }
        });
    }

    private void closeMenu() {
        llLeftMenu.setAnimation(AnimationUtils.makeOutAnimation(act, false));
        llLeftMenu.setVisibility(View.GONE);
    }

    private void showMenu() {
        llLeftMenu.setAnimation(AnimationUtils.makeInAnimation(act, true));
        llLeftMenu.setVisibility(View.VISIBLE);
    }

    private void initData() {
        goodsManager = new GoodsManager(act);

        initGoodsData();

        List<String> images = new ArrayList<>();
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546591312549&di=5e6a35970d026692b6fd4dbbec89cdf1&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D285966d73cd3d539d530078052ee8325%2Fb7003af33a87e950d499b5451a385343fbf2b409.jpg");
        images.add("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546591347518&di=05c38408f670c180b735a7a9d769746b&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F9922720e0cf3d7caceea8850f81fbe096b63a9fc.jpg");
        banner.setImages(images).setImageLoader(new GlideImageLoader()).start();

        SmallType st = new SmallType();
        st.typeName = "休闲食品";
        st.isSelected = true;
        typeList.add(st);
        for (int i = 0; i < 10; i++) {
            SmallType st2 = new SmallType();
            st2.typeName = "其它食品" + i;
            st2.isSelected = false;
            typeList.add(st2);
        }
        shopSmallTypeAdapter.applyData(typeList);
        shopSmallTypeAdapter.notifyDataSetChanged();
    }


    private void initGoodsData() {
        goodsManager.init(0, new GoodsManager.FetchGoodsCallback() {
            @Override
            public void onFailure(String errorMsg) {
            }

            @Override
            public void onFinish(List<Goods> data) {
                adapter.applyData(data);
                adapter.notifyDataSetChanged();

//                if (!ptrLayout.isLoadMoreEnable()) {
//                    ptrLayout.setLoadMoreEnable(true);
//                }
            }
        });
    }
}
