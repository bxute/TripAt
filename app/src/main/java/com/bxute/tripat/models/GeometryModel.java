/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeometryModel {

  @Expose
  @SerializedName("result")
  public Result result;

  public static class Result {
    @Expose
    @SerializedName("geometry")
    public Geometry geometry;

    public Geometry getGeometry() {
      return geometry;
    }

    public void setGeometry(Geometry geometry) {
      this.geometry = geometry;
    }
  }

  public static class Geometry {
    @Expose
    @SerializedName("viewport")
    public Viewport viewport;
    @Expose
    @SerializedName("location")
    public Location location;

    public Viewport getViewport() {
      return viewport;
    }

    public void setViewport(Viewport viewport) {
      this.viewport = viewport;
    }

    public Location getLocation() {
      return location;
    }

    public void setLocation(Location location) {
      this.location = location;
    }
  }

  public static class Viewport {
    @Expose
    @SerializedName("southwest")
    public Southwest southwest;
    @Expose
    @SerializedName("northeast")
    public Northeast northeast;

    public Southwest getSouthwest() {
      return southwest;
    }

    public void setSouthwest(Southwest southwest) {
      this.southwest = southwest;
    }

    public Northeast getNortheast() {
      return northeast;
    }

    public void setNortheast(Northeast northeast) {
      this.northeast = northeast;
    }
  }

  public static class Southwest {
    @Expose
    @SerializedName("lng")
    public double lng;
    @Expose
    @SerializedName("lat")
    public double lat;

    public double getLng() {
      return lng;
    }

    public void setLng(double lng) {
      this.lng = lng;
    }

    public double getLat() {
      return lat;
    }

    public void setLat(double lat) {
      this.lat = lat;
    }
  }

  public static class Northeast {
    @Expose
    @SerializedName("lng")
    public double lng;
    @Expose
    @SerializedName("lat")
    public double lat;

    public double getLng() {
      return lng;
    }

    public void setLng(double lng) {
      this.lng = lng;
    }

    public double getLat() {
      return lat;
    }

    public void setLat(double lat) {
      this.lat = lat;
    }
  }

  public static class Location {
    @Expose
    @SerializedName("lng")
    public double lng;
    @Expose
    @SerializedName("lat")
    public double lat;

    public double getLng() {
      return lng;
    }

    public void setLng(double lng) {
      this.lng = lng;
    }

    public double getLat() {
      return lat;
    }

    public void setLat(double lat) {
      this.lat = lat;
    }

    public LatLng getLatLng(){
      return new LatLng(lat,lng);
    }
  }

  public Result getResult() {
    return result;
  }

  public void setResult(Result result) {
    this.result = result;
  }
}
