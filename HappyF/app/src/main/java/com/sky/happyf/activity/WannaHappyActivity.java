package com.sky.happyf.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.bumptech.glide.Glide;
import com.orhanobut.logger.Logger;
import com.sky.happyf.Model.Happy;
import com.sky.happyf.Model.SelectType;
import com.sky.happyf.R;
import com.sky.happyf.manager.HappyManager;
import com.sky.happyf.util.GlideImageLoader;
import com.sky.happyf.util.Utils;
import com.sky.happyf.view.AutoNextLineLinearlayout;
import com.sky.happyf.view.PayDialog;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class WannaHappyActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private Button btnWanna, btnConfirmSelect;
    private Banner banner;
    private HappyManager happyManager;
    private RelativeLayout rlSelectDialog, rlSelectType, rlSelectDate, rlEmptySelect;
    private TextView tvPrice, tvTitle1, tvTitle2, tvDetail, tvSelected, tvSelectParam, tvDate;
    private AutoNextLineLinearlayout llSelectParam;
    private PayDialog payDialog;
    private LinearLayout lvImages;
    private SelectType currentSelectType;
    private List<Button> allSelectTypeButtons;
    private ImageView ivHappyCover, ivClose;
    private boolean isShowDialog;
    private TimePickerView pvTime;
    private String submitTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wanna_happy);

        getSupportActionBar().hide();//隐藏标题栏

        initView();

        initListener();

        initData();
    }

    private void initView() {
        btnWanna = findViewById(R.id.btn_wanna);
        titleBar = findViewById(R.id.titlebar);

        banner = findViewById(R.id.banner);
        DisplayMetrics dm = getResources().getDisplayMetrics();
        int screenWidth = dm.widthPixels;
        int realBannerHeight = 250 * screenWidth / 375;
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) banner.getLayoutParams();
        params.height = realBannerHeight;
        banner.setLayoutParams(params);

        tvTitle1 = findViewById(R.id.tv_title1);
        tvPrice = findViewById(R.id.tv_price);
        tvTitle2 = findViewById(R.id.tv_title2);
        tvDetail = findViewById(R.id.tv_detail);
        lvImages = findViewById(R.id.lv_images);
        llSelectParam = findViewById(R.id.ll_select_param);
        rlSelectDate = findViewById(R.id.rl_select_date);
        rlSelectType = findViewById(R.id.rl_select_type);
        rlSelectDialog = findViewById(R.id.rl_select_dialog);
        tvSelected = findViewById(R.id.tv_selectd);
        tvSelectParam = findViewById(R.id.tv_select_param);
        ivHappyCover = findViewById(R.id.iv_happy_cover);
        ivClose = findViewById(R.id.iv_close);
        rlEmptySelect = findViewById(R.id.rl_empty_select);
        btnConfirmSelect = findViewById(R.id.btn_confirm_select);
        tvDate = findViewById(R.id.tv_date);
        payDialog = new PayDialog(WannaHappyActivity.this, R.style.PayDialog);

        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                submitTime = getTime(date);
                tvDate.setText(submitTime);
            }
        })
                .setType(new boolean[]{true, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {
            FrameLayout.LayoutParams flParams = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            flParams.leftMargin = 0;
            flParams.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(flParams);

            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.1f);
            }
        }
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
//        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private void initListener() {
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    finish();
                }
            }
        });

        btnWanna.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                payDialog.show();
            }
        });

        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSelectDialog();
            }
        });

        rlEmptySelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSelectDialog();
            }
        });
        rlSelectType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showSelectDialog();
            }
        });
        btnConfirmSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideSelectDialog();
            }
        });

        rlSelectDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pvTime.show(v);
            }
        });


        banner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                Logger.d("点击了banner - " + position);
            }
        });

        payDialog.setAliOnclickListener(new PayDialog.onAliOnclickListener() {
            @Override
            public void onAliClick() {
//                // 1.创建支付宝支付订单的信息
//                String rawAliOrderInfo = new AliPayReq2.AliOrderInfo()
//                        .setPartner(partner) //商户PID || 签约合作者身份ID
//                        .setSeller(seller)  // 商户收款账号 || 签约卖家支付宝账号
//                        .setOutTradeNo(outTradeNo) //设置唯一订单号
//                        .setSubject(orderSubject) //设置订单标题
//                        .setBody(orderBody) //设置订单内容
//                        .setPrice(price) //设置订单价格
//                        .setCallbackUrl(callbackUrl) //设置回调链接
//                        .createOrderInfo(); //创建支付宝支付订单信息
//
//
//                //2.签名  支付宝支付订单的信息 ===>>>  商户私钥签名之后的订单信息
//                //TODO 这里需要从服务器获取用商户私钥签名之后的订单信息
//                String signAliOrderInfo = getSignAliOrderInfoFromServer(rawAliOrderInfo);
//
//                //3.发送支付宝支付请求
//                AliPayReq2 aliPayReq = new AliPayReq2.Builder()
//                        .with(ConfirmOrderActivity.this)//Activity实例
//                        .setRawAliPayOrderInfo(rawAliOrderInfo)//支付宝支付订单信息
//                        .setSignedAliPayOrderInfo(signAliOrderInfo) //设置 商户私钥RSA加密后的支付宝支付订单信息
//                        .create()//
//                        .setOnAliPayListener(null);//
//                PayAPI.getInstance().sendPayRequest(aliPayReq);
//
//                //关于支付宝支付的回调
//                aliPayReq.setOnAliPayListener(new AliPayReq2.OnAliPayListener() {
//                    @Override
//                    public void onPaySuccess(String resultInfo) {
//                        EventBus.getDefault().post(new MessageEvent(Constants.EVENT_MESSAGE_EDIT_USER));
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.pay_succ), Toast.LENGTH_LONG).show();
//                        payDialog.dismiss();
//                    }
//
//                    @Override
//                    public void onPayFailure(String resultInfo) {
//
//                    }
//
//                    @Override
//                    public void onPayConfirmimg(String resultInfo) {
//
//                    }
//
//                    @Override
//                    public void onPayCheck(String status) {
//
//                    }
//                });
            }
        });

        payDialog.setWechatOnclickListener(new PayDialog.onWechatOnclickListener() {
            @Override
            public void onWechatOnclick() {


////                //1.创建微信支付请求
////                WechatPayReq wechatPayReq = new WechatPayReq.Builder()
////                        .with(ConfirmOrderActivity.this) //activity实例
////                        .setAppId(appid) //微信支付AppID
////                        .setPartnerId(partnerid)//微信支付商户号
////                        .setPrepayId(prepayid)//预支付码
//////								.setPackageValue(wechatPayReq.get)//"Sign=WXPay"
////                        .setNonceStr(noncestr)
////                        .setTimeStamp(timestamp)//时间戳
////                        .setSign(sign)//签名
////                        .create();
////                //2.发送微信支付请求
////                PayAPI.getInstance().sendPayRequest(wechatPayReq);
//
//
//                //关于微信支付的回调
//                wechatPayReq.setOnWechatPayListener(new WechatPayReq.OnWechatPayListener() {
//                    @Override
//                    public void onPaySuccess(int errorCode) {
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.pay_succ), Toast.LENGTH_LONG).show();
//                        payDialog.dismiss();
//                        EventBus.getDefault().post(new MessageEvent(Constants.EVENT_MESSAGE_EDIT_USER));
//                        // TODO
//                    }
//
//                    @Override
//                    public void onPayFailure(int errorCode) {
//                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.pay_fail), Toast.LENGTH_LONG).show();
//                        payDialog.dismiss();
//
//                    }
//                });
            }
        });

    }

    private void initData() {
        Intent intent = getIntent();
        String id = intent.getStringExtra("id");

        happyManager = new HappyManager(this);

        happyManager.getHappyInfo(id, new HappyManager.FetchHappyCallback() {
            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(WannaHappyActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(final Happy happy) {
                allSelectTypeButtons = new ArrayList<>();
                titleBar.getCenterTextView().setText(happy.title1);
                banner.setImages(happy.covers).setImageLoader(new GlideImageLoader()).start();
                tvTitle1.setText(happy.title1);
                tvTitle2.setText(happy.title2);
                tvPrice.setText(getResources().getString(R.string.happy_money) + new BigDecimal(happy.price).divide(new BigDecimal("100")).setScale(2).toString());
                tvDetail.setText(happy.title3);
                for (String coverUrl : happy.descCovers) {
                    ImageView ivDescCover = new ImageView(WannaHappyActivity.this);
                    Glide.with(WannaHappyActivity.this).load(coverUrl).into(ivDescCover);
                    lvImages.addView(ivDescCover);
                }

                List<SelectType> stList = happy.selectTypeList;
                for (int i = 0; i < stList.size(); i++) {
                    SelectType st = stList.get(i);

                    Button btnSelectType = new Button(WannaHappyActivity.this);
                    btnSelectType.setText(st.name);
                    btnSelectType.setBackground(getDrawable(R.drawable.shop_unselect_param_button));
                    btnSelectType.setTextSize(14);
                    btnSelectType.setTextColor(getColor(R.color.black));
                    LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
                            Utils.dip2px(WannaHappyActivity.this, 36));
                    params.topMargin = Utils.dip2px(WannaHappyActivity.this, 12);
                    llSelectParam.addView(btnSelectType, params);
                    btnSelectType.setTag(st.id);

                    btnSelectType.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String stId = (String) view.getTag();
                            for (SelectType st : happy.selectTypeList) {
                                if (st.id.equals(stId)) {
                                    currentSelectType = st;
                                    break;
                                }
                            }

                            for (Button btn : allSelectTypeButtons) {
                                btn.setBackground(getDrawable(R.drawable.shop_unselect_param_button));
                                btn.setTextColor(getColor(R.color.black));
                            }
                            view.setBackground(getDrawable(R.drawable.shop_select_param_button));
                            ((Button) view).setTextColor(getColor(R.color.white));

                            processSelectType();
                        }
                    });
                    allSelectTypeButtons.add(btnSelectType);
                }
                if (stList.size() == 1) {
                    currentSelectType = stList.get(0);
                    allSelectTypeButtons.get(0).setBackground(getDrawable(R.drawable.shop_select_param_button));
                    allSelectTypeButtons.get(0).setTextColor(getColor(R.color.white));
                    processSelectType();
                }

                Glide.with(WannaHappyActivity.this).load(happy.covers.get(0)).into(ivHappyCover);
            }
        });


    }

    private void hideSelectDialog() {
        if (!isShowDialog) {
            return;
        }
        isShowDialog = false;
        rlSelectDialog.setVisibility(View.GONE);
        rlSelectDialog.setAnimation(Utils.moveToViewBottom());
    }

    private void showSelectDialog() {
        if (isShowDialog) {
            return;
        }
        isShowDialog = true;
        rlSelectDialog.setVisibility(View.VISIBLE);
        rlSelectDialog.setAnimation(Utils.moveToViewLocation());
    }

    private void processSelectType() {
        tvSelected.setText(currentSelectType.name);
        tvSelectParam.setText(currentSelectType.name);
//        tvSelectParam.setTextColor(getColor(R.color.black));
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
