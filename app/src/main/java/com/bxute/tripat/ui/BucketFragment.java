/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bxute.tripat.adapter.BucketTabsPagerAdapter;
import com.bxute.tripat.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class BucketFragment extends Fragment {
  @BindView(R.id.bucket_tabhost)
  TabLayout bucketTabhost;
  Unbinder unbinder;
  @BindView(R.id.viewPager)
  ViewPager viewPager;
  private Context mContext;

  public BucketFragment() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
  }

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.fragment_bucket, container, false);
    unbinder = ButterKnife.bind(this, view);
    addTabs();
    return view;
  }

  private void addTabs() {
    BucketTabsPagerAdapter adapter = new BucketTabsPagerAdapter(getChildFragmentManager());
    viewPager.setAdapter(adapter);
    bucketTabhost.setupWithViewPager(viewPager);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }
}
