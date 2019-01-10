/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat;

import io.reactivex.Single;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface Api {
  String KEY = "AIzaSyBQ54GncyQj7ndCURGa7unyRLCESDhqWR4";
  // NearBy Search Request
  //https://maps.googleapis.com/maps/api/place/nearbysearch/json?
  // location=-33.8670522,151.1957362
  // &radius=1500
  // &type=restaurant
  // &keyword=cruise
  // &key=YOUR_API_KEY
///sessiontoken=1234567890
  @POST("autocomplete/json")
  Single<String> getPlaceSuggestions(@Query("key") String key,
                                     @Query("input") String input,
                                     @Query("sessiontoken") String session_token,
                                     @Query("radius") String radius);
}
