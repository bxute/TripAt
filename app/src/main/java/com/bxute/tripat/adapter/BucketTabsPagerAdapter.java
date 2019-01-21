/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.adapter;

import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.bxute.tripat.ui.InBucketFragment;
import com.bxute.tripat.ui.VisitedFragment;

public class BucketTabsPagerAdapter extends FragmentPagerAdapter {

  private InBucketFragment inBucketFragment;
  private VisitedFragment visitedFragment;

  public BucketTabsPagerAdapter(FragmentManager fm) {
    super(fm);
    visitedFragment = new VisitedFragment();
    inBucketFragment = new InBucketFragment();
  }

  @Override
  public int getCount() {
    return 2;
  }

  @Override
  public Fragment getItem(int position) {
    if(position==0){
      return inBucketFragment;
    }else{
      return visitedFragment;
    }
  }

  @Nullable
  @Override
  public CharSequence getPageTitle(int position) {
    if(position==0){
      return "IN BUCKET";
    }else{
      return "VISITED";
    }
  }
}
