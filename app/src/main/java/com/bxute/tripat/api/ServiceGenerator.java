/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.api;

import com.bxute.tripat.api.Api;
import com.bxute.tripat.api.HaprampApiClient;

public class ServiceGenerator {
    public static Api getService() {
      return HaprampApiClient.getClient().create(Api.class);
    }
}
