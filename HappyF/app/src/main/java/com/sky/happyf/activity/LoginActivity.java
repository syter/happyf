package com.sky.happyf.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.orhanobut.logger.Logger;
import com.sky.happyf.R;
import com.sky.happyf.utils.Base64Utils;
import com.sky.happyf.utils.Constants;
import com.sky.happyf.utils.RSAUtils;
import com.sky.happyf.utils.Utils;


import java.io.InputStream;
import java.security.PublicKey;
import java.util.Calendar;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class LoginActivity extends BaseActivity {
    private EditText etPhone, etCode;
    private Button btnLogin, btnGetCode;
    private TextView tvChangeType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().hide();//隐藏标题栏

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.x开始需要把颜色设置透明，否则导航栏会呈现系统默认的浅灰色
                Window window = getWindow();
                View decorView = window.getDecorView();
                //两个 flag 要结合使用，表示让应用的主体内容占用系统状态栏的空间
                int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
                decorView.setSystemUiVisibility(option);
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.TRANSPARENT);
                //导航栏颜色也可以正常设置
//                window.setNavigationBarColor(Color.TRANSPARENT);
            } else {
                Window window = getWindow();
                WindowManager.LayoutParams attributes = window.getAttributes();
                int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                attributes.flags |= flagTranslucentStatus;
//                attributes.flags |= flagTranslucentNavigation;
                window.setAttributes(attributes);
            }
        }


        initView();

        initListener();


    }

    private void initView() {
        etPhone = (EditText) findViewById(R.id.et_phone);
        etCode = (EditText) findViewById(R.id.et_code);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnGetCode = (Button) findViewById(R.id.btn_get_code);
        tvChangeType = (TextView) findViewById(R.id.tv_change_type);
    }

    private void initListener() {
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                String phone = cs.toString();
                if (phone.length() == 11) {
                    ableGetCodeButton();
                } else {
                    disableGetCodeButton();
                }
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            }

            public void afterTextChanged(Editable arg0) {
            }
        });


        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        OkHttpClient client = new OkHttpClient();
//                        Calendar.getInstance().getTimeInMillis();
//                        String source = "phone=18612250715&ts=" + Calendar.getInstance().getTimeInMillis();
//                        try {
//                            InputStream inPublic = getResources().getAssets().open("pub_key.pem");
//                            PublicKey publicKey = RSAUtils.loadPublicKey(inPublic);
//                            // 加密
//                            byte[] encryptByte = RSAUtils.encryptData(source.getBytes(), publicKey);
//                            // 为了方便观察吧加密后的数据用base64加密转一下，要不然看起来是乱码,所以解密是也是要用Base64先转换
//                            String sign = Base64Utils.encode(encryptByte);
//
//                            String params = source + "&sign=" + sign;
//
//                            String url = "http://192.168.0.151/shop/Api/User/sendMsg";
//                            Logger.e(url);
//                            Logger.e(params);
//                            RequestBody body = RequestBody.create(Constants.JSON, "");
//                            Request request = new Request.Builder()
//                                    .url(url)
//                                    .post(body)
//                                    .build();
//
//                            Response response = client.newCall(request).execute();
//                            Logger.e(response.body().string());
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }).start();


                finish();
            }
        });

        tvChangeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(it);
                finish();
            }
        });
    }

    private void ableGetCodeButton() {
        btnGetCode.setBackground(getDrawable(R.drawable.login_button));
        btnGetCode.setTextColor(getColor(R.color.white));
        btnGetCode.setText(getString(R.string.login_getcode));
    }

    private void disableGetCodeButton() {
        btnGetCode.setBackground(getDrawable(R.drawable.login_button_disabled));
        btnGetCode.setTextColor(getColor(R.color.login_button_text_disabled));
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.bottom_out);
    }
}
