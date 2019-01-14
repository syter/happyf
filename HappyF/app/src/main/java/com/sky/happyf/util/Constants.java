package com.sky.happyf.util;


import okhttp3.MediaType;

public class Constants {
    public static final boolean IS_DEBUG = false;
    public static final String EXP_URL = "http://www.ruiqisky.com";
    public static final String BUGLY_APPKEY = "3bf0ab6970";
    public static final String HOST = "dev.ruiqisky.com/shop";
    //  public static final String HOST = "192.168.0.151/shop";
    public static final boolean IS_HTTP = true;


    public static final String HTTP = "http://";
    public static final String HTTPS = "https://";
    public static final MediaType JSON
            = MediaType.get("application/json; charset=utf-8");


    public static final String PATH_GET_LOGIN_CODE = "/Api/User/sendMsg";
    public static final String PATH_LOGIN = "/Api/User/register";
    public static final String PATH_LOGIN_BY_PWD = "/Api/User/login";
    public static final String PATH_SEARCH_GOODS = "/Api/Shop/searchList";
    public static final String PATH_GET_SHOP_BANNER = "/Api/Shop/getBanners";
    public static final String PATH_GET_SHOP_TYPES = "/Api/Shop/getShopCates";
    public static final String PATH_GET_SHOP_GOODS = "/Api/Shop/getList";
    public static final String PATH_GET_SHOP_QUICKWAYS = "/Api/Shop/getQuickCates";
    public static final String PATH_GET_USER_CART = "/Api/Shop/getCartMoney";
    public static final String PATH_GET_GOODS_DETAIL = "/Api/Shop/getInfo";
    public static final String PATH_ADD_TO_CART = "/Api/Shop/addToCart";
    public static final String PATH_GET_CART_LIST = "/Api/Shop/getCartInfo";
    public static final String PATH_UPDATE_CART_COUNT = "/Api/Shop/setCartNum";
    public static final String PATH_REMOVE_CART_GOODS = "/Api/Shop/delCart";
    public static final String PATH_GET_ORDER_CART_LIST = "/Api/Shop/orderInfo";
    public static final String PATH_CREATE_ORDER = "/Api/Shop/createOrder";
    public static final String PATH_GET_DEFAULT_ADDRESS = "/Api/User/getDefaultAddress";
    public static final String PATH_GET_ADDRESS_LIST = "/Api/User/getAddressList";
    public static final String PATH_SET_DEFAULT_ADDRESS = "/Api/User/setDefaultAddress";
    public static final String PATH_CREATE_ADDRESS = "/Api/User/addAddress";
    public static final String PATH_UPDATE_ADDRESS = "/Api/User/editAddress";


    public static final String SERVICE_TYPE_ONE = "1"; //
    public static final String SERVICE_TYPE_TWO = "2"; //

    public static final String ORDER_PAY_TYPE_SHELL = "1"; //
    public static final String ORDER_PAY_TYPE_PRICE = "2"; //
}
