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
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.bxute.tripat.R;
import com.bxute.tripat.adapter.BucketItemListAdapter;
import com.bxute.tripat.firestore.FireStoreHelper;
import com.bxute.tripat.models.PlaceModel;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


public class InBucketFragment extends Fragment {

  @BindView(R.id.inBucketItems)
  ListView inBucketItems;
  Unbinder unbinder;
  @BindView(R.id.progress_bar)
  ProgressBar progressBar;
  private Context mContext;

  private BucketItemListAdapter bucketItemListAdapter;

  public InBucketFragment() {
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
    View view = inflater.inflate(R.layout.fragment_in_bucket, container, false);
    unbinder = ButterKnife.bind(this, view);
    bucketItemListAdapter = new BucketItemListAdapter(mContext);
    inBucketItems.setAdapter(bucketItemListAdapter);
    loadBucketItems();
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  @Override
  public void onDetach() {
    super.onDetach();
  }

  private void loadBucketItems() {
    FireStoreHelper.getBucketItemReferences().addSnapshotListener((queryDocumentSnapshots, e) -> {
      if (queryDocumentSnapshots != null) {
        List<DocumentSnapshot> itemsInBuckets = queryDocumentSnapshots.getDocuments();
        updateListItems(itemsInBuckets);
      }
    });
  }

  private void updateListItems(List<DocumentSnapshot> itemsInBuckets) {
    List<PlaceModel.Results> results = new ArrayList<>();
    for (int i = 0; i < itemsInBuckets.size(); i++) {
      PlaceModel.Results place = itemsInBuckets.get(i).toObject(PlaceModel.Results.class);
      results.add(place);
    }
    if (progressBar != null) {
      progressBar.setVisibility(View.GONE);
    }
    bucketItemListAdapter.addResults(results);
  }
}
