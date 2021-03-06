/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.utils;

import com.bxute.tripat.models.PlaceSuggestion;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class JSONHelper {
  public static ArrayList<PlaceSuggestion> parsePlaceSuggestions(String json) {
    ArrayList<PlaceSuggestion> placeSuggestions = new ArrayList<>();
    try {
      JSONObject jsonObject = new JSONObject(json);
      JSONArray predictionsArray = jsonObject.getJSONArray("predictions");
      for (int i=0;i<predictionsArray.length();i++){
        JSONObject predictedItem = predictionsArray.getJSONObject(i);

        PlaceSuggestion placeSuggestion = new PlaceSuggestion();
        JSONObject structuredFormatting = predictedItem.getJSONObject("structured_formatting");

        placeSuggestion.setPlaceId(predictedItem.optString("place_id",""));
        placeSuggestion.setMainText(structuredFormatting.optString("main_text","NA"));
        placeSuggestion.setSecondaryText(structuredFormatting.optString("secondary_text",""));
        placeSuggestions.add(placeSuggestion);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    return placeSuggestions;
  }
}
