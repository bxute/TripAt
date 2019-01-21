/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.models;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;

import io.reactivex.Observable;

public class PlaceDetailsObservable {

  public static Observable<LatLng> getPlaceCordinatesObservable(String location, Context context) {
    return Observable.create(emitter -> {
      LatLng latLng = getPlaceCordinates(location, context);
      if (latLng != null) {
        emitter.onNext(latLng);
      } else {
        emitter.onError(new Throwable("NA"));
      }
      emitter.onComplete();
    });
  }

  private static LatLng getPlaceCordinates(String location, Context context) {
    if (Geocoder.isPresent()) {
      try {
        Geocoder gc = new Geocoder(context);
        List<Address> addresses = gc.getFromLocationName(location, 1);
        for (Address a : addresses) {
          if (a.hasLatitude() && a.hasLongitude()) {
            LatLng latLng = new LatLng(a.getLatitude(), a.getLongitude());
            return latLng;
          }
        }
      }
      catch (IOException e) {
        e.printStackTrace();
        return null;
      }
    }
    return null;
  }
}
