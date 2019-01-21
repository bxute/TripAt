/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bxute.tripat.R;
import com.bxute.tripat.ui.BaseActivity;
import com.bxute.tripat.ui.BucketFragment;
import com.bxute.tripat.ui.ExploreFragment;
import com.bxute.tripat.ui.UserFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Home extends BaseActivity {
  private static final int TAB_EXPLORE = 0;
  private static final int TAB_BUCKET = 1;
  private static final int TAB_USER = 2;
  @BindView(R.id.explore_tab)
  FrameLayout exploreTab;
  @BindView(R.id.bucket_tab)
  FrameLayout bucketTab;
  @BindView(R.id.user_tab)
  FrameLayout userTab;
  @BindView(R.id.bottom_bar_container)
  LinearLayout bottomBarContainer;
  @BindView(R.id.explore_tab_icon)
  ImageView exploreTabIcon;
  @BindView(R.id.bucket_tab_icon)
  ImageView bucketTabIcon;
  @BindView(R.id.user_tab_icon)
  ImageView userTabIcon;

  private ExploreFragment exploreFragment;
  private BucketFragment bucketFragment;
  private UserFragment userFragment;

  private int activeTab = -1;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_home);
    ButterKnife.bind(this);
    switchTab(TAB_EXPLORE);
    attachListeners();
  }

  private void switchTab(int newTab) {
    if (newTab != activeTab) {
      updateTabStates(newTab);
      switch (newTab) {
        case TAB_EXPLORE:
          if (exploreFragment == null) {
            exploreFragment = new ExploreFragment();
          }
          transactFragment(exploreFragment);
          break;
        case TAB_BUCKET:
          if (bucketFragment == null) {
            bucketFragment = new BucketFragment();
          }
          transactFragment(bucketFragment);
          break;
        case TAB_USER:
          if (userFragment == null) {
            userFragment = new UserFragment();
          }
          transactFragment(userFragment);
          break;
      }
      activeTab = newTab;
    }
  }

  private void attachListeners() {
    exploreTab.setOnClickListener(view -> switchTab(TAB_EXPLORE));
    bucketTab.setOnClickListener(view -> switchTab(TAB_BUCKET));
    userTab.setOnClickListener(view -> switchTab(TAB_USER));
  }

  private void updateTabStates(int newTab) {
    if (activeTab == newTab)
      return;
    switch (newTab) {
      case TAB_EXPLORE:
        exploreTabIcon.setSelected(true);
        bucketTabIcon.setSelected(false);
        userTabIcon.setSelected(false);
        break;
      case TAB_BUCKET:
        exploreTabIcon.setSelected(false);
        bucketTabIcon.setSelected(true);
        userTabIcon.setSelected(false);
        break;
      case TAB_USER:
        exploreTabIcon.setSelected(false);
        bucketTabIcon.setSelected(false);
        userTabIcon.setSelected(true);
    }
  }

  private void transactFragment(Fragment fragment) {
    FragmentManager fragmentManager = getSupportFragmentManager();
    fragmentManager
     .beginTransaction()
     .replace(R.id.fragment_container, fragment, fragment.getTag())
     .commit();
  }
}
