package com.sky.happyf.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.builder.OptionsPickerBuilder;
import com.bigkoo.pickerview.listener.OnOptionsSelectListener;
import com.bigkoo.pickerview.view.OptionsPickerView;
import com.orhanobut.logger.Logger;
import com.sky.happyf.Model.Address;
import com.sky.happyf.R;
import com.sky.happyf.manager.AddressManager;
import com.sky.happyf.util.Utils;
import com.wuhenzhizao.titlebar.widget.CommonTitleBar;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class EditAddressActivity extends BaseActivity {
    private CommonTitleBar titleBar;
    private EditText etName, etPhone, etAddress;
    private ArrayList<String> provinces = new ArrayList<>();
    private ArrayList<ArrayList<String>> citys = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> districts = new ArrayList<>();
    private OptionsPickerView pvOptions;
    private LinearLayout llPcd;
    private TextView tvProvince, tvCity, tvDistrict;
    private AddressManager addressManager;
    private String isNew;
    private Address currentAddress;
    private boolean isBtnCalled = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_address);

        getSupportActionBar().hide();//隐藏标题栏

        initView();

        initListener();

        initData();
    }

    private void initView() {
        etName = findViewById(R.id.et_name);
        etPhone = findViewById(R.id.et_phone);
        etAddress = findViewById(R.id.et_detail);
        titleBar = findViewById(R.id.titlebar);
        llPcd = findViewById(R.id.rl_pcd);
        tvProvince = findViewById(R.id.tv_province);
        tvCity = findViewById(R.id.tv_city);
        tvDistrict = findViewById(R.id.tv_district);

        pvOptions = new OptionsPickerBuilder(EditAddressActivity.this, new OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int option2, int options3, View v) {
                tvProvince.setText(provinces.get(options1));
                tvProvince.setTextColor(getColor(R.color.gray_text_2));
                tvCity.setText(citys.get(options1).get(option2));
                tvCity.setTextColor(getColor(R.color.gray_text_2));
                tvDistrict.setText(districts.get(options1).get(option2).get(options3));
                tvDistrict.setTextColor(getColor(R.color.gray_text_2));

            }
        })
                .setContentTextSize(20)
                .setCancelColor(getColor(R.color.main_color_blue))
                .setSubmitColor(getColor(R.color.main_color_blue))
                .build();

        llPcd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pvOptions.show();
            }
        });

    }

    private void initListener() {
        titleBar.setListener(new CommonTitleBar.OnTitleBarListener() {
            @Override
            public void onClicked(View v, int action, String extra) {
                if (action == CommonTitleBar.ACTION_LEFT_TEXT) {
                    finish();
                } else if (action == CommonTitleBar.ACTION_RIGHT_TEXT) {
                    if (isBtnCalled) {
                        return;
                    }
                    isBtnCalled = true;
                    String name = etName.getText().toString();
                    String phone = etPhone.getText().toString();
                    String province = tvProvince.getText().toString();
                    String city = tvCity.getText().toString();
                    String district = tvDistrict.getText().toString();
                    String address = etAddress.getText().toString();

                    String errorMsg = addressManager.validAddressParams(name, phone, province, city, district, address);
                    if (!Utils.isEmptyString(errorMsg)) {
                        isBtnCalled = false;
                        Toast.makeText(getApplicationContext(), errorMsg, Toast.LENGTH_LONG).show();
                        return;
                    }

                    Address addr = new Address();
                    addr.name = name;
                    addr.phone = phone;
                    addr.province = province;
                    addr.city = city;
                    addr.district = district;
                    addr.address = address;

                    if ("1".equals(isNew)) {
                        addressManager.createNewAddress(addr, new AddressManager.FetchCommonCallback() {
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
                    } else {
                        addr.id = currentAddress.id;
                        addressManager.updateAddress(addr, new AddressManager.FetchCommonCallback() {
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
                }
            }
        });
    }

    private void initData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        isNew = bundle.getString("is_new");
        if ("1".equals(isNew)) {
            titleBar.getCenterTextView().setText(getResources().getString(R.string.edit_address_title_1));
        } else {
            titleBar.getCenterTextView().setText(getResources().getString(R.string.edit_address_title_1));
            currentAddress = (Address) intent.getSerializableExtra("address");
            tvProvince.setText(currentAddress.province);
            tvProvince.setTextColor(getColor(R.color.gray_text_2));
            tvCity.setText(currentAddress.city);
            tvCity.setTextColor(getColor(R.color.gray_text_2));
            tvDistrict.setText(currentAddress.district);
            tvDistrict.setTextColor(getColor(R.color.gray_text_2));
            etName.setText(currentAddress.name);
            etPhone.setText(currentAddress.phone);
            etAddress.setText(currentAddress.address);
        }

        addressManager = new AddressManager(this);

        try {
            JSONArray provinceArray = Utils.getProvinceInfo(this);
            Logger.d(provinceArray.toString());
            for (int i = 0; i < provinceArray.length(); i++) {
                JSONObject provinceJson = provinceArray.getJSONObject(i);
                JSONArray cityArray = provinceJson.getJSONArray("cities");
                ArrayList<String> tempCitys = new ArrayList<>();
                ArrayList<ArrayList<String>> tempTempDistricts = new ArrayList<>();
                for (int j = 0; j < cityArray.length(); j++) {
                    JSONObject cityJson = cityArray.getJSONObject(j);
                    tempCitys.add(cityJson.getString("areaName"));
                    JSONArray districtArray = cityJson.getJSONArray("counties");
                    ArrayList<String> tempDistricts = new ArrayList<>();
                    for (int k = 0; k < districtArray.length(); k++) {
                        JSONObject districtJson = districtArray.getJSONObject(k);
                        tempDistricts.add(districtJson.getString("areaName"));
                    }
                    tempTempDistricts.add(tempDistricts);
                }
                provinces.add(provinceJson.getString("areaName"));
                citys.add(tempCitys);
                districts.add(tempTempDistricts);
            }
        } catch (Exception e) {

        }
        pvOptions.setPicker(provinces, citys, districts);
    }


    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.bottom_silent, R.anim.anim_exit);
    }
}
