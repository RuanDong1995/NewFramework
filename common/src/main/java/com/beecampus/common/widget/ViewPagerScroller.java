package com.beecampus.common.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.widget.Scroller;

import java.lang.reflect.Field;

/*******************************************************************
 * ViewPagerScroller.java  2017/9/29
 * <P>
 * 可用改变ViewPager本身的滑动速度<br/>
 * <br/>
 * </p>
 * Copyright2016 by GNNT Company. All Rights Reserved.
 *
 * @author:Zhoupeng
 ******************************************************************/
public class ViewPagerScroller extends Scroller {
    /**
     * 滚动速度，默认800ms
     */
    private int mScrollDuration = 800; // 滑动速度

    /**
     * 设置速度速度
     *
     * @param duration
     */
    public void setScrollDuration(int duration) {
        this.mScrollDuration = duration;
    }

    public ViewPagerScroller(Context context) {
        super(context);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy,
                            int duration) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    @Override
    public void startScroll(int startX, int startY, int dx, int dy) {
        super.startScroll(startX, startY, dx, dy, mScrollDuration);
    }

    public static void initViewPagerScroll(ViewPager viewPager) {
        try {
            ViewPagerScroller scroller = new ViewPagerScroller(viewPager.getContext());
            Field mScroller = ViewPager.class.getDeclaredField("mScroller");
            mScroller.setAccessible(true);
            mScroller.set(viewPager, scroller);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}