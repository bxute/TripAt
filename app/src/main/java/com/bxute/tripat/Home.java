/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.jakewharton.rxbinding2.widget.RxTextView;
import com.jakewharton.rxbinding2.widget.TextViewTextChangeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class Home extends BaseActivity implements OnMapReadyCallback {
  @BindView(R.id.bottom_sheet_root)
  LinearLayout bottomSheetRoot;
  @BindView(R.id.tip_indicator)
  View tipIndicator;
  @BindView(R.id.places_list_container)
  LinearLayout placesListContainer;
  @BindView(R.id.place_icon)
  ImageView placeIcon;
  @BindView(R.id.place_search_input)
  EditText placeSearchInput;
  @BindView(R.id.distance_icon)
  ImageView distanceIcon;
  @BindView(R.id.min_range)
  TextView minRange;
  @BindView(R.id.radius_selector)
  SeekBar radiusSelector;
  @BindView(R.id.max_range)
  TextView maxRange;
  @BindView(R.id.bottom_bar_container)
  RelativeLayout bottomBarContainer;
  @BindView(R.id.places_within_text)
  TextView placesWithinText;

  PublishSubject<String> publishSubject = PublishSubject.create();
  @BindView(R.id.place_search_input_container)
  RelativeLayout placeSearchInputContainer;
  @BindView(R.id.place_suggestion_list)
  ListView placeSuggestionList;
  @BindView(R.id.current_location_icon)
  ImageView currentLocationIcon;
  private BottomSheetBehavior<LinearLayout> sheetBehavior;
  private GoogleMap mMap;
  private int REQUEST_ID_MULTIPLE_PERMISSIONS = 111;
  private boolean isPermitted;
  private LocationTrack locationTrack;
  private Marker positionMarker;
  private BitmapDescriptor markerDescriptor;
  private Circle accuracyCircle;
  private boolean drawAccuracy = true;
  private SuggestionsAdapter suggestionsAdapter;
  private Handler mainHandler;
  private boolean observePlaceInputs = true;
  private LatLng lastLatLng;
  private LatLng deviceLastLatLon;
  private boolean activelyReactToDeviceLocation = true;

  private CompositeDisposable compositeDisposable = new CompositeDisposable();
  private LocationListener mLocationListener = new LocationListener() {
    @Override
    public void onLocationChanged(Location location) {
      double lat = location.getLatitude();
      double lon = location.getLongitude();
      double accuracy = location.getAccuracy();
      if (activelyReactToDeviceLocation) {
        deviceLastLatLon = new LatLng(lat, lon);
        PreferenceHelper.getInstance(Home.this).setLastLocation(lat, lon);
        bringFocus(lat, lon, accuracy, true);
      }
    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }
  };

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    Log.d("MSessionID", Mains.getSessionId() + "");
    setContentView(R.layout.activity_home);
    ButterKnife.bind(this);
    if (checkAndRequestPermissions()) {
      initialize();
    }
  }

  public boolean checkAndRequestPermissions() {
    int location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
    int course_location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
    int networkState = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_NETWORK_STATE);

    List<String> listPermissionsNeeded = new ArrayList<>();

    if (course_location != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
    }
    if (location != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
    }
    if (networkState != PackageManager.PERMISSION_GRANTED) {
      listPermissionsNeeded.add(Manifest.permission.ACCESS_NETWORK_STATE);
    }
    if (!listPermissionsNeeded.isEmpty()) {
      ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray
       (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
      return false;
    }
    return true;
  }

  private void initialize() {
    initializeMaps();
    initializeBottomSheet();
    loadTestData();
    mainHandler = new Handler();
  }

  private void initializeMaps() {
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
     .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
  }

  private void initializeBottomSheet() {
    sheetBehavior = BottomSheetBehavior.from(bottomSheetRoot);
    setListener();
  }

  private void loadTestData() {
    ArrayList<PlaceModel> placeModels = new ArrayList<>();
    PlaceModel placeModel = new PlaceModel();
    placeModel.setPlaceDistance("10 KM Away");
    placeModel.setPlaceTitle("Delhi");
    placeModel.setPlaceImageUrl("http://www.transindiatravels.com/wp-content/uploads/india-gate-delhi-india.jpg");
    for (int i = 0; i < 4; i++) {
      placeModels.add(placeModel);
    }
    addPlaceViews(placeModels);
  }

  private void setListener() {
    DisposableObserver<String> placeSuggestionsObsever = getPlaceSuggestionObserver();
    compositeDisposable.add(RxTextView
     .textChangeEvents(placeSearchInput)
     .skipInitialValue()
     .debounce(300, TimeUnit.MILLISECONDS)
     .subscribeOn(Schedulers.io())
     .observeOn(Schedulers.io())
     .subscribeWith(getPlaceInputObserver()));

    compositeDisposable.add(
     publishSubject
      .distinctUntilChanged()
      .switchMapSingle((Function<String, Single<String>>) s ->
       ServiceGenerator.getService().getPlaceSuggestions(Api.KEY, s, Mains.getSessionId(), getRadius()))
      .subscribeOn(Schedulers.io())
      .observeOn(AndroidSchedulers.mainThread())
      .subscribeWith(placeSuggestionsObsever));

    compositeDisposable.add(placeSuggestionsObsever);
    placeSearchInput.setOnClickListener(view -> observePlaceInputs = true);

    currentLocationIcon.setOnClickListener(view -> {
      bringFocus(deviceLastLatLon);
      setIsDeviceLocation(true);
    });

    radiusSelector.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
      @Override
      public void onProgressChanged(SeekBar seekBar, int progress, boolean byUser) {
        setPlacesWithinText(String.format("Places withing %d Km", progress));
      }

      @Override
      public void onStartTrackingTouch(SeekBar seekBar) {

      }

      @Override
      public void onStopTrackingTouch(SeekBar seekBar) {

      }
    });
    radiusSelector.setProgress(10);

    placeSuggestionList.setOnItemClickListener((adapterView, view, pos, l) -> {
      PlaceSuggestion placeSuggestion = (PlaceSuggestion) adapterView.getAdapter().getItem(pos);
      String mainText = placeSuggestion.getMainText();
      String secondaryText = placeSuggestion.getSecondaryText();
      placeSearchInput.setText(mainText);
      hideKeyboard();
      observePlaceInputs = false;
      observePlaceCordinates(secondaryText, Home.this);
      showPlaceSuggestions(false, null);
    });

    sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
      @Override
      public void onStateChanged(@NonNull View bottomSheet, int newState) {
        switch (newState) {
          case BottomSheetBehavior.STATE_HIDDEN:
            break;
          case BottomSheetBehavior.STATE_EXPANDED:
            break;
          case BottomSheetBehavior.STATE_COLLAPSED:
            break;
          case BottomSheetBehavior.STATE_DRAGGING:
            break;
          case BottomSheetBehavior.STATE_SETTLING:
            break;
        }
      }

      @Override
      public void onSlide(@NonNull View bottomSheet, float slideOffset) {

      }
    });
  }

  private void addPlaceViews(ArrayList<PlaceModel> places) {
    if (placesListContainer != null) {
      placesListContainer.removeAllViews();
    }
    for (int i = 0; i < places.size(); i++) {
      PlaceView placeView = new PlaceView(this);
      placeView.setPlaceData(places.get(i));
      placesListContainer.addView(placeView);
    }
  }

  private DisposableObserver<String> getPlaceSuggestionObserver() {
    return new DisposableObserver<String>() {
      @Override
      public void onNext(String response) {
        handlePlaceSuggestions(response);
      }

      @Override
      public void onError(Throwable e) {
        e.printStackTrace();
      }

      @Override
      public void onComplete() {

      }
    };
  }

  private DisposableObserver<TextViewTextChangeEvent> getPlaceInputObserver() {
    return new DisposableObserver<TextViewTextChangeEvent>() {
      @Override
      public void onNext(TextViewTextChangeEvent textViewTextChangeEvent) {
        String searchTerm = textViewTextChangeEvent.text().toString().toLowerCase();
        if (searchTerm.length() > 0) {
          //check connection
          if (observePlaceInputs) {
            publishSubject.onNext(searchTerm);
          }
        } else {
          //hide keyboard
          hideKeyboard();
          showPlaceSuggestions(false, null);
        }
      }

      @Override
      public void onError(Throwable e) {

      }

      @Override
      public void onComplete() {

      }
    };
  }

  private String getRadius() {
    return String.format(Locale.US, "%d", radiusSelector.getProgress());
  }

  private void bringFocus(LatLng latLng) {
    LatLng myLoc = new LatLng(latLng.latitude, latLng.longitude);
    addPositionMarker(latLng.latitude, latLng.longitude, 0, false);
    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 16));
  }

  private void setIsDeviceLocation(boolean isDeviceLocation) {
    if (isDeviceLocation) {
      currentLocationIcon.setImageResource(R.drawable.live_location);
    } else {
      currentLocationIcon.setImageResource(R.drawable.other_location);
    }
  }

  private void setPlacesWithinText(String text) {
    placesWithinText.setText(text);
  }

  private void hideKeyboard() {
    mainHandler.post(() -> {
      getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
      placeSearchInput.clearFocus();
    });
  }

  private void observePlaceCordinates(String location, Context context) {
    Observer<LatLng> observer = PlaceDetailsObservable
     .getPlaceCordinatesObservable(location, context)
     .subscribeOn(Schedulers.io())
     .observeOn(AndroidSchedulers.mainThread())
     .subscribeWith(new Observer<LatLng>() {
       @Override
       public void onSubscribe(Disposable d) {

       }

       @Override
       public void onNext(LatLng latLng) {
         setActivelyReactToDeviceLocation(false);
         lastLatLng = latLng;
         bringFocus(lastLatLng);
       }

       @Override
       public void onError(Throwable e) {

       }

       @Override
       public void onComplete() {

       }
     });
  }

  private void showPlaceSuggestions(boolean show, ArrayList<PlaceSuggestion> placeSuggestions) {
    if (suggestionsAdapter == null) {
      suggestionsAdapter = new SuggestionsAdapter(this);
    }
    mainHandler.post(() -> {
      if (placeSuggestionList != null) {
        if (show) {
          placeSuggestionList.setVisibility(View.VISIBLE);
          suggestionsAdapter.setPlaceSuggestions(placeSuggestions);
          placeSuggestionList.setAdapter(suggestionsAdapter);
        } else {
          placeSuggestionList.setVisibility(View.GONE);
        }
      }
    });
  }

  private void handlePlaceSuggestions(String response) {
    ArrayList<PlaceSuggestion> placeSuggestions = JSONHelper.parsePlaceSuggestions(response);
    showPlaceSuggestions(true, placeSuggestions);
  }

  private void addPositionMarker(double lat, double lon, double accuracy, boolean drawAccuracy) {
    if (positionMarker != null) {
      positionMarker.remove();
    } else {
      int height = PixelUtils.convertDpToPixel(24, this);
      int width = height;
      BitmapDrawable bitmapdraw = (BitmapDrawable) getResources().getDrawable(R.drawable.pin);
      Bitmap b = bitmapdraw.getBitmap();
      Bitmap smallMarker = Bitmap.createScaledBitmap(b, width, height, false);
      markerDescriptor = BitmapDescriptorFactory.fromBitmap(smallMarker);
    }
    final MarkerOptions positionMarkerOptions = new MarkerOptions()
     .position(new LatLng(lat, lon))
     .icon(markerDescriptor)
     .zIndex(3)
     .anchor(0.5f, 1f);
    positionMarker = mMap.addMarker(positionMarkerOptions);
    if (accuracyCircle != null) {
      accuracyCircle.remove();
    }
    int accuracyFillColor = Color.parseColor("#1407bdbd");
    int accuracyStrokeColor = Color.parseColor("#8d07bdbd");
    if (drawAccuracy) {
      final CircleOptions accuracyCircleOptions = new CircleOptions()
       .center(new LatLng(lat, lon))
       .radius(accuracy)
       .fillColor(accuracyFillColor)
       .strokeColor(accuracyStrokeColor)
       .strokeWidth(2.0f);
      accuracyCircle = mMap.addCircle(accuracyCircleOptions);
    }
  }

  private void setActivelyReactToDeviceLocation(boolean activelyReactToDeviceLocation) {
    this.activelyReactToDeviceLocation = activelyReactToDeviceLocation;
  }

  private String getSearchLocationCordinates() {
    String sl = deviceLastLatLon.latitude + "," + deviceLastLatLon.longitude;
    return sl;
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    compositeDisposable.dispose();
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    mMap = googleMap;
    if (locationTrack != null) {
      LatLng lastLocation = PreferenceHelper.getInstance(this).getLastLocation();
      deviceLastLatLon = lastLocation;
      bringFocus(lastLocation.latitude, lastLocation.longitude, 0, false);
      attachMapScrollListener();
    }
  }

  private void bringFocus(double lat, double lon, double accuracy, boolean animate) {
    LatLng myLoc = new LatLng(lat, lon);
    addPositionMarker(lat, lon, accuracy, drawAccuracy);
    if (animate) {
      mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 16));
    } else {
      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, 16));
    }
  }

  private void attachMapScrollListener() {
    mMap.setOnCameraMoveStartedListener(reasonCode -> {
      if (reasonCode == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
        setIsDeviceLocation(false);
      }
    });
  }

  @Override
  protected void onPause() {
    super.onPause();
    if (locationTrack != null) {
      locationTrack.stopListener();
    }
  }

  @Override
  protected void onResume() {
    super.onResume();
    initialzeLocationService();
  }

  private void initialzeLocationService() {
    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
     && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      checkAndRequestPermissions();
      return;
    }
    locationTrack = new LocationTrack(this, mLocationListener);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
      for (int i = 0; i < grantResults.length; i++) {
        isPermitted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
      }
      if (!isPermitted) {
        toast("Permission not granted.");
      } else {
        initialize();
      }
    }
  }
}
