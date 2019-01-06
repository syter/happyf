package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.Model.Address;
import com.sky.happyf.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class AddressManager extends Observable {
    private ArrayList<Address> addressList = new ArrayList<Address>();
    private Handler handler;
    private Context ct;
    private final static int POST_LIMIT = 20;
    private int page = 0;
    private int totalAddressCount = 0;

    public AddressManager(Context ct) {
        this.ct = ct;
        handler = new Handler();
    }

    public List<Address> getLocalAddresss() {
        List<Address> AddressList = null;
        return AddressList;
    }

    public void init(final FetchAddresssCallback callback) {
        page = 0;
        addressList = new ArrayList<Address>();
        if (Constants.IS_DEBUG) {
            getLocalAddresss(callback);
            return;
        }
        fetchRemoteAddresss(++page, new FetchAddresssCallback() {
            @Override
            public void onFailure(String errorMsg) {
                if (callback != null) {
                    callback.onFailure(errorMsg);
                }
            }

            @Override
            public void onFinish(List<Address> data) {
                addressList.addAll(data);
                if (callback != null) {
                    callback.onFinish(data);
                }
            }
        });
    }

    public void loadMore(final FetchAddresssCallback callback) {
        if (Constants.IS_DEBUG) {
            loadMoreLocalAddresss(callback, ++page);
            return;
        }
        fetchRemoteAddresss(++page, new FetchAddresssCallback() {
            @Override
            public void onFailure(String errorMsg) {
                page--;
                if (callback != null) {
                    callback.onFailure(errorMsg);
                }
            }

            @Override
            public void onFinish(List<Address> data) {
                addressList.addAll(data);
                if (callback != null) {
                    callback.onFinish(addressList);
                }
            }
        });
    }


    private void getLocalAddresss(final FetchAddresssCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 17; i++) {
                    Address o = new Address();
                    o.name = "xxx";
                    o.phone = "18612250174";
                    o.address = "浙江省宁波市xx区xxxx街道xxxx号";
                    addressList.add(o);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFinish(addressList);
                        }
                    }
                }, 1500);
            }
        }).start();
    }

    private void loadMoreLocalAddresss(final FetchAddresssCallback callback, final int page) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Address o = new Address();
                o.name = "xxx";
                o.phone = "18612250174";
                o.address = "浙江省宁波市xx区xxxx街道xxxx号";
                addressList.add(o);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFinish(addressList);
                        }
                    }
                }, 1000);
            }
        }).start();
    }

    private void fetchRemoteAddresss(final int page, final FetchAddresssCallback callback) {
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
//                                totalAddressCount = arg0.getJSONObject("meta").getInt("total");
//
//                                final List<Address> addresss = new ArrayList<Address>();
//                                JSONArray addressArray = arg0.getJSONObject("response").getJSONArray("posts");
//                                for (int i = 0; i < addressArray.length(); i++) {
//                                    JSONObject addressJson = addressArray.getJSONObject(i);
//                                    Address address = new Address();
//                                    address.parseJSON(addressJson,
//                                            UserManager.getInstance(ct).getCurrentUser().userId);
//                                    address.update();
//                                    addresss.add(address);
//
//                                    if (addressJson.has("like")) {
//                                        address.deleteAllLikes(UserManager.getInstance(ct).getCurrentUser().userId);
//                                        JSONObject likeJson = addressJson.getJSONObject("like");
//                                        Like like = new Like();
//                                        like.address = address.getFromTable(UserManager.getInstance(ct)
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
//                                            callback.onFinish(addresss);
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


    public interface FetchAddresssCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<Address> data);
    }
}
