package com.beecampus.common.widget;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.beecampus.common.R;
import com.beecampus.common.util.DisplayUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;

/*******************************************************************
 * CardItemDecoration.java  2019/1/11
 * <P>
 * <br/>
 * <br/>
 * </p>
 *
 * @author:zhoupeng
 *
 ******************************************************************/
public class CardItemDecoration extends RecyclerView.ItemDecoration {

    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * 绘制矩形
     */
    private RectF mRect = new RectF();

    /**
     * 边距
     */
    private float mPadding;

    /**
     * 圆角
     */
    private float mRadius;

    public CardItemDecoration(RecyclerView recyclerView, float padding, float radius) {
        this.mPadding = padding;
        this.mRadius = radius;

        // 关闭硬件加速
        recyclerView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        // 初始化画笔
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setAntiAlias(true);
        mPaint.setShadowLayer(DisplayUtils.dip2px(recyclerView.getContext(), 8),
                0, 0, 0x50000000);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.Adapter adapter = parent.getAdapter();
        int position = parent.getChildAdapterPosition(view);

        outRect.set(50, 0, 50, 0);
        if (position == 0) {
            outRect.top = 50;
        } else if (position == adapter.getItemCount() - 1) {
            outRect.bottom = 50;
        }

    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        RecyclerView.Adapter adapter = parent.getAdapter();
        // 当没有子项时，则不绘制
        if (adapter.getItemCount() == 0 ||
                (adapter instanceof BaseQuickAdapter && ((BaseQuickAdapter) adapter).getEmptyViewCount() == 1)){
            return;
        }
        float offset = layoutManager.computeVerticalScrollOffset(state);
        float top = mPadding - offset;
        float bottom = layoutManager.computeHorizontalScrollRange(state) - offset - 50;
        mRect.set(mPadding, top, parent.getWidth() - mPadding, bottom);
        c.drawRoundRect(mRect, mRadius, mRadius, mPaint);
    }
}
