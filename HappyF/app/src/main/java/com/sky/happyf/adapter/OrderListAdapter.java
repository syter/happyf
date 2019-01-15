package com.sky.happyf.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.happyf.Model.Order;
import com.sky.happyf.R;

import java.util.ArrayList;
import java.util.List;


public class OrderListAdapter extends BaseAdapter {
    private List<Order> orderList;
    private Context ct;

    public OrderListAdapter(Context ct) {
        this.ct = ct;
        orderList = new ArrayList<Order>();
    }

    public List<Order> getList() {
        return orderList;
    }

    public void applyData(List<Order> orders) {
        orderList.clear();
        orderList.addAll(orders);

        notifyDataSetChanged();
    }

    public void updateItem(int index, Order order) {
        orderList.remove(index);
        orderList.add(index, order);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return orderList.size();
    }

    @Override
    public Order getItem(int position) {
        return orderList.get(position);
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

        view.setData(orderList.get(position));

        return view;
    }

    public class OrderListItem extends LinearLayout {
        private ImageView ivCover;
        private TextView tvTitle, tvDesc, tvSize, tvState, tvAmount, tvCount, tvDate;

        public OrderListItem(Context ct) {
            super(ct);
            inflate(getContext(), R.layout.lvitem_order, this);
            ivCover = findViewById(R.id.iv_cover);
            tvTitle = findViewById(R.id.tv_title);
            tvDesc = findViewById(R.id.tv_desc);
            tvSize = findViewById(R.id.tv_size);
            tvState = findViewById(R.id.tv_state);
            tvAmount = findViewById(R.id.tv_amount);
            tvCount = findViewById(R.id.tv_count);
            tvDate = findViewById(R.id.tv_date);
        }

        public void setData(final Order order) {
            tvTitle.setText(order.title);
            tvDesc.setText(order.desc);
            tvSize.setText(order.size);
            tvState.setText(order.state + "");
            tvAmount.setText(ct.getResources().getString(R.string.rmb) + order.amount);
            tvCount.setText("X" + order.count);
            tvDate.setText(order.date);


        }
    }
}
