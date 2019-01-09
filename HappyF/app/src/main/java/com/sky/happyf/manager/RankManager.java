package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.Model.Rank;
import com.sky.happyf.Model.User;
import com.sky.happyf.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class RankManager extends Observable {
    private ArrayList<Rank> rankList = new ArrayList<Rank>();
    private Handler handler;
    private Context ct;
    private final static int POST_LIMIT = 20;
    private int page = 0;
    private int totalRankCount = 0;

    public RankManager(Context ct) {
        this.ct = ct;
        handler = new Handler();
    }

    public List<Rank> getLocalRanks() {
        List<Rank> RankList = null;
        return RankList;
    }

    public void init(final int state, final FetchRanksCallback callback) {
        page = 0;
        rankList = new ArrayList<Rank>();
        if (Constants.IS_DEBUG) {
            getLocalRanks(state, callback);
            return;
        }
        fetchRemoteRanks(state, ++page, new FetchRanksCallback() {
            @Override
            public void onFailure(String errorMsg) {
                if (callback != null) {
                    callback.onFailure(errorMsg);
                }
            }

            @Override
            public void onFinish(List<Rank> data) {
                rankList.addAll(data);
                if (callback != null) {
                    callback.onFinish(data);
                }
            }
        });
    }

    public void loadMore(final int state, final FetchRanksCallback callback) {
        if (Constants.IS_DEBUG) {
            loadMoreLocalRanks(state, callback, ++page);
            return;
        }
        fetchRemoteRanks(state, ++page, new FetchRanksCallback() {
            @Override
            public void onFailure(String errorMsg) {
                page--;
                if (callback != null) {
                    callback.onFailure(errorMsg);
                }
            }

            @Override
            public void onFinish(List<Rank> data) {
                rankList.addAll(data);
                if (callback != null) {
                    callback.onFinish(rankList);
                }
            }
        });
    }


    private void getLocalRanks(final int state, final FetchRanksCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 17; i++) {
                    Rank o = new Rank();
                    User u = new User();
                    u.cover = "";
                    u.name = "二大爷";
                    o.user = u;
                    o.type = state;
                    if (state == 1) {
                        o.length = "0.56";
                        o.weight = "14";
                        o.fishName = "鲈鱼";
                    } else {
                        o.amount = "5600";
                    }

                    rankList.add(o);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFinish(rankList);
                        }
                    }
                }, 1500);
            }
        }).start();
    }

    private void loadMoreLocalRanks(final int state, final FetchRanksCallback callback, final int page) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Rank o = new Rank();
                User u = new User();
                u.cover = "";
                u.name = "二大爷";
                o.user = u;
                o.type = state;
                if (state == 1) {
                    o.length = "0.56";
                    o.weight = "14";
                    o.fishName = "雕鱼";
                } else {
                    o.amount = "5600";
                }
                rankList.add(o);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFinish(rankList);
                        }
                    }
                }, 1000);
            }
        }).start();
    }

    private void fetchRemoteRanks(final int state, final int page, final FetchRanksCallback callback) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Map<String, Object> params = new HashMap<String, Object>();
//                params.put("page", page);
//                params.put("limit", POST_LIMIT);
//                params.put("sort", "-created_at");
//                params.put("type", Constant.BULLETIN_TYPE);
//                Map<String, String> customFields = new HashMap<String, String>();
//                customFields.put("state", "1");
//                params.put("custom_fields", customFields);
//                params.put("like_user_id", UserManager.getInstance(ct).getCurrentUser().userId);
//
//                try {
//                    anSocial.sendRequest("posts/query.json", AnSocialMethod.GET, params, new IAnSocialCallback() {
//                        @Override
//                        public void onFailure(final JSONObject arg0) {
//                            try {
//                                String message = arg0.getJSONObject("meta").getString("message");
//                                Toast.makeText(ct, message, Toast.LENGTH_SHORT).show();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (callback != null) {
//                                        callback.onFailure(arg0.toString());
//                                    }
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onSuccess(JSONObject arg0) {
//                            try {
//                                totalRankCount = arg0.getJSONObject("meta").getInt("total");
//
//                                final List<Rank> ranks = new ArrayList<Rank>();
//                                JSONArray rankArray = arg0.getJSONObject("response").getJSONArray("posts");
//                                for (int i = 0; i < rankArray.length(); i++) {
//                                    JSONObject rankJson = rankArray.getJSONObject(i);
//                                    Rank rank = new Rank();
//                                    rank.parseJSON(rankJson,
//                                            UserManager.getInstance(ct).getCurrentUser().userId);
//                                    rank.update();
//                                    ranks.add(rank);
//
//                                    if (rankJson.has("like")) {
//                                        rank.deleteAllLikes(UserManager.getInstance(ct).getCurrentUser().userId);
//                                        JSONObject likeJson = rankJson.getJSONObject("like");
//                                        Like like = new Like();
//                                        like.rank = rank.getFromTable(UserManager.getInstance(ct)
//                                                .getCurrentUser().userId);
//                                        like.parseJSON(likeJson, UserManager.getInstance(ct).getCurrentUser()
//                                                .getFromTable(), UserManager.getInstance(ct).getCurrentUser().userId);
//                                        boolean updated = like.update();
//                                        DBug.e("like.update", updated + "?");
//                                    }
//                                }
//
//                                handler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (callback != null) {
//                                            callback.onFinish(ranks);
//                                        }
//                                    }
//                                });
//
//                            } catch (final JSONException e) {
//                                e.printStackTrace();
//                                handler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (callback != null) {
//                                            callback.onFailure(e.getMessage());
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                    });
//                } catch (final ArrownockException e) {
//                    e.printStackTrace();
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (callback != null) {
//                                callback.onFailure(e.getMessage());
//                            }
//                        }
//                    });
//                }
//            }
//        }).start();
    }


    public interface FetchRanksCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<Rank> data);
    }
}
