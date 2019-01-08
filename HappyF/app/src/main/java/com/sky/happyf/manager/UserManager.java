package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.R;
import com.sky.happyf.utils.Constants;
import com.sky.happyf.utils.NetUtils;

import org.json.JSONObject;

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

    public void getCode(final String phone, final NetUtils.NetCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalCode(phone, callback);
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("phone", phone);
        NetUtils.post(ct.getApplicationContext(), params, Constants.PATH_GET_LOGIN_CODE, callback);
    }

    public void getLocalCode(final String phone, final NetUtils.NetCallback callback) {
        if (callback != null) {
            callback.onFinish(new JSONObject());
        }
    }

    public void login(final String phone, final String code, final NetUtils.NetCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalLogin(phone, code, callback);
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("phone", phone);
        params.put("code", code);
        NetUtils.post(ct.getApplicationContext(), params, Constants.PATH_LOGIN, callback);
    }

    public void getLocalLogin(final String phone, final String code, final NetUtils.NetCallback callback) {
        if (callback != null) {
            callback.onFinish(new JSONObject());
        }
    }

    public String validLoginParams(String phone, String code) {
        if (phone.length() != 11) {
            return ct.getResources().getString(R.string.login_phone_error);
        }
        if (code.length() != 6) {
            return ct.getResources().getString(R.string.login_code_error);
        }
        return null;
    }
}
