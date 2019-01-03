package com.sky.happyf.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.sky.happyf.Model.Rank;
import com.sky.happyf.R;
import com.sky.happyf.adapter.RankListAdapter;
import com.sky.happyf.manager.RankManager;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.util.ArrayList;
import java.util.List;

public class RankListActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private PtrClassicFrameLayout ptrLayout;
    private ListView lvRank;
    private RankListAdapter adapter;
    private RankManager rankManager;
    private TextView tvType, tvOne, tvTwo;
    private int selectIndex = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rank_list);

        getSupportActionBar().hide();//隐藏标题栏

        initView();

        initListener();

        initData();
    }

    private void initView() {
        titleBar = (CommonTitleBar) findViewById(R.id.titlebar);
        tvType = (TextView) findViewById(R.id.tv_type);
        tvOne = (TextView) findViewById(R.id.tv_one);
        tvTwo = (TextView) findViewById(R.id.tv_two);

        ptrLayout = (PtrClassicFrameLayout) this.findViewById(R.id.ptr_layout);
        lvRank = (ListView) findViewById(R.id.lv_rank);
        adapter = new RankListAdapter(this);
        lvRank.setAdapter(adapter);
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

        ptrLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                rankManager.init(selectIndex, new RankManager.FetchRanksCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        ptrLayout.refreshComplete();
                    }

                    @Override
                    public void onFinish(List<Rank> data) {
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
                rankManager.loadMore(selectIndex, new RankManager.FetchRanksCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        ptrLayout.refreshComplete();
                    }

                    @Override
                    public void onFinish(List<Rank> data) {
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
                selectIndex = 1;
                adapter.applyData(new ArrayList<Rank>());
                adapter.notifyDataSetChanged();
                ptrLayout.setLoadMoreEnable(false);
                processSelectIndexView();
                initRankData();
            }
        });
        tvTwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectIndex = 2;
                adapter.applyData(new ArrayList<Rank>());
                adapter.notifyDataSetChanged();
                ptrLayout.setLoadMoreEnable(false);
                processSelectIndexView();
                initRankData();
            }
        });
    }

    private void initData() {
        rankManager = new RankManager(this);
        processSelectIndexView();

        initRankData();
    }

    private void processSelectIndexView() {
        if (selectIndex == 1) {
            tvType.setText("xx鱼");
            tvOne.setTextColor(getColor(R.color.orangered));
            tvTwo.setTextColor(getColor(R.color.black));
        } else if (selectIndex == 2) {
            tvType.setText("贝壳总量排行");
            tvOne.setTextColor(getColor(R.color.black));
            tvTwo.setTextColor(getColor(R.color.orangered));
        }
    }

    private void initRankData() {
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
