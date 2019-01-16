package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.Model.Address;
import com.sky.happyf.Model.Cart;
import com.sky.happyf.Model.Goods;
import com.sky.happyf.Model.Order;
import com.sky.happyf.R;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.NetUtils;
import com.sky.happyf.util.SpfHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

public class OrderManager extends Observable {
    private ArrayList<Order> orderList = new ArrayList<Order>();
    private Handler handler;
    private Context ct;
    private int orderPage = 0;

    public OrderManager(Context ct) {
        this.ct = ct;
        handler = new Handler();
    }


    public void getOrderList(final FetchOrdersCallback callback) {
        orderPage = 1;
        if (Constants.IS_DEBUG) {
            getLocalOrderList(callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("limit", "10");
        params.put("page", "1");
        params.put("user_id", SpfHelper.getInstance(ct).getMyUserInfo().id);
        params.put("state", "-1");
        NetUtils.post(ct, params, Constants.PATH_GET_ORDER_LIST, new NetUtils.NetCallback() {
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
                    final List<Order> orderList = new ArrayList<>();
                    JSONArray listArray = data.getJSONArray("list");
                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject obj = listArray.getJSONObject(i);
                        Order order = new Order();
                        order.id = obj.getString("id");
                        order.orderNo = obj.getString("order_no");
                        order.status = obj.getString("status");
                        order.leftTime = obj.getString("leftTime");
                        order.postagePrice = obj.getString("postagePrice");
                        order.totalPrice = obj.getString("totalPrice");
                        order.totalShellPrice = obj.getString("totalShellPrice");
                        order.postPrice = obj.getString("postPrice");
                        order.postShellPrice = obj.getString("postShellPrice");
                        order.payType = obj.getString("pay_type");
                        order.postName = obj.getString("post_name");
                        order.postNo = obj.getString("post_no");
                        JSONArray cartArray = obj.getJSONArray("goods");
                        final List<Cart> cartList = new ArrayList<>();
                        for (int j = 0; j < cartArray.length(); j++) {
                            JSONObject cartObj = cartArray.getJSONObject(j);
                            Cart cart = new Cart();
                            cart.cover = cartObj.getString("cover");
                            cart.title = cartObj.getString("title");
                            cart.selectedType = cartObj.getString("selectType");
                            cart.price = cartObj.getString("price");
                            cart.shellPrice = cartObj.getString("shellPrice");
                            cart.count = cartObj.getString("count");
                            cartList.add(cart);
                        }
                        order.cartList = cartList;
                        orderList.add(order);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(orderList);
                            }
                        }
                    });
                } catch (JSONException e) {
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

    public void getLocalOrderList(final FetchOrdersCallback callback) {
        if (callback != null) {
            callback.onFinish(new ArrayList<Order>());
        }
    }


    public void loadMoreOrderList(final FetchOrdersCallback callback) {
        orderPage++;
        if (Constants.IS_DEBUG) {
            loadMoreLocalOrderList(callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("limit", "10");
        params.put("page", orderPage + "");
        params.put("user_id", SpfHelper.getInstance(ct).getMyUserInfo().id);
        params.put("state", "-1");
        NetUtils.post(ct, params, Constants.PATH_GET_ORDER_LIST, new NetUtils.NetCallback() {
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
                    final List<Order> orderList = new ArrayList<>();
                    JSONArray listArray = data.getJSONArray("list");
                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject obj = listArray.getJSONObject(i);
                        Order order = new Order();
                        order.id = obj.getString("id");
                        order.orderNo = obj.getString("order_no");
                        order.status = obj.getString("status");
                        order.leftTime = obj.getString("leftTime");
                        order.postagePrice = obj.getString("postagePrice");
                        order.totalPrice = obj.getString("totalPrice");
                        order.totalShellPrice = obj.getString("totalShellPrice");
                        order.postPrice = obj.getString("postPrice");
                        order.postShellPrice = obj.getString("postShellPrice");
                        order.payType = obj.getString("pay_type");
                        order.postName = obj.getString("post_name");
                        order.postNo = obj.getString("post_no");
                        JSONArray cartArray = obj.getJSONArray("goods");
                        final List<Cart> cartList = new ArrayList<>();
                        for (int j = 0; j < cartArray.length(); j++) {
                            JSONObject cartObj = cartArray.getJSONObject(j);
                            Cart cart = new Cart();
                            cart.cover = cartObj.getString("cover");
                            cart.title = cartObj.getString("title");
                            cart.selectedType = cartObj.getString("selectType");
                            cart.price = cartObj.getString("price");
                            cart.shellPrice = cartObj.getString("shellPrice");
                            cart.count = cartObj.getString("count");
                            cartList.add(cart);
                        }
                        order.cartList = cartList;
                        orderList.add(order);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(orderList);
                            }
                        }
                    });
                } catch (JSONException e) {
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

    public void loadMoreLocalOrderList(final FetchOrdersCallback callback) {
        if (callback != null) {
            callback.onFinish(new ArrayList<Order>());
        }
    }


    public void createOrder(final String cartIds, final String payType, final String remark, final CreateOrderCallback callback) {
        if (Constants.IS_DEBUG) {
            createLocalOrder(cartIds, payType, remark, callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("user_id", SpfHelper.getInstance(ct).getMyUserInfo().id);
        params.put("cart_ids", cartIds);
        params.put("pay_type", payType);
        params.put("remark", remark);
        NetUtils.post(ct, params, Constants.PATH_CREATE_ORDER, new NetUtils.NetCallback() {
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
                    final Map<String, String> resultMap = new HashMap<>();

                    resultMap.put("orderId", data.optString("orderId"));
                    resultMap.put("order_no", data.optString("order_no"));
                    resultMap.put("status", data.optString("status"));
                    resultMap.put("pay_type", data.optString("pay_type"));

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


    private void createLocalOrder(final String cartIds, final String payType, final String remark, final CreateOrderCallback callback) {
        if (callback != null) {
            callback.onFinish(new HashMap<String, String>());
        }
    }

    public void confirmReceiveGoods(final String orderId, final FetchCommonCallback callback) {
        if (Constants.IS_DEBUG) {
            confirmLocalReceiveGoods(orderId, callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("user_id", SpfHelper.getInstance(ct).getMyUserInfo().id);
        params.put("order_id", orderId);
        NetUtils.post(ct, params, Constants.PATH_RECEIVE_GOODS, new NetUtils.NetCallback() {
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


    private void confirmLocalReceiveGoods(final String order, final FetchCommonCallback callback) {
        if (callback != null) {
            callback.onFinish("");
        }
    }


    public void getContactInfo(final CreateOrderCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalContactInfo(callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        NetUtils.get(ct, params, Constants.PATH_GET_CONTACT_INFO, new NetUtils.NetCallback() {
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
                    final Map<String, String> resultMap = new HashMap<>();

                    resultMap.put("phone", data.optString("phone"));
                    resultMap.put("weixin", data.optString("weixin"));

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


    private void getLocalContactInfo(final CreateOrderCallback callback) {
        if (callback != null) {
            callback.onFinish(new HashMap<String, String>());
        }
    }


    public interface FetchOrdersCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<Order> data);
    }

    public interface CreateOrderCallback {
        public void onFailure(String errorMsg);

        public void onFinish(Map<String, String> data);
    }

    public interface FetchCommonCallback {
        public void onFailure(String errorMsg);

        public void onFinish(String text);
    }


}
