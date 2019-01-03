package com.sky.happyf.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.happyf.Model.Rank;
import com.sky.happyf.R;

import java.util.ArrayList;
import java.util.List;


public class RankListAdapter extends BaseAdapter {
    private List<Rank> rankList;
    private Context ct;

    public RankListAdapter(Context ct) {
        this.ct = ct;
        rankList = new ArrayList<Rank>();
    }

    public List<Rank> getList() {
        return rankList;
    }

    public void applyData(List<Rank> ranks) {
        rankList.clear();
        rankList.addAll(ranks);

        notifyDataSetChanged();
    }

    public void updateItem(int index, Rank rank) {
        rankList.remove(index);
        rankList.add(index, rank);
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return rankList.size();
    }

    @Override
    public Rank getItem(int position) {
        return rankList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        RankListItem view = (RankListItem) convertView;
        if (convertView == null) {
            view = new RankListItem(parent.getContext());
        }

        view.setData(rankList.get(position), position);

        return view;
    }

    public class RankListItem extends LinearLayout {
        private ImageView ivCover;
        private TextView tvRankNo, tvUsername, tvLength, tvWeight, tvTypename;

        public RankListItem(Context ct) {
            super(ct);
            inflate(getContext(), R.layout.lvitem_rank, this);
            ivCover = (ImageView) findViewById(R.id.iv_cover);
            tvRankNo = (TextView) findViewById(R.id.tv_rank_no);
            tvUsername = (TextView) findViewById(R.id.tv_username);
            tvLength = (TextView) findViewById(R.id.tv_length);
            tvWeight = (TextView) findViewById(R.id.tv_weight);
            tvTypename = (TextView) findViewById(R.id.tv_typename);
        }

        public void setData(final Rank rank, final int position) {
            tvRankNo.setText(position+1+"");
            tvUsername.setText(rank.user.name);

            if (rank.type == 1) {
                tvLength.setVisibility(View.VISIBLE);
                tvWeight.setVisibility(View.VISIBLE);
                tvLength.setText("长度："+rank.length + " 米");
                tvWeight.setText("重量：" + rank.weight + " 斤");
                tvTypename.setText(rank.fishName);
            } else {
                tvLength.setVisibility(View.GONE);
                tvWeight.setVisibility(View.GONE);
                tvTypename.setText("贝壳币：" + rank.amount);
            }



        }
    }
}
