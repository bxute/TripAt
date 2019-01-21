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
import com.bxute.tripat.adapter.VisitedItemListAdapter;
import com.bxute.tripat.firestore.FireStoreHelper;
import com.bxute.tripat.models.PlaceModel;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class VisitedFragment extends Fragment {


  @BindView(R.id.inBucketItems)
  ListView visitedItems;
  @BindView(R.id.progress_bar)
  ProgressBar progressBar;
  Unbinder unbinder;
  private Context mContext;
  private VisitedItemListAdapter visitedItemListAdapter;

  public VisitedFragment() {
    // Required empty public constructor
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_visited, container, false);
    unbinder = ButterKnife.bind(this, view);
    visitedItemListAdapter = new VisitedItemListAdapter(mContext);
    visitedItems.setAdapter(visitedItemListAdapter);
    loadVisitedItems();
    return view;
  }

  private void loadVisitedItems() {
    FireStoreHelper.getVisitedItemReferences().addSnapshotListener((queryDocumentSnapshots, e) -> {
      if (queryDocumentSnapshots != null) {
        List<DocumentSnapshot> visitedItems = queryDocumentSnapshots.getDocuments();
        updateListItems(visitedItems);
      }
    });
  }

  private void updateListItems(List<DocumentSnapshot> visitedItems) {
    List<PlaceModel.Results> results = new ArrayList<>();
    for (int i = 0; i < visitedItems.size(); i++) {
      PlaceModel.Results place = visitedItems.get(i).toObject(PlaceModel.Results.class);
      results.add(place);
    }
    if (progressBar != null) {
      progressBar.setVisibility(View.GONE);
    }
    visitedItemListAdapter.addResults(results);
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }
}
