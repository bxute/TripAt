/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat;

import android.content.Context;
import android.util.DisplayMetrics;

public class PixelUtils {
  public static int convertDpToPixel(int dp, Context context){
    return dp * (context.getResources().getDisplayMetrics().densityDpi / DisplayMetrics.DENSITY_DEFAULT);
  }
}
