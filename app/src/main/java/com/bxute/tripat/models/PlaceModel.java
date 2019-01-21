/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.models;

import com.bxute.tripat.api.Api;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PlaceModel {

  public List<Results> getResults() {
    return results;
  }

  @Expose
  @SerializedName("results")
  private List<Results> results;

  public static class Location {
    @Expose
    @SerializedName("lng")
    private double lng;
    @Expose
    @SerializedName("lat")
    private double lat;

    public double getLng() {
      return lng;
    }

    public double getLat() {
      return lat;
    }
  }

  public static class Geometry {
    @Expose
    @SerializedName("location")
    private Location location;

    public Location getLocation() {
      return location;
    }
  }

  public static class Photos {
    @Expose
    @SerializedName("width")
    private int width;
    @Expose
    @SerializedName("photo_reference")
    private String photo_reference;
    @Expose
    @SerializedName("height")
    private int height;

    public int getWidth() {
      return width;
    }

    public String getPhotoUrl() {
      return "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference="+photo_reference+"&key="+ Api.KEY;
    }

    public void setWidth(int width) {
      this.width = width;
    }

    public String getPhoto_reference() {
      return photo_reference;
    }

    public void setPhoto_reference(String photo_reference) {
      this.photo_reference = photo_reference;
    }

    public void setHeight(int height) {
      this.height = height;
    }

    public int getHeight() {
      return height;
    }
  }

  public static class Results {
    @Expose
    @SerializedName("vicinity")
    private String vicinity;
    @Expose
    @SerializedName("place_id")
    private String place_id;
    @Expose
    @SerializedName("photos")
    private List<Photos> photos;
    @Expose
    @SerializedName("name")
    private String name;
    @Expose
    @SerializedName("geometry")
    private Geometry geometry;

    public String getVicinity() {
      return vicinity;
    }

    public String getPlace_id() {
      return place_id;
    }

    public List<Photos> getPhotos() {
      return photos;
    }

    public String getName() {
      return name;
    }

    public Geometry getGeometry() {
      return geometry;
    }
  }
}
