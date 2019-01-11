package com.sky.happyf.util;

import android.content.Context;
import android.content.SharedPreferences;

import com.sky.happyf.Model.User;
import com.sky.happyf.R;

public class SpfHelper {
    private static SpfHelper sSpfHelper;
    public SharedPreferences Account;
    public SharedPreferences.Editor editor;

    private static final String KEY_USER_USERID = "userid";
    private static final String KEY_USER_PHONE = "phone";
    private static final String KEY_USER_NAME = "name";
    private static final String KEY_USER_COVER = "cover";
    private static final String KEY_USER_SHELLCOUNT = "shellcount";
    private static final String KEY_USER_EXP = "exp";
    private static final String KEY_USER_MAXEXP = "maxexp";
    private static final String KEY_USER_LEVEL = "level";

    private SpfHelper(Context ct) {
        Account = ct.getSharedPreferences(ct.getString(R.string.app_name), 0);
        editor = Account.edit();
    }

    public static SpfHelper getInstance(Context ct) {
        if (sSpfHelper == null) {
            sSpfHelper = new SpfHelper(ct);
        }
        return sSpfHelper;
    }

    public void clearUserInfo() {
        editor.putString(KEY_USER_USERID, "").commit();
        editor.putString(KEY_USER_PHONE, "").commit();
    }

    public void saveMyUserInfo(String id, String phone, String name, String cover, String shellCount, String exp, String maxExp, String level) {
        editor.putString(KEY_USER_USERID, id).commit();
        editor.putString(KEY_USER_PHONE, phone).commit();
        editor.putString(KEY_USER_NAME, name).commit();
        editor.putString(KEY_USER_COVER, cover).commit();
        editor.putString(KEY_USER_SHELLCOUNT, shellCount).commit();
        editor.putString(KEY_USER_EXP, exp).commit();
        editor.putString(KEY_USER_MAXEXP, maxExp).commit();
        editor.putString(KEY_USER_LEVEL, level).commit();
    }

    public void saveMyUserInfo(User user) {
        editor.putString(KEY_USER_USERID, user.id).commit();
        editor.putString(KEY_USER_PHONE, user.phone).commit();
        editor.putString(KEY_USER_NAME, user.name).commit();
        editor.putString(KEY_USER_COVER, user.cover).commit();
        editor.putString(KEY_USER_SHELLCOUNT, user.shellCount).commit();
        editor.putString(KEY_USER_EXP, user.exp).commit();
        editor.putString(KEY_USER_MAXEXP, user.maxExp).commit();
        editor.putString(KEY_USER_LEVEL, user.level).commit();
    }

    public User getMyUserInfo() {
        User user = new User();
        user.id = Account.getString(KEY_USER_USERID, "");
        user.phone = Account.getString(KEY_USER_PHONE, "");
        user.name = Account.getString(KEY_USER_NAME, "");
        user.cover = Account.getString(KEY_USER_COVER, "");
        user.shellCount = Account.getString(KEY_USER_SHELLCOUNT, "");
        user.exp = Account.getString(KEY_USER_EXP, "");
        user.maxExp = Account.getString(KEY_USER_MAXEXP, "");
        user.level = Account.getString(KEY_USER_LEVEL, "");

        return user;
    }

    public boolean hasSignIn() {
        return !Utils.isEmptyString(getMyUserInfo().id);
    }

}
