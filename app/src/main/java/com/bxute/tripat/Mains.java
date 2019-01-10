/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat;

import android.app.Application;

public class Mains extends Application {
  private static String seesionId;

  public static String getSessionId() {
    return seesionId;
  }

  @Override
  public void onCreate() {
    seesionId = String.valueOf(System.currentTimeMillis());
    super.onCreate();
  }
  /*
  * UPCOMMING:
  *  - search places depending on search location and radius
  *  - update bottom sheet
  *  - hide bottom sheet whenever applicable to enhance experience
  *  - improve visuals
  *  - try to make more accurate location searching
  *
  * */
}
