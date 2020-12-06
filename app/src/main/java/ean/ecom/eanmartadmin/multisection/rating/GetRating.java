package ean.ecom.eanmartadmin.multisection.rating;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Transaction;

import java.util.HashMap;
import java.util.Map;

import static ean.ecom.eanmartadmin.database.DBQuery.firebaseFirestore;

/**
 * Created by Shailendra (WackyCodes) on 06/12/2020 16:28
 * ( To Know more, Click : https://linktr.ee/wackycodes )
 */
public class GetRating {

    class Restaurant {
        String name;
        double avgRating;
        int numRatings;

        public Restaurant(String name, double avgRating, int numRatings) {
            this.name = name;
            this.avgRating = avgRating;
            this.numRatings = numRatings;
        }

        private void checkRating(){
            final DocumentReference ratingRef = firebaseFirestore.collection( "ratings" ).document();

            addRating( ratingRef, 3.0f ).addOnCompleteListener( new OnCompleteListener <Void>() {
                @Override
                public void onComplete(@NonNull Task <Void> task) {

                }
            } );

        }

    }


    // restaurantRef =
    private Task <Void> addRating(final DocumentReference restaurantRef, final float rating) {
        // Create reference for new rating, for use inside the transaction
        final DocumentReference ratingRef = restaurantRef.collection( "ratings" ).document();
        // In a transaction, add the new rating and update the aggregate totals
        final DocumentReference reference = firebaseFirestore.collection( "SHOPS" ).document( "SHOP_ID" );


        return firebaseFirestore.runTransaction( new Transaction.Function <Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {

                Restaurant restaurant = transaction.get( restaurantRef ).toObject( Restaurant.class );

                // Compute new number of ratings
                int newNumRatings = restaurant.numRatings + 1;

                // Compute new average rating
                double oldRatingTotal = restaurant.avgRating * restaurant.numRatings;
                double newAvgRating = (oldRatingTotal + rating) / newNumRatings;

                // Set new restaurant info
                restaurant.numRatings = newNumRatings;
                restaurant.avgRating = newAvgRating;

                // Update restaurant
                transaction.set( restaurantRef, restaurant );

                // Update rating
                Map <String, Object> data = new HashMap <>();
                data.put( "rating", rating );
                transaction.set( ratingRef, data, SetOptions.merge() );

                return null;
            }
        } );
    }


    private void updateRating( String shopID, final double userRate ) {
        final DocumentReference shopRef = firebaseFirestore.collection( "SHOPS" ).document( shopID );

        firebaseFirestore.runTransaction( new Transaction.Function <Void>() {
            @Override
            public Void apply(Transaction transaction) throws FirebaseFirestoreException {
                // Get Snapshot
                DocumentSnapshot snapshot = transaction.get( shopRef );

                // Note: this could be done without a transaction
                //       by updating the shop_rating_stars using FieldValue.increment()
                double ratingStarsAvgOld = snapshot.getDouble( "shop_rating_stars" );
                long ratingNumOld = snapshot.getLong( "shop_rating_peoples" );

//                int ratingNum = Integer.parseInt( String.valueOf( snapshot.getLong( "shop_rating_peoples" )));

                // Compute new average rating
                double ratingStarTotalOld = ratingStarsAvgOld * ratingNumOld;

                long ratingNumNew = ratingNumOld + 1;
                double ratingStarsAvgNew = (ratingStarTotalOld + userRate) / ratingNumNew;

                // To Update Single Field
//                transaction.update( shopRef, "shop_rating_stars", ratingStarsAvgNew );

                // Update With Map
                Map <String, Object> updateMap = new HashMap <>();
                updateMap.put( "shop_rating_stars", ratingStarsAvgNew );
                updateMap.put( "shop_rating_peoples", ratingNumNew );
                // TO Update Multiple Fields..
                transaction.update( shopRef, updateMap );

                // Success
                return null;
            }
        } ).addOnSuccessListener( new OnSuccessListener <Void>() {
            @Override
            public void onSuccess(Void aVoid) {
//                Log.d(TAG, "Transaction success!");
            }
        } ).addOnFailureListener( new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Transaction failure.", e);
            }
        } );
    }


}
