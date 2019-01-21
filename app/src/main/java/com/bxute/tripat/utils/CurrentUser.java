/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.utils;

import com.google.firebase.auth.FirebaseAuth;

public class CurrentUser {
  public static String getUserId(){
    FirebaseAuth auth = FirebaseAuth.getInstance();
    return auth.getCurrentUser().getUid();
  }
}
