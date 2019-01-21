/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import com.bxute.tripat.models.PlaceModel;
import com.bxute.tripat.views.PlaceView;

import java.util.ArrayList;
import java.util.List;

public class BucketItemListAdapter extends ArrayAdapter<PlaceModel.Results> {
  private List<PlaceModel.Results> results;
  private Context mContext;

  public BucketItemListAdapter(@NonNull Context context) {
    super(context, 0);
    results = new ArrayList<>();
    this.mContext = context;
  }

  public void addResults(List<PlaceModel.Results> results) {
    this.results = results;
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return results.size();
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    ViewHolder viewHolder;
    if (convertView == null) {
      convertView = new PlaceView(mContext);
      viewHolder = new ViewHolder();
      viewHolder.placeView = (PlaceView) convertView;
      viewHolder.placeView.enableButtonBar(true);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    viewHolder.placeView.setPlaceData(results.get(position));
    return convertView;
  }

  static class ViewHolder {
    private PlaceView placeView;
  }
}
