package yang.mobile.widget.recyclerview;

import android.graphics.Canvas;
import android.support.v7.widget.RecyclerView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

/**
 * Created by yang on 15/4/3.
 */
public class RecyclerHeaderWithoutSticky extends StickyRecyclerHeadersDecoration{
    public RecyclerHeaderWithoutSticky(StickyRecyclerHeadersAdapter adapter) {
        super(adapter);
    }

    @Override
    public void onDrawOver(Canvas canvas, RecyclerView parent, RecyclerView.State state) {
    }
}
