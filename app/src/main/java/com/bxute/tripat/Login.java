/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Login extends AppCompatActivity {

  @BindView(R.id.logo)
  ImageView logo;
  @BindView(R.id.app_title)
  TextView appTitle;
  @BindView(R.id.google_icon)
  ImageView googleIcon;
  @BindView(R.id.card_view)
  CardView cardView;
  @BindView(R.id.login_btn)
  RelativeLayout loginBtn;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);
    loginBtn.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        openHome();
      }
    });
  }

  private void openHome(){
    Intent intent = new Intent(this,Home.class);
    startActivity(intent);
    finish();
  }

}
