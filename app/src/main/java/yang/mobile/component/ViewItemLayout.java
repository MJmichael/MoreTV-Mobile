package yang.mobile.component;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import butterknife.ButterKnife;
import butterknife.InjectView;
import yang.mobile.R;
import yang.mobile.model.VideoItemInfo;

/**
 * Created by yang on 15/4/3.
 */
public class ViewItemLayout extends LinearLayout {

    @InjectView(R.id.video_item_img)
    protected ImageView posterImg;
    @InjectView(R.id.video_item_text)
    protected TextView videoTitle;

    public ViewItemLayout(Context context) {
        this(context, null);
    }

    public ViewItemLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ViewItemLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        setOrientation(VERTICAL);
        View view = LayoutInflater.from(getContext()).inflate(R.layout.video_item_layout, this, true);

        ButterKnife.inject(this);

//        posterImg = (ImageView) findViewById(R.id.video_item_img);
//        videoTitle = (TextView) findViewById(R.id.video_item_text);
    }

    public void setData(VideoItemInfo data) {
        if (data == null) {
            if (getVisibility() == VISIBLE) {
                setVisibility(INVISIBLE);
            }
        }
        else {
            ImageLoader.getInstance().displayImage(data.getValue(), posterImg);
            videoTitle.setText(data.getItem_title());

            if (getVisibility() != VISIBLE) {
                setVisibility(VISIBLE);
            }
        }

    }


}
