package com.sky.happyf.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.happyf.Model.Address;
import com.sky.happyf.R;
import com.sky.happyf.activity.EditAddressActivity;
import com.sky.happyf.activity.SelectAddressActivity;

import java.util.ArrayList;
import java.util.List;


public class AddressListAdapter extends BaseAdapter {
    private List<Address> addressList;
    private Activity act;

    public AddressListAdapter(Activity act) {
        this.act = act;
        addressList = new ArrayList<Address>();
    }

    public List<Address> getList() {
        return addressList;
    }

    public void applyData(List<Address> addresss) {
        addressList.clear();
        addressList.addAll(addresss);

        notifyDataSetChanged();
    }

    public void updateItem(int index, Address address) {
        addressList.remove(index);
        addressList.add(index, address);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return addressList.size();
    }

    @Override
    public Address getItem(int position) {
        return addressList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AddressListItem view = (AddressListItem) convertView;
        if (convertView == null) {
            view = new AddressListItem(parent.getContext());
        }

        view.setData(addressList.get(position));

        return view;
    }

    public class AddressListItem extends LinearLayout {
        private TextView tvEdit, tvName, tvPhone, tvAddress;

        public AddressListItem(Context ct) {
            super(ct);
            inflate(getContext(), R.layout.lvitem_address, this);
            tvEdit = findViewById(R.id.tv_edit);
            tvName = findViewById(R.id.tv_address_name);
            tvPhone = findViewById(R.id.tv_address_phone);
            tvAddress = findViewById(R.id.tv_address_detail);
        }

        public void setData(final Address address) {
            tvName.setText(address.name);
            tvPhone.setText(address.phone);
            tvAddress.setText(address.address);

            tvEdit.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    act.startActivity(new Intent(act, EditAddressActivity.class));
                    act.overridePendingTransition(R.anim.anim_enter, R.anim.bottom_silent);
                }
            });
        }
    }
}
