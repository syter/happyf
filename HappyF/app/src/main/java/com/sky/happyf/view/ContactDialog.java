package com.sky.happyf.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sky.happyf.R;


public class ContactDialog extends Dialog {
    private ImageView ivClose;
    private TextView tvPhone, tvWeixin;
    private String phone, weixin;

    public ContactDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_contact);
        //空白处不能取消动画
        setCanceledOnTouchOutside(false);

        //初始化界面控件
        initView();
        //初始化界面数据
        initData();
        //初始化界面控件的事件
        initEvent();
    }

    private void initView() {
        ivClose = findViewById(R.id.iv_close);
        tvPhone = findViewById(R.id.tv_contact_phone);
        tvWeixin = findViewById(R.id.tv_contact_weixin);
    }

    private void initData() {
        tvPhone.setText(phone);
        tvWeixin.setText(weixin);
    }

    private void initEvent() {
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public void setPhoneAndWeixin(String phone, String weixin) {
        this.phone = phone;
        this.weixin = weixin;

    }


}