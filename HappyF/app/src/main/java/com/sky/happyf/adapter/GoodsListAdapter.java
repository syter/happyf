package com.sky.happyf.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.happyf.Model.Goods;
import com.sky.happyf.R;

import java.util.ArrayList;
import java.util.List;


public class GoodsListAdapter extends BaseAdapter {
    private List<Goods> goodsList;
    private Context ct;

    public GoodsListAdapter(Context ct) {
        this.ct = ct;
        goodsList = new ArrayList<Goods>();
    }

    public List<Goods> getList() {
        return goodsList;
    }

    public void applyData(List<Goods> goodss) {
        goodsList.clear();
        goodsList.addAll(goodss);

        notifyDataSetChanged();
    }

    public void updateItem(int index, Goods goods) {
        goodsList.remove(index);
        goodsList.add(index, goods);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return goodsList.size();
    }

    @Override
    public Goods getItem(int position) {
        return goodsList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GoodsListItem view = (GoodsListItem) convertView;
        if (convertView == null) {
            view = new GoodsListItem(parent.getContext());
        }

        view.setData(goodsList.get(position));

        return view;
    }

    public class GoodsListItem extends LinearLayout {
        private ImageView ivCover;
        private TextView tvTitle1, tvTitle2, tvTitle3, tvPrice, tvShellPrice, tvSellCount;

        public GoodsListItem(Context ct) {
            super(ct);
            inflate(getContext(), R.layout.lvitem_goods, this);
            ivCover = (ImageView) findViewById(R.id.iv_cover);
            tvTitle1 = (TextView) findViewById(R.id.tv_title1);
            tvTitle2 = (TextView) findViewById(R.id.tv_title2);
            tvTitle3 = (TextView) findViewById(R.id.tv_title3);
            tvPrice = (TextView) findViewById(R.id.tv_price);
            tvShellPrice = (TextView) findViewById(R.id.tv_shell_price);
            tvSellCount = (TextView) findViewById(R.id.tv_sell_count);
        }

        public void setData(final Goods goods) {
            tvTitle1.setText(goods.title1);
            tvTitle2.setText(goods.title2);
            tvTitle3.setText(goods.title3);
            tvPrice.setText(ct.getResources().getString(R.string.rmb) + goods.price);
            tvShellPrice.setText(goods.shellPrice);
            tvSellCount.setText(goods.sellCount + " " + ct.getString(R.string.shop_selled));
        }
    }
}
