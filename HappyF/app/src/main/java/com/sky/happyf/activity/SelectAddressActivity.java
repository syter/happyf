package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sky.happyf.Model.Address;
import com.sky.happyf.Model.CartPrice;
import com.sky.happyf.Model.Rank;
import com.sky.happyf.R;
import com.sky.happyf.adapter.AddressListAdapter;
import com.sky.happyf.adapter.CartListAdapter;
import com.sky.happyf.manager.AddressManager;
import com.sky.happyf.manager.CartManager;
import com.sky.happyf.manager.RankManager;
import com.sky.happyf.manager.UserManager;
import com.sky.happyf.util.Utils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import java.math.BigDecimal;
import java.util.List;

public class SelectAddressActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private ListView lvAddress;
    private AddressListAdapter adapter;
    private AddressManager addressManager;
    private Address currentAddress;
    private TextView tvAddressName, tvAddressPhone, tvAddressDetail, tvEmptyAddress;
    private RelativeLayout rlHasaddress;
    private List<Address> currentAddressList;

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
        titleBar = findViewById(R.id.titlebar);
        lvAddress = findViewById(R.id.lv_address);
        adapter = new AddressListAdapter(this);
        lvAddress.setAdapter(adapter);
        tvAddressName = findViewById(R.id.tv_address_name);
        tvAddressPhone = findViewById(R.id.tv_address_phone);
        tvAddressDetail = findViewById(R.id.tv_address_detail);
        tvEmptyAddress = findViewById(R.id.tv_empty_address);
        rlHasaddress = findViewById(R.id.rl_hasaddress);
    }

    private void initListener() {
//        titleBar.setBackgroundResource(R.drawable.shape_gradient);
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    finish();
                } else if (action == CommonTitleBar.ACTION_RIGHT_TEXT) {
                    Intent intent = new Intent(SelectAddressActivity.this, EditAddressActivity.class);
                    intent.putExtra("is_new", "1");
                    startActivity(intent);
                    overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                }
            }
        });

        lvAddress.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int i, long l) {

                addressManager.setDefaultAddress(currentAddressList.get(i).id, new AddressManager.FetchCommonCallback() {
                    @Override
                    public void onFailure(String errorMsg) {
                        Toast.makeText(SelectAddressActivity.this, errorMsg, Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFinish(String text) {
                        currentAddress = adapter.getAddress(i);
                        processCurrentAddress();
                    }
                });
                finish();
            }
        });
    }

    private void initData() {
        addressManager = new AddressManager(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initAddressList();
    }

    private void initAddressList() {
        addressManager.getAddressList(new AddressManager.FetchAddressCallback() {
            @Override
            public void onFailure(String errorMsg) {
                Toast.makeText(SelectAddressActivity.this, errorMsg, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFinish(List<Address> addressList) {
                currentAddressList = addressList;
                for (Address address : addressList) {
                    if (address.isUsed) {
                        currentAddress = address;
                        break;
                    }
                }
                processCurrentAddress();
                adapter.applyData(addressList);
            }
        });
    }

    private void processCurrentAddress() {
        if (currentAddress != null) {
            rlHasaddress.setVisibility(View.VISIBLE);
            tvEmptyAddress.setVisibility(View.GONE);
            tvAddressName.setText(getResources().getString(R.string.select_name_hint) + currentAddress.name);
            tvAddressDetail.setText(getResources().getString(R.string.select_address_hint) + currentAddress.province + currentAddress.city + currentAddress.district + currentAddress.address);
            tvAddressPhone.setText(currentAddress.phone);
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.anim_exit);
    }
}
