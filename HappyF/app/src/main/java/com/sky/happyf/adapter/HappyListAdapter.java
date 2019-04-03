package com.sky.happyf.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sky.happyf.Model.Happy;
import com.sky.happyf.R;

import java.util.ArrayList;
import java.util.List;


public class HappyListAdapter extends BaseAdapter {
    private List<Happy> happyList;
    private Context ct;

    public HappyListAdapter(Context ct) {
        this.ct = ct;
        happyList = new ArrayList<Happy>();
    }

    public List<Happy> getList() {
        return happyList;
    }

    public void applyData(List<Happy> happys) {
        happyList.clear();
        happyList.addAll(happys);

        notifyDataSetChanged();
    }


    @Override
    public int getCount() {
        return happyList.size();
    }

    @Override
    public Happy getItem(int position) {
        return happyList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        HappyListItem view = (HappyListItem) convertView;
        if (convertView == null) {
            view = new HappyListItem(parent.getContext());
        }

        view.setData(happyList.get(position));

        return view;
    }

    public class HappyListItem extends LinearLayout {
        private ImageView ivCover;
        private TextView tvDate, tvAddress, tvFish, tvCount, tvState;

        public HappyListItem(Context ct) {
            super(ct);
            inflate(getContext(), R.layout.lvitem_happylist, this);
            ivCover =  findViewById(R.id.iv_cover);
            tvDate =  findViewById(R.id.tv_date);
            tvState = findViewById(R.id.tv_state);
            tvAddress =  findViewById(R.id.tv_address);
            tvFish =  findViewById(R.id.tv_fish);
            tvCount =  findViewById(R.id.tv_count);
        }

        public void setData(final Happy happy) {
//            tvAddress.setText(happy.address);
//            tvFish.setText(happy.fish);
//            tvState.setText(happy.state + "");
//            tvCount.setText(happy.count + "人");
//            tvDate.setText(happy.date);
        }
    }
}
