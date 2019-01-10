/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat;

public class PlaceSuggestion {
  private String mainText;
  private String secondaryText;

  public PlaceSuggestion(String mainText, String secondaryText) {
    this.mainText = mainText;
    this.secondaryText = secondaryText;
  }

  public PlaceSuggestion() {
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
