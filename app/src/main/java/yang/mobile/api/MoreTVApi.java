package yang.mobile.api;

import retrofit.Callback;
import retrofit.http.GET;
import yang.mobile.model.HomeVideosContainer;

/**
 * Created by yang on 15/3/28.
 */
public interface MoreTVApi {

    //首页专题
    @GET("/vod.moretv.com.cn/Service/Position?code=mobile_subject2")
    void getHomAlbums(Callback<HomeVideosContainer> cb);

    //热门推荐
    @GET("/vod.moretv.com.cn/Service/Position?code=mobile_movie_hot")
    void getHotVideos(Callback<HomeVideosContainer> cb);

    //院线大片
    @GET("/vod.moretv.com.cn/Service/Position?code=p_index")
    void getHomeMovies(Callback<HomeVideosContainer> cb);
}
