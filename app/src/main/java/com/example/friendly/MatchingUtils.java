package com.example.friendly;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.HashMap;
import java.util.List;

public class MatchingUtils {
    private static final String TAG = "MatchingUtils";
    private static final String KEY_USER_LOCATION = "Location";

    private ParseUser currentUser;

    /*
    Matching algorithm that retrieves best match for current user based on
    - location
    - similarity preference **
    - activity preference
    - hobbies
    - mutual friends??
     */
    public static HashMap<ParseUser, Integer> findMatches(ParseUser currentUser){
        // TODO: move places query to separate file
        Log.d(TAG, "trying to find matches");
        HashMap<ParseUser, Integer> topMatches = new HashMap<>();
        ParseGeoPoint currentLocation = ParseUser.getCurrentUser().getParseGeoPoint(KEY_USER_LOCATION);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNear(KEY_USER_LOCATION, currentLocation);
        query.setLimit(13); // out of 12 other nearest users
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> nearUsers, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < nearUsers.size(); i++) {
                        ParseUser nearbyUser = nearUsers.get(i);

                        double distance = currentLocation.distanceInKilometersTo(nearbyUser.getParseGeoPoint(KEY_USER_LOCATION));
                        getActivitySimilarityScore(nearbyUser);

                        topMatches.put(nearbyUser, 0);

                    }
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
        ParseQuery.clearAllCachedResults();
        return topMatches;
    }

    private static void getActivitySimilarityScore(ParseUser nearbyUser) {
        Log.i(TAG, String.valueOf(nearbyUser.get("preference1")));
    }
}
