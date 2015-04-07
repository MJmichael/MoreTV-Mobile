/*
 * Copyright 2015 Rudson Lima
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package yang.mobile.activity.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.astuetz.PagerSlidingTabStrip;
import com.malinskiy.superrecyclerview.SuperRecyclerView;
import com.timehop.stickyheadersrecyclerview.StickyRecyclerHeadersDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import yang.mobile.R;
import yang.mobile.Utils.ListUtils;
import yang.mobile.api.RestClient;
import yang.mobile.model.HomeVideosContainer;
import yang.mobile.model.VideoItemInfo;
import yang.mobile.widget.recyclerview.DividerDecoration;
import yang.mobile.widget.recyclerview.HeaderViewRecyclerAdapter;

public class FragmentHome extends Fragment {

    private boolean mSearchCheck;
    private static final String TEXT_FRAGMENT = "TEXT_FRAGMENT";

	public FragmentHome newInstance(String text){
		FragmentHome mFragment = new FragmentHome();
		Bundle mBundle = new Bundle();
		mBundle.putString(TEXT_FRAGMENT, text);
		mFragment.setArguments(mBundle);
		return mFragment;
	}

    protected PagerSlidingTabStrip titleTab;

    protected ViewPager pager;

    private List<VideoItemInfo> albumList;
    private List<Pair<VideoItemInfo, VideoItemInfo>> hotVideo = new ArrayList<Pair<VideoItemInfo, VideoItemInfo>>();
    private List<Pair<VideoItemInfo, VideoItemInfo>> movieVideo = new ArrayList<Pair<VideoItemInfo, VideoItemInfo>>();

    @InjectView(R.id.main_content_list)
    protected SuperRecyclerView mainContentList;

    private HeaderViewRecyclerAdapter headerViewRecyclerAdapter;
    private HomeRecyclerAdapter adapter;

    private RecyclerView recyclerView;

    @Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_main, container, false);
	}

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.inject(this, view);
//
//        titleTab.setViewPager(pager);
//        pager.setAdapter(new HomePageAdapter(getFragmentManager()));

        recyclerView = mainContentList.getRecyclerView();
        LinearLayoutManager manager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerView.setLayoutManager(manager);
        adapter = new HomeRecyclerAdapter();
//        headerViewRecyclerAdapter = new HeaderViewRecyclerAdapter()
        requestHomeData();

    }

    private void setAdapter() {
        if (getActivity() != null && recyclerView.getAdapter() == null) {
            recyclerView.setAdapter(adapter);
            StickyRecyclerHeadersDecoration headersDecor = new StickyRecyclerHeadersDecoration(adapter);
            recyclerView.addItemDecoration(headersDecor);
            recyclerView.addItemDecoration(new DividerDecoration(getActivity().getApplicationContext(),
                    R.drawable.divider_v));
            mainContentList.showRecycler();
        }
    }

    private void requestHomeData() {
//        if (ListUtils.isEmpty(albumList)) {
//            RestClient.get().getHomAlbums(new Callback<HomeVideosContainer>() {
//                @Override
//                public void success(HomeVideosContainer homeVideosContainer, Response response) {
////                    albumList = homeVideosContainer.getPosition().getPositionItems();
////                    adapter.setAlbumList(albumList);
//                }
//
//                @Override
//                public void failure(RetrofitError error) {
//
//                }
//            });
//        }

        if (ListUtils.isEmpty(hotVideo)) {
            RestClient.get().getHotVideos(new Callback<HomeVideosContainer>() {
                @Override
                public void success(HomeVideosContainer homeVideosContainer, Response response) {
                    List<VideoItemInfo> hotVideoList = homeVideosContainer.getPosition().getPositionItems();
                    ListUtils.fillPairList(hotVideoList, hotVideo);
                    adapter.setHotVideo(hotVideo);
                    setAdapter();
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }

        if (ListUtils.isEmpty(movieVideo)) {
            RestClient.get().getHomeMovies(new Callback<HomeVideosContainer>() {
                @Override
                public void success(HomeVideosContainer homeVideosContainer, Response response) {
                    List<VideoItemInfo> moviesList = homeVideosContainer.getPosition().getPositionItems();
                    ListUtils.fillPairList(moviesList, movieVideo);
                    adapter.setMovieVideo(movieVideo);
                    setAdapter();
                }

                @Override
                public void failure(RetrofitError error) {

                }
            });
        }
    }


    @Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		setHasOptionsMenu(true);
	}
	
//	@Override
//	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
//		// TODO Auto-generated method stub
//		super.onCreateOptionsMenu(menu, inflater);
//		inflater.inflate(R.menu.menu, menu);
//
//        //Select search item
//        final MenuItem menuItem = menu.findItem(R.id.menu_search);
//        menuItem.setVisible(true);
//
//        SearchView searchView = (SearchView) menuItem.getActionView();
//        searchView.setQueryHint(this.getString(R.string.search));
//
//        ((EditText) searchView.findViewById(R.id.search_src_text))
//                .setHintTextColor(getResources().getColor(R.color.nliveo_white));
//        searchView.setOnQueryTextListener(onQuerySearchView);
//
//		menu.findItem(R.id.menu_add).setVisible(true);
//
//		mSearchCheck = false;
//	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		
		switch (item.getItemId()) {

		case R.id.menu_add:
            Toast.makeText(getActivity(), R.string.add, Toast.LENGTH_SHORT).show();
			break;

		case R.id.menu_search:
			mSearchCheck = true;
            Toast.makeText(getActivity(), R.string.search, Toast.LENGTH_SHORT).show();
			break;
		}
		return true;
	}	

   private SearchView.OnQueryTextListener onQuerySearchView = new SearchView.OnQueryTextListener() {
       @Override
       public boolean onQueryTextSubmit(String s) {
           return false;
       }

       @Override
       public boolean onQueryTextChange(String s) {
           if (mSearchCheck){
               // implement your search here
           }
           return false;
       }
   };

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        ButterKnife.reset(this);
    }
}
