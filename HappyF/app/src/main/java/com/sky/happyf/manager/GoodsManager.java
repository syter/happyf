package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.Model.Goods;
import com.sky.happyf.Model.QuickwayType;
import com.sky.happyf.Model.ShopBanner;
import com.sky.happyf.Model.SmallType;
import com.sky.happyf.Model.Type;
import com.sky.happyf.R;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.NetUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.TreeMap;

public class GoodsManager extends Observable {
    private ArrayList<Goods> goodsList = new ArrayList<Goods>();
    private Handler handler;
    private Context ct;
    private final static int POST_LIMIT = 20;
    private int goodsPage = 1;
    private int totalGoodsCount = 0;

    public GoodsManager(Context ct) {
        this.ct = ct;
        handler = new Handler();
    }

    public void getBanners(final FetchBannersCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalBanners(callback);
            return;
        }
        NetUtils.get(ct, new TreeMap<String, String>(), Constants.PATH_GET_SHOP_BANNER, new NetUtils.NetCallback() {
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
                    final List<ShopBanner> images = new ArrayList<>();
                    JSONArray listArray = data.getJSONArray("list");
                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject sbObj = listArray.getJSONObject(i);
                        ShopBanner sb = new ShopBanner();
                        sb.id = sbObj.getString("id");
                        sb.link = sbObj.optString("link");
                        sb.url = sbObj.optString("path");
                        images.add(sb);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(images);
                            }
                        }
                    });
                } catch (JSONException e) {
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

    public void getLocalBanners(final FetchBannersCallback callback) {
        List<ShopBanner> images = new ArrayList<>();
        ShopBanner sb = new ShopBanner();
        sb.id = "1";
        sb.link = "";
        sb.url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546591312549&di=5e6a35970d026692b6fd4dbbec89cdf1&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimage%2Fc0%253Dshijue1%252C0%252C0%252C294%252C40%2Fsign%3D285966d73cd3d539d530078052ee8325%2Fb7003af33a87e950d499b5451a385343fbf2b409.jpg";
        images.add(sb);
        ShopBanner sb2 = new ShopBanner();
        sb2.id = "2";
        sb2.link = "";
        sb.url = "https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1546591347518&di=05c38408f670c180b735a7a9d769746b&imgtype=0&src=http%3A%2F%2Fimgsrc.baidu.com%2Fimgad%2Fpic%2Fitem%2F9922720e0cf3d7caceea8850f81fbe096b63a9fc.jpg";
        images.add(sb2);

        if (callback != null) {
            callback.onFinish(images);
        }
    }

    public void getTypes(final FetchTypesCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalTypes(callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("parentId", "0");
        NetUtils.get(ct, params, Constants.PATH_GET_SHOP_TYPES, new NetUtils.NetCallback() {
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
                    final List<Type> types = new ArrayList<>();
                    JSONArray listArray = data.getJSONArray("list");
                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject obj = listArray.getJSONObject(i);
                        Type t = new Type();
                        t.id = obj.getString("id");
                        t.name = obj.optString("name");
                        t.cover = obj.optString("cover");
                        JSONArray smallTypeArray = obj.optJSONArray("children");
                        List<SmallType> smallTypeList = new ArrayList<>();
                        for (int j = 0; j < smallTypeArray.length(); j++) {
                            JSONObject smallObj = smallTypeArray.getJSONObject(j);
                            SmallType st = new SmallType();
                            st.id = smallObj.getString("id");
                            st.name = smallObj.optString("name");
                            st.cover = smallObj.optString("cover");
                            if (i == 0) {
                                st.isSelected = true;
                            }
                            smallTypeList.add(st);
                        }
                        t.smallTypeList = smallTypeList;
                        types.add(t);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(types);
                            }
                        }
                    });
                } catch (JSONException e) {
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

    public void getLocalTypes(final FetchTypesCallback callback) {
        // TODO local data

        if (callback != null) {
            callback.onFinish(new ArrayList<Type>());
        }
    }

    public void getGoodsList(final String smallTypeId, final FetchGoodsCallback callback) {
        goodsPage = 1;
        if (Constants.IS_DEBUG) {
            getLocalGoodss(smallTypeId, callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("limit", "10");
        params.put("page", "1");
        params.put("type", smallTypeId);
        NetUtils.get(ct, params, Constants.PATH_GET_SHOP_GOODS, new NetUtils.NetCallback() {
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
                    final List<Goods> goodsList = new ArrayList<>();
                    JSONArray listArray = data.getJSONArray("list");
                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject obj = listArray.getJSONObject(i);
                        Goods g = new Goods();
                        g.id = obj.getString("id");
                        g.title1 = obj.optString("title1");
                        g.title2 = obj.optString("title2");
                        g.title3 = obj.optString("title3");
                        g.cover = obj.optString("cover");
                        g.sellCount = obj.optString("sellCount");
                        g.price = obj.optString("price");
                        g.shellPrice = obj.optString("shellPrice");
                        g.hasMorePrice = obj.optBoolean("hasMorePrice");

                        goodsList.add(g);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(goodsList);
                            }
                        }
                    });
                } catch (JSONException e) {
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

    public void getLocalGoodss(final String smallTypeId, final FetchGoodsCallback callback) {
        // TODO local data

        if (callback != null) {
            callback.onFinish(new ArrayList<Goods>());
        }
    }


    public void loadMoreGoods(final String smallTypeId, final FetchGoodsCallback callback) {
        goodsPage++;
        if (Constants.IS_DEBUG) {
            loadMoreLocalGoodss(smallTypeId, callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("limit", "10");
        params.put("page", goodsPage + "");
        params.put("type", smallTypeId);
        NetUtils.get(ct, params, Constants.PATH_GET_SHOP_GOODS, new NetUtils.NetCallback() {
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
                    final List<Goods> goodsList = new ArrayList<>();
                    JSONArray listArray = data.getJSONArray("list");
                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject obj = listArray.getJSONObject(i);
                        Goods g = new Goods();
                        g.id = obj.getString("id");
                        g.title1 = obj.optString("title1");
                        g.title2 = obj.optString("title2");
                        g.title3 = obj.optString("title3");
                        g.cover = obj.optString("cover");
                        g.sellCount = obj.optString("sellCount");
                        g.price = obj.optString("price");
                        g.shellPrice = obj.optString("shellPrice");
                        g.hasMorePrice = obj.optBoolean("hasMorePrice");

                        goodsList.add(g);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(goodsList);
                            }
                        }
                    });
                } catch (JSONException e) {
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

    private void loadMoreLocalGoodss(final String smallTypeId, final FetchGoodsCallback callback) {
        // TODO local data

        if (callback != null) {
            callback.onFinish(new ArrayList<Goods>());
        }
    }


    public void getQuickways(final FetchQuickwayTypesCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalTypes(callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        NetUtils.get(ct, params, Constants.PATH_GET_SHOP_QUICKWAYS, new NetUtils.NetCallback() {
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
                    final List<QuickwayType> types = new ArrayList<>();
                    JSONArray listArray = data.getJSONArray("list");
                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject obj = listArray.getJSONObject(i);
                        QuickwayType t = new QuickwayType();
                        t.id = obj.getString("id");
                        t.name = obj.optString("name");
                        t.cover = obj.optString("cover");
                        t.lv = obj.optString("lv");
                        types.add(t);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(types);
                            }
                        }
                    });
                } catch (JSONException e) {
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

    public void getLocalTypes(final FetchQuickwayTypesCallback callback) {
        // TODO local data

        if (callback != null) {
            callback.onFinish(new ArrayList<QuickwayType>());
        }
    }


    public void searchGoods(final String content, final NetUtils.NetCallback callback) {
        if (Constants.IS_DEBUG) {
            searchLocalGoods(content, callback);
            return;
        }
        Map<String, String> params = new TreeMap<>();
        params.put("content", content);
        NetUtils.post(ct.getApplicationContext(), params, Constants.PATH_SEARCH_GOODS, callback);
    }

    public void searchLocalGoods(final String content, final NetUtils.NetCallback callback) {
        if (callback != null) {
            callback.onFinish(new JSONObject());
        }
    }


    public interface FetchGoodsCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<Goods> data);
    }

    public interface FetchBannersCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<ShopBanner> data);
    }

    public interface FetchTypesCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<Type> data);
    }

    public interface FetchQuickwayTypesCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<QuickwayType> data);
    }


}
