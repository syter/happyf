package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.Model.Address;
import com.sky.happyf.Model.Article;
import com.sky.happyf.Model.Goods;
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

public class ArticleManager extends Observable {
    private ArrayList<Article> articleList = new ArrayList<Article>();
    private Handler handler;
    private Context ct;
    private final static int POST_LIMIT = 20;
    private int page = 0;
    private int totalArticleCount = 0;
    private int articlesPage = 1;

    public ArticleManager(Context ct) {
        this.ct = ct;
        handler = new Handler();
    }

    public List<Article> getLocalArticles() {
        List<Article> ArticleList = null;
        return ArticleList;
    }

    public void getArticleList(final String type, final FetchArticleCallback callback) {
        articlesPage = 1;
        if (Constants.IS_DEBUG) {
            getLocalArticleList(type, callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("cate_id", type);
        params.put("limit", "10");
        params.put("page", "1");


        NetUtils.get(ct, params, Constants.PATH_GET_ARTICLE, new NetUtils.NetCallback() {
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
                    final List<Article> articleList = new ArrayList<>();

                    JSONArray listArray = data.getJSONArray("list");
                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject obj = listArray.getJSONObject(i);
                        Article article = new Article();
                        article.id = obj.optString("id");
                        article.categoryId = obj.optString("cate_id");
                        article.title = obj.optString("title");
                        article.cover = obj.optString("cover");
                        article.desc = obj.optString("deacript");
                        String isTopStr = obj.optString("isTOP", "N");
                        if ("N".equals(isTopStr)) {
                            article.isTop = false;
                        } else {
                            article.isTop = true;
                        }
                        article.readCount = Integer.parseInt(obj.optString("hit", "0"));
                        article.date = obj.optString("createtime");
                        articleList.add(article);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(articleList);
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

    private void getLocalArticleList(final String type, final FetchArticleCallback callback) {
        if (callback != null) {
            callback.onFinish(new ArrayList<Article>());
        }
    }

    public void loadMore(final String type, final FetchArticleCallback callback) {
        articlesPage++;
        if (Constants.IS_DEBUG) {
            loadMoreLocalArticles(type, callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("limit", "10");
        params.put("page", articlesPage + "");
        params.put("type", type);
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
                    final List<Article> articleList = new ArrayList<>();

                    JSONArray listArray = data.getJSONArray("list");
                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject obj = listArray.getJSONObject(i);
                        Article article = new Article();
                        article.id = obj.optString("id");
                        article.categoryId = obj.optString("cate_id");
                        article.title = obj.optString("title");
                        article.cover = obj.optString("cover");
                        article.desc = obj.optString("deacript");
                        String isTopStr = obj.optString("isTOP", "N");
                        if ("N".equals(isTopStr)) {
                            article.isTop = false;
                        } else {
                            article.isTop = true;
                        }
                        article.readCount = Integer.parseInt(obj.optString("hit", "0"));
                        article.date = obj.optString("createtime");
                        articleList.add(article);
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(articleList);
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

    private void loadMoreLocalArticles(final String type, final FetchArticleCallback callback) {
        if (callback != null) {
            callback.onFinish(new ArrayList<Article>());
        }
    }


    public void getCategory(final FetchCategoryCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalCategory(callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();

        NetUtils.get(ct, params, Constants.PATH_GET_CATEGORY, new NetUtils.NetCallback() {
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
                    final List<String> idList = new ArrayList<>();

                    JSONArray listArray = data.getJSONArray("list");
                    for (int i = 0; i < listArray.length(); i++) {
                        JSONObject obj = listArray.getJSONObject(i);
                        idList.add(obj.optString("id"));
                    }

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(idList);
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


    private void getLocalCategory(final FetchCategoryCallback callback) {
        if (callback != null) {
            callback.onFinish(new ArrayList<String>());
        }
    }


    public void getArticleDetail(final String id, final FetchArticleCallback callback) {
        if (Constants.IS_DEBUG) {
            getLocalArticleDetail(id, callback);
            return;
        }
        Map<String, String> params = new TreeMap<String, String>();
        params.put("info_id", id);

        NetUtils.get(ct, params, Constants.PATH_GET_ARTICLE_DETAIL, new NetUtils.NetCallback() {
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
                    final List<Article> articleList = new ArrayList<>();

                    Article article = new Article();
                    article.id = data.optString("id");
                    article.categoryId = data.optString("cate_id");
                    article.title = data.optString("title");
                    article.cover = data.optString("cover");
                    article.desc = data.optString("deacript");
                    String isTopStr = data.optString("isTOP", "N");
                    if ("N".equals(isTopStr)) {
                        article.isTop = false;
                    } else {
                        article.isTop = true;
                    }
                    article.readCount = Integer.parseInt(data.optString("hit", "0"));
                    article.date = data.optString("createtime");
                    article.contentUrl = data.optString("content_urlpath");

                    List<String> contentList = new ArrayList<>();
                    String content = data.optString("content");
                    String[] contents = content.split(Constants.CONTENT_SPLIT);
                    for (int i = 0; i < contents.length; i++) {
                        String str = contents[i];
                        contentList.add(str);
//                        if (str.indexOf(Constants.CONTENT_TEXT) != -1) {
//                            contentList.add(str.replaceAll(Constants.CONTENT_TEXT, ""));
//                        } else if (str.indexOf(Constants.CONTENT_IMAGE) != -1) {
//                            str = str.replaceAll(Constants.CONTENT_IMAGE, "");
//                            str = contentUrlpath + str;
//                            contentList.add(str);
//                        } else if (str.indexOf(Constants.CONTENT_VIDEO) != -1) {
//                            str = str.replaceAll(Constants.CONTENT_VIDEO, "");
//                            str = contentUrlpath + str;
//                            contentList.add(str);
//                        }
                    }
                    article.contents = contentList;
                    articleList.add(article);

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (callback != null) {
                                callback.onFinish(articleList);
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

    private void getLocalArticleDetail(final String id, final FetchArticleCallback callback) {
        if (callback != null) {
            callback.onFinish(new ArrayList<Article>());
        }
    }


    public interface FetchArticleCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<Article> data);
    }

    public interface FetchCategoryCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<String> data);
    }


}
