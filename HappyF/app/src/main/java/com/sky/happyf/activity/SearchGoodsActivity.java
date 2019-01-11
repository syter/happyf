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
import com.sky.happyf.Model.Goods;
import com.sky.happyf.Model.Order;
import com.sky.happyf.R;
import com.sky.happyf.adapter.GoodsListAdapter;
import com.sky.happyf.adapter.OrderListAdapter;
import com.sky.happyf.manager.GoodsManager;
import com.sky.happyf.manager.OrderManager;
import com.sky.happyf.util.NetUtils;
import com.sky.happyf.util.Utils;
import com.wuhenzhizao.titlebar.statusbar.StatusBarUtils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import org.json.JSONObject;

import java.util.List;

public class SearchGoodsActivity extends BaseActivity {
    private EditText etSearch;
    private GoodsManager goodManager;
    private PtrClassicFrameLayout ptrLayout;
    private ListView lvGoods;
    private GoodsListAdapter adapter;
    private String content;
    private RelativeLayout rlBack, rlCart;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_goods);

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
                startActivity(new Intent(SearchGoodsActivity.this, CartListActivity.class));
                overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });

        ptrLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
//                goodManager.init(0, new GoodsManager.FetchGoodsCallback() {
//                    @Override
//                    public void onFailure(String errorMsg) {
//                        ptrLayout.refreshComplete();
//                    }
//
//                    @Override
//                    public void onFinish(List<Goods> data) {
//                        ptrLayout.refreshComplete();
//
//                        adapter.applyData(data);
//                        adapter.notifyDataSetChanged();
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
//                goodManager.loadMore(0, new GoodsManager.FetchGoodsCallback() {
//                    @Override
//                    public void onFailure(String errorMsg) {
//                        ptrLayout.refreshComplete();
//                    }
//
//                    @Override
//                    public void onFinish(List<Goods> data) {
//                        ptrLayout.loadMoreComplete(true);
//
//                        adapter.applyData(data);
//                        adapter.notifyDataSetChanged();
//                        ptrLayout.refreshComplete();
//
//                        if (data.isEmpty()) {
//                            ptrLayout.setLoadMoreEnable(false);
//                        }
//                    }
//                });
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId,
                                          KeyEvent event) {
                if ((actionId == 0 || actionId == 3) && event != null) {
                    content = etSearch.getText().toString();

//                    goodManager.init(0, new GoodsManager.FetchGoodsCallback() {
//                        @Override
//                        public void onFailure(String errorMsg) {
//                            Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
//                            ptrLayout.refreshComplete();
//                        }
//
//                        @Override
//                        public void onFinish(List<Goods> data) {
//                            ptrLayout.refreshComplete();
//
//                            adapter.applyData(data);
//                            adapter.notifyDataSetChanged();
//                            ptrLayout.refreshComplete();
//
//                            if (!ptrLayout.isLoadMoreEnable()) {
//                                ptrLayout.setLoadMoreEnable(true);
//                            }
//                        }
//                    });
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
            }
        });
    }

    private void initData() {
        content = getIntent().getStringExtra("content");
        goodManager = new GoodsManager(this);


        if (!Utils.isEmptyString(content)) {
            etSearch.setText(content);
            ptrLayout.postDelayed(new Runnable() {
                @Override
                public void run() {
                    ptrLayout.autoRefresh(true);
                }
            }, 150);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.anim_exit);
    }
}
