package com.beecampus.common.widget;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ScrollingView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/*******************************************************************
 * ExpendScrollCoordinatorLayout.java  2019/1/10
 * <P>
 * 扩展AppBar的滑动作用范围<br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class ExpendScrollCoordinatorLayout extends CoordinatorLayout {


    public ExpendScrollCoordinatorLayout(@NonNull Context context) {
        super(context);
    }

    public ExpendScrollCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public ExpendScrollCoordinatorLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean isPointInChildBounds(@NonNull View child, int x, int y) {
        if (child instanceof AppBarLayout) {
            // 如果AppBarLayout在范围内，或者范围内没有ScrollingView，则都交给AppBarLayout处理事件
            if (super.isPointInChildBounds(child, x, y) || !isContainedScrollingView(this, x, y)) {
                return true;
            }
        }
        return super.isPointInChildBounds(child, x, y);
    }

    /**
     * 检查该范围下是否包含NestedScrollView
     *
     * @return true 包含，false 不包含
     */
    private boolean isContainedScrollingView(View view, int x, int y) {
        if (view instanceof ScrollingView && isPointInChildBounds(view, x, y)) {
            return true;
        }


        if (view instanceof ViewGroup) {
            ViewGroup viewGroup = (ViewGroup) view;
            for (int i = 0; i < viewGroup.getChildCount(); i++) {
                if (isContainedScrollingView(viewGroup.getChildAt(i), x, y)) {
                    return true;
                }
            }
        }
        return false;
    }
}
