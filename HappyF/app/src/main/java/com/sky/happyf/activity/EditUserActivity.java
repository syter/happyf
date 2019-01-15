package com.sky.happyf.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.sky.happyf.R;
import com.sky.happyf.manager.UserManager;
import com.sky.happyf.message.MessageEvent;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.SpfHelper;
import com.sky.happyf.util.Utils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import org.greenrobot.eventbus.EventBus;

public class EditUserActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private EditText etName;
    private UserManager userManager;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user);

        getSupportActionBar().hide();//隐藏标题栏

        initView();

        initListener();

        initData();
    }

    private void initView() {
        etName = findViewById(R.id.et_name);
        titleBar = findViewById(R.id.titlebar);

    }

    private void initListener() {
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    finish();
                } else if (action == CommonTitleBar.ACTION_RIGHT_TEXT) {
                    String name = etName.getText().toString();
                    if (Utils.isEmptyString(name)) {
                        Toast.makeText(EditUserActivity.this, getResources().getString(R.string.name_error), Toast.LENGTH_LONG).show();
                        return;
                    }
                    userManager.editUserInfo(name, new UserManager.FetchCommonCallback() {
                        @Override
                        public void onFailure(String errorMsg) {
                            Toast.makeText(EditUserActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onFinish(String text) {
                            EventBus.getDefault().post(new MessageEvent(Constants.EVENT_MESSAGE_EDIT_USER));
                            finish();
                        }
                    });

                }
            }
        });
    }

    private void initData() {
        userManager = new UserManager(this);

        etName.setText(SpfHelper.getInstance(this).getMyUserInfo().name);


    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.anim_exit);
    }
}
