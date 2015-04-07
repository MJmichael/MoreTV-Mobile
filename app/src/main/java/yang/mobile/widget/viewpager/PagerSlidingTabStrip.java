package yang.mobile.widget.viewpager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Locale;

import yang.mobile.R;

public class PagerSlidingTabStrip extends HorizontalScrollView {

    private static final int DEFAULT_TAB_TEXT_SIZE = 12;

    private static final int DEFAULT_DIVIDER_PADDING = 12;

    private static final int DEFAULT_UNDER_LINE_HEIGHT = 2;

    private static final int DEFAULT_INDICATOR_HEIGHT = 8;

    private static final int DEFAULT_SCROLL_OFFSET = 52;

    private static final int DEFAULT_UNDER_LINE_COLOR = 0x1A000000;

    private static final int DEFAULT_INDICATOR_COLOR = 0xFF666666;

    public interface TabIconProvider {
        int getPageIconResId(int position);
    }
    
    public interface OnTabReselectedListener {
        void onTabReselected(View tab, int position);
    }
    
    // @formatter:off
    private static final int[] ATTRS = new int[] {
        android.R.attr.textSize,
        android.R.attr.textColor,
    };
    // @formatter:on

    private LinearLayout.LayoutParams defaultTabLayoutParams;
    private LinearLayout.LayoutParams expandedTabLayoutParams;

    private final PageListener pageListener = new PageListener();
    private OnPageChangeListener delegatePageListener;

    private LinearLayout tabsContainer;
    private ViewPager pager;

    private int tabCount;

    private int currentPosition;
    private float currentPositionOffset;

    private Paint rectPaint;
    private Paint dividerPaint;

    private int indicatorColor = DEFAULT_INDICATOR_COLOR;
    private int underlineColor = DEFAULT_UNDER_LINE_COLOR;
    private Drawable dividerDrawable;

    private boolean shouldExpand;
    private boolean textAllCaps = true;

    private int scrollOffset = DEFAULT_SCROLL_OFFSET;
    private int indicatorHeight = DEFAULT_INDICATOR_HEIGHT;
    private int underlineHeight = DEFAULT_UNDER_LINE_HEIGHT;
    private int dividerPadding = DEFAULT_DIVIDER_PADDING;
    private int tabPaddingLeft;
    private int tabPaddingTop;
    private int tabPaddingBottom;
    private int tabPaddingRight;
    private int dividerWidth = 1;

    private int tabTextSize = DEFAULT_TAB_TEXT_SIZE;
    private ColorStateList tabTextColor;
    private Typeface tabTypeface;
    private int tabTypefaceStyle = Typeface.NORMAL;

    private int lastScrollX;

    private Locale locale;

    private int srcollState = ViewPager.SCROLL_STATE_IDLE;

    private int iconPadding;
    
    private OnTabReselectedListener onTabReselectedListener;

    public PagerSlidingTabStrip(Context context) {
        this(context, null);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PagerSlidingTabStrip(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        setFillViewport(true);
        setWillNotDraw(false);

        tabsContainer = new LinearLayout(context);
        tabsContainer.setOrientation(LinearLayout.HORIZONTAL);
        tabsContainer.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        addView(tabsContainer);
        initDefaultValue();
        getAttrsValue(context, attrs);

        rectPaint = new Paint();
        rectPaint.setAntiAlias(true);
        rectPaint.setStyle(Style.FILL);

        dividerPaint = new Paint();
        dividerPaint.setAntiAlias(true);
        dividerPaint.setStrokeWidth(dividerWidth);

        defaultTabLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT);
        expandedTabLayoutParams = new LinearLayout.LayoutParams(0, LayoutParams.MATCH_PARENT, 1.0f);
        locale = getResources().getConfiguration().locale;
    }

