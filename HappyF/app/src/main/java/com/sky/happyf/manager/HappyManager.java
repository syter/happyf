package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.Model.Happy;
import com.sky.happyf.Model.SelectType;
import com.sky.happyf.Model.SmallType;
import com.sky.happyf.Model.Type;
import com.sky.happyf.R;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.NetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

public class HappyManager extends Observable {
    private ArrayList<Happy> happyList = new ArrayList<Happy>();
    private Handler handler;
    private Context ct;
    private final static int POST_LIMIT = 20;
    private int page = 0;
    private int totalHappyCount = 0;

    public HappyManager(Context ct) {
        this.ct = ct;
        handler = new Handler();
    }

    public void getMapInfo(final FetchHappysCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalMapInfo(callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        NetUtils.get(ct, params, Constants.PATH_GET_MAP_INFO, new NetUtils.NetCallback() {
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
                    final List<Happy> happyList = new ArrayList<>();
                    JSONArray objList = data.getJSONArray("list");
                    for (int i = 0; i < objList.length(); i++) {
                        JSONObject obj = objList.getJSONObject(i);
                        Happy happy = new Happy();
                        happy.id = obj.getString("id");
                        happy.title1 = obj.optString("title");
                        happy.title2 = obj.optString("title2");
                        happy.title3 = obj.optString("title3");
                        happy.longitude = obj.optString("lon");
                        happy.latitude = obj.optString("lat");
                        happyList.add(happy);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(happyList);
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

    public void getLocalMapInfo(final FetchHappysCallback callback) {
        if (callback != null) {
            callback.onFinish(new ArrayList<Happy>());
        }
    }


    public void getHappyInfo(final String id, final FetchHappyCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalHappyInfo(callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("area_id", id);
        NetUtils.get(ct, params, Constants.PATH_GET_HAPPY_INFO, new NetUtils.NetCallback() {
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
                    final Happy happy = new Happy();

                    happy.id = data.getString("id");
                    happy.title1 = data.optString("title");
                    happy.title2 = data.optString("title2");
                    happy.title3 = data.optString("title3");
                    happy.price = data.optString("price");
                    happy.content = data.optString("content");
                    JSONArray coverArray = data.optJSONArray("covers");
                    List<String> covers = new ArrayList<>();
                    for (int i = 0; i < coverArray.length(); i++) {
                        String cover = coverArray.getString(i);
                        covers.add(cover);
                    }
                    happy.covers = covers;

                    JSONArray descCoverArray = data.optJSONArray("desc_cover");
                    List<String> descCovers = new ArrayList<>();
                    for (int i = 0; i < descCoverArray.length(); i++) {
                        String cover = descCoverArray.getString(i);
                        descCovers.add(cover);
                    }
                    happy.descCovers = descCovers;

                    JSONArray selectTypeArray = data.optJSONArray("selectType");
                    List<SelectType> selectTypeList = new ArrayList<>();
                    for (int i = 0; i < selectTypeArray.length(); i++) {
                        JSONObject obj = selectTypeArray.getJSONObject(i);
                        SelectType st = new SelectType();
                        st.id = obj.getString("id");
                        st.name = obj.getString("name");
                        selectTypeList.add(st);
                    }
                    happy.selectTypeList = selectTypeList;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(happy);
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

    public void getLocalHappyInfo(final FetchHappyCallback callback) {
        if (callback != null) {
            callback.onFinish(new Happy());
        }
    }

    public interface FetchHappysCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<Happy> happyList);
    }

    public interface FetchHappyCallback {
        public void onFailure(String errorMsg);

        public void onFinish(Happy happy);
    }
}
