/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat;

import android.content.Context;
import android.support.design.widget.CoordinatorLayout;
import android.util.AttributeSet;
import android.view.View;

//Generic behavior for any View class
public class CustomBehavior<V extends View> extends CoordinatorLayout.Behavior<V> {

  //Default constructor for inflating from code
  public CustomBehavior() {
  }

  //Default constructor for inflating from layour
  public CustomBehavior(Context context, AttributeSet attrs){
    super(context,attrs);
  }

}
