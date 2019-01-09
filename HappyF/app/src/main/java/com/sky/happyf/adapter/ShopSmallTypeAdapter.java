package com.sky.happyf.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.happyf.Model.Order;
import com.sky.happyf.Model.SmallType;
import com.sky.happyf.R;

import java.util.ArrayList;
import java.util.List;


public class ShopSmallTypeAdapter extends BaseAdapter {
    private List<SmallType> typeList;
    private Context ct;

    public ShopSmallTypeAdapter(Context ct) {
        this.ct = ct;
        typeList = new ArrayList<SmallType>();
    }

    public List<SmallType> getList() {
        return typeList;
    }

    public void applyData(List<SmallType> orders) {
        typeList.clear();
        typeList.addAll(orders);

        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return typeList.size();
    }

    @Override
    public SmallType getItem(int position) {
        return typeList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        OrderListItem view = (OrderListItem) convertView;
        if (convertView == null) {
            view = new OrderListItem(parent.getContext());
        }

        view.setData(typeList.get(position));

        return view;
    }

    public class OrderListItem extends LinearLayout {
        private TextView tvSmallType;
        private LinearLayout llBottomLine;

        public OrderListItem(Context ct) {
            super(ct);
            inflate(getContext(), R.layout.lvitem_shop_small_type, this);
            tvSmallType = findViewById(R.id.tv_small_type);
            llBottomLine = findViewById(R.id.ll_bottom_line);
        }

        public void setData(final SmallType smallType) {
            tvSmallType.setText(smallType.typeName);
            if (smallType.isSelected) {
                tvSmallType.setTextColor(ct.getColor(R.color.main_color_blue));
                llBottomLine.setVisibility(View.VISIBLE);
            } else {
                tvSmallType.setTextColor(ct.getColor(R.color.gray_text_2));
                llBottomLine.setVisibility(View.GONE);
            }
        }
    }
}
