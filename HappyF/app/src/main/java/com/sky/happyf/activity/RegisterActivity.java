package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.happyf.Model.User;
import com.sky.happyf.R;
import com.sky.happyf.manager.UserManager;
import com.sky.happyf.util.NetUtils;
import com.sky.happyf.util.Utils;
import com.wuhenzhizao.titlebar.statusbar.StatusBarUtils;

import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private EditText etPhone, etPwd;
    private Button btnLogin;
    private boolean isLoginCalled = false;
    private TextView tvChangeType;
    private ImageView ivClose;
    private UserManager userManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //设置标题栏和状态栏
        getSupportActionBar().hide();
        StatusBarUtils.setStatusBarColor(getWindow(), getColor(R.color.login_bg_end), 0);

        initView();
        initListener();
    }

    private void initView() {
        etPhone = findViewById(R.id.et_phone);
        etPwd = findViewById(R.id.et_pwd);
        btnLogin = findViewById(R.id.btn_login);
        tvChangeType = findViewById(R.id.tv_change_type);
        ivClose = findViewById(R.id.iv_close);
    }

    private void initListener() {
        userManager = new UserManager(this);

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLoginCalled) {
                    return;
                }
                // 设置按钮不可用
                isLoginCalled = true;
                // 校验参数
                String phone = etPhone.getText().toString();
                String password = etPwd.getText().toString();
                String errorMsg = userManager.validLoginByPwdParams(phone, password);
                if (!Utils.isEmptyString(errorMsg)) {
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    return;
                }

                // 访问接口，调用登录方法
                userManager.loginByPwd(phone, password, new UserManager.FetchUserCallback() {
                    @Override
                    public void onFailure(final String errorMsg) {
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinish(User user) {
                        finish();
                    }
                });
            }
        });

        tvChangeType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent it = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(it);
                finish();
            }
        });
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.bottom_out);
    }
}
