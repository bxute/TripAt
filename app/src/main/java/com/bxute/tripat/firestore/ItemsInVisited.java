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

import static com.bxute.tripat.firestore.FireStoreHelper.Users;
import static com.bxute.tripat.firestore.FireStoreHelper.VisitedItems;

public class ItemsInVisited {

  private static List<String> placeIdsOfVisitedItems;

  public static void syncVisitedPlace() {
    placeIdsOfVisitedItems = new ArrayList<>();
    String userId = CurrentUser.getUserId();
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    db.collection(Users).document(userId)
     .collection(VisitedItems)
     .addSnapshotListener((queryDocumentSnapshots, e) -> {
       List<DocumentSnapshot> documentSnapshots = queryDocumentSnapshots.getDocuments();
       placeIdsOfVisitedItems.clear();
       for (int i = 0; i < documentSnapshots.size(); i++) {
         placeIdsOfVisitedItems.add(documentSnapshots.get(i).getId());
       }
     });
  }

  public static boolean contains(String place_id) {
    return placeIdsOfVisitedItems.contains(place_id);
  }

  public static int getCount() {
    return placeIdsOfVisitedItems.size();
  }
}
