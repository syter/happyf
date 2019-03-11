package com.sky.happyf.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.sky.happyf.Model.Weather;
import com.sky.happyf.R;

import java.util.ArrayList;
import java.util.List;


public class MainWeatherAdapter extends BaseAdapter {
    private List<Weather> weatherList;
    private Context ct;

    public MainWeatherAdapter(Context ct) {
        this.ct = ct;
        weatherList = new ArrayList<Weather>();
    }

    public List<Weather> getList() {
        return weatherList;
    }

    public void applyData(List<Weather> weathers) {
        weatherList.clear();
        weatherList.addAll(weathers);

        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return weatherList.size();
    }

    @Override
    public Weather getItem(int position) {
        return weatherList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderListItem view = (OrderListItem) convertView;
        if (convertView == null) {
            view = new OrderListItem(parent.getContext());
        }

        view.setData(weatherList.get(position));

        return view;
    }

    public class OrderListItem extends LinearLayout {
        private TextView tvLvDate, tvLvDesc, tvLvTempRange, tvLvWaveRange, tvLvFit;
        private ImageView ivLvWeather;

        public OrderListItem(Context ct) {
            super(ct);
            inflate(getContext(), R.layout.lvitem_weather, this);
            tvLvDate = findViewById(R.id.tv_lv_date);
            tvLvDesc = findViewById(R.id.tv_lv_desc);
            tvLvTempRange = findViewById(R.id.tv_lv_temp_range);
            tvLvWaveRange = findViewById(R.id.tv_lv_wave_range);
            tvLvFit = findViewById(R.id.tv_lv_date);
            ivLvWeather = findViewById(R.id.iv_lv_weather);
        }

        public void setData(final Weather weather) {
            tvLvDate.setText(weather.date);
            tvLvDesc.setText(weather.desc);
            tvLvTempRange.setText(weather.lowTemp + "℃—" + weather.highTemp + "℃");
            tvLvWaveRange.setText(weather.lowWave + "—" + weather.highWave + "米");
            tvLvFit.setText("不太合适");
            Glide.with(ct).load(R.drawable.weather_cloud).into(ivLvWeather);
        }
    }
}
