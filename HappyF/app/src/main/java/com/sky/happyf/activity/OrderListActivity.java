package com.sky.happyf.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.sky.happyf.Model.Order;
import com.sky.happyf.Model.Rank;
import com.sky.happyf.R;
import com.sky.happyf.adapter.OrderListAdapter;
import com.sky.happyf.manager.OrderManager;
import com.sky.happyf.manager.RankManager;
import com.sky.happyf.view.ContactDialog;
import com.sky.happyf.view.PayDialog;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderListActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private PtrClassicFrameLayout ptrLayout;
    private ListView lvOrder;
    private OrderListAdapter adapter;
    private OrderManager orderManager;
    private List<Order> orderList;
    private PayDialog payDialog;
    private ContactDialog contactDialog;
    private String currentOrderId, currentPhone, currentWeixin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);

        getSupportActionBar().hide();//隐藏标题栏

        initView();

        initListener();

        initData();
    }

    private void initView() {
        titleBar = findViewById(R.id.titlebar);

        ptrLayout = findViewById(R.id.ptr_layout);
        lvOrder = findViewById(R.id.lv_order);
        adapter = new OrderListAdapter(this);
        lvOrder.setAdapter(adapter);
        payDialog = new PayDialog(OrderListActivity.this, R.style.PayDialog);
        contactDialog = new ContactDialog(OrderListActivity.this, R.style.PayDialog);
    }

    private void initListener() {
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    finish();
                } else if (action == CommonTitleBar.ACTION_RIGHT_TEXT) {
                    processContact();
                }
            }
        });

        ptrLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                orderManager.getOrderList(new OrderManager.FetchOrdersCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        ptrLayout.refreshComplete();
                    }

                    @Override
                    public void onFinish(List<Order> data) {
                        orderList = data;
                        ptrLayout.refreshComplete();

                        adapter.applyData(data);

                        if (data.size() == 10) {
                            ptrLayout.setLoadMoreEnable(true);
                        }
                    }
                });
            }
        });


        ptrLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                orderManager.loadMoreOrderList(new OrderManager.FetchOrdersCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        ptrLayout.loadMoreComplete(true);
                    }

                    @Override
                    public void onFinish(List<Order> data) {
                        orderList.addAll(data);
                        adapter.applyData(orderList);
                        ptrLayout.loadMoreComplete(true);

                        if (data.size() == 10) {
                            ptrLayout.setLoadMoreEnable(true);
                        }
                    }
                });
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
        orderManager = new OrderManager(this);

        initOrderData();

        initContactInfo();
    }


    private void initOrderData() {
        ptrLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrLayout.autoRefresh(true);
            }
        }, 150);
    }

    private void initContactInfo() {
        orderManager.getContactInfo(new OrderManager.CreateOrderCallback() {
            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(Map<String, String> data) {
                currentPhone = data.get("phone");
                currentWeixin = data.get("weixin");
            }
        });
    }

    public void processContact() {
        contactDialog.setPhoneAndWeixin(currentPhone, currentWeixin);
        contactDialog.show();
    }

    public void processPay(String orderId) {
        currentOrderId = orderId;
        payDialog.show();
    }

    public void processConfirmOrder() {
        orderManager.confirmReceiveGoods(currentOrderId, new OrderManager.FetchCommonCallback() {
            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(String text) {
                Toast.makeText(getApplicationContext(), getResources().getString(R.string.operation_succ), Toast.LENGTH_LONG).show();
                finish();
            }
        });
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
