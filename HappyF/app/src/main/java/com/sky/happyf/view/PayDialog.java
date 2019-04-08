package com.sky.happyf.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.sky.happyf.R;


public class PayDialog extends Dialog {
    private LinearLayout llPayAli, llPayWechat;
    private ImageView ivClose;


    private onAliOnclickListener aliOnclickListener;//取消按钮被点击了的监听器
    private onWechatOnclickListener wechatOnclickListener;//确定按钮被点击了的监听器
    private onCloseOnclickListener closeOnclickListener;//确定按钮被点击了的监听器

    public PayDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }


    public void setAliOnclickListener(onAliOnclickListener aliOnclickListener) {
        this.aliOnclickListener = aliOnclickListener;
    }


    public void setWechatOnclickListener(onWechatOnclickListener wechatOnclickListener) {
        this.wechatOnclickListener = wechatOnclickListener;
    }

    public void setCloseOnclickListener(onCloseOnclickListener closeOnclickListener) {
        this.closeOnclickListener = closeOnclickListener;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_pay);
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
        llPayAli = findViewById(R.id.ll_pay_ali);
        llPayWechat = findViewById(R.id.ll_pay_wechat);
        ivClose = findViewById(R.id.iv_close);
    }

    private void initData() {

    }

    private void initEvent() {
        llPayAli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (aliOnclickListener != null) {
                    aliOnclickListener.onAliClick();
                }
            }
        });
        llPayWechat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wechatOnclickListener != null) {
                    wechatOnclickListener.onWechatOnclick();
                }
            }
        });
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (closeOnclickListener != null) {
                    closeOnclickListener.onCloseOnclick();
                }
            }
        });
    }

    public interface onAliOnclickListener {
        public void onAliClick();
    }

    public interface onWechatOnclickListener {
        public void onWechatOnclick();
    }

    public interface onCloseOnclickListener {
        public void onCloseOnclick();
    }
}