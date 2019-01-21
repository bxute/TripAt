/*
 * Developer email: hiankit.work@gmail.com
 * GitHub: https://github.com/bxute
 */

package com.bxute.tripat.firestore;

import com.bxute.tripat.utils.CurrentUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

import static com.bxute.tripat.firestore.FireStoreHelper.BucketItems;
import static com.bxute.tripat.firestore.FireStoreHelper.Users;

public class ItemsInBucket {
  private static List<String> placeIdsOfBucketItems;

  public static void syncBucketedPlace() {
    placeIdsOfBucketItems = new ArrayList<>();
    String userId = CurrentUser.getUserId();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection(Users).document(userId)
     .collection(BucketItems)
     .addSnapshotListener((queryDocumentSnapshots, e) -> {
       List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
       placeIdsOfBucketItems.clear();
       for (int i = 0; i < documentSnapshots.size(); i++) {
         placeIdsOfBucketItems.add(documentSnapshots.get(i).getId());
       }
     });
  }

  public static int getCount() {
    return placeIdsOfBucketItems.size();
  }

  public static boolean contains(String place_id) {
    return placeIdsOfBucketItems.contains(place_id);
  }
}
