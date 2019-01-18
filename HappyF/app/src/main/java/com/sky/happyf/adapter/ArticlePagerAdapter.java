package com.sky.happyf.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ArticlePagerAdapter extends PagerAdapter {
    private List<View> viewList;
    private List<String> dataList;
    private Context ct;

    public ArticlePagerAdapter(Context ct, List<View> viewList, List<String> dataList) {
        this.viewList = viewList;
        this.ct = ct;
        this.dataList = dataList;
    }

    @Override
    public int getCount() {
        return viewList == null ? 0 : viewList.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        container.addView(viewList.get(position));
        return viewList.get(position);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getItemPosition(Object object) {
        TextView textView = (TextView) object;
        String text = textView.getText().toString();
        return Integer.parseInt(text);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return dataList.get(position);
    }
}
