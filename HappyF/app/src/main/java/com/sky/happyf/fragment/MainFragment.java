package com.sky.happyf.fragment;

import android.graphics.DashPathEffect;
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

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.bumptech.glide.Glide;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.sky.happyf.Model.Club;
import com.sky.happyf.Model.Weather;
import com.sky.happyf.R;
import com.sky.happyf.adapter.MainWeatherAdapter;
import com.sky.happyf.manager.WeatherManager;
import com.sky.happyf.util.Utils;
import com.sky.happyf.view.HorizontalListView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class MainFragment extends Fragment {
    View view;

    private TextView tvTemp1, tvDesc1, tvTemp2, tvDesc2, tvFit, tvTopWind, tvHighWave, tvLowWave,
            tvClubName;
    private ImageView ivWeather;
    private RelativeLayout rlBg;
    private LinearLayout llTopLeft1, llTopLeft2, llTopRight, llFit, llClub;
    private ScrollView scrollView;
    private boolean isGone = true;
    private boolean isDown = false;
    private HorizontalListView horizontalLvWeather;
    private MainWeatherAdapter mainWeatherAdapter;
    private WeatherManager weatherManager;
    private Club currentClub;
    private LineChart lcWave;
    private LineDataSet set1;
    private OptionsPickerView clubsSelector;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_main, container, false);

        initView();

        initData();

        initListener();

        return view;
    }

    private void initView() {
        llClub = view.findViewById(R.id.ll_club);
        lcWave = view.findViewById(R.id.lc_wave);
        tvClubName = view.findViewById(R.id.tv_club_name);
        ivWeather = view.findViewById(R.id.iv_weather);
        tvHighWave = view.findViewById(R.id.tv_high_wave);
        tvLowWave = view.findViewById(R.id.tv_low_wave);
        llFit = view.findViewById(R.id.ll_fit);
        tvTemp1 = view.findViewById(R.id.tv_temp_1);
        tvDesc1 = view.findViewById(R.id.tv_desc_1);
        tvTemp2 = view.findViewById(R.id.tv_temp_2);
        tvDesc2 = view.findViewById(R.id.tv_desc_2);
        tvFit = view.findViewById(R.id.tv_fit);
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


        //设置手势滑动事件
//        lcWave.setOnChartGestureListener(this);
        //设置数值选择监听
//        lcWave.setOnChartValueSelectedListener(this);
        //后台绘制
        lcWave.setDrawGridBackground(false);
        //设置描述文本
        lcWave.getDescription().setEnabled(false);
        //设置支持触控手势
        lcWave.setTouchEnabled(true);
        //设置缩放
        lcWave.setDragEnabled(true);
        //设置推动
        lcWave.setScaleEnabled(true);
        //如果禁用,扩展可以在x轴和y轴分别完成
        lcWave.setPinchZoom(true);

        lcWave.setNoDataText("暂无数据");




        XAxis xAxis = lcWave.getXAxis();
        xAxis.enableGridDashedLine(10f, 10f, 0f);
        //设置x轴的最大值
//        xAxis.setAxisMaximum(100f);
        //设置x轴的最小值
//        xAxis.setAxisMinimum(0f);
        //设置x轴的显示位置
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        //设置X轴值为字符串
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String hour = String.valueOf((int) value);
                if (hour.length() == 1) {
                    return "0" + hour + ":00";
                } else {
                    return hour + ":00";
                }
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        YAxis leftAxis = lcWave.getAxisLeft();
        //重置所有限制线,以避免重叠线
        leftAxis.removeAllLimitLines();
        //y轴最大
//        leftAxis.setAxisMaximum(200f);
        //y轴最小
//        leftAxis.setAxisMinimum(0f);
        leftAxis.enableGridDashedLine(10f, 10f, 0f);
        leftAxis.setDrawZeroLine(false);

        // 限制数据(而不是背后的线条勾勒出了上面)
        leftAxis.setDrawLimitLinesBehindData(true);
        leftAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                String wave = String.valueOf((int) value);
                return wave + "米";
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });

        //默认动画
        lcWave.animateX(500);
        //刷新
        //mChart.invalidate();

        // 得到这个文字
        Legend l = lcWave.getLegend();

        // 修改文字 ...
        l.setForm(Legend.LegendForm.LINE);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_CENTER);

        lcWave.getAxisRight().setEnabled(false);
    }

    //传递数据集
    private void setLineChartData(ArrayList<Entry> values) {
        if (lcWave.getData() != null && lcWave.getData().getDataSetCount() > 0) {
            set1 = (LineDataSet) lcWave.getData().getDataSetByIndex(0);
            set1.setValues(values);
            lcWave.getData().notifyDataChanged();
            lcWave.notifyDataSetChanged();
        } else {
            // 创建一个数据集,并给它一个类型
            set1 = new LineDataSet(values, "每小时潮浪高度");

            // 在这里设置线
//            set1.enableDashedLine(10f, 5f, 0f);
//            set1.enableDashedHighlightLine(10f, 5f, 0f);
            set1.setColor(getActivity().getColor(R.color.line_chart));
            set1.setCircleColor(getActivity().getColor(R.color.line_chart));
            set1.setLineWidth(2f);
            set1.setCircleRadius(3f);
            set1.setDrawCircleHole(false);
            set1.setValueTextSize(9f);
            set1.setDrawFilled(true);
            set1.setFormLineWidth(1f);
            set1.setFormLineDashEffect(new DashPathEffect(new float[]{10f, 5f}, 0f));
            set1.setFormSize(15.f);

            // 不填充
            set1.setDrawFilled(false);
            set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);

            lcWave.setAutoScaleMinMaxEnabled(!lcWave.isAutoScaleMinMaxEnabled());
//            if (Utils.getSDKInt() >= 18) {
//                // 填充背景只支持18以上
//                //Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.ic_launcher);
//                //set1.setFillDrawable(drawable);
//                set1.setFillColor(Color.YELLOW);
//            } else {
//                set1.setFillColor(Color.TRANSPARENT);
//            }
            ArrayList<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
            //添加数据集
            dataSets.add(set1);

            //创建一个数据集的数据对象
            LineData data = new LineData(dataSets);

            //谁知数据
            lcWave.setData(data);
        }
    }

    private void initData() {
        weatherManager = new WeatherManager(getActivity());

        weatherManager.getClubs(new WeatherManager.FetchClubsCallback() {
            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(getActivity(), errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(final List<Club> data) {
                final List<String> items = new ArrayList<>();
                currentClub = data.get(0);

                tvClubName.setText(currentClub.name);

                for (Club club : data) {
                    items.add(club.name);
                }

                clubsSelector = new OptionsPickerBuilder(getActivity(), new OnOptionsSelectListener() {
                    @Override
                    public void onOptionsSelect(int options1, int option2, int options3, View v) {
                        //返回的分别是三个级别的选中位置
                        String tx = items.get(options1);
                        tvClubName.setText(tx);
                        for (int i = 0; i < data.size(); i++) {
                            Club c = data.get(i);
                            if (tx.equals(c.name)) {
                                currentClub = c;
                                break;
                            }
                        }
                        initWeather();
                    }
                })
                        .build();

                clubsSelector.setPicker(items);//添加数据

                initWeather();
            }
        });
    }

    private void initWeather() {
        weatherManager.getWeathers(currentClub, new WeatherManager.FetchWeathersCallback() {
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
                Utils.setWeatherImage(getActivity(), currentWeather.weatherType, ivWeather);
                tvTemp2.setText(currentWeather.lowTemp + "℃—" + currentWeather.highTemp + "℃");
                tvDesc2.setText(currentWeather.desc);

                // TODO
                llFit.removeAllViews();
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

                tvTopWind.setText(currentWeather.windDirection + "风 " + currentWeather.wind + "级");
                tvHighWave.setText("干潮：" + Utils.getTimeByIndex(currentWeather.highWaveTime) + " 潮高" +
                        new BigDecimal(currentWeather.highWave).divide(new BigDecimal("100")).setScale(1, BigDecimal.ROUND_HALF_UP) + "米");
                tvLowWave.setText("满潮：" + Utils.getTimeByIndex(currentWeather.lowWaveTime) + " 潮高" +
                        new BigDecimal(currentWeather.lowWave).divide(new BigDecimal("100")).setScale(1, BigDecimal.ROUND_HALF_UP) + "米");
                for (Weather weather : data) {
                    futureWeatherList.add(weather);
                }
                mainWeatherAdapter.applyData(futureWeatherList);

                // 初始化中间表格数据
                ArrayList<Entry> values = new ArrayList<Entry>();
                int currentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
                if (currentHour >= 16) {
                    currentHour = 16;
                }
                for (int i = currentHour; i < currentHour + 8; i++) {
                    BigDecimal wave = new BigDecimal(currentWeather.waveList.get(i));
                    wave = wave.divide(new BigDecimal("100"));
                    wave = wave.setScale(1, BigDecimal.ROUND_HALF_UP);
                    Entry entry = new Entry(i, wave.floatValue());
                    values.add(entry);
                }
//                for (int i = 0; i < 24; i++) {
//                    BigDecimal wave = new BigDecimal(currentWeather.waveList.get(i));
//                    wave = wave.divide(new BigDecimal("100"));
//                    wave = wave.setScale(2, BigDecimal.ROUND_HALF_UP);
//                    Entry entry = new Entry(i, wave.floatValue());
//                    values.add(entry);
//                }
                //设置数据
                setLineChartData(values);

                //刷新
                lcWave.invalidate();
            }
        });
    }

    private void initListener() {
        llClub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clubsSelector.show();
            }
        });

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
