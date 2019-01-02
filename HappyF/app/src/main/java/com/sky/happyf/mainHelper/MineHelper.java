package com.sky.happyf.mainHelper;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.sky.happyf.R;
import com.sky.happyf.activity.CartListActivity;
import com.sky.happyf.activity.EditUserActivity;
import com.sky.happyf.activity.HappyListActivity;
import com.sky.happyf.activity.LoginActivity;
import com.sky.happyf.activity.MainActivity;
import com.sky.happyf.activity.OrderListActivity;
import com.sky.happyf.activity.RankListActivity;
import com.sky.happyf.activity.SetActivity;
import com.sky.happyf.activity.VipActivity;

public class MineHelper {
    private View view;
    private Activity act;
    private RelativeLayout rlEdit, rlOrder, rlCart, rlHappy, rlRank, rlSet;
    private ImageView ivVip;

    public MineHelper(Activity act, View view) {
        this.act = act;
        this.view = view;
    }

    public void init() {
        initView();

        initListener();

        initData();
    }

    private void initView() {
        rlEdit = (RelativeLayout) view.findViewById(R.id.rl_edit);
        rlOrder = (RelativeLayout) view.findViewById(R.id.rl_edit);
        rlCart = (RelativeLayout) view.findViewById(R.id.rl_edit);
        rlHappy = (RelativeLayout) view.findViewById(R.id.rl_edit);
        rlRank = (RelativeLayout) view.findViewById(R.id.rl_edit);
        rlSet = (RelativeLayout) view.findViewById(R.id.rl_edit);
        ivVip = (ImageView) view.findViewById(R.id.iv_vip);
    }

    private void initListener() {
        rlEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.startActivity(new Intent(act, EditUserActivity.class));
                act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });

        rlOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.startActivity(new Intent(act, OrderListActivity.class));
                act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });

        rlCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.startActivity(new Intent(act, CartListActivity.class));
                act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });

        rlHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.startActivity(new Intent(act, HappyListActivity.class));
                act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });

        rlRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.startActivity(new Intent(act, RankListActivity.class));
                act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });

        rlSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.startActivity(new Intent(act, SetActivity.class));
                act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });

        ivVip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.startActivity(new Intent(act, VipActivity.class));
                act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });
    }

    private void initData() {

    }
}
