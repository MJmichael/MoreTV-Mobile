package yang.mobile.widget.viewpager;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class AutoLoopViewPager extends LoopViewPager {

    private long periodMillis = 10000;

    private Runnable selectNextPage = new Runnable() {

        public void run() {
            PagerAdapter adapter = getAdapter();
            if (adapter == null) {
                return;
            }
            int count = adapter.getCount();
            int currentItem = getCurrentItem();
            int nextItem = currentItem + 1;
            if (nextItem > count) {
                nextItem = 1;
            }
            setCurrentItem(nextItem);
            removeCallbacks(this);
            postDelayed(this, periodMillis);
        }
    };

    public AutoLoopViewPager(Context context) {
        this(context, null);
    }

    public AutoLoopViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        final int action = ev.getAction();
        pauseAutoLoop();
        if (action == MotionEvent.ACTION_UP) {
            PagerAdapter adapter = getAdapter();
            if (adapter != null && adapter.getCount() > 1) {
                startAutoLoop();
            }
        }
        return super.onTouchEvent(ev);
    }
    
    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        pauseAutoLoop();
    }
    
    public void pauseAutoLoop() {
        removeCallbacks(selectNextPage);
    }

    public void startAutoLoop() {
        removeCallbacks(selectNextPage);
        postDelayed(selectNextPage, periodMillis);
    }
}