    private void getAttrsValue(Context context, AttributeSet attrs) {
        // get system attrs (android:textSize and android:textColor)
        TypedArray a = context.obtainStyledAttributes(attrs, ATTRS);
        tabTextSize = a.getDimensionPixelSize(0, tabTextSize);
        tabTextColor = a.getColorStateList(1);
        a.recycle();
        // get custom attrs
        a = context.obtainStyledAttributes(attrs, R.styleable.PagerSlidingTabStrip);
        indicatorColor = a.getColor(R.styleable.PagerSlidingTabStrip_indicatorColor, indicatorColor);
        underlineColor = a.getColor(R.styleable.PagerSlidingTabStrip_underlineColor, underlineColor);
        indicatorHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_indicatorHeight, indicatorHeight);
        underlineHeight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_underlineHeight, underlineHeight);
        dividerDrawable = a.getDrawable(R.styleable.PagerSlidingTabStrip_tabDivider);
        dividerWidth = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_dividerWidth, dividerWidth);
        dividerPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_dividerPaddingTopBottom, dividerPadding);
        iconPadding = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_iconPadding, iconPadding);
        tabPaddingLeft = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_tabPaddingLeft, tabPaddingLeft);
        tabPaddingRight = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_tabPaddingRight, tabPaddingRight);
        tabPaddingTop = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_tabPaddingTop, tabPaddingTop);
        tabPaddingBottom = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_tabPaddingBottom, tabPaddingBottom);
        shouldExpand = a.getBoolean(R.styleable.PagerSlidingTabStrip_shouldExpand, shouldExpand);
        scrollOffset = a.getDimensionPixelSize(R.styleable.PagerSlidingTabStrip_scrollOffset, scrollOffset);
        textAllCaps = a.getBoolean(R.styleable.PagerSlidingTabStrip_tabTextAllCaps, textAllCaps);

        a.recycle();
    }

    private void initDefaultValue() {
        DisplayMetrics dm = getResources().getDisplayMetrics();
        scrollOffset = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, scrollOffset, dm);
        indicatorHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, indicatorHeight, dm);
        underlineHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, underlineHeight, dm);
        dividerPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerPadding, dm);
        iconPadding = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, iconPadding, dm);
        tabPaddingLeft = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPaddingLeft, dm);
        tabPaddingRight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPaddingRight, dm);
        tabPaddingTop = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPaddingTop, dm);
        tabPaddingBottom = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, tabPaddingBottom, dm);
        dividerWidth = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dividerWidth, dm);
        tabTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, tabTextSize, dm);
    }

    public void attachToViewPager(ViewPager pager) {
        this.pager = pager;

        if (pager.getAdapter() == null) {
            throw new IllegalStateException("ViewPager does not have adapter instance.");
        }

        pager.setOnPageChangeListener(pageListener);

        notifyDataSetChanged();
    }

    public void setOnPageChangeListener(OnPageChangeListener listener) {
        this.delegatePageListener = listener;
    }

    public void notifyDataSetChanged() {

        tabsContainer.removeAllViews();

        tabCount = pager.getAdapter().getCount();

        for (int i = 0; i < tabCount; i++) {

            if (pager.getAdapter() instanceof TabIconProvider) {
                String title = null;
                if (pager.getAdapter().getPageTitle(i) != null) {
                    title = pager.getAdapter().getPageTitle(i).toString();
                }
                addTabWithIcon(i, title, ((TabIconProvider) pager.getAdapter()).getPageIconResId(i));
            } else {
                addTab(i, pager.getAdapter().getPageTitle(i).toString());
            }

        }
        getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {

            @Override
            public void onGlobalLayout() {

                getViewTreeObserver().removeGlobalOnLayoutListener(this);

                currentPosition = pager.getCurrentItem();
                setSelection(pager.getCurrentItem());
                scrollToTab(currentPosition, 0);
            }
        });

    }

    public TabView getTabView(int index) {
        return (TabView) tabsContainer.getChildAt(index);
    }

    public int getCurrentPageIndex() {
        if (pager != null) {
            return pager.getCurrentItem();
        }
        return 0;
    }

    private void addTabWithIcon(int position, String title, int resId) {
        tabsContainer.addView(new TabView(getContext(), position, title, resId));
    }

    private void addTab(final int position, String title) {

        tabsContainer.addView(new TabView(getContext(), position, title, 0));

    }

    public class TabView extends FrameLayout {

        public TabView(Context context, final int position, String title,
                int resId) {
            super(context);
            TextView text = createTextTab(title);
            text.setDuplicateParentStateEnabled(true);
            setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (pager.getCurrentItem() == position && getOnTabReselectedListener() != null) {
                        getOnTabReselectedListener().onTabReselected(TabView.this, position);
                        return;
                    }
                    setSelection(position);
                }
            });
            if (resId != 0) {
                text.setCompoundDrawablesWithIntrinsicBounds(resId, 0, 0, 0);
                text.setCompoundDrawablePadding(iconPadding);
            }
            LayoutParams lps = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
            lps.gravity = Gravity.CENTER;
            addView(text, lps);
            setLayoutParams(shouldExpand ? expandedTabLayoutParams : defaultTabLayoutParams);
        }

        @SuppressLint("NewApi")
        private void updateTabTextStyles(TextView tabText) {
            if (shouldExpand) {
                tabText.setPadding(0, tabPaddingTop, 0, tabPaddingBottom);
            } else {
                tabText.setPadding(tabPaddingLeft, tabPaddingTop, tabPaddingRight, tabPaddingBottom);
            }
            tabText.setTextSize(TypedValue.COMPLEX_UNIT_PX, tabTextSize);
            tabText.setTypeface(tabTypeface, tabTypefaceStyle);
            tabText.setTextColor(tabTextColor);

            // setAllCaps() is only available from API 14, so the upper case is made manually if we are on a
            // pre-ICS-build
            if (textAllCaps) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                    tabText.setAllCaps(true);
                } else {
                    tabText.setText(tabText.getText().toString().toUpperCase(locale));
                }
            }
        }

        public void addView(View child, LayoutParams params) {
            super.addView(child, params);
        }

        private TextView createTextTab(String title) {
            TextView text = new TextView(getContext());
            text.setText(title);
            text.setGravity(Gravity.CENTER);
            text.setSingleLine();
            text.setBackgroundResource(android.R.color.transparent);
            updateTabTextStyles(text);
            return text;
        }
    }

    public void setSelection(int position) {
        pager.setCurrentItem(position);
        for (int i = 0; i < tabCount; i++) {
            View v = tabsContainer.getChildAt(i);
            if (i == position) {
                v.setSelected(true);
            } else {
                v.setSelected(false);
            }
        }
    }

    private void scrollToTab(int position, int offset) {
        if (tabCount == 0) {
            return;
        }
        if (offset == 0) {
            return;
        }
        int stripWidth = ((View) getParent()).getWidth();
        View currentTap = tabsContainer.getChildAt(position);
        int scrollX = currentTap.getRight() + offset;
        scrollX -= (stripWidth + currentTap.getWidth()) / 2;
        if (scrollX != lastScrollX) {
            lastScrollX = scrollX;
            scrollTo(scrollX, 0);
        }
    }

    public boolean isScrolling() {
        return srcollState != ViewPager.SCROLL_STATE_IDLE;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (isInEditMode() || tabCount == 0) {
            return;
        }
        final int height = getHeight();
        drawDivider(canvas, height);
        // draw underline
        rectPaint.setColor(underlineColor);
        canvas.drawRect(0, height - underlineHeight, tabsContainer.getWidth(), height, rectPaint);
        // draw indicator line
        rectPaint.setColor(indicatorColor);
        // default: line below current tab
        View currentTab = tabsContainer.getChildAt(currentPosition);
        float lineLeft = currentTab.getLeft();
        float lineRight = currentTab.getRight();
        // if there is an offset, start interpolating left and right coordinates between current and next tab
        if (currentPositionOffset > 0f && currentPosition < tabCount - 1) {
            View nextTab = tabsContainer.getChildAt(currentPosition + 1);
            final float nextTabLeft = nextTab.getLeft();
            final float nextTabRight = nextTab.getRight();

            lineLeft = currentPositionOffset * nextTabLeft + (1f - currentPositionOffset) * lineLeft;
            lineRight = currentPositionOffset * nextTabRight + (1f - currentPositionOffset) * lineRight;
        }
        canvas.drawRect(lineLeft, height - indicatorHeight, lineRight, height, rectPaint);
    }

    private void drawDivider(Canvas canvas, final int height) {
        for (int i = 0; i < tabCount - 1 && dividerDrawable != null; i++) {
            View tab = tabsContainer.getChildAt(i);
            if (i % 2 == 1) {
                dividerDrawable.setBounds(tab.getRight() - dividerWidth, dividerPadding
                        , tab.getRight(), height - dividerPadding);
            } else {
                dividerDrawable.setBounds(tab.getRight(), dividerPadding
                        , tab.getRight()  + dividerWidth, height - dividerPadding);
            }
            dividerDrawable.draw(canvas);
//            canvas.drawLine(tab.getRight(), dividerPadding, tab.getRight(), height - dividerPadding, dividerPaint);
        }
    }

    private class PageListener implements OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            currentPosition = position;
            currentPositionOffset = positionOffset;
            if (currentPosition + 1 < tabsContainer.getChildCount()) {
                scrollToTab(currentPosition, (int) (positionOffset * tabsContainer.getChildAt(currentPosition + 1).getWidth()));
            }
            invalidate();
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {
            srcollState = state;
            if (delegatePageListener != null) {
                delegatePageListener.onPageScrollStateChanged(state);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if (delegatePageListener != null) {
                delegatePageListener.onPageSelected(position);
            }
            setSelection(position);
        }

    }

    @Override
    public void onRestoreInstanceState(Parcelable state) {
        SavedState savedState = (SavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        currentPosition = savedState.currentPosition;
        requestLayout();
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        SavedState savedState = new SavedState(superState);
        savedState.currentPosition = currentPosition;
        return savedState;
    }

    public OnTabReselectedListener getOnTabReselectedListener() {
        return onTabReselectedListener;
    }

    public void setOnTabReselectedListener(OnTabReselectedListener onTabReselectedListener) {
        this.onTabReselectedListener = onTabReselectedListener;
    }

    static class SavedState extends BaseSavedState {

        public static final Creator<SavedState> CREATOR = new Creator<SavedState>() {
            @Override
            public SavedState createFromParcel(Parcel in) {
                return new SavedState(in);
            }

            @Override
            public SavedState[] newArray(int size) {
                return new SavedState[size];
            }
        };
        
        private int currentPosition;
        
        public SavedState(Parcelable superState) {
            super(superState);
        }

        private SavedState(Parcel in) {
            super(in);
            currentPosition = in.readInt();
        }

        @Override
        public void writeToParcel(Parcel dest, int flags) {
            super.writeToParcel(dest, flags);
            dest.writeInt(currentPosition);
        }
    }

}
