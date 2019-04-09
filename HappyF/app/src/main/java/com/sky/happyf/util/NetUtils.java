package com.sky.happyf.util;

import android.content.Context;

import com.orhanobut.logger.Logger;
import com.sky.happyf.R;

import org.json.JSONObject;

import java.io.InputStream;
import java.security.PublicKey;
import java.util.Calendar;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class NetUtils {

    public static void post(final Context ct, final Map<String, String> params, final String path, final NetCallback callback) {
        if (!Utils.isNetworkConnected(ct)) {
            if (callback != null) {
                callback.onFailure(ct.getString(R.string.net_error));
                return;
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 在参数里，加上ts
                    params.put("ts", Calendar.getInstance().getTimeInMillis() + "");
                    // 排序参数，然后获得加密后的sign，用来做验证
                    Map<String, String> resultMap = Utils.sortMapByKey(params);
                    StringBuilder sb = new StringBuilder();
                    for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                        sb.append("&" + entry.getKey() + "=" + entry.getValue());
                    }
                    String source = sb.substring(1, sb.length());
                    InputStream inPublic = ct.getResources().getAssets().open("pub_key.pem");
                    PublicKey publicKey = RSAUtils.loadPublicKey(inPublic);
                    // 加密

                    byte[] encryptByte = RSAUtils.encryptByPublic(source.getBytes(), publicKey);
                    String sign = Base64Utils.encode(encryptByte);
                    String realParams = source + "&sign=" + sign;

                    String url = Constants.IS_HTTP ? Constants.HTTP + Constants.HOST + path : Constants.HTTPS + Constants.HOST + path;
                    Logger.d("NetUtils post --- current url = " + url);
                    Logger.d("NetUtils post --- current realParams = " + realParams);
                    String params[] = realParams.split("&");
                    FormBody.Builder builder = new FormBody.Builder();
                    for (int i = 0; i < params.length; i++) {
                        String ps = params[i];
                        String[] psArray = ps.split("=");
                        String key = psArray[0];
                        String value = psArray[1];
                        builder.add(key, value);
                    }

                    OkHttpClient client = new OkHttpClient.Builder()
                            .connectTimeout(30, TimeUnit.SECONDS)//设置连接超时时间
                            .readTimeout(30, TimeUnit.SECONDS)//设置读取超时时间
                            .build();
                    RequestBody body = builder.build();
                    Request request = new Request.Builder()
                            .post(body)
                            .url(url)
                            .build();
                    request.method();

                    Response response = client.newCall(request).execute();
                    String bodyStr = response.body().string();
                    Logger.d("NetUtils post --- current response = " + bodyStr);

                    JSONObject responseObject = new JSONObject(bodyStr);
                    int code = responseObject.getInt("code");
                    if (code == 0) {
                        if (responseObject.has("data")) {
                            if (callback != null) {
                                JSONObject data = responseObject.optJSONObject("data");
                                callback.onFinish(data);
                            }
                        }
                    } else {
                        if (callback != null) {
                            callback.onFailure(responseObject.getString("errmsg"));
                        }
                    }
                } catch (Exception e) {
                    Logger.e("NetUtils post --- e = " + e.toString());
                    if (callback != null) {
                        callback.onFailure(ct.getResources().getString(R.string.common_error));
                    }
                }
            }
        }).start();
    }

    public static void get(final Context ct, final Map<String, String> params, final String path, final NetCallback callback) {
        if (!Utils.isNetworkConnected(ct)) {
            if (callback != null) {
                callback.onFailure(ct.getString(R.string.net_error));
                return;
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 在参数里，加上ts
                    params.put("ts", Calendar.getInstance().getTimeInMillis() + "");
                    // 排序参数，然后获得加密后的sign，用来做验证
                    Map<String, String> resultMap = Utils.sortMapByKey(params);
                    StringBuilder sb = new StringBuilder();
                    for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                        sb.append(entry.getKey() + "=" + entry.getValue() + "&");
                    }
                    String source = sb.substring(0, sb.length() - 1);
                    InputStream inPublic = ct.getResources().getAssets().open("pub_key.pem");
                    PublicKey publicKey = RSAUtils.loadPublicKey(inPublic);
                    // 加密
                    byte[] encryptByte = RSAUtils.encryptByPublic(source.getBytes(), publicKey);
                    String sign = Base64Utils.encode(encryptByte);
                    String realParams = source + "&sign=" + sign;

                    String url = Constants.IS_HTTP ? Constants.HTTP + Constants.HOST + path : Constants.HTTPS + Constants.HOST + path;
                    url += "?" + realParams;
                    Logger.d("NetUtils get --- current url = " + url);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = client.newCall(request).execute();
                    String bodyStr = response.body().string();
                    Logger.d("NetUtils get --- current response = " + bodyStr);

                    JSONObject responseObject = new JSONObject(bodyStr);
                    int code = responseObject.getInt("code");
                    if (code == 0) {
                        if (responseObject.has("data")) {
                            if (callback != null) {
                                JSONObject data = responseObject.optJSONObject("data");
                                callback.onFinish(data);
                            }
                        }
                    } else {
                        if (callback != null) {
                            callback.onFailure(responseObject.getString("errmsg"));
                        }
                    }
                } catch (Exception e) {
                    Logger.e("NetUtils get --- e = " + e.toString());
                    if (callback != null) {
                        callback.onFailure(ct.getResources().getString(R.string.common_error));
                    }
                }
            }
        }).start();
    }

    public static void pureGet(final Context ct, final Map<String, String> params, final String apiUrl, final NetCallback callback) {
        if (!Utils.isNetworkConnected(ct)) {
            if (callback != null) {
                callback.onFailure(ct.getString(R.string.net_error));
                return;
            }
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 排序参数，然后获得加密后的sign，用来做验证
                    Map<String, String> resultMap = Utils.sortMapByKey(params);
                    StringBuilder sb = new StringBuilder();
                    for (Map.Entry<String, String> entry : resultMap.entrySet()) {
                        sb.append(entry.getKey() + "=" + entry.getValue() + "&");
                    }
                    String source = "";
                    if (sb.length() > 0) {
                        source = sb.substring(0, sb.length() - 1);
                    }

                    String url = apiUrl + "?" + source;
                    Logger.d("NetUtils pureGet --- current url = " + url);

                    OkHttpClient client = new OkHttpClient();
                    Request request = new Request.Builder()
                            .url(url)
                            .build();

                    Response response = client.newCall(request).execute();
                    String bodyStr = response.body().string();
                    Logger.d("NetUtils pureGet --- current response = " + bodyStr);

                    JSONObject responseObject = new JSONObject(bodyStr);
                    if (callback != null) {
                        callback.onFinish(responseObject);
                    }

                } catch (Exception e) {
                    Logger.e("NetUtils pureGet --- e = " + e.toString());
                    if (callback != null) {
                        callback.onFailure(ct.getResources().getString(R.string.common_error));
                    }
                }
            }
        }).start();
    }

    public interface NetCallback {
        public void onFailure(String errorMsg);

        public void onFinish(JSONObject data);
    }


}
