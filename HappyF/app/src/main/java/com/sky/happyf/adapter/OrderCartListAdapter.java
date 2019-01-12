package com.sky.happyf.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
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
        private TextView tvTitle, tvParam, tvPrice, tvShell, tvCount;

        public CartListItem(Context ct) {
            super(ct);
            inflate(getContext(), R.layout.lvitem_ordercart, this);
            ivCover = (ImageView) findViewById(R.id.iv_cover);
            tvTitle = (TextView) findViewById(R.id.tv_title);
            tvParam = (TextView) findViewById(R.id.tv_param);
            tvPrice = (TextView) findViewById(R.id.tv_price);
            tvShell = (TextView) findViewById(R.id.tv_shell);
            tvCount = (TextView) findViewById(R.id.tv_count);

        }

        public void setData(final Cart cart) {
            Glide.with(ct).load(cart.cover).into(ivCover);
            tvTitle.setText(cart.title);
            tvParam.setText(cart.selectedType);
            tvPrice.setText(ct.getResources().getString(R.string.rmb) + cart.price);
            tvShell.setText(cart.shellPrice);
            tvCount.setText("X" + cart.count);
        }
    }
}
