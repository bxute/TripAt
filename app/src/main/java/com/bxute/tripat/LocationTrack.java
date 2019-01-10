/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.widget.Toast;

public class LocationTrack extends Service {
  private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
  private static final long MIN_TIME_BW_UPDATES = 1;
  private final Context mContext;
  protected LocationManager locationManager;
  boolean gpsProviderEnabled = false;
  boolean networkProviderEnabled = false;
  boolean canGetLocation = false;
  Location loc;
  double latitude;
  double longitude;
  LocationListener mLocationListener;

  public LocationTrack(Context mContext, LocationListener locationListener) {
    this.mContext = mContext;
    this.mLocationListener = locationListener;
    getLocation();
  }

  private Location getLocation() {
    try {
      locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
      gpsProviderEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
      networkProviderEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

      if (!gpsProviderEnabled && !networkProviderEnabled) {
        Toast.makeText(mContext, "No Service Provider is available", Toast.LENGTH_SHORT).show();
      } else {
        this.canGetLocation = true;
        if (gpsProviderEnabled) {
          if (ActivityCompat.checkSelfPermission(mContext,
           Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
           && ActivityCompat.checkSelfPermission(mContext,
           Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return null;
          }
          locationManager.requestLocationUpdates(
           LocationManager.GPS_PROVIDER,
           MIN_TIME_BW_UPDATES,
           MIN_DISTANCE_CHANGE_FOR_UPDATES,
           mLocationListener);

          if (locationManager != null) {
            loc = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (loc != null) {
              latitude = loc.getLatitude();
              longitude = loc.getLongitude();
            }
          }
        } else {
          showSettingsAlert();
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return loc;
  }

  public void showSettingsAlert() {
    AlertDialog.Builder alertDialog = new AlertDialog.Builder(mContext);
    alertDialog.setTitle("GPS is not Enabled!");
    alertDialog.setMessage("Do you want to turn on GPS?");
    alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        mContext.startActivity(intent);
      }
    });
    alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
      public void onClick(DialogInterface dialog, int which) {
        dialog.cancel();
      }
    });
    alertDialog.show();
  }

  public void stopListener() {
    if (locationManager != null) {
      if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
        return;
      }
      locationManager.removeUpdates(mLocationListener);
    }
  }

  @Override
  public IBinder onBind(Intent intent) {
    return null;
  }
}

