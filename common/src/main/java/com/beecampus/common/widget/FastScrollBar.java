package com.beecampus.common.widget;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.Scroller;
import android.widget.TextView;

import com.beecampus.common.R;
import com.beecampus.common.util.DisplayUtils;

import java.util.List;

/*******************************************************************
 * FastScrollBar.java 2017/3/23
 * <P>
 * 快速滚动栏<br/>
 * <br/>
 * </p>
 * Copyright2017 by GNNT Company. All Rights Reserved.
 *
 * @author:Zhoupeng
 ******************************************************************/
public class FastScrollBar extends View {

    /**
     * 默认文字颜色
     */
    static final int DEFAULT_TEXT_COLOR = 0xff000000;
    /**
     * 默认文字获得焦点时的颜色
     */
    static final int DEFAULT_TEXT_FOCUS_COLOR = 0xff000000;
    /**
     * 默认文字获得焦点时的颜色
     */
    static final int DEFAULT_BACKGROUND_COLOR = 0x00000000;
    /**
     * 默认字体大小
     */
    static final int DEFAULT_TEXT_SIZE = 12;
    /**
     * 节点列表
     */
    private List<String> mSectionList;
    /**
     * 文字绘制画笔
     */
    private Paint mTextPaint;
    /**
     * 背景画笔
     */
    private Paint mBackgroundPaint;
    /**
     * 设置文字颜色
     */
    private int mTextColor = DEFAULT_TEXT_COLOR;
    /**
     * 设置有焦点时文字颜色
     */
    private int mTextFocusColor = DEFAULT_TEXT_FOCUS_COLOR;
    /**
     * 背景色
     */
    private int mBackgroundColor;
    /**
     * 设置字体大小
     */
    private float mTextSize;
    /**
     * 隐藏比例,0~1
     */
    private float mHideOffset;
    /**
     * 触摸偏移量，0~1
     */
    private float mTouchOffset;
    /**
     * 保存当前触摸位置
     */
    private int mCurrentTouchIndex;
    /**
     * 每项高度
     */
    private float mItemHeight;

    /**
     * 位置改变监听
     */
    private OnIndexChangeListener mOnIndexChangeListener;
    /**
     * 收放使用的scroller
     */
    private Scroller mInOutScroller;
    /**
     * 显示手指按下时显示当前位置
     */
    private PopupWindow mPopupWindow;
    /**
     * 当前位置内容
     */
    private TextView mTvContent;
    /**
     * 弹窗偏移量
     */
    private int mPopupOffsetX;
    /**
     * 该view在当前window的top位置
     */
    private int mWindowTop;
    /**
     * 提示弹窗滚动控件
     */
    private Scroller mPopupScroller;

    public FastScrollBar(Context context) {
        this(context, null);
    }

