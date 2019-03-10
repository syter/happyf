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
    public static final String PATH_EDIT_USER = "/Api/User/setUname";
    public static final String PATH_GET_ORDER_LIST = "/Api/Shop/orderList";
    public static final String PATH_RECEIVE_GOODS = "/Api/Shop/receiveGoods";
    public static final String PATH_GET_CONTACT_INFO = "/Api/Index/getCustomerServer";
    public static final String PATH_GET_MAP_INFO = "/Api/Sea/getAreaList";
    public static final String PATH_GET_HAPPY_INFO = "/Api/Sea/getAreaInfo";
    public static final String PATH_GET_CATEGORY = "/Api/Info/getInfoCates";
    public static final String PATH_GET_ARTICLE = "/Api/Info/getInfoList";
    public static final String PATH_GET_ARTICLE_DETAIL = "/Api/Info/getInfo";
    public static final String PATH_GET_RANKS = "/Api/Info/getInfo";






    public static final String SERVICE_TYPE_ONE = "1"; //
    public static final String SERVICE_TYPE_TWO = "2"; //

    public static final String ORDER_PAY_TYPE_SHELL = "1"; //
    public static final String ORDER_PAY_TYPE_PRICE = "2"; //


    public static final String EVENT_MESSAGE_EDIT_USER = "1";
    public static final String EVENT_MESSAGE_LOGIN = "2";

    public static final String ORDER_STATUS_DAIFUKUAN = "0";
    public static final String ORDER_STATUS_YIFUKUAN = "1";
    public static final String ORDER_STATUS_YIFAHUO = "2";
    public static final String ORDER_STATUS_YISHOUHUO = "3";
    public static final String ORDER_STATUS_YIPINGJIA = "4";
    public static final String ORDER_STATUS_TUIHUOZHONG = "5";
    public static final String ORDER_STATUS_YITUIHUO = "6";
    public static final String ORDER_STATUS_YIQUXIAO = "7";

    public static final String CONTENT_SPLIT = "(!#$%)";
    public static final String CONTENT_TEXT = "$*@*$";
    public static final String CONTENT_IMAGE = "*@$@*";
    public static final String CONTENT_VIDEO = "@$*$@";
}
