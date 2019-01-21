/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.utils;

import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.view.Gravity;
import android.view.View;

import static android.view.View.LAYER_TYPE_SOFTWARE;

public class ViewUtils {

  public static Drawable generateBackgroundWithShadow(View view, int backgroundColor,
                                                      float cornerRadius,
                                                      int shadowColor,
                                                      int elevation,
                                                      int shadowGravity) {

    float bottomLeftRadii = 0;
    float bottomRightRadii = 0;

    float[] outerRadius = {
     cornerRadius, cornerRadius,
     cornerRadius, cornerRadius,
     bottomRightRadii, bottomRightRadii,
     bottomLeftRadii, bottomLeftRadii};

    Rect shapeDrawablePadding = new Rect();
    shapeDrawablePadding.left = 0;
    shapeDrawablePadding.right = 0;

    int DY;
    switch (shadowGravity) {
      case Gravity.CENTER:
        shapeDrawablePadding.top = elevation;
        shapeDrawablePadding.bottom = elevation;
        DY = 0;
        break;
      case Gravity.TOP:
        shapeDrawablePadding.top = elevation *2;
        shapeDrawablePadding.bottom = elevation;
        DY = -1* elevation /3;
        break;
      default:
      case Gravity.BOTTOM:
        shapeDrawablePadding.top = elevation;
        shapeDrawablePadding.bottom = elevation *2;
        DY = elevation /3;
        break;
    }

    ShapeDrawable shapeDrawable = new ShapeDrawable();
    shapeDrawable.setPadding(shapeDrawablePadding);
    shapeDrawable.getPaint().setColor(backgroundColor);
    shapeDrawable.getPaint().setShadowLayer(cornerRadius /3, 0, DY, shadowColor);
    view.setLayerType(LAYER_TYPE_SOFTWARE, shapeDrawable.getPaint());
    shapeDrawable.setShape(new RoundRectShape(outerRadius, null, null));

    LayerDrawable drawable = new LayerDrawable(new Drawable[]{shapeDrawable});
    drawable.setLayerInset(0,0,elevation,0,0);
    return drawable;
  }
}