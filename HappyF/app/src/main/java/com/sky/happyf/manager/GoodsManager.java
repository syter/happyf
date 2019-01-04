package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.Model.Goods;
import com.sky.happyf.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class GoodsManager extends Observable {
    private ArrayList<Goods> goodsList = new ArrayList<Goods>();
    private Handler handler;
    private Context ct;
    private final static int POST_LIMIT = 20;
    private int page = 0;
    private int totalGoodsCount = 0;

    public GoodsManager(Context ct) {
        this.ct = ct;
        handler = new Handler();
    }

    public List<Goods> getLocalGoodss() {
        List<Goods> GoodsList = null;
        return GoodsList;
    }

    public void init(final int state, final FetchGoodsCallback callback) {
        page = 0;
        goodsList = new ArrayList<Goods>();
        if (Constants.IS_DEBUG) {
            getLocalGoodss(state, callback);
            return;
        }
        fetchRemoteGoodss(state, ++page, new FetchGoodsCallback() {
            @Override
            public void onFailure(String errorMsg) {
                if (callback != null) {
                    callback.onFailure(errorMsg);
                }
            }

            @Override
            public void onFinish(List<Goods> data) {
                goodsList.addAll(data);
                if (callback != null) {
                    callback.onFinish(data);
                }
            }
        });
    }

    public void loadMore(final int state, final FetchGoodsCallback callback) {
        if (Constants.IS_DEBUG) {
            loadMoreLocalGoodss(state, callback, ++page);
            return;
        }
        fetchRemoteGoodss(state, ++page, new FetchGoodsCallback() {
            @Override
            public void onFailure(String errorMsg) {
                page--;
                if (callback != null) {
                    callback.onFailure(errorMsg);
                }
            }

            @Override
            public void onFinish(List<Goods> data) {
                goodsList.addAll(data);
                if (callback != null) {
                    callback.onFinish(goodsList);
                }
            }
        });
    }


    private void getLocalGoodss(final int state, final FetchGoodsCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 17; i++) {
                    Goods o = new Goods();
                    o.id = i + "goods";
                    o.covers = "xxx";
                    o.title = "title";
                    o.state = 1;
                    o.desc = "desc";
                    List<String> sizes = new ArrayList<String>();
                    sizes.add("红色，超大");
                    sizes.add("蓝色，超大");
                    sizes.add("黑色，超大");
                    o.sizes = sizes;
                    o.content = "商品详情 无啦啦啦啦啦了";
                    o.price = "￥100";
                    o.shellPrice = "￥98";
                    o.backPrice = "￥48";
                    o.sell_count = 56;
                    goodsList.add(o);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFinish(goodsList);
                        }
                    }
                }, 1500);
            }
        }).start();
    }

    private void loadMoreLocalGoodss(final int state, final FetchGoodsCallback callback, final int page) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Goods o = new Goods();
                o.id = page + "";
                o.covers = "xxx";
                o.title = "title";
                o.state = 1;
                o.desc = "desc";
                List<String> sizes = new ArrayList<String>();
                sizes.add("红色，超大");
                sizes.add("蓝色，超大");
                sizes.add("黑色，超大");
                o.sizes = sizes;
                o.content = "商品详情 无啦啦啦啦啦了";
                o.price = "￥100";
                o.shellPrice = "￥98";
                o.backPrice = "￥48";
                o.sell_count = 56;
                goodsList.add(o);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFinish(goodsList);
                        }
                    }
                }, 1000);
            }
        }).start();
    }

    private void fetchRemoteGoodss(final int state, final int page, final FetchGoodsCallback callback) {
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
//                                totalGoodsCount = arg0.getJSONObject("meta").getInt("total");
//
//                                final List<Goods> goodss = new ArrayList<Goods>();
//                                JSONArray goodsArray = arg0.getJSONObject("response").getJSONArray("posts");
//                                for (int i = 0; i < goodsArray.length(); i++) {
//                                    JSONObject goodsJson = goodsArray.getJSONObject(i);
//                                    Goods goods = new Goods();
//                                    goods.parseJSON(goodsJson,
//                                            UserManager.getInstance(ct).getCurrentUser().userId);
//                                    goods.update();
//                                    goodss.add(goods);
//
//                                    if (goodsJson.has("like")) {
//                                        goods.deleteAllLikes(UserManager.getInstance(ct).getCurrentUser().userId);
//                                        JSONObject likeJson = goodsJson.getJSONObject("like");
//                                        Like like = new Like();
//                                        like.goods = goods.getFromTable(UserManager.getInstance(ct)
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
//                                            callback.onFinish(goodss);
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


    public interface FetchGoodsCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<Goods> data);
    }
}
