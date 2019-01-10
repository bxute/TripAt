/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat;

public class PlaceModel {
  private String placeImageUrl;
  private String placeTitle;
  private String placeDistance;
  private String[] tags;

  public PlaceModel() {
  }

  public PlaceModel(String placeImageUrl, String placeTitle, String placeDistance, String[] tags) {
    this.placeImageUrl = placeImageUrl;
    this.placeTitle = placeTitle;
    this.placeDistance = placeDistance;
    this.tags = tags;
  }

  public String getPlaceImageUrl() {
    return placeImageUrl;
  }

  public void setPlaceImageUrl(String placeImageUrl) {
    this.placeImageUrl = placeImageUrl;
  }

  public String getPlaceTitle() {
    return placeTitle;
  }

  public void setPlaceTitle(String placeTitle) {
    this.placeTitle = placeTitle;
  }

  public String getPlaceDistance() {
    return placeDistance;
  }

  public void setPlaceDistance(String placeDistance) {
    this.placeDistance = placeDistance;
  }

  public String[] getTags() {
    return tags;
  }

  public void setTags(String[] tags) {
    this.tags = tags;
  }
}
