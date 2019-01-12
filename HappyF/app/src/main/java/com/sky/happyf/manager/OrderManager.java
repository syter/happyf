package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.Model.Address;
import com.sky.happyf.Model.Cart;
import com.sky.happyf.Model.Order;
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

public class OrderManager extends Observable {
    private ArrayList<Order> orderList = new ArrayList<Order>();
    private Handler handler;
    private Context ct;
    private final static int POST_LIMIT = 20;
    private int page = 0;
    private int totalOrderCount = 0;

    public OrderManager(Context ct) {
        this.ct = ct;
        handler = new Handler();
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
        NetUtils.get(ct, params, Constants.PATH_CREATE_ORDER, new NetUtils.NetCallback() {
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












    public List<Order> getLocalOrders() {
        List<Order> OrderList = null;
        return OrderList;
    }

    public void init(final int state, final FetchOrdersCallback callback) {
        page = 0;
        orderList = new ArrayList<Order>();
        if (Constants.IS_DEBUG) {
            getLocalOrders(state, callback);
            return;
        }
        fetchRemoteOrders(state, ++page, new FetchOrdersCallback() {
            @Override
            public void onFailure(String errorMsg) {
                if (callback != null) {
                    callback.onFailure(errorMsg);
                }
            }

            @Override
            public void onFinish(List<Order> data) {
                orderList.addAll(data);
                if (callback != null) {
                    callback.onFinish(data);
                }
            }
        });
    }

    public void loadMore(final int state, final FetchOrdersCallback callback) {
        if (Constants.IS_DEBUG) {
            loadMoreLocalOrders(state, callback, ++page);
            return;
        }
        fetchRemoteOrders(state, ++page, new FetchOrdersCallback() {
            @Override
            public void onFailure(String errorMsg) {
                page--;
                if (callback != null) {
                    callback.onFailure(errorMsg);
                }
            }

            @Override
            public void onFinish(List<Order> data) {
                orderList.addAll(data);
                if (callback != null) {
                    callback.onFinish(orderList);
                }
            }
        });
    }


    private void getLocalOrders(final int state, final FetchOrdersCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 17; i++) {
                    Order o = new Order();
                    o.orderNo = "  ListView item  -" + i;
                    o.title = "title";
                    o.state = 1;
                    o.desc = "desc";
                    o.size = "size";
                    o.amount = "100";
                    o.count = 1;
                    o.date = "2019-01-10";
                    orderList.add(o);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFinish(orderList);
                        }
                    }
                }, 1500);
            }
        }).start();
    }

    private void loadMoreLocalOrders(final int state, final FetchOrdersCallback callback, final int page) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Order o = new Order();
                o.orderNo = "  ListView item  - more " + page;
                o.title = "title";
                o.state = 1;
                o.desc = "desc";
                o.size = "size";
                o.amount = "100";
                o.count = 1;
                o.date = "2019-01-10";
                orderList.add(o);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFinish(orderList);
                        }
                    }
                }, 1000);
            }
        }).start();
    }

    private void fetchRemoteOrders(final int state, final int page, final FetchOrdersCallback callback) {
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
//                                totalOrderCount = arg0.getJSONObject("meta").getInt("total");
//
//                                final List<Order> orders = new ArrayList<Order>();
//                                JSONArray orderArray = arg0.getJSONObject("response").getJSONArray("posts");
//                                for (int i = 0; i < orderArray.length(); i++) {
//                                    JSONObject orderJson = orderArray.getJSONObject(i);
//                                    Order order = new Order();
//                                    order.parseJSON(orderJson,
//                                            UserManager.getInstance(ct).getCurrentUser().userId);
//                                    order.update();
//                                    orders.add(order);
//
//                                    if (orderJson.has("like")) {
//                                        order.deleteAllLikes(UserManager.getInstance(ct).getCurrentUser().userId);
//                                        JSONObject likeJson = orderJson.getJSONObject("like");
//                                        Like like = new Like();
//                                        like.order = order.getFromTable(UserManager.getInstance(ct)
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
//                                            callback.onFinish(orders);
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


    public interface FetchOrdersCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<Order> data);
    }
    public interface CreateOrderCallback {
        public void onFailure(String errorMsg);

        public void onFinish(Map<String, String> data);
    }



}
