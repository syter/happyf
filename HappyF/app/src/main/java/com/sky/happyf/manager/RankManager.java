package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.Model.Rank;
import com.sky.happyf.R;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.NetUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

public class RankManager extends Observable {
    private Handler handler;
    private Context ct;

    public RankManager(Context ct) {
        this.ct = ct;
        handler = new Handler();
    }

    public void getRanks(final RankManager.FetchRanksCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalRanks(callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
//        params.put("type", "login");
        NetUtils.post(ct, params, Constants.PATH_GET_RANKS, new NetUtils.NetCallback() {
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
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(new ArrayList<Rank>());
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

    public void getLocalRanks(final RankManager.FetchRanksCallback callback) {
        if (callback != null) {
            callback.onFinish(new ArrayList<Rank>());
        }
    }


    public interface FetchRanksCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<Rank> data);
    }
}
