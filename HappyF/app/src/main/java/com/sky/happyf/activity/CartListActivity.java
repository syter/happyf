package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.chanven.lib.cptr.PtrClassicFrameLayout;
import com.chanven.lib.cptr.PtrDefaultHandler;
import com.chanven.lib.cptr.PtrFrameLayout;
import com.chanven.lib.cptr.loadmore.OnLoadMoreListener;
import com.sky.happyf.Model.Cart;
import com.sky.happyf.R;
import com.sky.happyf.adapter.CartListAdapter;
import com.sky.happyf.manager.CartManager;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class CartListActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private PtrClassicFrameLayout ptrLayout;
    private ListView lvCart;
    private CartListAdapter adapter;
    private CartManager cartManager;
    private Button btnBuy;



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
        titleBar = (CommonTitleBar) findViewById(R.id.titlebar);

        ptrLayout = (PtrClassicFrameLayout) this.findViewById(R.id.ptr_layout);
        lvCart = (ListView) findViewById(R.id.lv_cart);
        adapter = new CartListAdapter(this);
        lvCart.setAdapter(adapter);

        btnBuy = (Button) findViewById(R.id.btn_buy);
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

        // set auto load more disable,default available
//        ptrLayout.setAutoLoadMoreEnable(false);


        ptrLayout.setPtrHandler(new PtrDefaultHandler() {

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                cartManager.init(new CartManager.FetchCartsCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        ptrLayout.refreshComplete();
                    }

                    @Override
                    public void onFinish(List<Cart> data) {
                        ptrLayout.refreshComplete();

                        adapter.applyData(data);
                        adapter.notifyDataSetChanged();
                        ptrLayout.refreshComplete();

                        if (!ptrLayout.isLoadMoreEnable()) {
                            ptrLayout.setLoadMoreEnable(true);
                        }
                    }
                });
            }
        });


        ptrLayout.setOnLoadMoreListener(new OnLoadMoreListener() {

            @Override
            public void loadMore() {
                cartManager.loadMore(new CartManager.FetchCartsCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        ptrLayout.refreshComplete();
                    }

                    @Override
                    public void onFinish(List<Cart> data) {
                        ptrLayout.loadMoreComplete(true);

                        adapter.applyData(data);
                        adapter.notifyDataSetChanged();
                        ptrLayout.refreshComplete();

                        if (data.isEmpty()) {
                            ptrLayout.setLoadMoreEnable(false);
                        }
                    }
                });
            }
        });

        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CartListActivity.this, ConfirmOrderActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("id", "x");
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
        ptrLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                ptrLayout.autoRefresh(true);
            }
        }, 150);
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
