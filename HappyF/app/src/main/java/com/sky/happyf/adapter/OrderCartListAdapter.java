package com.sky.happyf.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.happyf.Model.Cart;
import com.sky.happyf.R;

import java.util.ArrayList;
import java.util.List;


public class OrderCartListAdapter extends BaseAdapter {
    private List<Cart> cartList;
    private Context ct;

    public OrderCartListAdapter(Context ct) {
        this.ct = ct;
        cartList = new ArrayList<Cart>();
    }

    public List<Cart> getList() {
        return cartList;
    }

    public void applyData(List<Cart> carts) {
        cartList.clear();
        cartList.addAll(carts);

        notifyDataSetChanged();
    }

    public void updateItem(int index, Cart cart) {
        cartList.remove(index);
        cartList.add(index, cart);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return cartList.size();
    }

    @Override
    public Cart getItem(int position) {
        return cartList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        CartListItem view = (CartListItem) convertView;
        if (convertView == null) {
            view = new CartListItem(parent.getContext());
        }

        view.setData(cartList.get(position));

        return view;
    }

    public class CartListItem extends LinearLayout {
        private ImageView ivCover;
        private TextView tvTitle, tvDesc, tvSize, tvAmount, tvCount;

        public CartListItem(Context ct) {
            super(ct);
            inflate(getContext(), R.layout.lvitem_ordercart, this);
            ivCover = (ImageView) findViewById(R.id.iv_cover);
            tvTitle = (TextView) findViewById(R.id.tv_title);
            tvDesc = (TextView) findViewById(R.id.tv_desc);
            tvSize = (TextView) findViewById(R.id.tv_size);
            tvAmount = (TextView) findViewById(R.id.tv_amount);
            tvCount = (TextView) findViewById(R.id.tv_count);

        }

        public void setData(final Cart cart) {
            tvTitle.setText(cart.title);
//            tvDesc.setText(cart.desc);
//            tvSize.setText(cart.size);
//            tvAmount.setText("￥" + cart.amount);
            tvCount.setText(cart.count + "");
        }
    }
}
