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
package yang.mobile.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.SparseIntArray;
import android.view.Menu;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import yang.mobile.R;
import yang.mobile.Utils.ListUtils;
import yang.mobile.activity.home.FragmentHome;
import yang.mobile.interfaces.NavigationLiveoListener;
import yang.mobile.navigationliveo.NavigationLiveo;

public class MainActivity extends NavigationLiveo implements NavigationLiveoListener {
    private static final String TEXT_FRAGMENT = "TEXT_FRAGMENT";

    public List<String> mListNameItem;
    private static List<Class<? extends Fragment>> fragmentList = new ArrayList<>();

    @Override
    public void onUserInformation() {
        this.mUserName.setText("Rudson Lima");
        this.mUserEmail.setText("rudsonlive@gmail.com");
        this.mUserPhoto.setImageResource(R.drawable.ic_no_user);
        this.mUserBackground.setImageResource(R.drawable.ic_user_background);
    }

    @Override
    public void onInt(Bundle savedInstanceState) {
        //Creation of the list items is here

        // set listener {required}
        this.setNavigationListener(this);

        //First item of the position selected from the list
        this.setDefaultStartPositionNavigation(1);

        // name of the list items
        mListNameItem = new ArrayList<>();
        mListNameItem.add(0, "直播");
        mListNameItem.add(1, "热门推荐");
        mListNameItem.add(2, "电视剧");
        mListNameItem.add(3, "电影");
        mListNameItem.add(4, "体育");
        mListNameItem.add(5, "精选专题");
        mListNameItem.add(6, "综艺");

        fragmentList.add(FragmentHome.class);
        fragmentList.add(FragmentHome.class);

        // icons list items
        List<Integer> mListIconItem = new ArrayList<>();
        mListIconItem.add(0, R.drawable.ic_inbox_black_24dp);
        mListIconItem.add(1, R.drawable.ic_star_black_24dp); //Item no icon set 0
        mListIconItem.add(2, R.drawable.ic_send_black_24dp); //Item no icon set 0
        mListIconItem.add(3, R.drawable.ic_drafts_black_24dp);
        mListIconItem.add(4, R.drawable.ic_send_black_24dp); //When the item is a subHeader the value of the icon 0
        mListIconItem.add(5, R.drawable.ic_delete_black_24dp);
        mListIconItem.add(6, R.drawable.ic_report_black_24dp);

        //{optional} - Among the names there is some subheader, you must indicate it here
        List<Integer> mListHeaderItem = new ArrayList<>();
        mListHeaderItem.add(4);

        //{optional} - Among the names there is any item counter, you must indicate it (position) and the value here
        SparseIntArray mSparseCounterItem = new SparseIntArray(); //indicate all items that have a counter
        mSparseCounterItem.put(0, 7);
        mSparseCounterItem.put(1, 123);
        mSparseCounterItem.put(6, 250);

        //If not please use the FooterDrawer use the setFooterVisible(boolean visible) method with value false
        this.setFooterInformationDrawer(R.string.settings, R.drawable.ic_settings_black_24dp);

        this.setNavigationAdapter(mListNameItem, mListIconItem);
    }

    @Override
    public void onItemClickNavigation(int position, int layoutContainerId) {
        FragmentManager mFragmentManager = getSupportFragmentManager();
        if (position == getCurrentPosition() &&
                ListUtils.size(mFragmentManager.getFragments()) > 0) {
            return;
        }

        Class fragmentClass = fragmentList.get(position);
        String tag = fragmentClass.getName();
        Fragment mFragment = mFragmentManager.findFragmentByTag(tag);
        if (mFragment == null) {
            Bundle mBundle = new Bundle();
            mBundle.putString(TEXT_FRAGMENT, mListNameItem.get(position));
            mFragment = Fragment.instantiate(getApplicationContext(),
                    fragmentList.get(position).getName(), mBundle);
        }
        mFragmentManager.beginTransaction().replace(layoutContainerId, mFragment,
                mFragment.getClass().getName()).commit();
    }

    @Override
    public void onPrepareOptionsMenuNavigation(Menu menu, int position, boolean visible) {
//
//        //hide the menu when the navigation is opens
//        switch (position) {
//            case 0:
//                menu.findItem(R.id.menu_add).setVisible(!visible);
//                menu.findItem(R.id.menu_search).setVisible(!visible);
//                break;
//
//            case 1:
//                menu.findItem(R.id.menu_add).setVisible(!visible);
//                menu.findItem(R.id.menu_search).setVisible(!visible);
//                break;
//        }
    }

    @Override
    public void onClickUserPhotoNavigation(View v) {
        //user photo onClick
        Toast.makeText(this, R.string.open_user_profile, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClickFooterItemNavigation(View v) {
        //footer onClick
        startActivity(new Intent(this, SettingsActivity.class));
    }
}
