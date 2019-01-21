/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.models;

import com.bxute.tripat.api.ServiceGenerator;
import com.bxute.tripat.api.Api;

import io.reactivex.Single;

public class PlaceGeometryObservable {
  public static Single<GeometryModel> getPlaceGeometryObservable(String placeId) {
    String key = Api.KEY;
    String fields = "geometry";
    return ServiceGenerator.getService().getGeometry(key, fields, placeId);
  }
}
