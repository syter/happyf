package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.Model.CartPrice;
import com.sky.happyf.Model.QuickwayType;
import com.sky.happyf.Model.ShopBanner;
import com.sky.happyf.Model.User;
import com.sky.happyf.R;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.NetUtils;
import com.sky.happyf.util.SpfHelper;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

public class UserManager extends Observable {
    private Handler handler;
    private Context ct;

    public UserManager(Context ct) {
        this.ct = ct;
        handler = new Handler();
    }

    public boolean isUserLogin() {
        return SpfHelper.getInstance(ct).hasSignIn();
    }


    public void getMyCartPrice(final FetchCartPriceCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalMyCartPrice(callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("user_id", SpfHelper.getInstance(ct).getMyUserInfo().id);
        NetUtils.get(ct, params, Constants.PATH_GET_USER_CART, new NetUtils.NetCallback() {
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
                    String price = data.optString("price");
                    String shellPrice = data.optString("shellPrice");
                    final CartPrice cp = new CartPrice();
                    cp.price = price;
                    cp.shellPrice = shellPrice;
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(cp);
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

    public void getLocalMyCartPrice(final FetchCartPriceCallback callback) {
        if (callback != null) {
            callback.onFinish(new CartPrice());
        }
    }


    public void getCode(final String phone, final FetchCommonCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalCode(phone, callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("phone", phone);
        params.put("type", "login");
        NetUtils.get(ct, params, Constants.PATH_GET_LOGIN_CODE, new NetUtils.NetCallback() {
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
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish("");
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

    public void getLocalCode(final String phone, final FetchCommonCallback callback) {
        if (callback != null) {
            callback.onFinish("");
        }
    }

    public void login(final String phone, final String code, final FetchUserCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalLogin(phone, code, callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("phone", phone);
        params.put("valid_code", code);
        NetUtils.get(ct, params, Constants.PATH_LOGIN, new NetUtils.NetCallback() {
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
            public void onFinish(final JSONObject data) {
                try {
                    final User user = new User();
                    JSONObject obj = data.getJSONObject("user");
                    user.id = obj.optString("id");
                    user.cover = obj.optString("cover");
                    user.phone = phone;
                    user.name = obj.optString("name");
                    user.level = obj.optString("level");
                    user.shellCount = obj.optString("shellCount");
                    user.exp = obj.optString("exp");
                    user.maxExp = obj.optString("maxExp");

                    SpfHelper.getInstance(ct).saveMyUserInfo(user);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(user);
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

    public void getLocalLogin(final String phone, final String code, final FetchUserCallback callback) {
        if (callback != null) {
            callback.onFinish(new User());
        }
    }

    public void loginByPwd(final String phone, final String password, final FetchUserCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalLoginByPwd(phone, password, callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("phone", phone);
        params.put("password", password);
        NetUtils.get(ct, params, Constants.PATH_LOGIN_BY_PWD, new NetUtils.NetCallback() {
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
            public void onFinish(final JSONObject data) {
                try {
                    final User user = new User();
                    JSONObject obj = data.getJSONObject("user");
                    user.id = obj.optString("id");
                    user.cover = obj.optString("cover");
                    user.phone = phone;
                    user.name = obj.optString("name");
                    user.level = obj.optString("level");
                    user.shellCount = obj.optString("shellCount");
                    user.exp = obj.optString("exp");
                    user.maxExp = obj.optString("maxExp");

                    SpfHelper.getInstance(ct).saveMyUserInfo(user);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(user);
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

    public void getLocalLoginByPwd(final String phone, final String password, final FetchUserCallback callback) {
        if (callback != null) {
            callback.onFinish(new User());
        }
    }

    public String validLoginParams(String phone, String code) {
        if (phone.length() == 0) {
            return ct.getResources().getString(R.string.login_phone_empty);
        }
        if (phone.length() != 11) {
            return ct.getResources().getString(R.string.login_phone_error);
        }
        if (code.length() == 0) {
            return ct.getResources().getString(R.string.login_code_empty);
        }
        if (code.length() != 6) {
            return ct.getResources().getString(R.string.login_code_error);
        }
        return null;
    }

    public String validLoginByPwdParams(String phone, String password) {
        if (phone.length() == 0) {
            return ct.getResources().getString(R.string.login_phone_empty);
        }
        if (phone.length() != 11) {
            return ct.getResources().getString(R.string.login_phone_error);
        }
        if (password.length() == 0) {
            return ct.getResources().getString(R.string.login_pwd_empty);
        }
        if (password.length() < 6) {
            return ct.getResources().getString(R.string.login_pwd_error);
        }
        return null;
    }


    public interface FetchCartPriceCallback {
        public void onFailure(String errorMsg);

        public void onFinish(CartPrice cp);
    }

    public interface FetchCommonCallback {
        public void onFailure(String errorMsg);

        public void onFinish(String text);
    }

    public interface FetchUserCallback {
        public void onFailure(String errorMsg);

        public void onFinish(User user);
    }
}
