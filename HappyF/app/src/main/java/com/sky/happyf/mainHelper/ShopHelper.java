package com.sky.happyf.mainHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.orhanobut.logger.Logger;
import com.sky.happyf.Model.Goods;
import com.sky.happyf.R;
import com.sky.happyf.activity.GoodsDetailActivity;
import com.sky.happyf.activity.HappyListActivity;
import com.sky.happyf.adapter.GoodsListAdapter;
import com.sky.happyf.manager.GoodsManager;

import java.util.ArrayList;
import java.util.List;

public class ShopHelper {
    private View view;
    private Activity act;
    private PtrClassicFrameLayout ptrLayout;
    private ListView lvGoods;
    private GoodsListAdapter adapter;
    private GoodsManager goodsManager;
    private TextView tvOne, tvTwo, tvThree;
    private int selectIndex = 0;
    private LinearLayout llSort;

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
        tvOne = (TextView) view.findViewById(R.id.tv_one);
        tvTwo = (TextView) view.findViewById(R.id.tv_two);
        tvThree = (TextView) view.findViewById(R.id.tv_three);

        ptrLayout = (PtrClassicFrameLayout) view.findViewById(R.id.ptr_layout);
        lvGoods = (ListView) view.findViewById(R.id.lv_goods);
        adapter = new GoodsListAdapter(act);
        lvGoods.setAdapter(adapter);

        llSort = (LinearLayout) view.findViewById(R.id.ll_sort);
    }

    private void initListener() {
        ptrLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                goodsManager.init(selectIndex, new GoodsManager.FetchGoodsCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        ptrLayout.refreshComplete();
                    }

                    @Override
                    public void onFinish(List<Goods> data) {
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
                goodsManager.loadMore(selectIndex, new GoodsManager.FetchGoodsCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        ptrLayout.refreshComplete();
                    }

                    @Override
                    public void onFinish(List<Goods> data) {
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


        tvOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIndex = 0;
                adapter.applyData(new ArrayList<Goods>());
                adapter.notifyDataSetChanged();
                ptrLayout.setLoadMoreEnable(false);
                processSelectIndexView();
                initGoodsData();
            }
        });
        tvTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIndex = 1;
                adapter.applyData(new ArrayList<Goods>());
                adapter.notifyDataSetChanged();
                ptrLayout.setLoadMoreEnable(false);
                processSelectIndexView();
                initGoodsData();
            }
        });
        tvThree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIndex = 2;
                adapter.applyData(new ArrayList<Goods>());
                adapter.notifyDataSetChanged();
                ptrLayout.setLoadMoreEnable(false);
                processSelectIndexView();
                initGoodsData();
            }
        });
        llSort.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Logger.d("啊啊啊啊 排序啦");
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
    }

    private void initData() {
        goodsManager = new GoodsManager(act);
        processSelectIndexView();

        initGoodsData();
    }

    private void processSelectIndexView() {
        if (selectIndex == 0) {
            tvOne.setTextColor(act.getColor(R.color.orangered));
            tvTwo.setTextColor(act.getColor(R.color.black));
            tvThree.setTextColor(act.getColor(R.color.black));
        } else if (selectIndex == 1) {
            tvOne.setTextColor(act.getColor(R.color.black));
            tvTwo.setTextColor(act.getColor(R.color.orangered));
            tvThree.setTextColor(act.getColor(R.color.black));
        } else if (selectIndex == 2) {
            tvOne.setTextColor(act.getColor(R.color.black));
            tvTwo.setTextColor(act.getColor(R.color.black));
            tvThree.setTextColor(act.getColor(R.color.orangered));
        }
    }

    private void initGoodsData() {
        ptrLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrLayout.autoRefresh(true);
            }
        }, 150);
    }
}
