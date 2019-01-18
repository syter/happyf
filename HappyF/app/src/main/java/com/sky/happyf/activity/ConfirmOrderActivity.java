package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.happyf.Model.Address;
import com.sky.happyf.Model.Cart;
import com.sky.happyf.R;
import com.sky.happyf.adapter.OrderCartListAdapter;
import com.sky.happyf.manager.CartManager;
import com.sky.happyf.manager.FinishActivityManager;
import com.sky.happyf.manager.OrderManager;
import com.sky.happyf.util.Constants;
import com.sky.happyf.view.PayDialog;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.util.List;
import java.util.Map;

public class ConfirmOrderActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private RelativeLayout rlSelectAddress, rlHasaddress;
    private ListView lvCart;
    private OrderCartListAdapter adapter;
    private CartManager cartManager;
    private OrderManager orderManager;
    private TextView tvEmptyAddress, tvAddressName, tvAddressDetail, tvAddressPhone, tvPostage,
            tvPrice, tvShellPrice, tvShellPay;
    private LinearLayout llShellPay, llPricePay;
    private EditText etRemark;
    private String cartIds;
    private PayDialog payDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_order);

        getSupportActionBar().hide();//隐藏标题栏

        initView();

        initListener();

        initData();
    }

    private void initView() {
        titleBar = findViewById(R.id.titlebar);
        rlSelectAddress = findViewById(R.id.rl_select_address);
        rlHasaddress = findViewById(R.id.rl_hasaddress);
        tvEmptyAddress = findViewById(R.id.tv_empty_address);
        tvAddressName = findViewById(R.id.tv_address_name);
        tvAddressDetail = findViewById(R.id.tv_address_detail);
        tvAddressPhone = findViewById(R.id.tv_address_phone);
        tvPostage = findViewById(R.id.tv_postage);
        tvPrice = findViewById(R.id.tv_price);
        tvShellPrice = findViewById(R.id.tv_shell_price);
        tvShellPay = findViewById(R.id.tv_shell_pay);
        llShellPay = findViewById(R.id.ll_shell_pay);
        llPricePay = findViewById(R.id.ll_price_pay);
        etRemark = findViewById(R.id.et_remark);
        lvCart = findViewById(R.id.lv_cart);
        adapter = new OrderCartListAdapter(this);
        lvCart.setAdapter(adapter);
        payDialog = new PayDialog(ConfirmOrderActivity.this, R.style.PayDialog);
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

        rlSelectAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(ConfirmOrderActivity.this, SelectAddressActivity.class));
                overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
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
        Bundle bundle = intent.getExtras();
        cartIds = bundle.getString("cart_ids");

        cartManager = new CartManager(this);
        orderManager = new OrderManager(this);


    }

    private void initCartData(final String cartIds) {
        cartManager.getOrderCartList(cartIds, new CartManager.FetchCartsCallback() {
            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(ConfirmOrderActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(Map<String, Object> data) {
                Address address = (Address) data.get("address");
                if (address != null) {
                    rlHasaddress.setVisibility(View.VISIBLE);
                    tvEmptyAddress.setVisibility(View.GONE);
                    tvAddressName.setText(getResources().getString(R.string.select_name_hint) + address.name);
                    tvAddressDetail.setText(getResources().getString(R.string.select_address_hint) + address.province + address.city + address.district + address.address);
                    tvAddressPhone.setText(address.phone);
                }

                List<Cart> cartList = (List<Cart>) data.get("cart");
                adapter.applyData(cartList);

                tvPostage.setText((String) data.get("postagePrice") + "：" + getResources().getString(R.string.rmb) + data.get("postPrice"));
                tvPrice.setText((String) data.get("totalPrice"));
                tvShellPrice.setText((String) data.get("totalShellPrice"));
                if ((Boolean) data.get("canPayByShell")) {
                    llShellPay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String remark = etRemark.getText().toString();
                            orderManager.createOrder(cartIds, Constants.ORDER_PAY_TYPE_SHELL, remark, new OrderManager.CreateOrderCallback() {
                                @Override
                                public void onFailure(String errorMsg) {
                                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onFinish(Map<String, String> data) {
                                    if ("Y".equals(data.get("status"))) {
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.confirm_pay_shell_succ), Toast.LENGTH_LONG).show();
                                        processPaySuccess();
                                    } else {
                                        Toast.makeText(getApplicationContext(), getResources().getString(R.string.confirm_pay_shell_not_enough), Toast.LENGTH_LONG).show();
                                        initCartData(cartIds);
                                    }
                                }
                            });
                        }
                    });
                    llPricePay.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            String remark = etRemark.getText().toString();
                            orderManager.createOrder(cartIds, Constants.ORDER_PAY_TYPE_PRICE, remark, new OrderManager.CreateOrderCallback() {
                                @Override
                                public void onFailure(String errorMsg) {
                                    Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                                }

                                @Override
                                public void onFinish(Map<String, String> data) {
                                    payDialog.show();
                                }
                            });
                        }
                    });
                } else {
                    llShellPay.setBackground(getDrawable(R.drawable.pay_button_shell_not_enough));
                    tvShellPay.setText(getResources().getString(R.string.confirm_pay_shell_not_enough));
                }
            }
        });
    }

    private void processPaySuccess() {
        FinishActivityManager.getManager().finishActivity(GoodsDetailActivity.class);
        FinishActivityManager.getManager().finishActivity(CartListActivity.class);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCartData(cartIds);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.anim_exit);
    }


}
