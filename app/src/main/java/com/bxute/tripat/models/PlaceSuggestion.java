/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.models;

public class PlaceSuggestion {
  private String mainText;
  private String secondaryText;
  private String placeId;

  public PlaceSuggestion() {
  }

  public String getPlaceId() {
    return placeId;
  }

  public void setPlaceId(String placeId) {
    this.placeId = placeId;
  }

  public String getMainText() {
    return mainText;
  }

  public void setMainText(String mainText) {
    this.mainText = mainText;
  }

  public String getSecondaryText() {
    return secondaryText;
  }

  public void setSecondaryText(String secondaryText) {
    this.secondaryText = secondaryText;
  }

  @Override
  public String toString() {
    return "PlaceSuggestion{" +
     "mainText='" + mainText + '\'' +
     ", secondaryText='" + secondaryText + '\'' +
     '}';
  }
}
