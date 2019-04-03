package com.sky.happyf.util;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.sky.happyf.Model.Weather;
import com.sky.happyf.R;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;


public class Utils {
    /**
     * 获取当前网络连接的类型信息
     */
    public static int getConnectedType(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null && mNetworkInfo.isAvailable()) {
                return mNetworkInfo.getType();
            }
        }
        return -1;
    }

    /**
     * 判断是否有网络连接
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断WIFI网络是否可用
     */
    public static boolean isWifiConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mWiFiNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (mWiFiNetworkInfo != null) {
                return mWiFiNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 判断MOBILE网络是否可用
     */
    public static boolean isMobileConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mMobileNetworkInfo = mConnectivityManager
                    .getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (mMobileNetworkInfo != null) {
                return mMobileNetworkInfo.isAvailable();
            }
        }
        return false;
    }

    /**
     * 使用 Map按key进行排序
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(new Comparator<String>() {
            @Override
            public int compare(String s1, String s2) {
                return s1.compareTo(s2);
            }
        });
        sortMap.putAll(map);

        return sortMap;
    }

    /**
     * 判断空字符串
     */
    public static boolean isEmptyString(String str) {
        if (str == null || str.length() == 0 || str.trim().length() == 0 || str.equals("null")) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 根据手机分辨率从DP转成PX
     *
     * @param context
     * @param dpValue
     * @return
     */
    public static int dip2px(Context context, float dpValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 将sp值转换为px值，保证文字大小不变
     *
     * @param spValue
     * @return
     */
    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * 根据手机的分辨率PX(像素)转成DP
     *
     * @param context
     * @param pxValue
     * @return
     */
    public static int px2dip(Context context, float pxValue) {
        float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 将px值转换为sp值，保证文字大小不变
     *
     * @param pxValue
     * @return
     */

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }


    /**
     * 从控件所在位置移动到控件的底部
     *
     * @return
     */
    public static TranslateAnimation moveToViewBottom() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 1.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    /**
     * 从控件所在位置移动到控件的左边
     *
     * @return
     */
    public static TranslateAnimation moveToViewLeft() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, -1.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    /**
     * 从控件的底部移动到控件所在位置
     *
     * @return
     */
    public static TranslateAnimation moveToViewLocation() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                1.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    /**
     * 从控件的左边移动到控件所在位置
     *
     * @return
     */
    public static TranslateAnimation moveToViewLocationLeft() {
        TranslateAnimation mHiddenAction = new TranslateAnimation(Animation.RELATIVE_TO_SELF, -1.0f,
                Animation.RELATIVE_TO_SELF, 0.0f, Animation.RELATIVE_TO_SELF,
                0.0f, Animation.RELATIVE_TO_SELF, 0.0f);
        mHiddenAction.setDuration(500);
        return mHiddenAction;
    }

    /**
     * 获取默认的省市区信息
     *
     * @return
     */
    public static JSONArray getProvinceInfo(Activity act) {
        try {
            InputStreamReader isr = new InputStreamReader(act.getAssets().open("city.json"), "UTF-8");
            BufferedReader br = new BufferedReader(isr);
            String line;
            StringBuilder builder = new StringBuilder();
            while ((line = br.readLine()) != null) {
                builder.append(line);
            }
            br.close();
            isr.close();
            JSONArray array = new JSONArray(builder.toString());//builder读取了JSON中的数据。
            return array;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setWeatherImage(Context ct, String weatherType, ImageView ivWeather) {
        // https://www.seniverse.com/doc#code
        switch (weatherType) {
            case "0":
            case "1":
            case "2":
            case "3":
            case "38":
                Glide.with(ct).load(R.drawable.weather_1).into(ivWeather);
                break;
            case "4":
            case "5":
            case "6":
            case "7":
            case "8":
                Glide.with(ct).load(R.drawable.weather_4).into(ivWeather);
                break;
            case "9":
                Glide.with(ct).load(R.drawable.weather_9).into(ivWeather);
                break;
            case "10":
                Glide.with(ct).load(R.drawable.weather_10).into(ivWeather);
                break;
            case "11":
                Glide.with(ct).load(R.drawable.weather_11).into(ivWeather);
                break;
            case "12":
                Glide.with(ct).load(R.drawable.weather_12).into(ivWeather);
                break;
            case "13":
                Glide.with(ct).load(R.drawable.weather_13).into(ivWeather);
                break;
            case "14":
                Glide.with(ct).load(R.drawable.weather_14).into(ivWeather);
                break;
            case "15":
                Glide.with(ct).load(R.drawable.weather_15).into(ivWeather);
                break;
            case "16":
            case "17":
            case "18":
                Glide.with(ct).load(R.drawable.weather_16).into(ivWeather);
                break;
            case "19":
                Glide.with(ct).load(R.drawable.weather_19).into(ivWeather);
                break;
            case "20":
                Glide.with(ct).load(R.drawable.weather_20).into(ivWeather);
                break;
            case "21":
                Glide.with(ct).load(R.drawable.weather_21).into(ivWeather);
                break;
            case "22":
                Glide.with(ct).load(R.drawable.weather_22).into(ivWeather);
                break;
            case "23":
                Glide.with(ct).load(R.drawable.weather_23).into(ivWeather);
                break;
            case "24":
                Glide.with(ct).load(R.drawable.weather_24).into(ivWeather);
                break;
            case "25":
                Glide.with(ct).load(R.drawable.weather_25).into(ivWeather);
                break;
            case "26":
                Glide.with(ct).load(R.drawable.weather_26).into(ivWeather);
                break;
            case "27":
                Glide.with(ct).load(R.drawable.weather_27).into(ivWeather);
                break;
            case "28":
            case "29":
                Glide.with(ct).load(R.drawable.weather_28).into(ivWeather);
                break;
            case "30":
                Glide.with(ct).load(R.drawable.weather_30).into(ivWeather);
                break;
            case "31":
                Glide.with(ct).load(R.drawable.weather_31).into(ivWeather);
                break;
            case "32":
            case "33":
                Glide.with(ct).load(R.drawable.weather_32).into(ivWeather);
                break;
            case "34":
            case "35":
                Glide.with(ct).load(R.drawable.weather_34).into(ivWeather);
                break;
            case "36":
                Glide.with(ct).load(R.drawable.weather_36).into(ivWeather);
                break;
            case "37":
                Glide.with(ct).load(R.drawable.weather_37).into(ivWeather);
                break;
            default:
                Glide.with(ct).load(R.drawable.weather_99).into(ivWeather);
                break;
        }
    }

    public static String getTimeByIndex(String timeIndex) {
        if (timeIndex.length() == 1) {
            return "0" + timeIndex + ":00";
        } else {
            return timeIndex + ":00";
        }
    }

    public static int getFitType(Weather weather) {
        int temp = Integer.parseInt(weather.lowTemp);
        int type = Integer.parseInt(weather.weatherType);
        if (isEmptyString(weather.wind)) {
            weather.wind = "0";
        }
        int wind = Integer.parseInt(weather.wind);
        BigDecimal highWave = new BigDecimal(weather.highWave);
        BigDecimal lowWave = new BigDecimal(weather.lowWave);
        float wave = highWave.subtract(lowWave).floatValue();
        if ((temp <= 30 && temp >= 10) &&
                (type == 0 || type == 1 || type == 2 || type == 3 || type == 4 || type == 5 || type == 6 || type == 7 || type == 8 || type == 9) &&
                wind < 6 &&
                wave < 3) {
            return 3;
        } else if (((temp <= 40 && temp >= 30) || (temp <= 10 && temp >= 2)) &&
                (type == 0 || type == 1 || type == 2 || type == 3 || type == 4 || type == 5 || type == 6 || type == 7 || type == 8 || type == 9 || type == 13) &&
                (wind >= 6 && wind < 8) &&
                (wave >= 3 && wave < 6)) {
            return 2;
        } else {
            return 1;
        }
    }

    public static void showKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            view.requestFocus();
            imm.showSoftInput(view, 0);
        }
    }

    public static void hideKeyboard(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void toggleSoftInput(View view) {
        InputMethodManager imm = (InputMethodManager) view.getContext()
                .getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.toggleSoftInput(0, 0);
        }
    }

}
