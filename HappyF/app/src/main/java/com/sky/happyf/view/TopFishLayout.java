package com.sky.happyf.view;

import android.content.Context;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.sky.happyf.adapter.TopFishPagerAdapter;

public class TopFishLayout extends RelativeLayout {
    private float scale = TopFishPagerAdapter.SMALL_SCALE;
    public String name;

    public TopFishLayout(Context context) {
        super(context);
        this.setWillNotDraw(false);
    }

    public TopFishLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.setWillNotDraw(false);
    }

    public void setScaleBoth(float scale) {
        this.scale = scale;
        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int w = this.getWidth();
        int h = this.getHeight();
        canvas.scale(scale, scale, w / 2, h / 2);
    }
}
