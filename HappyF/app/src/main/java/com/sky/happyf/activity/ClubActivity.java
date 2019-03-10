package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.happyf.Model.Address;
import com.sky.happyf.R;
import com.sky.happyf.manager.AddressManager;
import com.sky.happyf.manager.OrderManager;
import com.sky.happyf.util.Constants;
import com.sky.happyf.util.Utils;
import com.sky.happyf.view.PayDialog;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.util.List;
import java.util.Map;

public class ClubActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private RelativeLayout rlAddress, rlHasAddress;
    private EditText etName, etId, etPhone;
    private Button btnSubmit;
    private TextView tvAddressEmpty, tvAddressPhone, tvAddressDetail, tvAddressName;
    private PayDialog payDialog;
    private AddressManager addressManager;
    private Address currentAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_club);

        getSupportActionBar().hide();//隐藏标题栏

        initView();

        initListener();

        initData();
    }

    private void initView() {
        titleBar = findViewById(R.id.titlebar);
        rlAddress = findViewById(R.id.rl_address);
        rlHasAddress = findViewById(R.id.rl_hasaddress);
        etName = findViewById(R.id.et_name);
        etId = findViewById(R.id.et_id);
        etPhone = findViewById(R.id.et_phone);
        tvAddressEmpty = findViewById(R.id.tv_address_empty);
        tvAddressPhone = findViewById(R.id.tv_address_phone);
        tvAddressDetail = findViewById(R.id.tv_address_detail);
        tvAddressName = findViewById(R.id.tv_address_name);
        btnSubmit = findViewById(R.id.btn_submit);
        payDialog = new PayDialog(ClubActivity.this, R.style.PayDialog);
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
        rlAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ClubActivity.this, SelectAddressActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString();
                String phone = etPhone.getText().toString();
                String id = etId.getText().toString();
                if (Utils.isEmptyString(name)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.club_name_empty), Toast.LENGTH_LONG).show();
                    return;
                }
                if (Utils.isEmptyString(phone)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.club_phone_empty), Toast.LENGTH_LONG).show();
                    return;
                }
                if (Utils.isEmptyString(id)) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.club_id_empty), Toast.LENGTH_LONG).show();
                    return;
                }
                if (currentAddress == null) {
                    Toast.makeText(getApplicationContext(), getResources().getString(R.string.club_address_empty), Toast.LENGTH_LONG).show();
                    return;
                }


                payDialog.show();
//                orderManager.createOrder(cartIds, Constants.ORDER_PAY_TYPE_PRICE, remark, new OrderManager.CreateOrderCallback() {
//                    @Override
//                    public void onFailure(String errorMsg) {
//                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
//                    }
//
//                    @Override
//                    public void onFinish(Map<String, String> data) {
//                        payDialog.show();
//                    }
//                });
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
        addressManager = new AddressManager(ClubActivity.this);
    }


    @Override
    protected void onResume() {
        super.onResume();
        initAddress();

    }

    private void initAddress() {
        addressManager.getDefaultAddress(new AddressManager.FetchAddressCallback() {
            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(List<Address> data) {
                if (!data.isEmpty()) {
                    currentAddress = data.get(0);
                    rlHasAddress.setVisibility(View.VISIBLE);
                    tvAddressEmpty.setVisibility(View.GONE);
                    tvAddressName.setText(getResources().getString(R.string.select_name_hint) + currentAddress.name);
                    tvAddressDetail.setText(getResources().getString(R.string.select_address_hint) +
                            currentAddress.province + currentAddress.city + currentAddress.district + currentAddress.address);
                    tvAddressPhone.setText(currentAddress.phone);
                } else {
                    rlHasAddress.setVisibility(View.GONE);
                    tvAddressEmpty.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.anim_exit);
    }
}
