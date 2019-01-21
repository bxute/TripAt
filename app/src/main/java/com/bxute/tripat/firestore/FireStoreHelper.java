/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.firestore;

import android.util.Log;

import com.bxute.tripat.models.PlaceModel;
import com.bxute.tripat.utils.CurrentUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class FireStoreHelper {
  public static final String BucketItems = "bucketItems";
  public static final String Users = "users";
  public static final String VisitedItems = "visitedItems";

  public static void addItemToBucket(PlaceModel.Results place) {
    String userId = CurrentUser.getUserId();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection(Users).document(userId)
     .collection(BucketItems).document(place.getPlace_id())
     .set(place)
     .addOnSuccessListener(aVoid -> Log.d("FirestoreUtils", "added to bucket"));
  }

  public static void addItemToVisited(PlaceModel.Results place){
    String userId = CurrentUser.getUserId();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection(Users).document(userId)
     .collection(VisitedItems).document(place.getPlace_id())
     .set(place)
     .addOnSuccessListener(aVoid -> Log.d("FirestoreUtils", "added to visited"));
  }

  public static void removeFromBucket(String place_id) {
    String userId = CurrentUser.getUserId();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection(Users).document(userId)
     .collection(BucketItems).document(place_id)
     .delete().addOnSuccessListener(aVoid -> Log.d("FirestoreUtils", "item removed from bucket"));
  }

  public static CollectionReference getBucketItemReferences() {
    String userId = CurrentUser.getUserId();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    return db
     .collection(Users).document(userId)
     .collection(BucketItems);
  }

  public static CollectionReference getVisitedItemReferences() {
    String userId = CurrentUser.getUserId();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    return db
     .collection(Users).document(userId)
     .collection(VisitedItems);
  }
}
