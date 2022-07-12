package com.example.friendly;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseConfig;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class MatchingUtils {
    private static final String TAG = "MatchingUtils";
    private static final String KEY_USER_LOCATION = "Location";
    private static final String KEY_ACTIVITIES_PREFERENCE = "activitiesPreference";
    private static final int USER_QUERY_LIMIT = 13;

    private static final ParseUser currentUser = ParseUser.getCurrentUser();

    /*
    Matching algorithm that retrieves best match for current user based on
    - location
    - time availability (scheduling algorithm) --
    - similarity preference ** --
    - hangout place/activity preference
    - hobbies
    - mutual friends?? --

    - can take into account hangout history?
    - dynamically weigh?

    - Elo matching (Tinder)
    - bucket sorting (TikTok) -- gives you buckets of videos
     */
    public static HashMap<ParseUser, Double> findMatches(ParseUser currentUser){
        // TODO: move places query to separate file
        HashMap<ParseUser, Double> topMatches = new HashMap<>();
        ParseGeoPoint currentLocation = currentUser.getParseGeoPoint(KEY_USER_LOCATION);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNear(KEY_USER_LOCATION, currentLocation);
//        query.whereWithinRadians(KEY_USER_LOCATION);
        query.setLimit(USER_QUERY_LIMIT); // out of 12 other nearest users
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> nearUsers, ParseException e) {
                if (e == null) {
                    for (int i = 1; i < nearUsers.size(); i++) { // index set to 1 to skip over current user (index 0)
                        ParseUser nearbyUser = nearUsers.get(i);

                        double distanceScore = currentLocation.distanceInKilometersTo(nearbyUser.getParseGeoPoint(KEY_USER_LOCATION));
                        double activityScore = getActivitySimilarityScore(nearbyUser);

                        Log.i(TAG, "distance: " + String.valueOf(distanceScore) + "; activity: " + String.valueOf(activityScore));

                        double similarityScore = distanceScore + activityScore;
                        topMatches.put(nearbyUser, similarityScore);

                    }
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
        ParseQuery.clearAllCachedResults();
        return topMatches;
    }

    /**
     * Returns number of overlapping preferences.
     * @param nearbyUser - current nearby ParseUser to compare preferences for
     * @return double of number of number of overlapping preferences
     */
    private static double getActivitySimilarityScore(ParseUser nearbyUser) {
//        for testing purposes, set values
        List<Boolean> array1 = new ArrayList<Boolean>();
        array1.add(true);
        array1.add(true);
        array1.add(false);
        List<Boolean> array2 = new ArrayList<Boolean>();
        array2.add(false);
        array2.add(true);
        array2.add(true);
        currentUser.put(KEY_ACTIVITIES_PREFERENCE, array1);
        nearbyUser.put(KEY_ACTIVITIES_PREFERENCE, array2);

        return getSimilarityScore(nearbyUser, KEY_ACTIVITIES_PREFERENCE);

    }

    private static double getSimilarityScore(ParseUser nearbyUser, String listKey){
        List<String> currentUserList = currentUser.getList(listKey);
        List<String> nearbyUserList = nearbyUser.getList(listKey);
        int score = 0;
        for (int i = 0; i < currentUserList.size(); i++){
            if (Objects.equals(currentUserList.get(i), currentUserList.get(i))){
                score++;
            }
        }
        return (double) score;
    }

}
