package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.Model.Happy;
import com.sky.happyf.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

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

    public List<Happy> getLocalHappys() {
        List<Happy> HappyList = null;
        return HappyList;
    }

    public void init(final int state, final FetchHappysCallback callback) {
        page = 0;
        happyList = new ArrayList<Happy>();
        if (Constants.IS_DEBUG) {
            getLocalHappys(state, callback);
            return;
        }
        fetchRemoteHappys(state, ++page, new FetchHappysCallback() {
            @Override
            public void onFailure(String errorMsg) {
                if (callback != null) {
                    callback.onFailure(errorMsg);
                }
            }

            @Override
            public void onFinish(List<Happy> data) {
                happyList.addAll(data);
                if (callback != null) {
                    callback.onFinish(data);
                }
            }
        });
    }

    public void loadMore(final int state, final FetchHappysCallback callback) {
        if (Constants.IS_DEBUG) {
            loadMoreLocalHappys(state, callback, ++page);
            return;
        }
        fetchRemoteHappys(state, ++page, new FetchHappysCallback() {
            @Override
            public void onFailure(String errorMsg) {
                page--;
                if (callback != null) {
                    callback.onFailure(errorMsg);
                }
            }

            @Override
            public void onFinish(List<Happy> data) {
                happyList.addAll(data);
                if (callback != null) {
                    callback.onFinish(happyList);
                }
            }
        });
    }


    private void getLocalHappys(final int state, final FetchHappysCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 17; i++) {
                    Happy o = new Happy();
                    o.address = "目标地址";
                    o.state = 1;
                    o.fish = "目标鱼";
                    o.count = 6;
                    o.date = "2019-01-10";
                    happyList.add(o);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFinish(happyList);
                        }
                    }
                }, 1500);
            }
        }).start();
    }

    private void loadMoreLocalHappys(final int state, final FetchHappysCallback callback, final int page) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Happy o = new Happy();
                o.address = "目标地址";
                o.state = 1;
                o.fish = "目标鱼";
                o.count = 6;
                o.date = "2019-01-10";
                happyList.add(o);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFinish(happyList);
                        }
                    }
                }, 1000);
            }
        }).start();
    }

    private void fetchRemoteHappys(final int state, final int page, final FetchHappysCallback callback) {
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
//                                totalHappyCount = arg0.getJSONObject("meta").getInt("total");
//
//                                final List<Happy> happys = new ArrayList<Happy>();
//                                JSONArray happyArray = arg0.getJSONObject("response").getJSONArray("posts");
//                                for (int i = 0; i < happyArray.length(); i++) {
//                                    JSONObject happyJson = happyArray.getJSONObject(i);
//                                    Happy happy = new Happy();
//                                    happy.parseJSON(happyJson,
//                                            UserManager.getInstance(ct).getCurrentUser().userId);
//                                    happy.update();
//                                    happys.add(happy);
//
//                                    if (happyJson.has("like")) {
//                                        happy.deleteAllLikes(UserManager.getInstance(ct).getCurrentUser().userId);
//                                        JSONObject likeJson = happyJson.getJSONObject("like");
//                                        Like like = new Like();
//                                        like.happy = happy.getFromTable(UserManager.getInstance(ct)
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
//                                            callback.onFinish(happys);
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


    public interface FetchHappysCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<Happy> data);
    }
}
