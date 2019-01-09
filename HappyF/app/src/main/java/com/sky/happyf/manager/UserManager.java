package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.R;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.NetUtils;

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
        params.put("type", "login");
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
        params.put("valid_code", code);
        NetUtils.post(ct.getApplicationContext(), params, Constants.PATH_LOGIN, callback);
    }

    public void getLocalLogin(final String phone, final String code, final NetUtils.NetCallback callback) {
        if (callback != null) {
            callback.onFinish(new JSONObject());
        }
    }

    public void loginByPwd(final String phone, final String password, final NetUtils.NetCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalLoginByPwd(phone, password, callback);
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("phone", phone);
        params.put("password", password);
        NetUtils.post(ct.getApplicationContext(), params, Constants.PATH_LOGIN_BY_PWD, callback);
    }

    public void getLocalLoginByPwd(final String phone, final String password, final NetUtils.NetCallback callback) {
        if (callback != null) {
            callback.onFinish(new JSONObject());
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
}
