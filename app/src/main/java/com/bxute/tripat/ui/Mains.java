/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.ui;

import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;

public class Mains extends MultiDexApplication {
  private static String seesionId;

  public static String getSessionId() {
    return seesionId;
  }

  @Override
  public void onCreate() {
    MultiDex.install(this);
    seesionId = String.valueOf(System.currentTimeMillis());
    super.onCreate();
  }
}
