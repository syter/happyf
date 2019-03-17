package com.sky.happyf.activity;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.sky.happyf.R;
import com.sky.happyf.manager.UserManager;
import com.sky.happyf.util.Utils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

public class UpdatePwdActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private EditText etPhone, etCode, etPassword;
    private Button btnGetCode;
    private UserManager userManager;
    private Handler handler = new Handler();
    private boolean isLoginCalled = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);

        getSupportActionBar().hide();//隐藏标题栏

        initView();

        initListener();


    }

    private void initView() {
        etPhone = findViewById(R.id.et_phone);
        etCode = findViewById(R.id.et_code);
        btnGetCode = findViewById(R.id.btn_get_code);
        etPassword = findViewById(R.id.et_password);
        titleBar = (CommonTitleBar) findViewById(R.id.titlebar);
    }

    private void initListener() {
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    finish();
                } else if (action == CommonTitleBar.ACTION_RIGHT_TEXT) {
                    if (isLoginCalled) {
                        return;
                    }
                    // 设置按钮不可用
                    isLoginCalled = true;
                    // 校验参数
                    String phone = etPhone.getText().toString();
                    String code = etCode.getText().toString();
                    String errorMsg = userManager.validLoginParams(phone, code);
                    String password = etPassword.getText().toString();
                    if (!Utils.isEmptyString(errorMsg)) {
                        isLoginCalled = true;
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (!Utils.isEmptyString(password)) {
                        isLoginCalled = true;
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_pwd_empty), Toast.LENGTH_LONG).show();
                        return;
                    }

                    // 访问接口，调用登录方法
                    userManager.updatePwd(code, password, new UserManager.FetchCommonCallback() {
                        @Override
                        public void onFailure(final String errorMsg) {
                            isLoginCalled = true;
                            Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFinish(final String text) {
                            finish();
                        }
                    });
                }
            }
        });

        userManager = new UserManager(this);
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
                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.login_getcode_succ), Toast.LENGTH_LONG).show();

                        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    }
                });
            }
        });

    }

    private void startCountingThread(final int seconds) {
        if (seconds == 0) {
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
        overridePendingTransition(R.anim.bottom_silent, R.anim.anim_exit);
    }
}
