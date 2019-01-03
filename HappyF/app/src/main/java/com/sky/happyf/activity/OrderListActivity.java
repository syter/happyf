package com.sky.happyf.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.sky.happyf.Model.Order;
import com.sky.happyf.R;
import com.sky.happyf.adapter.OrderListAdapter;
import com.sky.happyf.manager.OrderManager;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.util.ArrayList;
import java.util.List;

public class OrderListActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private PtrClassicFrameLayout ptrLayout;
    private ListView lvOrder;
    private OrderListAdapter adapter;
    private OrderManager orderManager;
    private TextView tvAll, tvOne, tvTwo;
    private int selectIndex = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        getSupportActionBar().hide();//隐藏标题栏

        initView();

        initListener();

        initData();
    }

    private void initView() {
        titleBar = (CommonTitleBar) findViewById(R.id.titlebar);
        tvAll = (TextView) findViewById(R.id.tv_all);
        tvOne = (TextView) findViewById(R.id.tv_one);
        tvTwo = (TextView) findViewById(R.id.tv_two);

        ptrLayout = (PtrClassicFrameLayout) this.findViewById(R.id.ptr_layout);
        lvOrder = (ListView) findViewById(R.id.lv_order);
        adapter = new OrderListAdapter(this);
        lvOrder.setAdapter(adapter);
    }

    private void initListener() {
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    finish();
                }
            }
        });

        // set auto load more disable,default available
//        ptrLayout.setAutoLoadMoreEnable(false);


        ptrLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                orderManager.init(selectIndex, new OrderManager.FetchOrdersCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        ptrLayout.refreshComplete();
                    }

                    @Override
                    public void onFinish(List<Order> data) {
                        ptrLayout.refreshComplete();

                        adapter.applyData(data);
                        adapter.notifyDataSetChanged();
                        ptrLayout.refreshComplete();

                        if (!ptrLayout.isLoadMoreEnable()) {
                            ptrLayout.setLoadMoreEnable(true);
                        }
                    }
                });
            }
        });


        ptrLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                orderManager.loadMore(selectIndex, new OrderManager.FetchOrdersCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        ptrLayout.refreshComplete();
                    }

                    @Override
                    public void onFinish(List<Order> data) {
                        ptrLayout.loadMoreComplete(true);

                        adapter.applyData(data);
                        adapter.notifyDataSetChanged();
                        ptrLayout.refreshComplete();

                        if (data.isEmpty()) {
                            ptrLayout.setLoadMoreEnable(false);
                        }
                    }
                });
            }
        });

        tvAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIndex = 0;
                adapter.applyData(new ArrayList<Order>());
                adapter.notifyDataSetChanged();
                ptrLayout.setLoadMoreEnable(false);
                processSelectIndexView();
                initOrderData();
            }
        });
        tvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIndex = 1;
                adapter.applyData(new ArrayList<Order>());
                adapter.notifyDataSetChanged();
                ptrLayout.setLoadMoreEnable(false);
                processSelectIndexView();
                initOrderData();
            }
        });
        tvTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIndex = 2;
                adapter.applyData(new ArrayList<Order>());
                adapter.notifyDataSetChanged();
                ptrLayout.setLoadMoreEnable(false);
                processSelectIndexView();
                initOrderData();
            }
        });
    }

    private void initData() {
        orderManager = new OrderManager(this);
        processSelectIndexView();

        initOrderData();
    }

    private void processSelectIndexView() {
        if (selectIndex == 0) {
            tvAll.setTextColor(getColor(R.color.orangered));
            tvOne.setTextColor(getColor(R.color.black));
            tvTwo.setTextColor(getColor(R.color.black));
        } else if (selectIndex == 1) {
            tvAll.setTextColor(getColor(R.color.black));
            tvOne.setTextColor(getColor(R.color.orangered));
            tvTwo.setTextColor(getColor(R.color.black));
        } else if (selectIndex == 2) {
            tvAll.setTextColor(getColor(R.color.black));
            tvOne.setTextColor(getColor(R.color.black));
            tvTwo.setTextColor(getColor(R.color.orangered));
        }
    }

    private void initOrderData() {
        ptrLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrLayout.autoRefresh(true);
            }
        }, 150);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.anim_exit);
    }

}
