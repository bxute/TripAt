/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.api;

import com.bxute.tripat.models.GeometryModel;
import com.bxute.tripat.models.PlaceModel;

import io.reactivex.Single;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface Api {
  String KEY = "AIzaSyBQ54GncyQj7ndCURGa7unyRLCESDhqWR4";
  @POST("autocomplete/json")
  Single<String> getPlaceSuggestions(@Query("key") String key,
                                     @Query("input") String input,
                                     @Query("sessiontoken") String session_token,
                                     @Query("radius") String radius);

  @POST("details/json")
  Single<GeometryModel> getGeometry(@Query("key") String key,
                                    @Query("fields") String fields,
                                    @Query("placeid") String place_id);

  @POST("nearbysearch/json")
  Single<PlaceModel> getNearbyPlaces(@Query("key") String key,
                                     @Query("location") String location,
                                     @Query("radius") String radiusInMeter,
                                     @Query("type") String type);
}
