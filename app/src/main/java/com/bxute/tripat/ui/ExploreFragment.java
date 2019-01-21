/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.ui;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bxute.tripat.api.Api;
import com.bxute.tripat.firestore.ItemsInVisited;
import com.bxute.tripat.utils.JSONHelper;
import com.bxute.tripat.utils.LocationTrack;
import com.bxute.tripat.utils.PixelUtils;
import com.bxute.tripat.models.PlaceGeometryObservable;
import com.bxute.tripat.models.PlaceSuggestion;
import com.bxute.tripat.models.PlaceTypes;
import com.bxute.tripat.views.PlaceView;
import com.bxute.tripat.utils.PreferenceHelper;
import com.bxute.tripat.R;
import com.bxute.tripat.api.ServiceGenerator;
import com.bxute.tripat.adapter.SuggestionsAdapter;
import com.bxute.tripat.utils.ViewUtils;
import com.bxute.tripat.firestore.ItemsInBucket;
import com.bxute.tripat.models.GeometryModel;
import com.bxute.tripat.models.PlaceModel;
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
import butterknife.Unbinder;
import io.reactivex.Single;
import io.reactivex.SingleObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Function;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


/**
 * A simple {@link Fragment} subclass.
 */
public class ExploreFragment extends Fragment implements OnMapReadyCallback {

  @BindView(R.id.place_icon)
  ImageView placeIcon;
  @BindView(R.id.place_search_input)
  EditText placeSearchInput;
  @BindView(R.id.current_location_icon)
  ImageView currentLocationIcon;
  @BindView(R.id.place_search_input_container)
  RelativeLayout placeSearchInputContainer;
  @BindView(R.id.distance_icon)
  ImageView distanceIcon;
  @BindView(R.id.min_range)
  TextView minRange;
  @BindView(R.id.radius_selector)
  SeekBar radiusSelector;
  @BindView(R.id.max_range)
  TextView maxRange;
  @BindView(R.id.place_suggestion_list)
  ListView placeSuggestionList;
  @BindView(R.id.tip_indicator)
  View tipIndicator;
  @BindView(R.id.places_within_text)
  TextView placesWithinText;
  @BindView(R.id.bottom_sheet_header)
  RelativeLayout bottomSheetHeader;
  @BindView(R.id.places_list_container)
  LinearLayout placesListContainer;
  @BindView(R.id.bottom_sheet_root)
  LinearLayout bottomSheetRoot;
  @BindView(R.id.shadow_layer)
  View shadowLayer;

  Unbinder unbinder;
  PublishSubject<String> publishSubject = PublishSubject.create();
  private Context mContext;
  private BottomSheetBehavior<LinearLayout> sheetBehavior;
  private GoogleMap mMap;
  private int REQUEST_ID_MULTIPLE_PERMISSIONS = 111;
  private int MAP_ZOOM = 17;
  private boolean isPermitted;
  private LocationTrack locationTrack;
  private Marker positionMarker;
  private BitmapDescriptor markerDescriptor;
  private Circle accuracyCircle;
  private boolean drawAccuracy = true;
  private SuggestionsAdapter suggestionsAdapter;
  private Handler mainHandler;
  private boolean observePlaceInputs = true;
  private LatLng userSearchLocation;
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
        userSearchLocation = deviceLastLatLon;
        PreferenceHelper.getInstance(mContext).setLastLocation(lat, lon);
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

  public ExploreFragment() {
    // Required empty public constructor
  }

