package com.sky.happyf.fragment;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.res.ResourcesCompat;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.sky.happyf.Model.Weather;
import com.sky.happyf.R;
import com.sky.happyf.adapter.MainWeatherAdapter;
import com.sky.happyf.manager.WeatherManager;
import com.sky.happyf.util.Utils;
import com.sky.happyf.view.HorizontalListView;

import java.util.ArrayList;
import java.util.List;

public class MainFragment extends Fragment {
    View view;

    private TextView tvTemp1, tvDesc1, tvTemp2, tvDesc2, tvFit, tvTopWind, tvHighWave, tvLowWave;
    private ImageView ivWeather;
    private RelativeLayout rlBg;
    private LinearLayout llTopLeft1, llTopLeft2, llTopRight, llFit;
    private ScrollView scrollView;
    private boolean isGone = true;
    private boolean isDown = false;
    private HorizontalListView horizontalLvWeather;
    private MainWeatherAdapter mainWeatherAdapter;
    private WeatherManager weatherManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        initView();

        initData();

        initListener();

        return view;
    }

    private void initView() {
        ivWeather = view.findViewById(R.id.iv_weather);
        tvHighWave = view.findViewById(R.id.tv_high_wave);
        tvLowWave = view.findViewById(R.id.tv_low_wave);
        llFit = view.findViewById(R.id.ll_fit);
        tvTemp1 = view.findViewById(R.id.tv_temp_1);
        tvDesc1 = view.findViewById(R.id.tv_desc_1);
        tvTemp2 = view.findViewById(R.id.tv_temp_2);
        tvDesc2 = view.findViewById(R.id.tv_desc_2);
        tvFit = view.findViewById(R.id.tv_top_fish);
        tvTopWind = view.findViewById(R.id.tv_top_wind);
        llTopLeft1 = view.findViewById(R.id.ll_top_left_1);
        llTopLeft2 = view.findViewById(R.id.ll_top_left_2);
        llTopRight = view.findViewById(R.id.ll_top_right);
        scrollView = view.findViewById(R.id.sv_bg);
        rlBg = view.findViewById(R.id.rl_bg);
        horizontalLvWeather = view.findViewById(R.id.horizontal_lv_weather);
        mainWeatherAdapter = new MainWeatherAdapter(getActivity());
        horizontalLvWeather.setAdapter(mainWeatherAdapter);
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
        weatherManager = new WeatherManager(getActivity());
        weatherManager.getWeathers(new WeatherManager.FetchWeathersCallback() {
            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(List<Weather> data) {
                List<Weather> futureWeatherList = new ArrayList<>();
                Weather currentWeather = data.get(0);
                tvTemp1.setText(currentWeather.currentTemp);
                tvDesc1.setText(currentWeather.highTemp + "℃/" + currentWeather.lowTemp + "℃ " + currentWeather.desc);
                Glide.with(getActivity()).load(R.drawable.weather_cloud).into(ivWeather);
                tvTemp2.setText(currentWeather.lowTemp + "℃—" + currentWeather.highTemp + "℃");
                tvDesc2.setText(currentWeather.desc);

                for (int i = 0; i < 3; i++) {
                    LinearLayout.LayoutParams ivFishParams = new LinearLayout.LayoutParams(Utils.dip2px(getActivity(),
                            20), Utils.dip2px(getActivity(), 12));
                    ImageView ivFish = new ImageView(getActivity());
                    ivFishParams.leftMargin = Utils.dip2px(getActivity(), 5);
                    ivFishParams.rightMargin = Utils.dip2px(getActivity(), 5);
                    ivFish.setLayoutParams(ivFishParams);
                    Glide.with(getActivity()).load(R.drawable.main_fish).into(ivFish);
                    llFit.addView(ivFish);
                }
                tvFit.setText("非常适合钓鱼");
                tvTopWind.setText(currentWeather.windDirection + currentWeather.wind + "级");
                tvHighWave.setText("涨潮：" + currentWeather.highWaveTime + " 潮高" + currentWeather.highWave + "米");
                tvLowWave.setText("落潮：" + currentWeather.lowWaveTime + " 潮高" + currentWeather.highWave + "米");

                for (Weather weather : data) {
                    if (!"0".equals(weather.id)) {
                        futureWeatherList.add(weather);
                    }
                }
                mainWeatherAdapter.applyData(futureWeatherList);
            }
        });
    }

    private void initListener() {
        scrollView.setOnScrollChangeListener(new View.OnScrollChangeListener() {
            @Override
            public void onScrollChange(View view, int x, int y, int oldx, int oldy) {
//                Logger.e("now y = " + y + "==== isGone = " + isGone);
                if (y >= Utils.dip2px(getActivity(), 35)) {
                    if (isGone) {
                        llTopLeft2.setVisibility(View.VISIBLE);
                        llTopLeft1.setVisibility(View.GONE);
                        isGone = false;
                    }
                } else {
                    if (!isGone) {
                        llTopLeft1.setVisibility(View.VISIBLE);
                        llTopLeft2.setVisibility(View.GONE);
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
