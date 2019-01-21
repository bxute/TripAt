/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.ui;


import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bxute.tripat.R;
import com.bxute.tripat.firestore.ItemsInBucket;
import com.bxute.tripat.firestore.ItemsInVisited;
import com.bxute.tripat.utils.ImageHandler;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * A simple {@link Fragment} subclass.
 */
public class UserFragment extends Fragment {
  @BindView(R.id.place_visited_count)
  TextView placeVisitedCount;
  @BindView(R.id.place_saved_count)
  TextView placeSavedCount;
  @BindView(R.id.place_count_container)
  LinearLayout placeCountContainer;
  @BindView(R.id.account_image)
  ImageView accountImage;
  @BindView(R.id.account_name)
  TextView accountName;
  @BindView(R.id.logout_btn)
  RelativeLayout logoutBtn;
  Unbinder unbinder;
  FirebaseAuth mAuth;
  private Context mContext;

  public UserFragment() {
    // Required empty public constructor
    mAuth = FirebaseAuth.getInstance();
  }

  @Override
  public void onAttach(Context context) {
    super.onAttach(context);
    this.mContext = context;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container,
                           Bundle savedInstanceState) {
    // Inflate the layout for this fragment
    View view = inflater.inflate(R.layout.fragment_user, container, false);
    unbinder = ButterKnife.bind(this, view);
    loadUserInfo();
    return view;
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }

  private void loadUserInfo() {
    FirebaseUser currentUser = mAuth.getCurrentUser();
    String profileUrl = currentUser.getPhotoUrl().toString();
    String username = currentUser.getDisplayName();
    if (username != null) {
      String accountNameDisplay = "Signed in as " + username;
      int len = username.length() - 1;
      Spannable accountDisplaySpan = new SpannableString(accountNameDisplay);
      accountDisplaySpan.setSpan(new ForegroundColorSpan(Color.parseColor("#07bdbd")), 13, 14 + len, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
      accountDisplaySpan.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), 13, 14 + len, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
      accountName.setText(accountDisplaySpan);
    }
    ImageHandler.loadCircularImage(mContext, accountImage, profileUrl);
    int saved = ItemsInBucket.getCount();
    int visited = ItemsInVisited.getCount();
    placeVisitedCount.setText(String.valueOf(visited));
    placeSavedCount.setText(String.valueOf(saved));
  }
}
