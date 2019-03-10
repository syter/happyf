package com.sky.happyf.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

import com.pixplicity.multiviewpager.MultiViewPager;
import com.sky.happyf.Model.Rank;
import com.sky.happyf.fragment.TopFishFragment;
import com.sky.happyf.listensers.TopFishEventListener;
import com.sky.happyf.view.TopFishLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TopFishPagerAdapter extends FragmentStatePagerAdapter implements ViewPager.OnPageChangeListener {
    public final static float BIG_SCALE = 1.0f;
    public final static float SMALL_SCALE = 0.75f;
    public final static float DIFF_SCALE = BIG_SCALE - SMALL_SCALE;
    private float scale;
    private FragmentManager fm = null;
    private List<Rank> ranks = null;
    private MultiViewPager viewPager = null;
    private TopFishEventListener listener = null;
    private Map<Integer, Fragment> fragmentMap = null;

    public TopFishPagerAdapter(MultiViewPager viewPager, FragmentManager fm, TopFishEventListener listener) {
        super(fm);
        this.fm = fm;
        this.viewPager = viewPager;
        this.listener = listener;
        fragmentMap = new HashMap<Integer, Fragment>();
    }

    public Map<Integer, Fragment> getFragmentMap() {
        return fragmentMap;
    }

    public void setFishs(List<Rank> ranks) {
        this.ranks = ranks;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return ranks.size();
    }

    @Override
    public Fragment getItem(int position) {
        // make the first pager bigger than others
        try {
//            if (position == 0) {
//                scale = BIG_SCALE;
//            } else {
//                scale = SMALL_SCALE;
//            }
            scale = SMALL_SCALE;
        } catch (Exception e) {
            e.printStackTrace();
        }
        Fragment f = TopFishFragment.create(viewPager, listener, position, scale, ranks);
        fragmentMap.put(position, f);
        return f;
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;   // 返回发生改变，让系统重新加载
        // 系统默认返回的是     POSITION_UNCHANGED，未改变
    }

    private TopFishLayout getRootView(int position) {
        return (TopFishLayout) fragmentMap.get(position).getView();
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        try {
            if (positionOffset == 0) {
                TopFishLayout current = getRootView(position);
                current.setScaleBoth(BIG_SCALE);
            } else {
                if (positionOffset >= 0f && positionOffset <= 1f) {
                    TopFishLayout cur = getRootView(position);
                    TopFishLayout next = getRootView(position + 1);

                    cur.setScaleBoth(BIG_SCALE - DIFF_SCALE * positionOffset);
                    next.setScaleBoth(SMALL_SCALE + DIFF_SCALE * positionOffset);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (listener != null) {
            listener.onTopFishChanged(position);
        }
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
}
