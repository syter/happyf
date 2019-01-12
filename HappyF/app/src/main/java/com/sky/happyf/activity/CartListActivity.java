package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.sky.happyf.Model.Address;
import com.sky.happyf.Model.Cart;
import com.sky.happyf.R;
import com.sky.happyf.adapter.CartListAdapter;
import com.sky.happyf.manager.CartManager;
import com.sky.happyf.manager.UserManager;
import com.sky.happyf.util.Utils;
import com.sky.happyf.view.SmoothCheckBox;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CartListActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private ListView lvCart;
    private CartListAdapter adapter;
    private CartManager cartManager;
    private UserManager userManager;
    private LinearLayout llPay, llRemove;
    private RelativeLayout rlLeft, rlCheckall;
    private SmoothCheckBox cbAll;
    public TextView tvPrice, tvShellPrice;
    private boolean isEdit = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_list);

        getSupportActionBar().hide();//隐藏标题栏

        initView();

        initListener();

        initData();
    }

    private void initView() {
        titleBar = findViewById(R.id.titlebar);
        lvCart = findViewById(R.id.lv_cart);
        cartManager = new CartManager(this);
        adapter = new CartListAdapter(this, isEdit, cartManager);
        lvCart.setAdapter(adapter);
        llPay = findViewById(R.id.ll_pay);
        llRemove = findViewById(R.id.ll_remove);
        rlLeft = findViewById(R.id.rl_left);
        cbAll = findViewById(R.id.cb_all);
        rlCheckall = findViewById(R.id.rl_checkall);
        tvPrice = findViewById(R.id.tv_price);
        tvShellPrice = findViewById(R.id.tv_shell_price);
    }

    private void initListener() {
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    finish();
                } else if (action == CommonTitleBar.ACTION_RIGHT_TEXT) {
                    TextView rightTextview = titleBar.getRightTextView();
                    // 切换模式
                    if (isEdit) {
                        rightTextview.setText(R.string.edit);
                        isEdit = false;
                        llRemove.setVisibility(View.GONE);
                        rlCheckall.setVisibility(View.GONE);
                        llPay.setVisibility(View.VISIBLE);
                        rlLeft.setVisibility(View.VISIBLE);
                        adapter.setEdit(isEdit);
                    } else {
                        rightTextview.setText(R.string.complete);
                        isEdit = true;
                        llRemove.setVisibility(View.VISIBLE);
                        rlCheckall.setVisibility(View.VISIBLE);
                        llPay.setVisibility(View.GONE);
                        rlLeft.setVisibility(View.GONE);
                        adapter.setEdit(isEdit);

                    }
                }
            }
        });

        cbAll.setOnCheckedChangeListener(new SmoothCheckBox.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(SmoothCheckBox checkBox, boolean isChecked) {
                adapter.checkAll(isChecked);
            }
        });


        llPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String selectCartIds = adapter.getSelectCartIds();
                if (Utils.isEmptyString(selectCartIds)) {
                    Toast.makeText(CartListActivity.this, getResources().getString(R.string.cart_select_error), Toast.LENGTH_LONG).show();
                } else {
                    Intent intent = new Intent(CartListActivity.this, ConfirmOrderActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("cart_ids", selectCartIds);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                }

            }
        });

        llRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.removeCartGoods();
            }
        });
    }

    private void initData() {
        userManager = new UserManager(this);

        tvPrice.setText(getResources().getString(R.string.rmb) + "0");
    }


    private void initCartData() {
        cartManager.getCartList(new CartManager.FetchCartsCallback() {
            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(CartListActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(Map<String, Object> data) {
                Address address = (Address) data.get("address");
                if (address != null) {
                    // TODO ??????????????
                }

                List<Cart> cartList = (List<Cart>) data.get("cart");
                adapter.applyData(cartList);
            }

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        initCartData();
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.anim_exit);
    }

}
