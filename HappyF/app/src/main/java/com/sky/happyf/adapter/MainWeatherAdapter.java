package com.sky.happyf.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.happyf.Model.Weather;
import com.sky.happyf.R;
import com.sky.happyf.util.Utils;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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
            tvLvFit = findViewById(R.id.tv_lv_fit);
            ivLvWeather = findViewById(R.id.iv_lv_weather);
        }

        public void setData(final Weather weather) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                Date date = sdf.parse(weather.date);
                Calendar c = Calendar.getInstance();
                c.setTime(date);
                int weekday = c.get(Calendar.DAY_OF_WEEK);
                SimpleDateFormat sdf2 = new SimpleDateFormat("MM/dd");
                tvLvDate.setText(sdf2.format(date) + " (" + getWeek(weekday) + ")");
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Utils.setWeatherImage(ct, weather.weatherType, ivLvWeather);

            tvLvDesc.setText(weather.desc);
            tvLvTempRange.setText(weather.lowTemp + "℃—" + weather.highTemp + "℃");
            tvLvWaveRange.setText(new BigDecimal(weather.lowWave).divide(new BigDecimal("100")).setScale(1, BigDecimal.ROUND_HALF_UP) +
                    "—" + new BigDecimal(weather.highWave).divide(new BigDecimal("100")).setScale(1, BigDecimal.ROUND_HALF_UP) + "米");

            // TODO
            tvLvFit.setText("非常适合钓鱼");


        }

        private String getWeek(int day) {
            String weekDay = "";
            switch (day) {
                case 0:
                    weekDay = "周六";
                    break;
                case 1:
                    weekDay = "周日";
                    break;
                case 2:
                    weekDay = "周一";
                    break;
                case 3:
                    weekDay = "周二";
                    break;
                case 4:
                    weekDay = "周三";
                    break;
                case 5:
                    weekDay = "周四";
                    break;
                case 6:
                    weekDay = "周五";
                    break;

            }
            return weekDay;
        }
    }
}