  @Override
  public void onMapReady(GoogleMap googleMap) {
    try {
      mMap = googleMap;
      // mMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(mContext,R.raw.maps_style));
      if (locationTrack != null) {
        LatLng lastLocation = PreferenceHelper.getInstance(mContext).getLastLocation();
        deviceLastLatLon = lastLocation;
        userSearchLocation = lastLocation;
        bringFocus(lastLocation.latitude, lastLocation.longitude, 0, false);
        attachMapScrollListener();
        loadNearByPlaces();
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void bringFocus(double lat, double lon, double accuracy, boolean animate) {
    try {
      LatLng myLoc = new LatLng(lat, lon);
      addPositionMarker(lat, lon, accuracy, drawAccuracy);
      if (animate) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc, MAP_ZOOM));
      } else {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLoc, MAP_ZOOM));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void attachMapScrollListener() {
    mMap.setOnCameraMoveStartedListener(reasonCode -> {
      if (reasonCode == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
        setIsDeviceLocation(false);
      }
    });
  }

  private void loadNearByPlaces() {
    String location = userSearchLocation.latitude + "," + userSearchLocation.longitude;
    String radius = String.valueOf(radiusSelector.getProgress() * 1000);
    String type = PlaceTypes.MUSEUM;
    ServiceGenerator.getService().getNearbyPlaces(Api.KEY, location, radius, type)
     .subscribeOn(Schedulers.io())
     .observeOn(AndroidSchedulers.mainThread())
     .subscribeWith(new SingleObserver<PlaceModel>() {
       @Override
       public void onSubscribe(Disposable d) {

       }

       @Override
       public void onSuccess(PlaceModel placeModel) {
         addPlaceViews(placeModel);
       }

       @Override
       public void onError(Throwable e) {

       }
     });
  }

  private void addPositionMarker(double lat, double lon, double accuracy, boolean drawAccuracy) {
    try {
      if (positionMarker != null) {
        positionMarker.remove();
      } else {
        int height = PixelUtils.convertDpToPixel(24, mContext);
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
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setIsDeviceLocation(boolean isDeviceLocation) {
    if (isDeviceLocation) {
      currentLocationIcon.setImageResource(R.drawable.current_location_filled);
    } else {
      currentLocationIcon.setImageResource(R.drawable.current_location);
    }
  }

  private void addPlaceViews(PlaceModel place) {
    try {
      if (placesListContainer != null) {
        placesListContainer.removeAllViews();
      }
      for (int i = 0; i < place.getResults().size(); i++) {
        PlaceView placeView = new PlaceView(mContext);
        placeView.setPlaceData(place.getResults().get(i));
        placesListContainer.addView(placeView);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void hideKeyboard(Activity activity) {
    InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
    View view = activity.getCurrentFocus();
    if (view == null) {
      view = new View(activity);
    }
    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
  }

  @Override
  public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    if (requestCode == REQUEST_ID_MULTIPLE_PERMISSIONS) {
      for (int i = 0; i < grantResults.length; i++) {
        isPermitted = grantResults[i] == PackageManager.PERMISSION_GRANTED;
      }
      if (!isPermitted) {
        Toast.makeText(mContext, "Permission not granted!", Toast.LENGTH_SHORT).show();
      } else {
        initialize();
      }
    }
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
    initialzeLocationService();
  }

  private void initialzeLocationService() {
    if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
     && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      checkAndRequestPermissions();
      return;
    }
    locationTrack = new LocationTrack(mContext, mLocationListener);
  }

  public boolean checkAndRequestPermissions() {
    int location = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION);
    int course_location = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION);
    int networkState = ContextCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_NETWORK_STATE);

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
      ActivityCompat.requestPermissions(((AppCompatActivity) mContext), listPermissionsNeeded.toArray
       (new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
      return false;
    }
    return true;
  }

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    //sync bucketed place
    ItemsInBucket.syncBucketedPlace();
    ItemsInVisited.syncVisitedPlace();
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_explore, container, false);
    unbinder = ButterKnife.bind(this, view);
    if (checkAndRequestPermissions()) {
      initialize();
    }
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
    compositeDisposable.dispose();
    if (locationTrack != null) {
      locationTrack.stopListener();
    }
  }

  private void initialize() {
    initializeMaps();
    initializeBottomSheet();
    mainHandler = new Handler();
  }

  private void initializeMaps() {
    SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
     .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
  }

  private void updateBottomSheetHeaderBackground(float offset) {
    //for 0 offset cornerRadius = 8dp
    //for 1 offset cornerRadius = 0dp
    int _elevation = (int) ((1 - offset) * 4);
    int _cornerRadius = (int) ((1 - offset) * 8);
    int cornerRadius = PixelUtils.convertDpToPixel(_cornerRadius, mContext);
    Drawable backgroundDrawable = ViewUtils.generateBackgroundWithShadow(bottomSheetHeader,
     Color.parseColor("#ffffff"),
     cornerRadius,
     Color.parseColor("#e2e2e2"),
     _elevation,
     Gravity.TOP);
    bottomSheetHeader.setBackgroundDrawable(backgroundDrawable);
  }

  private void initializeBottomSheet() {
    updateBottomSheetHeaderBackground(0);
    sheetBehavior = BottomSheetBehavior.from(bottomSheetRoot);
    sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
      @Override
      public void onStateChanged(@NonNull View bottomSheet, int newState) {

      }

      @Override
      public void onSlide(@NonNull View bottomSheet, float slideOffset) {
        updateBottomSheetHeaderBackground(slideOffset);
      }
    });
    setListener();
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
        loadNearByPlaces();
      }
    });
    radiusSelector.setProgress(10);

    placeSuggestionList.setOnItemClickListener((adapterView, view, pos, l) -> {
      PlaceSuggestion placeSuggestion = (PlaceSuggestion) adapterView.getAdapter().getItem(pos);
      String mainText = placeSuggestion.getMainText();
      placeSearchInput.setText(mainText);
      hideKeyboard();
      observePlaceInputs = false;
      observePlaceGeometry(placeSuggestion.getPlaceId());
      showPlaceSuggestions(false, null);
    });
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
    try {
      LatLng myLoc = new LatLng(latLng.latitude, latLng.longitude);
      addPositionMarker(latLng.latitude, latLng.longitude, 0, false);
      mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLoc, MAP_ZOOM));
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void setPlacesWithinText(String text) {
    placesWithinText.setText(text);
  }

  private void hideKeyboard() {
    mainHandler.post(() -> {
      hideKeyboard((Activity) mContext);
    });
  }

  private void observePlaceGeometry(String placeId) {
    SingleObserver<GeometryModel> a = PlaceGeometryObservable
     .getPlaceGeometryObservable(placeId)
     .subscribeOn(Schedulers.io())
     .observeOn(AndroidSchedulers.mainThread())
     .subscribeWith(new SingleObserver<GeometryModel>() {
       @Override
       public void onSubscribe(Disposable d) {

       }

       @Override
       public void onSuccess(GeometryModel geometryModel) {
         LatLng location = geometryModel.getResult().getGeometry().getLocation().getLatLng();
         userSearchLocation = location;
         bringFocus(location);
         //load near by places
         loadNearByPlaces();
       }

       @Override
       public void onError(Throwable e) {

       }
     });
  }

  private void showPlaceSuggestions(boolean show, ArrayList<PlaceSuggestion> placeSuggestions) {
    if (suggestionsAdapter == null) {
      suggestionsAdapter = new SuggestionsAdapter(mContext);
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
}
