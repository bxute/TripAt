/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.android.gms.maps.model.LatLng;

public class PreferenceHelper {
  private static final String PREF_NAME = "tripAtPref";
  private static SharedPreferences preferences;
  private static SharedPreferences.Editor editor;
  private static PreferenceHelper mInstance;

  public PreferenceHelper(Context context) {
    preferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
    editor = preferences.edit();
  }

  public static PreferenceHelper mInstance() {
    return mInstance;
  }

  public static PreferenceHelper getInstance(Context context) {
    if (mInstance == null) {
      mInstance = new PreferenceHelper(context);
    }
    return mInstance;
  }

  public void setLastLocation(double lastLatitude, double lastLongitude) {
    editor.putFloat("lastLat", (float) lastLatitude);
    editor.putFloat("lastLon", (float) lastLongitude);
    editor.apply();
  }

  public LatLng getLastLocation() {
    double lat = preferences.getFloat("lastLat", 0);
    double lon = preferences.getFloat("lastLon", 0);
    LatLng latLng = new LatLng(lat, lon);
    return latLng;
  }
}
