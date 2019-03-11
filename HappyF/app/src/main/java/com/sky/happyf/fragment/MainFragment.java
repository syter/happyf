package com.sky.happyf.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sky.happyf.R;
import com.sky.happyf.util.Utils;

public class MainFragment extends Fragment {
    View view;

    private TextView tvTemp1, tvDesc1, tvTemp2, tvDesc2, tvTopFish, tvTopWind;
    private RelativeLayout rlBg;
    private LinearLayout llTopLeft1, llTopLeft2, llTopRight;
    private ScrollView scrollView;
    private boolean isGone = true;
    private boolean isDown = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        initView();

        initData();

        initListener();

        return view;
    }

    private void initView() {
        tvTemp1 = view.findViewById(R.id.tv_temp_1);
        tvDesc1 = view.findViewById(R.id.tv_desc_1);
        tvTemp2 = view.findViewById(R.id.tv_temp_2);
        tvDesc2 = view.findViewById(R.id.tv_desc_2);
        tvTopFish = view.findViewById(R.id.tv_top_fish);
        tvTopWind = view.findViewById(R.id.tv_top_wind);
        llTopLeft1 = view.findViewById(R.id.ll_top_left_1);
        llTopLeft2 = view.findViewById(R.id.ll_top_left_2);
        llTopRight = view.findViewById(R.id.ll_top_right);
        scrollView = view.findViewById(R.id.sv_bg);
        rlBg = view.findViewById(R.id.rl_bg);
        Typeface typeface = ResourcesCompat.getFont(getActivity(), R.font.fzltcxhjw);
        tvTemp1.setTypeface(typeface);
        tvDesc1.setTypeface(typeface);
        tvTemp2.setTypeface(typeface);
        tvDesc2.setTypeface(typeface);
        tvTopWind.setTypeface(typeface);

        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int realBgHeight = 1724 * screenWidth / 750;
        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) rlBg.getLayoutParams();
        params.height = realBgHeight;
        rlBg.setLayoutParams(params);

        int halfScreenWidth = screenWidth / 2;
        Logger.e("====halfScreenWidth=" + halfScreenWidth);
        LinearLayout.LayoutParams params1 = (LinearLayout.LayoutParams) llTopLeft1.getLayoutParams();
        params1.width = halfScreenWidth;
        llTopLeft1.setLayoutParams(params1);
        LinearLayout.LayoutParams params2 = (LinearLayout.LayoutParams) llTopLeft2.getLayoutParams();
        params2.width = halfScreenWidth;
        llTopLeft2.setLayoutParams(params2);
        LinearLayout.LayoutParams params3 = (LinearLayout.LayoutParams) llTopRight.getLayoutParams();
        params3.width = halfScreenWidth;
        llTopRight.setLayoutParams(params3);

    }

    private void initData() {

    }

    private void initListener() {
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int x, int y, int oldx, int oldy) {
                Logger.e("now y = " + y + "==== isGone = " + isGone);
                if (y >= Utils.dip2px(getActivity(), 35)) {
                    if (isGone) {
                        llTopLeft2.setVisibility(View.VISIBLE);
                        llTopLeft1.setVisibility(View.GONE);
//                        TranslateAnimation visibleAnim = new TranslateAnimation(0, 0, 150, 0);
//                        visibleAnim.setDuration(200);
//                        visibleAnim.setFillAfter(true);
//                        llTopLeft1.setAnimation(visibleAnim);
                        isGone = false;
                    }
                } else {
                    if (!isGone) {
                        llTopLeft1.setVisibility(View.VISIBLE);
                        llTopLeft2.setVisibility(View.GONE);
//                        TranslateAnimation goneAnim = new TranslateAnimation(0, 0, 0, 150);
//                        goneAnim.setDuration(200);
//                        goneAnim.setFillAfter(true);
//                        llTopLeft2.setAnimation(goneAnim);
                        isGone = true;
                    }
                }

                if (!isDown) {
                    if (y >= Utils.dip2px(getActivity(), 80)) {
                        scrollView.fullScroll(ScrollView.FOCUS_DOWN);
                        isDown = true;
                    }
                } else {
                    if (y < Utils.dip2px(getActivity(), 80)) {
                        scrollView.fullScroll(ScrollView.FOCUS_UP);
                        isDown = false;
                    }
                }

            }
        });
    }
}
