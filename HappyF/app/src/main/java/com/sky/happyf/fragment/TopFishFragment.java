package com.sky.happyf.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.pixplicity.multiviewpager.MultiViewPager;
import com.sky.happyf.Model.Rank;
import com.sky.happyf.R;
import com.sky.happyf.listensers.TopFishEventListener;
import com.sky.happyf.view.TopFishLayout;

import java.util.List;

public class TopFishFragment extends Fragment {
    private static final String POSITON = "position";
    private static final String SCALE = "scale";
    private TopFishEventListener listener = null;
    private static List<Rank> rankList;

    private MultiViewPager pager;

    public static TopFishFragment create(MultiViewPager pager, TopFishEventListener listener, int position, float scale, List<Rank> ranks) {
        TopFishFragment fragment = new TopFishFragment();
        Bundle b = new Bundle();
        b.putInt(POSITON, position);
        b.putFloat(SCALE, scale);
        fragment.setArguments(b);
        fragment.pager = pager;
        fragment.setListener(listener);
        rankList = ranks;
        return fragment;
    }

    public void setListener(TopFishEventListener listener) {
        this.listener = listener;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final int postion = this.getArguments().getInt(POSITON);
        float scale = this.getArguments().getFloat(SCALE);

        View rootView = inflater.inflate(R.layout.item_top_fish, container, false);
        ImageView ivCover = rootView.findViewById(R.id.top_fish_cover);
        if (rankList == null) {
            return rootView;
        }
        Rank topFish = rankList.get(postion);
        Glide.with(this).load(topFish.cover).into(ivCover);

        final Bundle arguments = getArguments();
        if (arguments != null) {
            ivCover.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    pager.setCurrentItem(postion, true);
                    if (listener != null) {
                        listener.onTopFishClicked(postion);
                    }
                }
            });
        }
        ((TopFishLayout) rootView).name = postion + "=====TopFishLayout";
        ((TopFishLayout) rootView).setScaleBoth(scale);
        return rootView;
    }
}