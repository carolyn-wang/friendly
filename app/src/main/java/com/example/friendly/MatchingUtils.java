package com.example.friendly;

import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.Set;

public class MatchingUtils {
    private static final String TAG = "MatchingUtils";
    private static final String KEY_USER_LOCATION = "Location";
    private static final String KEY_SIMILARITY_PREFERENCE = "similarityPreference";
    private static final String KEY_HOBBY_PREFERENCE = "hobbyPreference";
    //    private static final String KEY_ACTIVITY_PREFERENCE = "activitiesPreference";
    private static final int USER_QUERY_LIMIT = 13;
    private static final double MAX_DISTANCE_RADIANS = 0.5;

    private static ParseUser currentUser;

    // TODO: account for null preference/location values
    // TODO: fix nearby queries
    /*
    Matching algorithm that retrieves best match for current user based on
    - location
    - time availability (scheduling algorithm) --
    - similarity preference ** --
    - hangout place/activity preference
    - hobbies
    - mutual friends?? --

TODO:
    - time availability (scheduling algorithm) --
    - similarity preference ** --
    - hangout place/activity preference
    - mutual friends?? --

    - can take into account hangout history?
    - dynamically weigh?

    - Elo matching (Tinder)
    - bucket sorting (TikTok) -- gives you buckets of videos
     */

    public static Set<ParseUser> getMatches(){
        HashMap<ParseUser, Double> sortedMatches = getSortedMatches();
        return sortedMatches.keySet();
    }


    public static HashMap<ParseUser, Double> getSortedMatches() {
        currentUser = ParseUser.getCurrentUser();
        // TODO: move places query to separate file
        HashMap<ParseUser, Double> topMatches = new HashMap<>();
        // TODO: account for if user location is null (hasn't opened map fragment yet)
        ParseGeoPoint currentLocation = currentUser.getParseGeoPoint(KEY_USER_LOCATION);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNear(KEY_USER_LOCATION, currentLocation);
        query.whereWithinRadians(KEY_USER_LOCATION, currentLocation, MAX_DISTANCE_RADIANS);
        query.setLimit(USER_QUERY_LIMIT); // out of 12 other nearest users
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> nearUsers, ParseException e) {
                if (e == null) {
                    for (int i = 1; i < nearUsers.size(); i++) { // index set to 1 to skip over current user (index 0)
                        ParseUser nearbyUser = nearUsers.get(i);

                        double distanceScore = currentLocation.distanceInKilometersTo(nearbyUser.getParseGeoPoint(KEY_USER_LOCATION));
                        double hobbyScore = getArraySimilarityScore(nearbyUser, KEY_HOBBY_PREFERENCE);

//                        double activityScore = getArraySimilarityScore(nearbyUser, KEY_ACTIVITY_PREFERENCE));

                        Log.i(TAG, "distance: " + String.valueOf(distanceScore) + "; hobby: " + String.valueOf(hobbyScore));

                        double overallScore = distanceScore;
                        topMatches.put(nearbyUser, overallScore);

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
     * Counts number of overlapping elements between preference array for current user and another user.
     *
     * @param nearbyUser - current other ParseUser to compare preferences to
     * @param listKey    - String database key to access lists
     * @return double number of overlapping preferences
     */
    private static double getArraySimilarityScore(ParseUser nearbyUser, String listKey) {
        JSONArray currentUserList = currentUser.getJSONArray(listKey);
        JSONArray nearbyUserList = nearbyUser.getJSONArray(listKey);
        int score = 0;
        for (int i = 0; i < currentUserList.length(); i++) {
            try {
                if (Objects.equals(currentUserList.getBoolean(i), nearbyUserList.getBoolean(i))) {
                    score++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return (double) score;
    }

    /**
     * Returns difference between two int values
     *
     * @param nearbyUser - current other ParseUser to compare preferences to
     * @param listKey    - String database key to access int value
     * @return double difference between two users' integer values
     */
    private static double getIntSimilarityScore(ParseUser nearbyUser, String listKey) {
        int currentUserInt = currentUser.getInt(listKey);
        int nearbyUserInt = nearbyUser.getInt(listKey);
        return (double) Math.abs(currentUserInt - nearbyUserInt);
    }

}
