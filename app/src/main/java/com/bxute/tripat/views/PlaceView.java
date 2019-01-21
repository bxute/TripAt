/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.views;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bxute.tripat.R;
import com.bxute.tripat.firestore.FireStoreHelper;
import com.bxute.tripat.firestore.ItemsInBucket;
import com.bxute.tripat.firestore.ItemsInVisited;
import com.bxute.tripat.models.PlaceModel;
import com.bxute.tripat.utils.ImageHandler;

import java.util.List;

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
  @BindView(R.id.bucket_button)
  ImageView bucketButton;
  @BindView(R.id.info_container)
  RelativeLayout infoContainer;
  @BindView(R.id.removeFromBucketButton)
  TextView removeFromBucketButton;
  @BindView(R.id.markVisitedButton)
  TextView markVisitedButton;
  @BindView(R.id.button_container)
  LinearLayout buttonContainer;

  private PlaceModel.Results place;
  private Context context;
  private boolean isInBucket;
  private boolean isInVisited;

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

  public void setPlaceData(PlaceModel.Results place) {
    this.place = place;
    if (place != null) {
      bindData();
    }
  }

  private void bindData() {
    List<PlaceModel.Photos> photosList = place.getPhotos();
    if (photosList != null)
      if (photosList.size() > 0) {
        ImageHandler.loadRoundedImage(context, placeImage, photosList.get(0).getPhotoUrl());
      }
    placeTitle.setText(this.place.getName());
    placeDistance.setText(this.place.getVicinity());
    invalidatePlaceRelations();
  }

  private void invalidatePlaceRelations() {
    isInBucket = ItemsInBucket.contains(place.getPlace_id());
    isInVisited = ItemsInVisited.contains(place.getPlace_id());
    if (isInVisited) {
      bucketButton.setOnClickListener(null);
      bucketButton.setImageResource(R.drawable.tick);
    } else {
      bucketButton.setOnClickListener(view1 -> {
        if (isInBucket) {
          FireStoreHelper.removeFromBucket(place.getPlace_id());
          setBucketState(false);
        } else {
          FireStoreHelper.addItemToBucket(place);
          setBucketState(true);
        }
      });
      setBucketState(isInBucket);
    }
  }

  private void setBucketState(boolean isInBucket) {
    int bucketIcon = isInBucket ? R.drawable.bucket_filled : R.drawable.bucket;
    bucketButton.setImageResource(bucketIcon);
  }

  public void enableButtonBar(boolean enable) {
    if (enable) {
      bucketButton.setVisibility(GONE);
      buttonContainer.setVisibility(VISIBLE);
      removeFromBucketButton.setOnClickListener(view -> {
        FireStoreHelper.removeFromBucket(place.getPlace_id());
      });

      markVisitedButton.setOnClickListener(view -> {
        FireStoreHelper.addItemToVisited(place);
        FireStoreHelper.removeFromBucket(place.getPlace_id());
      });
    } else {
      bucketButton.setVisibility(VISIBLE);
      buttonContainer.setVisibility(GONE);
    }
  }
}
