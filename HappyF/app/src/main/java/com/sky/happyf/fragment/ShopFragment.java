package com.sky.happyf.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.sky.happyf.Model.CartPrice;
import com.sky.happyf.Model.Goods;
import com.sky.happyf.Model.QuickwayType;
import com.sky.happyf.Model.ShopBanner;
import com.sky.happyf.Model.SmallType;
import com.sky.happyf.Model.Type;
import com.sky.happyf.R;
import com.sky.happyf.activity.CartListActivity;
import com.sky.happyf.activity.GoodsDetailActivity;
import com.sky.happyf.activity.LoginActivity;
import com.sky.happyf.activity.SearchGoodsActivity;
import com.sky.happyf.adapter.GoodsListAdapter;
import com.sky.happyf.adapter.ShopSmallTypeAdapter;
import com.sky.happyf.manager.GoodsManager;
import com.sky.happyf.manager.UserManager;
import com.sky.happyf.util.GlideImageLoader;
import com.sky.happyf.util.Utils;
import com.sky.happyf.view.HorizontalListView;
import com.sky.happyf.view.MyListView;
import com.wang.avi.AVLoadingIndicatorView;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class ShopFragment extends Fragment {
    private View view;

    private Handler handler = new Handler();

    private List<View> viewList = new ArrayList<>();
    private PtrClassicFrameLayout ptrLayout;
    private MyListView lvGoods;
    private GoodsListAdapter adapter;
    private GoodsManager goodsManager;
    private UserManager userManager;
    private Banner banner;
    private RelativeLayout rlBanner, rlCart;
    private LinearLayout llLeftMenu, llTitleSearch, llQuickway, llLoadMore, llType1,
            llType2, llType3, llType4, llQuickway1, llQuickway2, llQuickway3, llQuickway4, llQuickway5,
            llQuickway6, llQuickway7, llQuickway8;
    private EditText etSearch1, etSearch2;
    private ImageView ivMenu, ivType1, ivType2, ivType3, ivType4, ivQuickway1, ivQuickway2, ivQuickway3,
            ivQuickway4, ivQuickway5, ivQuickway6, ivQuickway7, ivQuickway8;
    private TextView tvType1, tvType2, tvType3, tvType4, tvQuickway1, tvQuickway2, tvQuickway3, tvQuickway4,
            tvQuickway5, tvQuickway6, tvQuickway7, tvQuickway8, tvCartPrice;
    private ScrollView svMain;
    private boolean isSearchGone = false;
    private boolean isSmallTypeGone = false;
    private HorizontalListView smallTypeListView;
    private ShopSmallTypeAdapter shopSmallTypeAdapter;
    private List<Type> typeList = new ArrayList<>();
    private String currentSelectSmallTypeId;
    private List<SmallType> currentSmallTypeList;
    private List<Goods> goodsList;
    private int showSmallTypeHeight;
    private AVLoadingIndicatorView loadingView;
    private DrawerLayout dlMain;

    private RelativeLayout rlHideKBView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_shop, container, false);

        initView();

        initListener();

        initData();

        return view;
    }

    private void initView() {
        dlMain = view.findViewById(R.id.dl_main);
        etSearch1 = view.findViewById(R.id.et_search1);
        etSearch2 = view.findViewById(R.id.et_search2);
        ivMenu = view.findViewById(R.id.iv_menu);
        llLeftMenu = view.findViewById(R.id.ll_left_menu);
        llTitleSearch = view.findViewById(R.id.ll_title_search);
        llQuickway = view.findViewById(R.id.ll_quickway);
        llLoadMore = view.findViewById(R.id.ll_load_more);
        rlCart = view.findViewById(R.id.rl_cart);
        tvCartPrice = view.findViewById(R.id.tv_cart_price);
        loadingView = view.findViewById(R.id.loadingview);

        llType1 = view.findViewById(R.id.ll_type1);
        llType2 = view.findViewById(R.id.ll_type2);
        llType3 = view.findViewById(R.id.ll_type3);
        llType4 = view.findViewById(R.id.ll_type4);
        ivType1 = view.findViewById(R.id.iv_type1);
        ivType2 = view.findViewById(R.id.iv_type2);
        ivType3 = view.findViewById(R.id.iv_type3);
        ivType4 = view.findViewById(R.id.iv_type4);
        tvType1 = view.findViewById(R.id.tv_type1);
        tvType2 = view.findViewById(R.id.tv_type2);
        tvType3 = view.findViewById(R.id.tv_type3);
        tvType4 = view.findViewById(R.id.tv_type4);
        llQuickway1 = view.findViewById(R.id.ll_quickway1);
        llQuickway2 = view.findViewById(R.id.ll_quickway2);
        llQuickway3 = view.findViewById(R.id.ll_quickway3);
        llQuickway4 = view.findViewById(R.id.ll_quickway4);
        llQuickway5 = view.findViewById(R.id.ll_quickway5);
        llQuickway6 = view.findViewById(R.id.ll_quickway6);
        llQuickway7 = view.findViewById(R.id.ll_quickway7);
        llQuickway8 = view.findViewById(R.id.ll_quickway8);
        ivQuickway1 = view.findViewById(R.id.iv_quickway1);
        ivQuickway2 = view.findViewById(R.id.iv_quickway2);
        ivQuickway3 = view.findViewById(R.id.iv_quickway3);
        ivQuickway4 = view.findViewById(R.id.iv_quickway4);
        ivQuickway5 = view.findViewById(R.id.iv_quickway5);
        ivQuickway6 = view.findViewById(R.id.iv_quickway6);
        ivQuickway7 = view.findViewById(R.id.iv_quickway7);
        ivQuickway8 = view.findViewById(R.id.iv_quickway8);
        tvQuickway1 = view.findViewById(R.id.tv_quickway1);
        tvQuickway2 = view.findViewById(R.id.tv_quickway2);
        tvQuickway3 = view.findViewById(R.id.tv_quickway3);
        tvQuickway4 = view.findViewById(R.id.tv_quickway4);
        tvQuickway5 = view.findViewById(R.id.tv_quickway5);
        tvQuickway6 = view.findViewById(R.id.tv_quickway6);
        tvQuickway7 = view.findViewById(R.id.tv_quickway7);
        tvQuickway8 = view.findViewById(R.id.tv_quickway8);

        rlHideKBView = view.findViewById(R.id.rl_hideKBView);

        lvGoods = view.findViewById(R.id.lv_goods);
        adapter = new GoodsListAdapter(getActivity());
        lvGoods.setAdapter(adapter);
        svMain = view.findViewById(R.id.sv_main);

        rlBanner = view.findViewById(R.id.rl_banner);
        banner = view.findViewById(R.id.banner);
        banner.setDelayTime(3000);
        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int realBannerHeight = 182 * screenWidth / 375;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlBanner.getLayoutParams();
        params.height = realBannerHeight;
        rlBanner.setLayoutParams(params);

        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) llQuickway.getLayoutParams();
        showSmallTypeHeight += realBannerHeight + params2.height - Utils.dip2px(getActivity(), 30);

        smallTypeListView = view.findViewById(R.id.horizontal_lv);
        shopSmallTypeAdapter = new ShopSmallTypeAdapter(getActivity());
        smallTypeListView.setAdapter(shopSmallTypeAdapter);

        viewList.add(etSearch1);
        viewList.add(etSearch2);

        rlHideKBView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(viewList.get(0));
            }
        });

        rlBanner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(viewList.get(0));
            }
        });

        svMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Utils.hideKeyboard(viewList.get(0));
            }
        });

        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Utils.hideKeyboard(viewList.get(0));
            }
        });


    }

    @SuppressLint("ClickableViewAccessibility")
    private void initListener() {
        rlCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userManager.isUserLogin()) {
                    getActivity().startActivity(new Intent(getActivity(), CartListActivity.class));
                    getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }
            }
        });

        ivMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
            }
        });

        svMain.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
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
                            goodsManager.loadMoreGoods(currentSelectSmallTypeId, new GoodsManager.FetchGoodsCallback() {
                                @Override
                                public void onFailure(String errorMsg) {
                                    llLoadMore.setVisibility(View.GONE);
                                }

                                @Override
                                public void onFinish(List<Goods> data) {
                                    llLoadMore.setVisibility(View.GONE);
                                    goodsList.addAll(data);
                                    adapter.applyData(goodsList);
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
//                Logger.e("now y = " + y);
                if (y >= Utils.dip2px(getActivity(), 35)) {
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
                    if (y <= showSmallTypeHeight + Utils.dip2px(getActivity(), 30)) {
                        smallTypeListView.setVisibility(View.GONE);
                        isSmallTypeGone = true;
                    }
                }
            }
        });

        lvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utils.hideKeyboard(viewList.get(0));

                Intent intent = new Intent(getActivity(), GoodsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", adapter.getList().get(i).id);
                intent.putExtras(bundle);
                getActivity().startActivity(intent);
                getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });

        smallTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utils.hideKeyboard(viewList.get(0));

                for (SmallType st : currentSmallTypeList) {
                    st.isSelected = false;
                }
                SmallType st = currentSmallTypeList.get(i);
                st.isSelected = true;
                currentSelectSmallTypeId = st.id;
                shopSmallTypeAdapter.applyData(currentSmallTypeList);

                initGoodsData();
            }
        });

        etSearch1.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    String content = etSearch1.getText().toString();
                    if (Utils.isEmptyString(content)) {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.string_null_error), Toast.LENGTH_LONG).show();
                    } else {
                        // 点击搜索要做的操作
                        Intent intent = new Intent(getActivity(), SearchGoodsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("content", content);
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                        Utils.hideKeyboard(viewList.get(0));

                        etSearch1.setText("");
                        etSearch2.setText("");
                    }

                }
                return false;
            }
        });
        etSearch2.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    String content = etSearch2.getText().toString();
                    if (Utils.isEmptyString(content)) {
                        Toast.makeText(getActivity(), getActivity().getResources().getString(R.string.string_null_error), Toast.LENGTH_LONG).show();
                    } else {
                        // 点击搜索要做的操作
                        Intent intent = new Intent(getActivity(), SearchGoodsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("content", content);
                        intent.putExtras(bundle);
                        getActivity().startActivity(intent);
                        getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                    }

                }
                return false;
            }
        });
    }

    private void closeMenu() {
        dlMain.closeDrawer(Gravity.START);
    }

    private void showMenu() {
        Utils.hideKeyboard(viewList.get(0));
        dlMain.openDrawer(Gravity.START);
    }

    private void initData() {
        goodsManager = new GoodsManager(getActivity());
        userManager = new UserManager(getActivity());

        initBanners();

        initTypes();

        initQuickways();

        initCart();
    }

    private void initBanners() {
        goodsManager.getBanners(new GoodsManager.FetchBannersCallback() {
            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(List<ShopBanner> data) {
                List<String> images = new ArrayList<>();
                for (ShopBanner sb : data) {
                    images.add(sb.url);
                }
                banner.setImages(images).setImageLoader(new GlideImageLoader()).start();
            }
        });
    }

    private void initTypes() {
        goodsManager.getTypes(new GoodsManager.FetchTypesCallback() {
            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(final List<Type> data) {
                typeList = data;
                Glide.with(getActivity()).load(data.get(0).cover).into(ivType1);
                Glide.with(getActivity()).load(data.get(1).cover).into(ivType2);
                Glide.with(getActivity()).load(data.get(2).cover).into(ivType3);
                Glide.with(getActivity()).load(data.get(3).cover).into(ivType4);
                tvType1.setText(data.get(0).name);
                tvType2.setText(data.get(1).name);
                tvType3.setText(data.get(2).name);
                tvType4.setText(data.get(3).name);


                currentSmallTypeList = data.get(0).smallTypeList;
                currentSelectSmallTypeId = currentSmallTypeList.get(0).id;

                shopSmallTypeAdapter.applyData(currentSmallTypeList);

                initGoodsData();

                llType1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setTypeClickListener(0);
                    }
                });
                llType2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setTypeClickListener(1);
                    }
                });
                llType3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setTypeClickListener(2);
                    }
                });
                llType4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setTypeClickListener(3);
                    }
                });
            }
        });
    }

    private void initGoodsData() {
        loadingView.setVisibility(View.VISIBLE);
        goodsList = new ArrayList<>();
        adapter.applyData(goodsList);
        goodsManager.getGoodsList(currentSelectSmallTypeId, new GoodsManager.FetchGoodsCallback() {
            @Override
            public void onFailure(String errorMsg) {
                loadingView.setVisibility(View.GONE);
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(List<Goods> data) {
                loadingView.setVisibility(View.GONE);
                goodsList = data;
                adapter.applyData(goodsList);

                if (smallTypeListView.getVisibility() == View.GONE) {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            svMain.smoothScrollTo(0, lvGoods.getTop()+10);
                        }
                    }, 100);
                }
            }
        });
    }

    private void initQuickways() {
        goodsManager.getQuickways(new GoodsManager.FetchQuickwayTypesCallback() {
            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(final List<QuickwayType> data) {
                Glide.with(getActivity()).load(data.get(0).cover).into(ivQuickway1);
                Glide.with(getActivity()).load(data.get(1).cover).into(ivQuickway2);
                Glide.with(getActivity()).load(data.get(2).cover).into(ivQuickway3);
                Glide.with(getActivity()).load(data.get(3).cover).into(ivQuickway4);
                Glide.with(getActivity()).load(data.get(4).cover).into(ivQuickway5);
                Glide.with(getActivity()).load(data.get(5).cover).into(ivQuickway6);
                Glide.with(getActivity()).load(data.get(6).cover).into(ivQuickway7);
                Glide.with(getActivity()).load(data.get(7).cover).into(ivQuickway8);
                tvQuickway1.setText(data.get(0).name);
                tvQuickway2.setText(data.get(1).name);
                tvQuickway3.setText(data.get(2).name);
                tvQuickway4.setText(data.get(3).name);
                tvQuickway5.setText(data.get(4).name);
                tvQuickway6.setText(data.get(5).name);
                tvQuickway7.setText(data.get(6).name);
                tvQuickway8.setText(data.get(7).name);

                llQuickway1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setQuickwayClickListener(data, 0);
                    }
                });
                llQuickway2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setQuickwayClickListener(data, 1);
                    }
                });
                llQuickway3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setQuickwayClickListener(data, 2);
                    }
                });
                llQuickway4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setQuickwayClickListener(data, 3);
                    }
                });
                llQuickway5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setQuickwayClickListener(data, 4);
                    }
                });
                llQuickway6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setQuickwayClickListener(data, 5);
                    }
                });
                llQuickway7.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setQuickwayClickListener(data, 6);
                    }
                });
                llQuickway8.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setQuickwayClickListener(data, 7);
                    }
                });
            }
        });
    }

    public void initCart() {
        if (userManager.isUserLogin()) {
            userManager.getMyCartPrice(new UserManager.FetchCartPriceCallback() {
                @Override
                public void onFailure(String errorMsg) {
                    Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinish(CartPrice cp) {
                    tvCartPrice.setVisibility(View.VISIBLE);
                    if (Utils.isEmptyString(cp.price)) {
                        tvCartPrice.setText(getActivity().getResources().getString(R.string.rmb) + "0");
                    } else {
                        BigDecimal tempPrice = new BigDecimal(cp.price);
                        if (tempPrice.compareTo(new BigDecimal("10000")) >= 0) {
                            tvCartPrice.setText(getActivity().getResources().getString(R.string.rmb) + "9999+");
                        } else {
                            tvCartPrice.setText(getActivity().getResources().getString(R.string.rmb) + cp.price);
                        }
                    }
                }
            });
        } else {
            tvCartPrice.setVisibility(View.GONE);
        }
    }

    private void setQuickwayClickListener(final List<QuickwayType> data, final int index) {
        Utils.hideKeyboard(viewList.get(0));
        QuickwayType qt = data.get(index);
        if (qt.lv.equals("2")) {
            currentSelectSmallTypeId = qt.id;
            for (Type type : typeList) {
                for (SmallType st : type.smallTypeList) {
                    if (st.id.equals(currentSelectSmallTypeId)) {
                        currentSmallTypeList = type.smallTypeList;
                        break;
                    }
                }
            }

            for (SmallType st : currentSmallTypeList) {
                if (currentSelectSmallTypeId.equals(st.id)) {
                    st.isSelected = true;
                } else {
                    st.isSelected = false;
                }
            }
            shopSmallTypeAdapter.applyData(currentSmallTypeList);
        } else {
            for (Type t : typeList) {
                String id = t.id;
                if (id.equals(qt.id)) {
                    currentSmallTypeList = t.smallTypeList;
                    currentSelectSmallTypeId = t.smallTypeList.get(0).id;

                    for (SmallType st : currentSmallTypeList) {
                        st.isSelected = false;
                    }
                    SmallType st = currentSmallTypeList.get(0);
                    st.isSelected = true;
                    shopSmallTypeAdapter.applyData(currentSmallTypeList);

                    break;
                }
            }
        }


        initGoodsData();
    }

    private void setTypeClickListener(final int index) {
        closeMenu();
        currentSmallTypeList = typeList.get(index).smallTypeList;
        currentSelectSmallTypeId = currentSmallTypeList.get(0).id;
        for (SmallType st : currentSmallTypeList) {
            if (currentSelectSmallTypeId.equals(st.id)) {
                st.isSelected = true;
            } else {
                st.isSelected = false;
            }
        }
        shopSmallTypeAdapter.applyData(currentSmallTypeList);

        initGoodsData();
    }
}
