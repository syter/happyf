package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.Model.Address;
import com.sky.happyf.R;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.NetUtils;
import com.sky.happyf.util.SpfHelper;
import com.sky.happyf.util.Utils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

public class AddressManager extends Observable {
    private ArrayList<Address> addressList = new ArrayList<Address>();
    private Handler handler;
    private Context ct;

    public AddressManager(Context ct) {
        this.ct = ct;
        handler = new Handler();
    }

    public void getDefaultAddress(final FetchAddressCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalDefaultAddress(callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("user_id", SpfHelper.getInstance(ct).getMyUserInfo().id);
        NetUtils.post(ct, params, Constants.PATH_GET_DEFAULT_ADDRESS, new NetUtils.NetCallback() {
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
                    final List<Address> addressList = new ArrayList<>();
                    Address address = new Address();
                    address.id = data.optString("id");
                    address.name = data.optString("name");
                    address.province = data.optString("province");
                    address.city = data.optString("city");
                    address.district = data.optString("district");
                    address.address = data.optString("address");
                    address.phone = data.optString("phone");
                    addressList.add(address);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(addressList);
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


    private void getLocalDefaultAddress(final FetchAddressCallback callback) {
        if (callback != null) {
            callback.onFinish(new ArrayList<Address>());
        }
    }


    public void getAddressList(final FetchAddressCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalAddressList(callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("user_id", SpfHelper.getInstance(ct).getMyUserInfo().id);
        NetUtils.post(ct, params, Constants.PATH_GET_ADDRESS_LIST, new NetUtils.NetCallback() {
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
                    final List<Address> addressList = new ArrayList<>();

                    JSONArray listArray = data.getJSONArray("list");
                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject obj = listArray.getJSONObject(i);
                        Address address = new Address();
                        address.id = obj.optString("id");
                        address.name = obj.optString("name");
                        address.province = obj.optString("province");
                        address.city = obj.optString("city");
                        address.district = obj.optString("district");
                        address.address = obj.optString("address");
                        address.phone = obj.optString("phone");
                        address.isUsed = obj.optBoolean("isUsed");
                        addressList.add(address);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(addressList);
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


    private void getLocalAddressList(final FetchAddressCallback callback) {
        if (callback != null) {
            callback.onFinish(new ArrayList<Address>());
        }
    }


    public void setDefaultAddress(final String addressId, final FetchCommonCallback callback) {
        if (Constants.IS_DEBUG) {
            setLocalDefaultAddress(addressId, callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("user_id", SpfHelper.getInstance(ct).getMyUserInfo().id);
        params.put("address_id", addressId);
        NetUtils.get(ct, params, Constants.PATH_SET_DEFAULT_ADDRESS, new NetUtils.NetCallback() {
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


    private void setLocalDefaultAddress(final String addressId, final FetchCommonCallback callback) {
        if (callback != null) {
            callback.onFinish("");
        }
    }


    public void createNewAddress(final Address address, final FetchCommonCallback callback) {
        if (Constants.IS_DEBUG) {
            createLocalAddress(address, callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("user_id", SpfHelper.getInstance(ct).getMyUserInfo().id);
        params.put("name", address.name);
        params.put("phone", address.phone);
        params.put("province", address.province);
        params.put("city", address.city);
        params.put("district", address.district);
        params.put("address", address.address);

        NetUtils.get(ct, params, Constants.PATH_CREATE_ADDRESS, new NetUtils.NetCallback() {
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


    private void createLocalAddress(final Address address, final FetchCommonCallback callback) {
        if (callback != null) {
            callback.onFinish("");
        }
    }


    public void updateAddress(final Address address, final FetchCommonCallback callback) {
        if (Constants.IS_DEBUG) {
            updateLocalAddress(address, callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("user_id", SpfHelper.getInstance(ct).getMyUserInfo().id);
        params.put("address_id", address.id);
        params.put("name", address.name);
        params.put("phone", address.phone);
        params.put("province", address.province);
        params.put("city", address.city);
        params.put("district", address.district);
        params.put("address", address.address);
        NetUtils.get(ct, params, Constants.PATH_UPDATE_ADDRESS, new NetUtils.NetCallback() {
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


    private void updateLocalAddress(final Address address, final FetchCommonCallback callback) {
        if (callback != null) {
            callback.onFinish("");
        }
    }


    public String validAddressParams(String name, String phone, String province, String city, String district, String address) {
        if (Utils.isEmptyString(name)) {
            return ct.getResources().getString(R.string.address_name_invalid);
        }
        if (phone.length() != 11) {
            return ct.getResources().getString(R.string.address_phone_invalid);
        }
        if (Utils.isEmptyString(province)) {
            return ct.getResources().getString(R.string.address_pcd_invalid);
        }
        if (Utils.isEmptyString(city)) {
            return ct.getResources().getString(R.string.address_pcd_invalid);
        }
        if (Utils.isEmptyString(district)) {
            return ct.getResources().getString(R.string.address_pcd_invalid);
        }
        if (Utils.isEmptyString(address)) {
            return ct.getResources().getString(R.string.address_address_invalid);
        }
        return null;
    }


    public interface FetchAddressCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<Address> data);
    }

    public interface FetchCommonCallback {
        public void onFailure(String errorMsg);

        public void onFinish(String text);
    }
}
