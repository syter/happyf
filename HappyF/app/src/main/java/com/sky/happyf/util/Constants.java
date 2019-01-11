package com.sky.happyf.util;


import okhttp3.MediaType;

public class Constants {
    public static final boolean IS_DEBUG = false;
    public static final String EXP_URL = "http://www.ruiqisky.com";
    public static final String BUGLY_APPKEY = "3bf0ab6970";
    public static final String HOST = "dev.ruiqisky.com/shop";
    public static final boolean IS_HTTP = true;





    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");



    public static final String PATH_GET_LOGIN_CODE = "/Api/User/sendMsg";
    public static final String PATH_LOGIN = "/Api/User/register";
    public static final String PATH_LOGIN_BY_PWD = "/Api/User/login";
    public static final String PATH_SEARCH_GOODS = "/Api/User/login";
    public static final String PATH_GET_SHOP_BANNER = "/Api/Shop/getBanners";
    public static final String PATH_GET_SHOP_TYPES = "/Api/Shop/getShopCates";
    public static final String PATH_GET_SHOP_GOODS = "/Api/Shop/getList";
    public static final String PATH_GET_SHOP_QUICKWAYS = "/Api/Shop/getQuickCates";
    public static final String PATH_GET_USER_CART = "/Api/Shop/getCartMoney";


}
