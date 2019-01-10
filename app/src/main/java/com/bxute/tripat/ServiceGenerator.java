/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat;

public class ServiceGenerator {
    public static Api getService() {
      return HaprampApiClient.getClient().create(Api.class);
    }
}
