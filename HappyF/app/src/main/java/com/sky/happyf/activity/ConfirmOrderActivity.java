package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.happyf.Model.Address;
import com.sky.happyf.Model.Cart;
import com.sky.happyf.R;
import com.sky.happyf.adapter.CartListAdapter;
import com.sky.happyf.adapter.OrderCartListAdapter;
import com.sky.happyf.manager.CartManager;
import com.sky.happyf.manager.OrderManager;
import com.sky.happyf.util.Constants;
import com.sky.happyf.view.MyListView;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.util.List;
import java.util.Map;

public class ConfirmOrderActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private RelativeLayout rlSelectAddress, rlHasaddress;
    private MyListView lvCart;
    private OrderCartListAdapter adapter;
    private CartManager cartManager;
    private OrderManager orderManager;
    private TextView tvEmptyAddress, tvAddressName, tvAddressDetail, tvAddressPhone, tvPostage,
            tvPrice, tvShellPrice, tvShellPay;
    private LinearLayout llShellPay, llPricePay;
    private EditText etRemark;

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
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String cartIds = bundle.getString("cart_ids");

        cartManager = new CartManager(this);
        orderManager = new OrderManager(this);

        initCartData(cartIds);
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
                                        // TODO 可能也需要关闭上一个页面???
                                        finish();
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
                                    // TODO 选择微信或支付宝付款
//                                    Intent intent = new Intent(ConfirmOrderActivity.this, PayActivity.class);
//                                    Bundle bundle = new Bundle();
//                                    bundle.putString("order_id", data.get("orderId"));
//                                    intent.putExtras(bundle);
//                                    startActivity(intent);
//                                    overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);

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
