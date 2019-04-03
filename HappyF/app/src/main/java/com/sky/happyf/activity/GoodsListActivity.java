package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.sky.happyf.Model.CartPrice;
import com.sky.happyf.Model.Goods;
import com.sky.happyf.Model.SmallType;
import com.sky.happyf.Model.Type;
import com.sky.happyf.R;
import com.sky.happyf.adapter.GoodsListAdapter;
import com.sky.happyf.adapter.ShopSmallTypeAdapter;
import com.sky.happyf.manager.GoodsManager;
import com.sky.happyf.manager.UserManager;
import com.sky.happyf.util.Utils;
import com.sky.happyf.view.HorizontalListView;
import com.wuhenzhizao.titlebar.statusbar.StatusBarUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class GoodsListActivity extends BaseActivity {
    private EditText etSearch;
    private GoodsManager goodsManager;
    private UserManager userManager;
    private PtrClassicFrameLayout ptrLayout;
    private ListView lvGoods;
    private GoodsListAdapter adapter;
    private LinearLayout llType1, llType2, llType3, llType4, llLeftMenu;
    private TextView tvType1, tvType2, tvType3, tvType4;
    private ImageView ivType1, ivType2, ivType3, ivType4;
    private RelativeLayout rlBack, rlCart, rlMain, rlMenu;
    private TextView tvCartPrice;
    private HorizontalListView smallTypeListView;
    private ShopSmallTypeAdapter shopSmallTypeAdapter;
    private List<Type> typeList = new ArrayList<>();
    private String currentSelectSmallTypeId;
    private List<SmallType> currentSmallTypeList;
    private List<Goods> goodsList;
    private boolean isShowMenu = false;
    private RelativeLayout rlDialogBlack;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goods_list);

        //设置标题栏和状态栏
        getSupportActionBar().hide();
        StatusBarUtils.setDarkMode(getWindow());
        StatusBarUtils.setStatusBarColor(getWindow(), getColor(R.color.white), 0);

        initView();

        initListener();

        initData();
    }

    private void initView() {
        rlBack = findViewById(R.id.rl_back);
        etSearch = findViewById(R.id.et_search);
        ptrLayout = findViewById(R.id.ptr_layout);
        rlCart = findViewById(R.id.rl_cart);
        lvGoods = findViewById(R.id.lv_goods);
        adapter = new GoodsListAdapter(this);
        lvGoods.setAdapter(adapter);
        tvCartPrice = findViewById(R.id.tv_cart_price);
        rlMain = findViewById(R.id.rl_main);
        llType1 = findViewById(R.id.ll_type1);
        llType2 = findViewById(R.id.ll_type2);
        llType3 = findViewById(R.id.ll_type3);
        llType4 = findViewById(R.id.ll_type4);
        ivType1 = findViewById(R.id.iv_type1);
        ivType2 = findViewById(R.id.iv_type2);
        ivType3 = findViewById(R.id.iv_type3);
        ivType4 = findViewById(R.id.iv_type4);
        tvType1 = findViewById(R.id.tv_type1);
        tvType2 = findViewById(R.id.tv_type2);
        tvType3 = findViewById(R.id.tv_type3);
        tvType4 = findViewById(R.id.tv_type4);
        llLeftMenu = findViewById(R.id.ll_left_menu);
        rlMenu = findViewById(R.id.rl_menu);
        rlDialogBlack = findViewById(R.id.rl_dialog_black);

        smallTypeListView = findViewById(R.id.horizontal_lv);
        shopSmallTypeAdapter = new ShopSmallTypeAdapter(GoodsListActivity.this);
        smallTypeListView.setAdapter(shopSmallTypeAdapter);


        Utils.hideKeyboard(etSearch);
    }

    private void initListener() {
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rlMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showMenu();
            }
        });

        rlDialogBlack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                closeMenu();
            }
        });

        rlCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userManager.isUserLogin()) {
                    startActivity(new Intent(GoodsListActivity.this, CartListActivity.class));
                    overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    startActivity(new Intent(GoodsListActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }
                Utils.hideKeyboard(etSearch);
            }
        });

        ptrLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                goodsManager.getGoodsList(currentSelectSmallTypeId, new GoodsManager.FetchGoodsCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        Toast.makeText(GoodsListActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        ptrLayout.refreshComplete();
                    }

                    @Override
                    public void onFinish(List<Goods> data) {
                        goodsList = data;
                        ptrLayout.refreshComplete();

                        if (data.size() != 10) {
                            ptrLayout.setLoadMoreEnable(false);
                        } else {
                            ptrLayout.setLoadMoreEnable(true);
                        }
                        adapter.applyData(data);
                    }
                });

                Utils.hideKeyboard(etSearch);
            }
        });


        ptrLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                goodsManager.loadMoreGoods(currentSelectSmallTypeId, new GoodsManager.FetchGoodsCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        ptrLayout.loadMoreComplete(true);
                    }

                    @Override
                    public void onFinish(List<Goods> data) {
                        ptrLayout.loadMoreComplete(true);

                        if (data.size() != 10) {
                            ptrLayout.setLoadMoreEnable(false);
                        } else {
                            adapter.addData(data);
                            ptrLayout.setLoadMoreEnable(true);
                        }
                    }
                });
                Utils.hideKeyboard(etSearch);
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    String content = etSearch.getText().toString();
                    if (Utils.isEmptyString(content)) {
                        Toast.makeText(GoodsListActivity.this, GoodsListActivity.this.getResources().getString(R.string.string_null_error), Toast.LENGTH_LONG).show();
                    } else {
                        // 点击搜索要做的操作
                        Intent intent = new Intent(GoodsListActivity.this, SearchGoodsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putString("content", content);
                        intent.putExtras(bundle);
                        GoodsListActivity.this.startActivity(intent);
                        GoodsListActivity.this.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                        Utils.hideKeyboard(etSearch);

                        etSearch.setText("");
                    }

                }
                return false;
            }
        });

        lvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(GoodsListActivity.this, GoodsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", adapter.getList().get(i).id);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                Utils.hideKeyboard(etSearch);
            }
        });

        smallTypeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Utils.hideKeyboard(view);

                for (SmallType st : currentSmallTypeList) {
                    st.isSelected = false;
                }
                SmallType st = currentSmallTypeList.get(i);
                st.isSelected = true;
                currentSelectSmallTypeId = st.id;
                shopSmallTypeAdapter.applyData(currentSmallTypeList);

                ptrLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrLayout.autoRefresh(true);
                    }
                }, 150);
            }
        });

        rlMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(etSearch);
            }
        });


        ptrLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.hideKeyboard(etSearch);
            }
        });


    }

    private void initData() {
        goodsManager = new GoodsManager(this);
        userManager = new UserManager(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        currentSelectSmallTypeId = bundle.getString("currentSelectSmallTypeId");

        goodsList = new ArrayList<>();
        adapter.applyData(goodsList);

        initTypes();

        initCart();
    }

    private void initTypes() {
        goodsManager.getTypes(new GoodsManager.FetchTypesCallback() {
            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(GoodsListActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(final List<Type> data) {
                typeList = data;
                Glide.with(GoodsListActivity.this).load(data.get(0).cover).into(ivType1);
                Glide.with(GoodsListActivity.this).load(data.get(1).cover).into(ivType2);
                Glide.with(GoodsListActivity.this).load(data.get(2).cover).into(ivType3);
                Glide.with(GoodsListActivity.this).load(data.get(3).cover).into(ivType4);
                tvType1.setText(data.get(0).name);
                tvType2.setText(data.get(1).name);
                tvType3.setText(data.get(2).name);
                tvType4.setText(data.get(3).name);

                if (Utils.isEmptyString(currentSelectSmallTypeId)) {
                    currentSmallTypeList = data.get(0).smallTypeList;
                    currentSelectSmallTypeId = data.get(0).smallTypeList.get(0).id;
                } else {
                    for (Type type : data) {
                        for (SmallType st : type.smallTypeList) {
                            if (st.id.equals(currentSelectSmallTypeId)) {
                                currentSmallTypeList = type.smallTypeList;
                                break;
                            }
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

                ptrLayout.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        ptrLayout.autoRefresh(true);
                    }
                }, 150);
            }
        });
    }

    private void initCart() {
        if (userManager.isUserLogin()) {
            userManager.getMyCartPrice(new UserManager.FetchCartPriceCallback() {
                @Override
                public void onFailure(String errorMsg) {
                    Toast.makeText(GoodsListActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                }

                @Override
                public void onFinish(CartPrice cp) {
                    tvCartPrice.setVisibility(View.VISIBLE);
                    if (Utils.isEmptyString(cp.price)) {
                        tvCartPrice.setText(getResources().getString(R.string.rmb) + "0");
                    } else {
                        BigDecimal tempPrice = new BigDecimal(cp.price);
                        if (tempPrice.compareTo(new BigDecimal("10000")) >= 0) {
                            tvCartPrice.setText(getResources().getString(R.string.rmb) + "9999+");
                        } else {
                            tvCartPrice.setText(getResources().getString(R.string.rmb) + cp.price);
                        }
                    }
                }
            });
        } else {
            tvCartPrice.setVisibility(View.GONE);
        }
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

        ptrLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrLayout.autoRefresh(true);
            }
        }, 150);
    }

    private void closeMenu() {
        if (!isShowMenu) {
            return;
        }
        isShowMenu = false;
        rlDialogBlack.setVisibility(View.GONE);
        llLeftMenu.setVisibility(View.GONE);
        llLeftMenu.setAnimation(Utils.moveToViewLeft());
    }

    private void showMenu() {
        Utils.hideKeyboard(etSearch);
        if (isShowMenu) {
            return;
        }
        isShowMenu = true;
        rlDialogBlack.setVisibility(View.VISIBLE);
        llLeftMenu.setVisibility(View.VISIBLE);
        llLeftMenu.setAnimation(Utils.moveToViewLocationLeft());
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.anim_exit);

        Utils.hideKeyboard(etSearch);
    }

}
