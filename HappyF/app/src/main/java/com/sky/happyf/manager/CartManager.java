package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.Model.Cart;
import com.sky.happyf.util.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class CartManager extends Observable {
    private ArrayList<Cart> cartList = new ArrayList<Cart>();
    private Handler handler;
    private Context ct;
    private final static int POST_LIMIT = 20;
    private int page = 0;
    private int totalCartCount = 0;

    public CartManager(Context ct) {
        this.ct = ct;
        handler = new Handler();
    }

    public List<Cart> getLocalCarts() {
        List<Cart> CartList = null;
        return CartList;
    }

    public void init(final FetchCartsCallback callback) {
        page = 0;
        cartList = new ArrayList<Cart>();
        if (Constants.IS_DEBUG) {
            getLocalCarts(callback);
            return;
        }
        fetchRemoteCarts(++page, new FetchCartsCallback() {
            @Override
            public void onFailure(String errorMsg) {
                if (callback != null) {
                    callback.onFailure(errorMsg);
                }
            }

            @Override
            public void onFinish(List<Cart> data) {
                cartList.addAll(data);
                if (callback != null) {
                    callback.onFinish(data);
                }
            }
        });
    }

    public void loadMore(final FetchCartsCallback callback) {
        if (Constants.IS_DEBUG) {
            loadMoreLocalCarts(callback, ++page);
            return;
        }
        fetchRemoteCarts(++page, new FetchCartsCallback() {
            @Override
            public void onFailure(String errorMsg) {
                page--;
                if (callback != null) {
                    callback.onFailure(errorMsg);
                }
            }

            @Override
            public void onFinish(List<Cart> data) {
                cartList.addAll(data);
                if (callback != null) {
                    callback.onFinish(cartList);
                }
            }
        });
    }


    private void getLocalCarts(final FetchCartsCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    if (i == 0) {
                        Cart o = new Cart();
//                        o.removeSelected = true;
//                        o.selected = true;
                        o.id = i + "";
                        o.goods_id = i + "";
                        o.title = "title";
                        o.state = 0;
                        o.param = "选定参数";
                        o.price = "100";
                        o.shell = "200";
                        o.type = 1;
                        o.count = 1;
                        o.date = "2019-01-10";
                        cartList.add(o);
                    } else if (i == 1) {
                        Cart o = new Cart();
                        o.id = i + "";
                        o.goods_id = i + "";
                        o.title = "title";
                        o.state = 0;
                        o.param = "选定参数";
                        o.price = "100";
                        o.shell = "200";
                        o.type = 2;
                        o.count = 1;
                        o.date = "2019-01-10";
                        cartList.add(o);
                    } else if (i == 2) {
                        Cart o = new Cart();
                        o.id = i + "";
                        o.goods_id = i + "";
                        o.title = "title";
                        o.state = 1;
                        o.param = "选定参数";
                        o.price = "100";
                        o.shell = "200";
                        o.type = 2;
                        o.count = 1;
                        o.date = "2019-01-10";
                        cartList.add(o);
                    } else {
                        Cart o = new Cart();
                        o.id = i + "";
                        o.goods_id = i + "";
                        o.title = "title";
                        o.state = 1;
                        o.param = "选定参数";
                        o.price = "100";
                        o.shell = "200";
                        o.type = 1;
                        o.count = 1;
                        o.date = "2019-01-10";
                        cartList.add(o);
                    }
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFinish(cartList);
                        }
                    }
                }, 1500);
            }
        }).start();
    }

    private void loadMoreLocalCarts(final FetchCartsCallback callback, final int page) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Cart o = new Cart();
                o.title = "title";
                o.state = 1;
                o.param = "选定参数";
                o.price = "100";
                o.shell = "200";
                o.type = 1;
                o.count = 1;
                o.date = "2019-01-10";
                cartList.add(o);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFinish(cartList);
                        }
                    }
                }, 1000);
            }
        }).start();
    }

    private void fetchRemoteCarts(final int page, final FetchCartsCallback callback) {
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
//                                totalCartCount = arg0.getJSONObject("meta").getInt("total");
//
//                                final List<Cart> carts = new ArrayList<Cart>();
//                                JSONArray cartArray = arg0.getJSONObject("response").getJSONArray("posts");
//                                for (int i = 0; i < cartArray.length(); i++) {
//                                    JSONObject cartJson = cartArray.getJSONObject(i);
//                                    Cart cart = new Cart();
//                                    cart.parseJSON(cartJson,
//                                            UserManager.getInstance(ct).getCurrentUser().userId);
//                                    cart.update();
//                                    carts.add(cart);
//
//                                    if (cartJson.has("like")) {
//                                        cart.deleteAllLikes(UserManager.getInstance(ct).getCurrentUser().userId);
//                                        JSONObject likeJson = cartJson.getJSONObject("like");
//                                        Like like = new Like();
//                                        like.cart = cart.getFromTable(UserManager.getInstance(ct)
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
//                                            callback.onFinish(carts);
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


    public interface FetchCartsCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<Cart> data);
    }
}
