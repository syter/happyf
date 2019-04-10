package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.sky.happyf.Model.CartPrice;
import com.sky.happyf.Model.Goods;
import com.sky.happyf.R;
import com.sky.happyf.adapter.GoodsListAdapter;
import com.sky.happyf.fragment.MineFragment;
import com.sky.happyf.fragment.ShopFragment;
import com.sky.happyf.manager.GoodsManager;
import com.sky.happyf.manager.UserManager;
import com.sky.happyf.message.MessageEvent;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.Utils;
import com.wuhenzhizao.titlebar.statusbar.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.math.BigDecimal;
import java.util.List;

public class SearchGoodsActivity extends BaseActivity {
    private EditText etSearch;
    private GoodsManager goodManager;
    private UserManager userManager;
    private PtrClassicFrameLayout ptrLayout;
    private ListView lvGoods;
    private GoodsListAdapter adapter;
    private String content;
    private RelativeLayout rlBack, rlCart, rlMain;
    private TextView tvCartPrice;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_goods);

        //设置标题栏和状态栏
        getSupportActionBar().hide();
        StatusBarUtils.setDarkMode(getWindow());
        StatusBarUtils.setStatusBarColor(getWindow(), getColor(R.color.white), 0);

        EventBus.getDefault().register(this);

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


        Utils.hideKeyboard(etSearch);
    }

    private void initListener() {
        rlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        rlCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (userManager.isUserLogin()) {
                    startActivity(new Intent(SearchGoodsActivity.this, CartListActivity.class));
                    overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    startActivity(new Intent(SearchGoodsActivity.this, LoginActivity.class));
                    overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }
                Utils.hideKeyboard(etSearch);
            }
        });

        ptrLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                goodManager.searchGoods(content, new GoodsManager.FetchGoodsCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        ptrLayout.refreshComplete();
                    }

                    @Override
                    public void onFinish(List<Goods> data) {
                        ptrLayout.refreshComplete();

                        if (data.size() != 10) {
                            ptrLayout.setLoadMoreEnable(false);
                        } else {
                            adapter.applyData(data);
                            ptrLayout.setLoadMoreEnable(true);
                        }
                    }
                });
                Utils.hideKeyboard(etSearch);
            }
        });


        ptrLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                goodManager.loadMoreSearchGoods(etSearch.getText().toString(), new GoodsManager.FetchGoodsCallback() {
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
                    content = etSearch.getText().toString();
                    if (Utils.isEmptyString(content)) {
                        Toast.makeText(SearchGoodsActivity.this, getResources().getString(R.string.string_null_error), Toast.LENGTH_LONG).show();
                    } else {
                        initSearchGoods();
                    }
                }
                return false;
            }
        });

        lvGoods.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(SearchGoodsActivity.this, GoodsDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", adapter.getList().get(i).id);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                Utils.hideKeyboard(etSearch);
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
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        content = bundle.getString("content");
        etSearch.setText(content);

        goodManager = new GoodsManager(this);
        userManager = new UserManager(this);

        initSearchGoods();

        initCart();
    }

    private void initSearchGoods() {
        ptrLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrLayout.autoRefresh(true);
            }
        }, 150);
    }

    private void initCart() {
        if (userManager.isUserLogin()) {
            userManager.getMyCartPrice(new UserManager.FetchCartPriceCallback() {
                @Override
                public void onFailure(String errorMsg) {
                    Toast.makeText(SearchGoodsActivity.this, errorMsg, Toast.LENGTH_LONG).show();
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

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.anim_exit);

        Utils.hideKeyboard(etSearch);
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
        if (Constants.EVENT_MESSAGE_CART.equals(messageEvent.getMessage()) || Constants.EVENT_MESSAGE_LOGIN.equals(messageEvent.getMessage())) {
            initCart();
        }
    }
}
