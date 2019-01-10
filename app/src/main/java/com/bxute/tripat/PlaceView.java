/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlaceView extends FrameLayout {
  @BindView(R.id.place_image)
  ImageView placeImage;
  @BindView(R.id.place_title)
  TextView placeTitle;
  @BindView(R.id.place_distance)
  TextView placeDistance;
  @BindView(R.id.place_tags)
  TextView placeTags;

  private PlaceModel place;
  private Context context;

  public PlaceView(@NonNull Context context) {
    this(context, null);
  }

  public PlaceView(@NonNull Context context, @Nullable AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public PlaceView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context);
  }

  private void init(Context context) {
    this.context = context;
    View view = LayoutInflater.from(context).inflate(R.layout.place_view, this);
    ButterKnife.bind(this, view);
  }

  public void setPlaceData(PlaceModel place) {
    this.place = place;
    if (place != null) {
      bindData();
    }
  }

  private void bindData() {
    ImageHandler.loadRoundedImage(context, placeImage, this.place.getPlaceImageUrl());
    placeTitle.setText(this.place.getPlaceTitle());
    placeDistance.setText(this.place.getPlaceDistance());
  }
}
