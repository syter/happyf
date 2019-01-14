package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.Model.Address;
import com.sky.happyf.Model.Cart;
import com.sky.happyf.R;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.NetUtils;
import com.sky.happyf.util.SpfHelper;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

public class CartManager extends Observable {
    private Handler handler;
    private Context ct;

    public CartManager(Context ct) {
        this.ct = ct;
        handler = new Handler();
    }

    public void getCartList(final FetchCartsCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalCartList(callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("user_id", SpfHelper.getInstance(ct).getMyUserInfo().id);
        NetUtils.get(ct, params, Constants.PATH_GET_CART_LIST, new NetUtils.NetCallback() {
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
                    final Map<String, Object> resultMap = new HashMap<>();
                    Address address = null;
                    JSONObject addressObj = data.optJSONObject("address");
                    if (addressObj != null) {
                        address = new Address();
                        address.id = addressObj.optString("id");
                        address.name = addressObj.optString("name");
                        address.province = addressObj.optString("province");
                        address.city = addressObj.optString("city");
                        address.district = addressObj.optString("district");
                        address.address = addressObj.optString("address");
                        address.phone = addressObj.optString("phone");
                    }

                    List<Cart> cartList = new ArrayList<>();
                    JSONArray listArray = data.optJSONArray("cart");
                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject obj = listArray.optJSONObject(i);
                        Cart c = new Cart();
                        c.id = obj.optString("id");
                        c.goodsId = obj.optString("goods_id");
                        c.cover = obj.optString("cover");
                        c.title = obj.optString("title");
                        c.selectedType = obj.optString("selectedType");
                        c.price = obj.optString("price");
                        c.shellPrice = obj.optString("shellPrice");
                        c.count = obj.optString("count");
                        c.isSeaFood = obj.optBoolean("isSeaFood");
                        c.isFreePost = obj.optBoolean("isFreePost");
                        String status = obj.optString("status");
                        if ("Y".equals(status)) {
                            c.isInvalid = false;
                        } else {
                            c.isInvalid = true;
                        }
                        cartList.add(c);
                    }
                    resultMap.put("cart", cartList);
                    resultMap.put("address", address);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(resultMap);
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


    private void getLocalCartList(final FetchCartsCallback callback) {
        if (callback != null) {
            callback.onFinish(new HashMap<String, Object>());
        }
    }


    public void getOrderCartList(final String cartIds, final FetchCartsCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalOrderCartList(cartIds, callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("user_id", SpfHelper.getInstance(ct).getMyUserInfo().id);
        params.put("cart_ids", cartIds);
        NetUtils.get(ct, params, Constants.PATH_GET_ORDER_CART_LIST, new NetUtils.NetCallback() {
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
                    final Map<String, Object> resultMap = new HashMap<>();
                    Address address = null;
                    JSONObject addressObj = data.optJSONObject("address");
                    if (addressObj != null) {
                        address = new Address();
                        address.id = addressObj.optString("id");
                        address.name = addressObj.optString("name");
                        address.province = addressObj.optString("province");
                        address.city = addressObj.optString("city");
                        address.district = addressObj.optString("district");
                        address.address = addressObj.optString("address");
                        address.phone = addressObj.optString("phone");
                    }

                    List<Cart> cartList = new ArrayList<>();
                    JSONArray listArray = data.optJSONArray("cart");
                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject obj = listArray.optJSONObject(i);
                        Cart c = new Cart();
                        c.id = obj.optString("id");
                        c.goodsId = obj.optString("goods_id");
                        c.cover = obj.optString("cover");
                        c.title = obj.optString("title");
                        c.selectedType = obj.optString("selectedType");
                        c.price = obj.optString("price");
                        c.shellPrice = obj.optString("shellPrice");
                        c.count = obj.optString("count");
                        c.isSeaFood = obj.optBoolean("isSeaFood");
                        c.isFreePost = obj.optBoolean("isFreePost");
                        String status = obj.optString("status");
                        if ("Y".equals(status)) {
                            c.isInvalid = false;
                        } else {
                            c.isInvalid = true;
                        }
                        cartList.add(c);
                    }
                    resultMap.put("cart", cartList);
                    resultMap.put("address", address);

                    resultMap.put("postagePrice", data.optString("postagePrice"));
                    resultMap.put("postPrice", data.optString("postPrice"));
                    resultMap.put("postShellPrice", data.optString("postShellPrice"));
                    resultMap.put("totalPrice", data.optString("totalPrice"));
                    resultMap.put("totalShellPrice", data.optString("totalShellPrice"));
                    resultMap.put("canPayByShell", data.optBoolean("canPayByShell"));
                    resultMap.put("isSameType", data.optBoolean("isSameType"));

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(resultMap);
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


    private void getLocalOrderCartList(final String cartIds, final FetchCartsCallback callback) {
        if (callback != null) {
            callback.onFinish(new HashMap<String, Object>());
        }
    }


    public void updateCount(final String cartId, final String count, final FetchCommonCallback callback) {
        if (Constants.IS_DEBUG) {
            updateLocalCount(cartId, count, callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("cart_id", cartId);
        params.put("user_id", SpfHelper.getInstance(ct).getMyUserInfo().id);
        params.put("count", count);
        NetUtils.post(ct, params, Constants.PATH_UPDATE_CART_COUNT, new NetUtils.NetCallback() {
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
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFinish("");
                        }
                    }
                });
            }
        });
    }


    private void updateLocalCount(final String cartId, final String count, final FetchCommonCallback callback) {
        if (callback != null) {
            callback.onFinish("");
        }
    }


    public void removeCarts(final String cartIds, final FetchCommonCallback callback) {
        if (Constants.IS_DEBUG) {
            removeLocalCarts(cartIds, callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("cart_ids", cartIds);
        params.put("user_id", SpfHelper.getInstance(ct).getMyUserInfo().id);
        NetUtils.post(ct, params, Constants.PATH_REMOVE_CART_GOODS, new NetUtils.NetCallback() {
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
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFinish("");
                        }
                    }
                });
            }
        });
    }


    private void removeLocalCarts(final String cartIds, final FetchCommonCallback callback) {
        if (callback != null) {
            callback.onFinish("");
        }
    }


    public interface FetchCartsCallback {
        public void onFailure(String errorMsg);

        public void onFinish(Map<String, Object> data);
    }

    public interface FetchCommonCallback {
        public void onFailure(String errorMsg);

        public void onFinish(String text);
    }


}
