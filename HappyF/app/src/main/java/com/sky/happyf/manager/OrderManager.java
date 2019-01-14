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









    public interface FetchOrdersCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<Order> data);
    }
    public interface CreateOrderCallback {
        public void onFailure(String errorMsg);

        public void onFinish(Map<String, String> data);
    }



}