    public FastScrollBar(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FastScrollBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        // 解析属性
        if (attrs != null) {
            TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.FastScrollBar);
            mTextColor = typedArray.getColor(R.styleable.FastScrollBar_textColor, DEFAULT_TEXT_COLOR);
            mTextFocusColor = typedArray.getColor(R.styleable.FastScrollBar_textFocusColor, DEFAULT_TEXT_FOCUS_COLOR);
            mTextSize = typedArray.getDimensionPixelSize(R.styleable.FastScrollBar_textSize, DisplayUtils.dip2px(context, DEFAULT_TEXT_SIZE));
            mBackgroundColor = typedArray.getColor(R.styleable.FastScrollBar_backgroundColor, DEFAULT_BACKGROUND_COLOR);
            mTouchOffset = typedArray.getFloat(R.styleable.FastScrollBar_touchOffset, 0f);
            mHideOffset = typedArray.getFloat(R.styleable.FastScrollBar_hiddenOffset, 0.5f);
            if (mHideOffset > 1) {
                mHideOffset = 1;
            }
            typedArray.recycle();
        } else {
            mTextColor = DEFAULT_TEXT_COLOR;
            mTextFocusColor = DEFAULT_TEXT_FOCUS_COLOR;
            mTextSize = DisplayUtils.dip2px(context, DEFAULT_TEXT_SIZE);
            mHideOffset = 0.5f;
            mBackgroundColor = DEFAULT_BACKGROUND_COLOR;
            mTouchOffset = 0;
        }
        // 初始化画笔
        mTextPaint = new Paint();
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mTextSize);
        mBackgroundPaint = new Paint();
        mBackgroundPaint.setStyle(Paint.Style.FILL);
        mBackgroundPaint.setColor(mBackgroundColor);
        // 初始化参数
        mCurrentTouchIndex = -1;
        mInOutScroller = new Scroller(context);
        // 初始化PopupWindow
        View contentView = View.inflate(context, R.layout.pop_fast_scroll_bar, null);
        mTvContent = (TextView) contentView.findViewById(R.id.tv_content);
        mPopupWindow = new PopupWindow(contentView, DisplayUtils.dip2px(context, 100), DisplayUtils.dip2px(context, 60));
        mPopupWindow.setAnimationStyle(R.style.FastPopupWindow);
        mPopupScroller = new Scroller(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (mSectionList != null && !mSectionList.isEmpty()) {
            mItemHeight = getMeasuredHeight() / mSectionList.size();
        }
        int width = getMeasuredWidth();
        int offsetX = (int) (width - width * mHideOffset);
        mInOutScroller.startScroll(offsetX, 0, 0, 0, 0);
        mInOutScroller.computeScrollOffset();
        // 计算弹窗偏移量
        mPopupOffsetX = getMeasuredWidth() + DisplayUtils.dip2px(getContext(), 10);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mSectionList == null || mSectionList.isEmpty()) {
            return;
        }
        // 文字绘制Y起始位置
        float startY = (mItemHeight + mTextSize) / 2f;
        float offsetX = mInOutScroller.getCurrX();

        // 绘制背景
        canvas.drawRect(offsetX, 0, getRight(), getBottom(), mBackgroundPaint);

        // 循环列表绘制所有文字
        for (int i = 0; i < mSectionList.size(); i++) {
            String text = mSectionList.get(i) == null ? "" : mSectionList.get(i);
            // 文字绘制X位置
            float paintX = ((float) getWidth() - mTextPaint.measureText(text)) / 2f + offsetX;
            if (i == mCurrentTouchIndex) {
                mTextPaint.setColor(mTextFocusColor);
            } else {
                mTextPaint.setColor(mTextColor);
            }
            canvas.drawText(text, paintX, startY + mItemHeight * i, mTextPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mSectionList == null || mSectionList.isEmpty()) {
            return super.onTouchEvent(event);
        }
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (!isNeedCatchEvent(event.getX())) {
                    return super.onTouchEvent(event);
                }
                show();
                // 重新获取顶部位置
                int[] location = new int[2];
                getLocationInWindow(location);
                mWindowTop = (int) (location[1] + (mItemHeight - mPopupWindow.getHeight()) / 2);
            case MotionEvent.ACTION_MOVE:
                int currentIndex = getFocusIndexByY(event.getY());
                if (currentIndex != mCurrentTouchIndex && currentIndex >= 0 && currentIndex < mSectionList.size()) {
                    mCurrentTouchIndex = currentIndex;
                    if (mOnIndexChangeListener != null) {
                        mOnIndexChangeListener.onIndexChanged(mCurrentTouchIndex, mSectionList.get(mCurrentTouchIndex));
                    }
                    scrollPopupWindowToPosition(currentIndex);
                    invalidate();
                }
                break;
            case MotionEvent.ACTION_UP:
                hidden();
                mCurrentTouchIndex = -1;
                invalidate();
                if (mPopupWindow.isShowing()) {
                    mPopupWindow.dismiss();
                }
                break;
        }
        return true;
    }

    /**
     * 是否需要捕获时间
     *
     * @param x 当前手指横坐标
     * @return
     */
    private boolean isNeedCatchEvent(float x) {
        if (x > (float) getWidth() * mTouchOffset) {
            return true;
        }
        return false;
    }

    /**
     * 滚动popupwindow到指定位置
     *
     * @param newPosition 新位置
     */
    private void scrollPopupWindowToPosition(int newPosition) {
        mTvContent.setText(mSectionList.get(newPosition));
        // 根据新位置获取Popup显示位置
        int offsetY = (int) (mItemHeight * newPosition);
        if (!mPopupWindow.isShowing()) {
            mPopupWindow.showAtLocation(getRootView(), Gravity.TOP | Gravity.RIGHT, mPopupOffsetX, offsetY + mWindowTop);
            mPopupScroller.startScroll(0, offsetY, 0, 0, 0);
            mPopupScroller.computeScrollOffset();
        } else {
            mPopupScroller.startScroll(0, mPopupScroller.getCurrY(), 0, offsetY - mPopupScroller.getCurrY(), 200);
        }
    }

    @Override
    public void computeScroll() {
        if (mInOutScroller.computeScrollOffset()) {
            postInvalidate();
        }
        if (mPopupScroller.computeScrollOffset()) {
            mPopupWindow.update(mPopupOffsetX, mPopupScroller.getCurrY() + mWindowTop, mPopupWindow.getWidth(), mPopupWindow.getHeight());
            postInvalidate();
        }
    }

    /**
     * 显示
     */
    private void show() {
        int startX = getWidth() - (int) ((float) getWidth() * mHideOffset);
        int endX = 0;
        if (!mInOutScroller.isFinished()) {
            startX = mInOutScroller.getCurrX();
        }
        mInOutScroller.startScroll(startX, 0, endX - startX, 0, 300);
        postInvalidate();
    }

    /**
     * 隐藏
     */
    private void hidden() {
        int startX = 0;
        int endX = getWidth() - (int) ((float) getWidth() * mHideOffset);
        if (!mInOutScroller.isFinished()) {
            startX = mInOutScroller.getCurrX();
        }
        mInOutScroller.startScroll(startX, 0, endX - startX, 0, 300);
        postInvalidate();
    }

    /**
     * 根据Y坐标获取当前触摸返回的index
     *
     * @param y Y坐标
     * @return
     */
    public int getFocusIndexByY(float y) {
        if (mItemHeight == 0) {
            return -1;
        }
        int index = (int) (y / mItemHeight);
        if (index >= mSectionList.size()) {
            index = mSectionList.size();
        }
        return index;
    }

    /**
     * 设置节点列表
     *
     * @param mSectionList
     */
    public void setSectionList(List<String> mSectionList) {
        this.mSectionList = mSectionList;
        if (mSectionList != null && mSectionList.size() > 0) {
            mItemHeight = (float) getHeight() / (float) mSectionList.size();
        } else {
            mItemHeight = 0;
        }
        invalidate();
    }

    /**
     * 设置位置改变监听
     *
     * @param onIndexChangeListener
     */
    public void setOnIndexChangeListener(OnIndexChangeListener onIndexChangeListener) {
        this.mOnIndexChangeListener = onIndexChangeListener;
    }

    /**
     * 触摸位置改变监听
     */
    public interface OnIndexChangeListener {
        void onIndexChanged(int currentIndex, String select);
    }
}
