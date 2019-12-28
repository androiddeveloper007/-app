package com.zp.mobile91q.widget_and_tool;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.customview.widget.ViewDragHelper;


/**
 * 可拖动的viewGroup，判断弹框的边界
 * author manley
 */
public class DraggableFrameLayout extends ConstraintLayout {
    private ViewDragHelper dragHelper;
    private int finalLeft = -1;
    private int finalTop = -1;
    private View dialog;
    private int mVdhYOffset, mVdhXOffset;
    private int dialogHeightDefault;

    public DraggableFrameLayout(@NonNull Context context) {
        this(context, null);
    }

    public DraggableFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DraggableFrameLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        dragHelper = ViewDragHelper.create(this, new ViewDragHelper.Callback() {

            @Override
            public boolean tryCaptureView(@NonNull View child, int pointerId) {
                return true;
            }

            @Override
            public void onViewCaptured(@NonNull View capturedChild, int activePointerId) {
                if (mOnDragStateListener != null) {
                    mOnDragStateListener.onStartDrag();
                }
                super.onViewCaptured(capturedChild, activePointerId);
            }

            @Override
            public int clampViewPositionHorizontal(@NonNull View child, int left, int dx) {
                if (left < 0) return 0;//添加规则，限制左右边界不超出
                int pos = getWidth() - child.getWidth();
                if (left > pos) {
                    return pos;
                }
                return left;
            }

            @Override
            public int clampViewPositionVertical(@NonNull View child, int top, int dy) {
                if (top < 0) return 0;//添加规则，限制上下边界不超出
                int pos = getHeight() - child.getHeight();
                if (top > pos) {
                    return pos;
                }
                return top;
            }

            @Override
            public int getViewHorizontalDragRange(@NonNull View child) {
                return getMeasuredWidth() - child.getMeasuredWidth();
            }

            @Override
            public int getViewVerticalDragRange(@NonNull View child) {
                return getMeasuredHeight() - child.getMeasuredHeight();
            }

            @Override
            public void onViewReleased(@NonNull View releasedChild, float xvel, float yvel) {
                super.onViewReleased(releasedChild, xvel, yvel);
                int viewWidth = releasedChild.getWidth();
                int viewHeight = releasedChild.getHeight();
                int curLeft = releasedChild.getLeft();
                int curTop = releasedChild.getTop();
                finalTop = curTop < 0 ? 0 : curTop;
                finalLeft = curLeft < 0 ? 0 : curLeft;
                if ((finalTop + viewHeight) > getHeight()) {
                    finalTop = getHeight() - viewHeight;
                }
                if ((finalLeft + viewWidth) > getWidth()) {
                    finalLeft = getWidth() - viewWidth;
                }
                dragHelper.settleCapturedViewAt(finalLeft, finalTop);
                invalidate();
                if (mOnDragStateListener != null) {
                    mOnDragStateListener.onEndDrag();
                }
            }
        });
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (dialogHeightDefault == 0 && dialog != null) {
            dialogHeightDefault = dialog.getMeasuredHeight();
        }
        if (finalTop != -1 && dialog != null) {//保持上一个状态的位置
            dialog.offsetTopAndBottom(mVdhYOffset - (bottom - dialogHeightDefault) / 2);
            int start = (right - bottom) / 2;
            dialog.offsetLeftAndRight(mVdhXOffset - start);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (dragHelper != null) {
            dragHelper.processTouchEvent(event);
        }
        return isTouchChildView(event);
    }

    @Override
    public void computeScroll() {
        super.computeScroll();
        if (!dragHelper.continueSettling(true) && dialog != null) {
            mVdhYOffset = dialog.getTop();
            mVdhXOffset = dialog.getLeft();
        }
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
    }

    private boolean isTouchChildView(MotionEvent ev) {
        Rect rect = new Rect();
        for (int i = 0; i < getChildCount(); i++) {
            View view = getChildAt(i);
            rect.set((int) view.getX(), (int) view.getY(), (int) view.getX() + view.getWidth(), (int) view.getY() + view.getHeight());
            if (rect.contains((int) ev.getX(), (int) ev.getY())) {
                return true;
            }
        }
        return false;
    }

    private OnDragStateListener mOnDragStateListener;

    public void setOnDragStateListener(OnDragStateListener listener) {
        mOnDragStateListener = listener;
    }

    public interface OnDragStateListener {
        void onStartDrag();

        void onEndDrag();
    }
}
