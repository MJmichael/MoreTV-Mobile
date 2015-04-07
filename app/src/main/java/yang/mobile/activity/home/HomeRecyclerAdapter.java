package yang.mobile.activity.home;

import android.support.v7.widget.RecyclerView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersAdapter;

import java.util.List;

import yang.mobile.R;
import yang.mobile.Utils.ListUtils;
import yang.mobile.component.ViewItemLayout;
import yang.mobile.model.VideoItemInfo;

/**
 * Created by yang on 15/4/1.
 */
public class HomeRecyclerAdapter extends RecyclerView.Adapter implements StickyRecyclerHeadersAdapter<RecyclerView.ViewHolder> {

    private final static int ABLUM_TYPE = 0;
    private final static int VIDEO_TYPE = 1;
    private final static int INFO_ITEM = 2;


    private List<VideoItemInfo> albumList;
//    private List<VideoItemInfo> hotVideoList;
//    private List<VideoItemInfo> moviesList;

    private List<Pair<VideoItemInfo, VideoItemInfo>> hotVideo;
    private List<Pair<VideoItemInfo, VideoItemInfo>> movieVideo;

    private int albumListTypeCount;
    private int hotVideoTypeCount;
    private int moviesListTypeCount;

    public void setAlbumList(List<VideoItemInfo> albumList) {
        this.albumList = albumList;
        notifyDataSetChanged();
    }

    public void setHotVideo(List<Pair<VideoItemInfo, VideoItemInfo>> hotVideo) {
        this.hotVideo = hotVideo;
        notifyDataSetChanged();
    }

    public void setMovieVideo(List<Pair<VideoItemInfo, VideoItemInfo>> movieVideo) {
        this.movieVideo = movieVideo;
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ABLUM_TYPE) {
            return null;
        } else if (viewType == VIDEO_TYPE) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_list_item, parent, false);
            return new VideoItemListHolder(view);
        }

        throw new IllegalArgumentException(String.format("unkonw viewType %d", viewType));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        int itemType = getItemViewType(position);
        if (itemType == ABLUM_TYPE){

        } else if (itemType == VIDEO_TYPE) {
            ((VideoItemListHolder) holder).bindView(position < albumListTypeCount + hotVideoTypeCount ?
                hotVideo.get(position) : movieVideo.get(position - albumListTypeCount - hotVideoTypeCount));
        }

    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (!ListUtils.isEmpty(albumList)) {
            count ++;
        }
        albumListTypeCount = count;

        if(!ListUtils.isEmpty(hotVideo)) {
            hotVideoTypeCount = hotVideo.size();
        } else {
            hotVideoTypeCount = 0;
        }
        count += hotVideoTypeCount;

        if(!ListUtils.isEmpty(movieVideo)) {
            moviesListTypeCount = movieVideo.size();
        } else {
            moviesListTypeCount = 0;
        }
        count += moviesListTypeCount;

        return count;
    }

    @Override
    public int getItemViewType(int position) {
        getItemCount();
        if (position < albumListTypeCount) {
            return ABLUM_TYPE;
        } else if (position < albumListTypeCount + hotVideoTypeCount + moviesListTypeCount){
            return VIDEO_TYPE;
        } else {
            return INFO_ITEM;
        }
    }

    @Override
    public long getHeaderId(int position) {
//        if (position < albumListTypeCount) {
//            return -1;
//        } else if (position < albumListTypeCount + hotVideoTypeCount){
//            return 1;
//        } else {
//            return 2;
//        }

        if (position < albumListTypeCount) {
            return -1;
        } else if (position == albumListTypeCount){
            return 1;
        } else if (position == albumListTypeCount + hotVideoTypeCount){
            return 2;
        }else {
            return -1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup viewGroup) {
        View view = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.home_view_header, viewGroup, false);
        return new RecyclerView.ViewHolder(view) {};
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        TextView textView = (TextView) viewHolder.itemView;
        long headerId = getHeaderId(i);
        if (headerId == 1) {
            textView.setText("热门推荐");
        } else if (headerId == 2) {
            textView.setText("最新影片");
        }
    }

    private static class VideoItemListHolder extends RecyclerView.ViewHolder{
        public ViewItemLayout[] itemLayouts = new ViewItemLayout[2];

        private VideoItemListHolder(View itemView) {
            super(itemView);

            ViewGroup viewGroup = (ViewGroup) itemView;
            itemLayouts[0] = (ViewItemLayout) viewGroup.getChildAt(0);
            itemLayouts[1] = (ViewItemLayout) viewGroup.getChildAt(1);
        }

        public void bindView(Pair<VideoItemInfo, VideoItemInfo> info) {
            itemLayouts[0].setData(info.first);
            itemLayouts[1].setData(info.second);
        }
    }
}
