package com.sky.happyf.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.orhanobut.logger.Logger;
import com.sky.happyf.Model.Rank;
import com.sky.happyf.R;

import org.json.JSONObject;

import java.io.InputStream;
import java.security.PublicKey;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

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
                        sb.append(entry.getKey() + "=" + entry.getValue() + "&");
                    }
                    String source = sb.substring(sb.length() - 1);
                    InputStream inPublic = ct.getResources().getAssets().open("pub_key.pem");
                    PublicKey publicKey = RSAUtils.loadPublicKey(inPublic);
                    // 加密
                    byte[] encryptByte = RSAUtils.encryptData(source.getBytes(), publicKey);
                    String sign = Base64Utils.encode(encryptByte);
                    String realParams = source + "&sign=" + sign;

                    String url = Constants.IS_HTTP ? Constants.HTTP + Constants.HOST + path : Constants.HTTPS + Constants.HOST + path;
                    Logger.d("NetUtils post --- current url = " + url);
                    Logger.d("NetUtils post --- current realParams = " + realParams);

                    OkHttpClient client = new OkHttpClient();
                    RequestBody body = RequestBody.create(Constants.JSON, realParams);
                    Request request = new Request.Builder()
                            .url(url)
                            .post(body)
                            .build();
                    Response response = client.newCall(request).execute();
                    Logger.d("NetUtils post --- current response = " + response.body().string());

                    JSONObject responseObject = new JSONObject(response.body().string());
                    int code = responseObject.getInt("code");
                    if (code == 0) {
                        if (responseObject.has("data")) {
                            if (callback != null) {
                                JSONObject data = responseObject.getJSONObject("data");
                                callback.onFinish(data);
                            }
                        }
                    } else {
                        if (callback != null) {
                            callback.onFailure(responseObject.getString("errorMsg"));
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public interface NetCallback {
        public void onFailure(String errorMsg);
        public void onFinish(JSONObject data);
    }


}
