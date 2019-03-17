package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.Model.Rank;
import com.sky.happyf.Model.RankData;
import com.sky.happyf.R;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.NetUtils;
import com.sky.happyf.util.SpfHelper;

import org.json.JSONArray;
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
        params.put("user_id", SpfHelper.getInstance(ct).getMyUserInfo().id);
        params.put("limit", "8");
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
                    final List<Rank> rankList = new ArrayList<>();
                    JSONArray rankArray = data.getJSONArray("ranks");
                    for (int i = 0; i < rankArray.length(); i++) {
                        JSONObject obj = rankArray.getJSONObject(i);
                        Rank rank = new Rank();
                        rank.id = obj.optString("id");
                        rank.fishName = obj.optString("fishName");
                        rank.cover = obj.optString("fishCover");
                        rank.date = obj.optString("fishDate");
                        rank.reward = obj.optString("rewardCover");
                        JSONArray rankDataArray = obj.getJSONArray("rankData");
                        List<RankData> rankDataList = new ArrayList<>();
                        for (int j = 0; j < rankDataArray.length(); j++) {
                            JSONObject dataObj = rankDataArray.getJSONObject(j);
                            RankData rankData = new RankData();
                            rankData.name = obj.optString("userName");
                            rankData.phone = obj.optString("userPhone");
                            rankData.weight = obj.optString("fishWeight");
                            rankData.length = obj.optString("fishLength");
                            rankData.userLevel = obj.optString("userLevel");
                            rankDataList.add(rankData);
                            rank.rankDataList = rankDataList;
                        }

                        rankList.add(rank);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(rankList);
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
