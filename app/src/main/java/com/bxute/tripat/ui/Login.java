/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bxute.tripat.R;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

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
  FirebaseAuth mAuth;
  private int RC_SIGN_IN = 109;
  private GoogleSignInOptions gso;
  private GoogleSignInClient mGoogleSignInClient;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_login);
    ButterKnife.bind(this);
    mAuth = FirebaseAuth.getInstance();
    loginBtn.setOnClickListener(view -> signIn());
    configureGoogle();
  }

  @Override
  public void onStart() {
    super.onStart();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    updateUI(currentUser);
  }

  private void updateUI(FirebaseUser currentUser) {
    if (currentUser != null) {
      openHome();
    }
  }

  private void signIn() {
    Intent signInIntent = mGoogleSignInClient.getSignInIntent();
    startActivityForResult(signInIntent, RC_SIGN_IN);
  }

  private void configureGoogle() {
    gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
     .requestIdToken(getString(R.string.default_web_client_id))
     .requestEmail()
     .build();
    mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
  }

  private void openHome() {
    Intent intent = new Intent(this, Home.class);
    startActivity(intent);
    finish();
  }

  @Override
  public void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == RC_SIGN_IN) {
      Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
      try {
        GoogleSignInAccount account = task.getResult(ApiException.class);
        firebaseAuthWithGoogle(account);
      }
      catch (ApiException e) {
        e.printStackTrace();
        Log.d("Login",""+e.getMessage());
      }
    }
  }

  private void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
    AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
    mAuth.signInWithCredential(credential)
     .addOnCompleteListener(this, task -> {
       if (task.isSuccessful()) {
         FirebaseUser user = mAuth.getCurrentUser();
         updateUI(user);
       } else {
         updateUI(null);
       }
     });
  }

}
