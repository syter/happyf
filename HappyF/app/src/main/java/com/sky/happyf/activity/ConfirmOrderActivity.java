package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;

import com.sky.happyf.Model.Cart;
import com.sky.happyf.R;
import com.sky.happyf.adapter.CartListAdapter;
import com.sky.happyf.adapter.OrderCartListAdapter;
import com.sky.happyf.manager.CartManager;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.util.List;

public class ConfirmOrderActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private Button btnConfirm;
    private RelativeLayout rlSelectAddress;
    private ListView lvCart;
    private OrderCartListAdapter adapter;
    private CartManager cartManager;


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
        btnConfirm = (Button) findViewById(R.id.btn_confirm);
        titleBar = (CommonTitleBar) findViewById(R.id.titlebar);
        rlSelectAddress = (RelativeLayout) findViewById(R.id.rl_select_address);

        lvCart = (ListView) findViewById(R.id.lv_cart);
        adapter = new OrderCartListAdapter(this);
        lvCart.setAdapter(adapter);
    }

    private void initListener() {
//        titleBar.setBackgroundResource(R.drawable.shape_gradient);
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

        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ConfirmOrderActivity.this, PayActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("price", "100");
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
            }
        });
    }

    private void initData() {
        cartManager = new CartManager(this);

        initCartData();
    }

    private void initCartData() {
        cartManager.init(new CartManager.FetchCartsCallback() {
            @Override
            public void onFailure(String errorMsg) {

            }

            @Override
            public void onFinish(List<Cart> data) {
                adapter.applyData(data);
                adapter.notifyDataSetChanged();
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
