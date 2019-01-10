package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;

import com.sky.happyf.Model.Address;
import com.sky.happyf.Model.Rank;
import com.sky.happyf.R;
import com.sky.happyf.adapter.AddressListAdapter;
import com.sky.happyf.adapter.CartListAdapter;
import com.sky.happyf.manager.AddressManager;
import com.sky.happyf.manager.CartManager;
import com.sky.happyf.manager.RankManager;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.util.List;

public class SelectAddressActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private ListView lvAddress;
    private AddressListAdapter adapter;
    private AddressManager addressManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_address);

        getSupportActionBar().hide();//隐藏标题栏

        initView();

        initListener();

        initData();
    }

    private void initView() {
        titleBar = (CommonTitleBar) findViewById(R.id.titlebar);
        lvAddress = (ListView) findViewById(R.id.lv_address);
        adapter = new AddressListAdapter(this);
        lvAddress.setAdapter(adapter);
    }

    private void initListener() {
//        titleBar.setBackgroundResource(R.drawable.shape_gradient);
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    finish();
                } else if (action == CommonTitleBar.ACTION_RIGHT_TEXT) {
                    startActivity(new Intent(SelectAddressActivity.this, EditAddressActivity.class));
                    overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                }
            }
        });

        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // TODO select address
                finish();
            }
        });
    }

    private void initData() {
        addressManager = new AddressManager(this);
        addressManager.init(new AddressManager.FetchAddresssCallback() {
            @Override
            public void onFailure(String errorMsg) {

            }

            @Override
            public void onFinish(List<Address> data) {
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
