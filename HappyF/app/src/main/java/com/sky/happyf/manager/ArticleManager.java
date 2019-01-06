package com.sky.happyf.manager;

import android.content.Context;
import android.os.Handler;

import com.sky.happyf.Model.Article;
import com.sky.happyf.utils.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class ArticleManager extends Observable {
    private ArrayList<Article> articleList = new ArrayList<Article>();
    private Handler handler;
    private Context ct;
    private final static int POST_LIMIT = 20;
    private int page = 0;
    private int totalArticleCount = 0;

    public ArticleManager(Context ct) {
        this.ct = ct;
        handler = new Handler();
    }

    public List<Article> getLocalArticles() {
        List<Article> ArticleList = null;
        return ArticleList;
    }

    public void init(final int type, final FetchArticleCallback callback) {
        page = 0;
        articleList = new ArrayList<Article>();
        if (Constants.IS_DEBUG) {
            getLocalArticles(type, callback);
            return;
        }
        fetchRemoteArticles(type, ++page, new FetchArticleCallback() {
            @Override
            public void onFailure(String errorMsg) {
                if (callback != null) {
                    callback.onFailure(errorMsg);
                }
            }

            @Override
            public void onFinish(List<Article> data) {
                articleList.addAll(data);
                if (callback != null) {
                    callback.onFinish(data);
                }
            }
        });
    }

    public void loadMore(final int type, final FetchArticleCallback callback) {
        if (Constants.IS_DEBUG) {
            loadMoreLocalArticles(type, callback, ++page);
            return;
        }
        fetchRemoteArticles(type, ++page, new FetchArticleCallback() {
            @Override
            public void onFailure(String errorMsg) {
                page--;
                if (callback != null) {
                    callback.onFailure(errorMsg);
                }
            }

            @Override
            public void onFinish(List<Article> data) {
                articleList.addAll(data);
                if (callback != null) {
                    callback.onFinish(articleList);
                }
            }
        });
    }


    private void getLocalArticles(final int type, final FetchArticleCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 17; i++) {
                    Article o = new Article();
                    o.id = i + "article";
                    o.covers = "xxx";
                    o.title = "title";
                    o.type = 1;
                    o.desc = "desc";
                    o.content = "商品详情 无啦啦啦啦啦了";
                    o.read_count = 56;
                    o.date = "2019-10-10";
                    articleList.add(o);
                }
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFinish(articleList);
                        }
                    }
                }, 1500);
            }
        }).start();
    }

    private void loadMoreLocalArticles(final int type, final FetchArticleCallback callback, final int page) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                Article o = new Article();
                o.id = page + "article";
                o.covers = "xxx";
                o.title = "title";
                o.type = 1;
                o.desc = "desc";
                o.content = "商品详情 无啦啦啦啦啦了";
                o.read_count = 56;
                o.date = "2019-10-10";
                articleList.add(o);
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (callback != null) {
                            callback.onFinish(articleList);
                        }
                    }
                }, 1000);
            }
        }).start();
    }

    private void fetchRemoteArticles(final int type, final int page, final FetchArticleCallback callback) {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                Map<String, Object> params = new HashMap<String, Object>();
//                params.put("page", page);
//                params.put("limit", POST_LIMIT);
//                params.put("sort", "-created_at");
//                params.put("type", Constant.BULLETIN_TYPE);
//                Map<String, String> customFields = new HashMap<String, String>();
//                customFields.put("type", "1");
//                params.put("custom_fields", customFields);
//                params.put("like_user_id", UserManager.getInstance(ct).getCurrentUser().userId);
//
//                try {
//                    anSocial.sendRequest("posts/query.json", AnSocialMethod.GET, params, new IAnSocialCallback() {
//                        @Override
//                        public void onFailure(final JSONObject arg0) {
//                            try {
//                                String message = arg0.getJSONObject("meta").getString("message");
//                                Toast.makeText(ct, message, Toast.LENGTH_SHORT).show();
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//                            handler.post(new Runnable() {
//                                @Override
//                                public void run() {
//                                    if (callback != null) {
//                                        callback.onFailure(arg0.toString());
//                                    }
//                                }
//                            });
//                        }
//
//                        @Override
//                        public void onSuccess(JSONObject arg0) {
//                            try {
//                                totalArticleCount = arg0.getJSONObject("meta").getInt("total");
//
//                                final List<Article> articles = new ArrayList<Article>();
//                                JSONArray articleArray = arg0.getJSONObject("response").getJSONArray("posts");
//                                for (int i = 0; i < articleArray.length(); i++) {
//                                    JSONObject articleJson = articleArray.getJSONObject(i);
//                                    Article article = new Article();
//                                    article.parseJSON(articleJson,
//                                            UserManager.getInstance(ct).getCurrentUser().userId);
//                                    article.update();
//                                    articles.add(article);
//
//                                    if (articleJson.has("like")) {
//                                        article.deleteAllLikes(UserManager.getInstance(ct).getCurrentUser().userId);
//                                        JSONObject likeJson = articleJson.getJSONObject("like");
//                                        Like like = new Like();
//                                        like.article = article.getFromTable(UserManager.getInstance(ct)
//                                                .getCurrentUser().userId);
//                                        like.parseJSON(likeJson, UserManager.getInstance(ct).getCurrentUser()
//                                                .getFromTable(), UserManager.getInstance(ct).getCurrentUser().userId);
//                                        boolean updated = like.update();
//                                        DBug.e("like.update", updated + "?");
//                                    }
//                                }
//
//                                handler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (callback != null) {
//                                            callback.onFinish(articles);
//                                        }
//                                    }
//                                });
//
//                            } catch (final JSONException e) {
//                                e.printStackTrace();
//                                handler.post(new Runnable() {
//                                    @Override
//                                    public void run() {
//                                        if (callback != null) {
//                                            callback.onFailure(e.getMessage());
//                                        }
//                                    }
//                                });
//                            }
//                        }
//                    });
//                } catch (final ArrownockException e) {
//                    e.printStackTrace();
//                    handler.post(new Runnable() {
//                        @Override
//                        public void run() {
//                            if (callback != null) {
//                                callback.onFailure(e.getMessage());
//                            }
//                        }
//                    });
//                }
//            }
//        }).start();
    }


    public interface FetchArticleCallback {
        public void onFailure(String errorMsg);

        public void onFinish(List<Article> data);
    }
}
