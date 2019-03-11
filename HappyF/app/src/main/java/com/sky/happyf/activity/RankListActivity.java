package com.sky.happyf.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.pixplicity.multiviewpager.MultiViewPager;
import com.sky.happyf.Model.Rank;
import com.sky.happyf.Model.RankData;
import com.sky.happyf.R;
import com.sky.happyf.adapter.TopFishPagerAdapter;
import com.sky.happyf.listensers.TopFishEventListener;
import com.sky.happyf.manager.RankManager;
import com.sky.happyf.util.Utils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.util.ArrayList;
import java.util.List;

public class RankListActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private MultiViewPager mvpImage;
    private TextView tvFishDestiny, tvDate;
    private TopFishPagerAdapter topAdapter;
    private ArrayList<Rank> ranks = new ArrayList<>();
    private RankManager rankManager;
    private LinearLayout llRank;


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
        titleBar = findViewById(R.id.titlebar);
        tvFishDestiny = findViewById(R.id.tv_fish_destiny);
        tvDate = findViewById(R.id.tv_date);
        llRank = findViewById(R.id.ll_rank);

        mvpImage = findViewById(R.id.mvp_image);
        mvpImage.setOffscreenPageLimit(3);

        topAdapter = new TopFishPagerAdapter(mvpImage, getSupportFragmentManager(), new TopFishEventListener() {
            @Override
            public void onTopFishChanged(int position) {
                if (ranks.isEmpty()) {
                    return;
                }
                Rank rank = ranks.get(position);
                tvFishDestiny.setText(rank.fishName);
                tvDate.setText(rank.date);
                llRank.removeAllViews();

                for (int i = 0; i < rank.rankDataList.size(); i++) {
                    RankData rd = rank.rankDataList.get(i);
                    LinearLayout.LayoutParams llItemParam = new LinearLayout.LayoutParams(ViewGroup.
                            LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    llItemParam.topMargin = Utils.dip2px(RankListActivity.this, 15);
                    RelativeLayout rlItem = new RelativeLayout(RankListActivity.this);
                    llRank.setLayoutParams(llItemParam);
                    llRank.setOrientation(LinearLayout.VERTICAL);

                    LinearLayout.LayoutParams rlItemParam = new LinearLayout.LayoutParams(ViewGroup.
                            LayoutParams.MATCH_PARENT, Utils.dip2px(RankListActivity.this,
                            44));
                    rlItemParam.leftMargin = Utils.dip2px(RankListActivity.this, 15);
                    rlItemParam.topMargin = Utils.dip2px(RankListActivity.this, 5);
                    rlItemParam.rightMargin = Utils.dip2px(RankListActivity.this, 15);
                    RelativeLayout rlRank = new RelativeLayout(RankListActivity.this);
                    rlRank.setBackground(getDrawable(R.drawable.input_boder));
                    rlRank.setLayoutParams(rlItemParam);
                    llRank.addView(rlRank);

                    RelativeLayout.LayoutParams tvSParams = new RelativeLayout.LayoutParams(ViewGroup.
                            LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    TextView tvS = new TextView(RankListActivity.this);
                    tvSParams.leftMargin = Utils.dip2px(RankListActivity.this, 18);
                    tvSParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    tvS.setLayoutParams(tvSParams);
                    tvS.setText(i + 1 + "");
                    tvS.setTextColor(getColor(R.color.black));
                    tvS.setTextSize(18);
                    rlRank.addView(tvS);

                    RelativeLayout.LayoutParams ivCoverParams = new RelativeLayout.LayoutParams(Utils.dip2px(RankListActivity.this,
                            30), Utils.dip2px(RankListActivity.this, 30));
                    ImageView ivCover = new ImageView(RankListActivity.this);
                    ivCoverParams.leftMargin = Utils.dip2px(RankListActivity.this, 20);
                    ivCoverParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    ivCoverParams.addRule(RelativeLayout.RIGHT_OF, tvS.getId());
                    ivCover.setLayoutParams(ivCoverParams);
                    Glide.with(RankListActivity.this).load(rd.cover).into(ivCover);
                    rlRank.addView(ivCover);

                    RelativeLayout.LayoutParams tvNameParams = new RelativeLayout.LayoutParams(ViewGroup.
                            LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    TextView tvName = new TextView(RankListActivity.this);
                    tvNameParams.leftMargin = Utils.dip2px(RankListActivity.this, 10);
                    tvNameParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    tvName.setLayoutParams(tvNameParams);
                    tvName.setText(rd.name);
                    tvName.setTextColor(getColor(R.color.gray_text_2));
                    tvName.setTextSize(14);
                    tvName.setEllipsize(TextUtils.TruncateAt.END);
                    tvName.setMaxLines(1);
                    tvName.setMaxWidth(Utils.dip2px(RankListActivity.this, 100));
                    rlRank.addView(tvName);

                    RelativeLayout.LayoutParams tvPhoneParams = new RelativeLayout.LayoutParams(ViewGroup.
                            LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                    TextView tvPhone = new TextView(RankListActivity.this);
                    tvPhoneParams.leftMargin = Utils.dip2px(RankListActivity.this, 10);
                    tvPhoneParams.addRule(RelativeLayout.CENTER_VERTICAL);
                    tvPhoneParams.addRule(RelativeLayout.RIGHT_OF, tvName.getId());
                    tvPhone.setLayoutParams(tvPhoneParams);
                    tvPhone.setText(rd.phone);
                    tvPhone.setTextColor(getColor(R.color.gray_text_1));
                    tvPhone.setTextSize(10);
                    rlRank.addView(tvPhone);

                    RelativeLayout.LayoutParams llRightParams = new RelativeLayout.LayoutParams(Utils.
                            dip2px(RankListActivity.this, 60), ViewGroup.LayoutParams.MATCH_PARENT);
                    LinearLayout llRight = new LinearLayout(RankListActivity.this);
                    llRightParams.leftMargin = Utils.dip2px(RankListActivity.this, 10);
                    llRightParams.rightMargin = Utils.dip2px(RankListActivity.this, 10);
                    llRightParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                    llRight.setLayoutParams(llRightParams);
                    llRight.setGravity(Gravity.RIGHT | Gravity.CENTER_VERTICAL);
                    llRight.setOrientation(LinearLayout.VERTICAL);
                    rlRank.addView(llRight);

                    TextView tvWeight = new TextView(RankListActivity.this);
                    tvWeight.setText(rd.weight);
                    tvWeight.setTextColor(getColor(R.color.price_red));
                    tvWeight.setTextSize(12);
                    llRight.addView(tvWeight);

                    TextView tvLength = new TextView(RankListActivity.this);
                    tvLength.setText(rd.length);
                    tvLength.setTextColor(getColor(R.color.price_red));
                    tvLength.setTextSize(12);
                    llRight.addView(tvLength);
                }
            }

            @Override
            public void onTopFishClicked(int position) {

            }
        });
        topAdapter.setFishs(ranks);
        mvpImage.setAdapter(topAdapter);
        mvpImage.addOnPageChangeListener(topAdapter);
        topAdapter.onPageSelected(0);
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


    }

    private void initData() {
        rankManager = new RankManager(RankListActivity.this);

        initTopFishData();
    }


    private void initTopFishData() {
        rankManager.getRanks(new RankManager.FetchRanksCallback() {
            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(RankListActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(List<Rank> data) {
                if (topAdapter != null) {
                    topAdapter.setFishs(data);
                }
            }
        });
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
