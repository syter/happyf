package com.sky.happyf.mainHelper;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.sky.happyf.Model.User;
import com.sky.happyf.R;
import com.sky.happyf.activity.CartListActivity;
import com.sky.happyf.activity.ClubActivity;
import com.sky.happyf.activity.EditUserActivity;
import com.sky.happyf.activity.ExpActivity;
import com.sky.happyf.activity.HappyListActivity;
import com.sky.happyf.activity.LoginActivity;
import com.sky.happyf.activity.OrderListActivity;
import com.sky.happyf.activity.RankListActivity;
import com.sky.happyf.activity.SetActivity;
import com.sky.happyf.util.SpfHelper;
import com.sky.happyf.util.Utils;

import java.math.BigDecimal;

import de.hdodenhof.circleimageview.CircleImageView;

public class MineHelper {
    private View view;
    private Activity act;
    private LinearLayout llShell, llClub, llOrder, llCart, llHappy, llRank, llSet, llCurrentExp;
    private ImageView ivLv;
    private RelativeLayout rlExp;
    private TextView tvName, tvExp;
    private CircleImageView ivImage;

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
        tvName = view.findViewById(R.id.tv_name);
        ivImage = view.findViewById(R.id.iv_image);
        llShell = view.findViewById(R.id.ll_shell);
        llClub = view.findViewById(R.id.ll_club);
        llOrder = view.findViewById(R.id.ll_order);
        llCart = view.findViewById(R.id.ll_cart);
        llHappy = view.findViewById(R.id.ll_happy);
        llRank = view.findViewById(R.id.ll_rank);
        llSet = view.findViewById(R.id.ll_set);
        rlExp = view.findViewById(R.id.rl_exp_total);
        tvExp = view.findViewById(R.id.tv_exp);
        llCurrentExp = view.findViewById(R.id.ll_current_exp);
    }

    private void initListener() {
        tvName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpfHelper.getInstance(act).hasSignIn()) {
                    act.startActivity(new Intent(act, EditUserActivity.class));
                    act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    act.startActivity(new Intent(act, LoginActivity.class));
                    act.overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }
            }
        });
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpfHelper.getInstance(act).hasSignIn()) {
                    act.startActivity(new Intent(act, EditUserActivity.class));
                    act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    act.startActivity(new Intent(act, LoginActivity.class));
                    act.overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }
            }
        });

        llOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpfHelper.getInstance(act).hasSignIn()) {
                    act.startActivity(new Intent(act, OrderListActivity.class));
                    act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    act.startActivity(new Intent(act, LoginActivity.class));
                    act.overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }
            }
        });

        llCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpfHelper.getInstance(act).hasSignIn()) {
                    act.startActivity(new Intent(act, CartListActivity.class));
                    act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    act.startActivity(new Intent(act, LoginActivity.class));
                    act.overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }

            }
        });

        llHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpfHelper.getInstance(act).hasSignIn()) {
                    act.startActivity(new Intent(act, HappyListActivity.class));
                    act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    act.startActivity(new Intent(act, LoginActivity.class));
                    act.overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }

            }
        });

        llRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpfHelper.getInstance(act).hasSignIn()) {
                    act.startActivity(new Intent(act, RankListActivity.class));
                    act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    act.startActivity(new Intent(act, LoginActivity.class));
                    act.overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }
            }
        });

        llSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpfHelper.getInstance(act).hasSignIn()) {
                    act.startActivity(new Intent(act, SetActivity.class));
                    act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    act.startActivity(new Intent(act, LoginActivity.class));
                    act.overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }

            }
        });

        rlExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                act.startActivity(new Intent(act, ExpActivity.class));
                act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });

        llClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpfHelper.getInstance(act).hasSignIn()) {
                    act.startActivity(new Intent(act, ClubActivity.class));
                    act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    act.startActivity(new Intent(act, LoginActivity.class));
                    act.overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }

            }
        });

    }

    public void initData() {
        User user = SpfHelper.getInstance(act).getMyUserInfo();
        tvName.setText(user.name);
        tvExp.setText(user.exp + "/" + user.maxExp);
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llCurrentExp.getLayoutParams();
        BigDecimal currentExp = new BigDecimal(user.exp);
        BigDecimal exp = new BigDecimal(user.maxExp);
        BigDecimal widthBD = currentExp.divide(exp).setScale(2, BigDecimal.ROUND_HALF_UP);
        int width = Utils.dip2px(act, 200) * widthBD.intValue();
        params.width = width;
        llCurrentExp.setLayoutParams(params);
    }
}
