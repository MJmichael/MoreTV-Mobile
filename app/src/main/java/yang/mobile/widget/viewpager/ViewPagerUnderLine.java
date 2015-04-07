package yang.mobile.widget.viewpager;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.View;

public class ViewPagerUnderLine extends View implements OnPageChangeListener {

    private ViewPager mViewPager;
    private int mCurrentPage;
    // private int mUnderLineHeight;
    private float mCurentPageOffset;
    private int mScrollState = ViewPager.SCROLL_STATE_IDLE;
    private OnPageChangeListener mListener;
    private final Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public ViewPagerUnderLine(Context context) {
        super(context);
    }

    public ViewPagerUnderLine(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ViewPagerUnderLine(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    
    public void attachToViewPager(ViewPager viewPager) {
        if (viewPager == null) {
            return;
        }
        if (viewPager.getAdapter() == null) {
            throw new IllegalStateException(
                    "ViewPager does not have adapter instance.");
        }
        mViewPager = viewPager;
        mViewPager.setOnPageChangeListener(this);
        invalidate();
        setFocusable(false);
        setFocusableInTouchMode(false);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (mViewPager == null) {
            return;
        }
        final int count = mViewPager.getAdapter().getCount();
        if (count == 0) {
            return;
        }

        if (mCurrentPage >= count) {
            setCurrentItem(count - 1);
            return;
        }

        final int paddingLeft = getPaddingLeft();
        final float lineWidth = (getWidth() - paddingLeft - getPaddingRight())
                / (1f * count);
        final float left = paddingLeft + lineWidth
                * (mCurrentPage + mCurentPageOffset);
        final float right = left + lineWidth;
        final float top = getPaddingTop();
        final float bottom = getMeasuredHeight() - getPaddingBottom();
        canvas.drawRect(left, top, right, bottom, mPaint);
    }

    // public int getUnderLineHeight() {
    // return mUnderLineHeight;
    // }

    public void setCurrentItem(int item) {
        if (mViewPager == null) {
            throw new IllegalStateException("ViewPager has not been bound.");
        }
        mViewPager.setCurrentItem(item);
        mCurrentPage = item;
        invalidate();
    }

    // public void setUnderLineHeight(int underLineHeight) {
    // this.mUnderLineHeight = underLineHeight;
    // }

    public int getSelectedColor() {
        return mPaint.getColor();
    }

    public void setSelectedColor(int selectedColor) {
        mPaint.setColor(selectedColor);
        invalidate();
    }

    @Override
    public void onPageScrollStateChanged(int state) {
        mScrollState = state;

        if (mListener != null) {
            mListener.onPageScrollStateChanged(state);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset,
            int positionOffsetPixels) {
        mCurrentPage = position;
        mCurentPageOffset = positionOffset;
        invalidate();

        if (mListener != null) {
            mListener.onPageScrolled(position, positionOffset,
                    positionOffsetPixels);
        }
    }

    @Override
    public void onPageSelected(int position) {
        if (mScrollState == ViewPager.SCROLL_STATE_IDLE) {
            mCurrentPage = position;
            mCurentPageOffset = 0;
        }
        if (mListener != null) {
            mListener.onPageSelected(position);
        }
    }
    
    public boolean isScrolling() {
        return mScrollState != ViewPager.SCROLL_STATE_IDLE;
    }

    public void setOnPageChangeListener(ViewPager.OnPageChangeListener listener) {
        this.mListener = listener;
    }

}
