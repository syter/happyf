package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.Model.Club;
import com.sky.happyf.Model.Weather;
import com.sky.happyf.R;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.NetUtils;
import com.sky.happyf.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

public class WeatherManager extends Observable {
    private Handler handler;
    private Context ct;

    public WeatherManager(Context ct) {
        this.ct = ct;
        handler = new Handler();
    }

    public void getClubs(final WeatherManager.FetchClubsCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalClubs(callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
//        params.put("type", "login");
        NetUtils.post(ct, params, Constants.PATH_GET_CLUBS, new NetUtils.NetCallback() {
            @Override
            public void onFailure(final String errorMsg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFailure(errorMsg);
                        }
                    }
                });
            }

            @Override
            public void onFinish(JSONObject data) {
                try {
                    final List<Club> clubList = new ArrayList<>();
                    JSONArray clubsArray = data.getJSONArray("clubs");
                    for (int i = 0; i < clubsArray.length(); i++) {
                        JSONObject obj = clubsArray.getJSONObject(i);
                        Club club = new Club();
                        club.name = obj.optString("name");
                        club.code = obj.optString("code");
                        clubList.add(club);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(clubList);
                            }
                        }
                    });
                } catch (Exception e) {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFailure(ct.getResources().getString(R.string.common_error));
                            }
                        }
                    });
                }
            }
        });
    }

    public void getLocalClubs(final WeatherManager.FetchClubsCallback callback) {
        if (callback != null) {
            callback.onFinish(new ArrayList<Club>());
        }
    }


    public void getWeathers(final Club club, final WeatherManager.FetchWeathersCallback callback) {
        // 获取天气
        Map<String, String> params = new TreeMap<String, String>();
        params.put("key", Constants.WEATHER_APIKEY);
        params.put("location", club.code);
        params.put("language", "zh-Hans");
        params.put("unit", "c");
        params.put("start", "0");
        params.put("days", "10");

        NetUtils.pureGet(ct, params, Constants.WEATHER_PATH_WEATHER, new NetUtils.NetCallback() {
            @Override
            public void onFailure(final String errorMsg) {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFailure(errorMsg);
                        }
                    }
                });
            }

            @Override
            public void onFinish(final JSONObject weatherData) {
                // 获取潮汐
                Map<String, String> params = new TreeMap<String, String>();
                params.put("key", Constants.WEATHER_APIKEY);
                params.put("location", club.code);
                NetUtils.pureGet(ct, params, Constants.WEATHER_PATH_TIDE, new NetUtils.NetCallback() {
                    @Override
                    public void onFailure(final String errorMsg) {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (callback != null) {
                                    callback.onFailure(errorMsg);
                                }
                            }
                        });
                    }

                    @Override
                    public void onFinish(final JSONObject tideData) {
                        // 获取当前天气
                        Map<String, String> params = new TreeMap<String, String>();
                        params.put("key", Constants.WEATHER_APIKEY);
                        params.put("location", club.code);
                        params.put("language", "zh-Hans");
                        params.put("unit", "c");
                        NetUtils.pureGet(ct, params, Constants.WEATHER_PATH_WEATHER_NOW, new NetUtils.NetCallback() {
                            @Override
                            public void onFailure(final String errorMsg) {
                                handler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (callback != null) {
                                            callback.onFailure(errorMsg);
                                        }
                                    }
                                });
                            }

                            @Override
                            public void onFinish(final JSONObject nowData) {
                                try {
                                    final List<Weather> weatherList = new ArrayList<>();
                                    JSONArray weatherArray = weatherData.getJSONArray("results").getJSONObject(0).getJSONArray("daily");
                                    JSONArray tideArray = tideData.getJSONArray("results").getJSONObject(0).getJSONArray("ports").
                                            getJSONObject(0).getJSONArray("data");
                                    JSONObject nowObj = nowData.getJSONArray("results").getJSONObject(0).getJSONObject("now");
                                    for (int i = 0; i < weatherArray.length(); i++) {
                                        if (i == 10) {
                                            break;
                                        }
                                        JSONObject weatherObj = weatherArray.getJSONObject(i);
                                        JSONObject tideDataObj = tideArray.getJSONObject(i);
                                        Weather weather = new Weather();
                                        weather.date = weatherObj.optString("date");
                                        weather.lowTemp = weatherObj.optString("low");
                                        weather.highTemp = weatherObj.optString("high");

                                        weather.windDirection = nowObj.optString("wind_direction");
                                        weather.wind = nowObj.optString("wind_scale");
                                        weather.currentTemp = nowObj.optString("temperature");
                                        weather.desc = nowObj.optString("text");
                                        weather.weatherType = nowObj.optString("code");

                                        JSONArray waveArray = tideDataObj.getJSONArray("tide");
                                        List<String> waveList = new ArrayList<>();
                                        String maxWave = "";
                                        String minWave = "";
                                        int maxWaveIndex = -1;
                                        int minWaveIndex = -1;
                                        for (int j = 0; j < waveArray.length(); j++) {
                                            String height = waveArray.getString(j);
                                            waveList.add(height);
                                            if (Utils.isEmptyString(maxWave)) {
                                                maxWave = height;
                                                minWave = height;
                                                maxWaveIndex = j;
                                                minWaveIndex = j;
                                            } else {
                                                if (new BigDecimal(height).compareTo(new BigDecimal(maxWave)) > 0) {
                                                    maxWave = height;
                                                    maxWaveIndex = j;
                                                } else if (new BigDecimal(height).compareTo(new BigDecimal(minWave)) < 0) {
                                                    minWave = height;
                                                    minWaveIndex = j;
                                                }
                                            }
                                        }
                                        weather.waveList = waveList;
                                        weather.lowWave = minWave;
                                        weather.lowWaveTime = minWaveIndex + "";
                                        weather.highWave = maxWave;
                                        weather.highWaveTime = maxWaveIndex + "";
                                        weatherList.add(weather);
                                    }

                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (callback != null) {
                                                callback.onFinish(weatherList);
                                            }
                                        }
                                    });
                                } catch (Exception e) {
                                    handler.post(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (callback != null) {
                                                callback.onFailure(ct.getResources().getString(R.string.common_error));
                                            }
                                        }
                                    });
                                }
                            }
                        });
                    }
                });
            }
        });
    }


    public interface FetchWeathersCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<Weather> data);
    }

    public interface FetchClubsCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<Club> data);
    }


}
