/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;

import com.bxute.tripat.R;

public class Splash extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_splash);
    new Handler().postDelayed(new Runnable() {
      @Override
      public void run() {
        openLogin();
      }
    }, 2000);
  }

  private void openLogin() {
    Intent intent = new Intent(this, Login.class);
    startActivity(intent);
    //enter , exit
    overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    finish();
  }
}
