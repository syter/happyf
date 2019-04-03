package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.happyf.Model.User;
import com.sky.happyf.R;
import com.sky.happyf.manager.UserManager;
import com.sky.happyf.message.MessageEvent;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.Utils;
import com.wuhenzhizao.titlebar.statusbar.StatusBarUtils;

import org.greenrobot.eventbus.EventBus;

public class LoginActivity extends AppCompatActivity {
    private EditText etPhone, etCode;
    private Button btnLogin, btnGetCode;
    private boolean isLoginCalled = false;
    private TextView tvChangeType;
    private ImageView ivClose;
    private Handler handler = new Handler();
    private UserManager userManager;
    private boolean isSendedCode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //设置标题栏和状态栏
        getSupportActionBar().hide();
        StatusBarUtils.setStatusBarColor(getWindow(), getColor(R.color.login_bg_end), 0);

        initView();
        initListener();
    }

    private void initView() {
        etPhone = findViewById(R.id.et_phone);
        etCode = findViewById(R.id.et_code);
        btnLogin = findViewById(R.id.btn_login);
        btnGetCode = findViewById(R.id.btn_get_code);
        tvChangeType = findViewById(R.id.tv_change_type);
        ivClose = findViewById(R.id.iv_close);
    }

    private void initListener() {
        userManager = new UserManager(this);
        etPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                String phone = cs.toString();
                if (phone.length() == 11 && !isSendedCode) {
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

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnGetCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 阻止按钮继续使用
                disableGetCodeButton();
                // 开启60秒计时器
                startCountingThread(60);
                // 访问接口，获取验证码
                userManager.getCode(etPhone.getText().toString(), new UserManager.FetchCommonCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinish(String text) {
                        isSendedCode = true;
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_getcode_succ), Toast.LENGTH_LONG).show();

                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    }
                });
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
                String code = etCode.getText().toString();
                String errorMsg = userManager.validLoginParams(phone, code);
                if (!Utils.isEmptyString(errorMsg)) {
                    isLoginCalled = true;
                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    return;
                }

                // 访问接口，调用登录方法
                userManager.login(phone, code, new UserManager.FetchUserCallback() {
                    @Override
                    public void onFailure(final String errorMsg) {
                        isLoginCalled = true;
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinish(final User user) {
                        EventBus.getDefault().post(new MessageEvent(Constants.EVENT_MESSAGE_LOGIN));
                        finish();
                    }
                });
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

    private void startCountingThread(final int seconds) {
        if (seconds == 0) {
            isSendedCode = false;
            if (etPhone.getText().length() == 11) {
                ableGetCodeButton();
            } else {
                btnGetCode.setText(getString(R.string.login_getcode));
                disableGetCodeButton();
            }
            return;
        }
        String getCodeText = getString(R.string.login_getcode) + " (" + seconds + "s)";
        btnGetCode.setText(getCodeText);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                int nextSeconds = seconds - 1;
                startCountingThread(nextSeconds);
            }
        }, 1000);
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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.bottom_out);
    }
}
