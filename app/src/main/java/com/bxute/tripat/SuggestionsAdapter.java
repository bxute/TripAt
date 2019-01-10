/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class SuggestionsAdapter extends ArrayAdapter<PlaceSuggestion> {

  private Context context;
  private ArrayList<PlaceSuggestion> placeSuggestions;

  public SuggestionsAdapter(@NonNull Context context) {
    super(context, 0);
    this.context = context;
    placeSuggestions = new ArrayList<>();
  }

  public void setPlaceSuggestions(ArrayList<PlaceSuggestion> placeSuggestions) {
    this.placeSuggestions = placeSuggestions;
    notifyDataSetChanged();
  }

  @Override
  public int getCount() {
    return placeSuggestions.size();
  }

  @Nullable
  @Override
  public PlaceSuggestion getItem(int position) {
    return placeSuggestions.get(position);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    ViewHolder viewHolder;
    if (convertView == null) {
      convertView = LayoutInflater.from(context).inflate(R.layout.place_suggestion_item_view, null);
      viewHolder = new ViewHolder();
      viewHolder.mainText = convertView.findViewById(R.id.mainText);
      viewHolder.secondaryText = convertView.findViewById(R.id.secondaryText);
      convertView.setTag(viewHolder);
    } else {
      viewHolder = (ViewHolder) convertView.getTag();
    }
    PlaceSuggestion suggestion = placeSuggestions.get(position);
    viewHolder.mainText.setText(suggestion.getMainText());
    viewHolder.secondaryText.setText(suggestion.getSecondaryText());
    return convertView;
  }

  static class ViewHolder {
    private TextView mainText;
    private TextView secondaryText;
  }
}
