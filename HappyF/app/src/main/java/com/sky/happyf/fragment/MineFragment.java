package com.sky.happyf.fragment;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class MineFragment extends Fragment {
    private View view;

    private LinearLayout llShell, llClub, llOrder, llCart, llHappy, llRank, llSet, llCurrentExp;
    private ImageView ivLv;
    private RelativeLayout rlExp;
    private TextView tvName, tvExp;
    private CircleImageView ivImage;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mine, container, false);

        initView();

        initListener();

        initData();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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
                if (SpfHelper.getInstance(getActivity()).hasSignIn()) {
                    getActivity().startActivity(new Intent(getActivity(), EditUserActivity.class));
                    getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }
            }
        });
        ivImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpfHelper.getInstance(getActivity()).hasSignIn()) {
                    getActivity().startActivity(new Intent(getActivity(), EditUserActivity.class));
                    getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }
            }
        });

        llOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpfHelper.getInstance(getActivity()).hasSignIn()) {
                    getActivity().startActivity(new Intent(getActivity(), OrderListActivity.class));
                    getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }
            }
        });

        llCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpfHelper.getInstance(getActivity()).hasSignIn()) {
                    getActivity().startActivity(new Intent(getActivity(), CartListActivity.class));
                    getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }

            }
        });

        llHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpfHelper.getInstance(getActivity()).hasSignIn()) {
                    getActivity().startActivity(new Intent(getActivity(), HappyListActivity.class));
                    getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }

            }
        });

        llRank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpfHelper.getInstance(getActivity()).hasSignIn()) {
                    getActivity().startActivity(new Intent(getActivity(), RankListActivity.class));
                    getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }
            }
        });

        llSet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpfHelper.getInstance(getActivity()).hasSignIn()) {
                    getActivity().startActivity(new Intent(getActivity(), SetActivity.class));
                    getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }

            }
        });

        rlExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getActivity().startActivity(new Intent(getActivity(), ExpActivity.class));
                getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });

        llClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SpfHelper.getInstance(getActivity()).hasSignIn()) {
                    getActivity().startActivity(new Intent(getActivity(), ClubActivity.class));
                    getActivity().overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                } else {
                    getActivity().startActivity(new Intent(getActivity(), LoginActivity.class));
                    getActivity().overridePendingTransition(R.anim.bottom_in, R.anim.bottom_silent);
                }

            }
        });

    }

    public void initData() {
        if (SpfHelper.getInstance(getActivity()).hasSignIn()) {
            User user = SpfHelper.getInstance(getActivity()).getMyUserInfo();
            tvName.setText(user.name);
            tvExp.setText(user.exp + "/" + user.maxExp);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llCurrentExp.getLayoutParams();
            BigDecimal currentExp = new BigDecimal(user.exp);
            BigDecimal exp = new BigDecimal(user.maxExp);
            BigDecimal widthBD = currentExp.divide(exp).setScale(2, BigDecimal.ROUND_HALF_UP);
            int width = Utils.dip2px(getActivity(), 200) * widthBD.intValue();
            params.width = width;
            llCurrentExp.setLayoutParams(params);

        } else {
            tvName.setText("未登录");
            tvExp.setText(0 + "/" + 100);
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) llCurrentExp.getLayoutParams();
            BigDecimal currentExp = new BigDecimal(0);
            BigDecimal exp = new BigDecimal(100);
            BigDecimal widthBD = currentExp.divide(exp).setScale(2, BigDecimal.ROUND_HALF_UP);
            int width = Utils.dip2px(getActivity(), 200) * widthBD.intValue();
            params.width = width;
            llCurrentExp.setLayoutParams(params);
        }
    }
}
