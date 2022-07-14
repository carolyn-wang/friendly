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
    private static final String KEY_YEAR_PREFERENCE = "yearPreference";
    private static final String KEY_ACTIVITY_PREFERENCE = "activityPreference";
    private static final int USER_QUERY_LIMIT = 13;
    private static final double MAX_DISTANCE_RADIANS = 0.5;

    private static ParseUser currentUser;
    private static ParseGeoPoint currentLocation;

    // TODO: account for null preference/location values
    // TODO: fix nearby queries
    /*
    Matching algorithm that retrieves best match for current user based on
    - location
    - time availability (scheduling algorithm) --
    - similarity preference ** -- (negatively or positively weigh hobbies, year, and mutual friends score)
    - hangout place/activity preference
    - year
    - hobbies
    - mutual friends?? -- **

    - can take into account hangout history?
    - dynamically weigh?

    - Elo matching (Tinder)
    - bucket sorting (TikTok) -- gives you buckets of videos
     */

    public static Set<ParseUser> getMatches() {

        int arr1[][] = {{0, 4}, {5, 10},
                {13, 20}, {24, 25}};

        int arr2[][] = {{1, 5}, {8, 12},
                {15, 24}, {25, 26}};

        int overlapHours = findIntersection(arr1, arr2);
        Log.i(TAG, String.valueOf(overlapHours));

        currentUser = ParseUser.getCurrentUser();
        currentLocation = currentUser.getParseGeoPoint(KEY_USER_LOCATION);

        HashMap<ParseUser, Double> sortedMatches = getSortedMatches();
        return sortedMatches.keySet();
    }


    public static HashMap<ParseUser, Double> getSortedMatches() {
        // TODO: move places query to separate file
        HashMap<ParseUser, Double> topMatches = new HashMap<>();
        // TODO: account for if user location is null (hasn't opened map fragment yet)
        // TODO: account for if user preference JSONarrays have null values

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
                        Double overallScore = calculateScore(nearbyUser);
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

    private static double calculateScore(ParseUser nearbyUser) {// if number of overlapping hours is 0; deduct score by a lot

        double distanceWeight = 1.0;
        double activityWeight = 1.0;
        double hobbyWeight = 1.0;
        double yearWeight = 1.0;
        double distanceScore = currentLocation.distanceInKilometersTo(nearbyUser.getParseGeoPoint(KEY_USER_LOCATION));
        double hobbyScore = getArraySimilarityScore(nearbyUser, KEY_HOBBY_PREFERENCE);
        double activityScore = getArraySimilarityScore(nearbyUser, KEY_ACTIVITY_PREFERENCE);
        // TODO: get length of array from Preference class after merging branch
        double yearScore = getIntSimilarityScore(nearbyUser, KEY_YEAR_PREFERENCE, 5);
        Log.i(TAG, "distance: " + distanceScore
                + "; hobby: " + hobbyScore
                + "; year: " + yearScore
                + "; activity: " + activityScore);

        double overallScore = (distanceWeight * distanceScore)
                + (activityWeight * activityScore)
                + (hobbyWeight * hobbyScore)
                + (yearWeight * yearScore);
        return overallScore;
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
                assert nearbyUserList != null;
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
     * Returns similarity of two ints proportional to number of all possible options.
     * SimilarityScore = (ArrayLength - abs(value1 - value2)) / ArrayLength
     *
     * @param nearbyUser - current other ParseUser to compare preferences to
     * @param listKey    - String database key to access int value
     * @return double difference between two users' integer values
     */
    private static double getIntSimilarityScore(ParseUser nearbyUser, String listKey, int lenArray) {
        int currentUserInt = currentUser.getInt(listKey);
        int nearbyUserInt = nearbyUser.getInt(listKey);
        return (double) (lenArray - Math.abs(currentUserInt - nearbyUserInt))/lenArray;
    }


    /**
     * Finds intersection between user availabilities and returns number of overlapping hours
     *
     * @param arr1 int[][] - first User's time availability
     * @param arr2 int[][] - second User's time availability
     * @return - int number of overlapping hours in availabilities
     */
    static int findIntersection(int arr1[][],
                                int arr2[][]) {
        int totalHoursOverlap = 0;

        // i and j pointers for arr1 and arr2 respectively
        int i = 0, j = 0;
        int n = arr1.length, m = arr2.length;

        while (i < n && j < m) {
            // Left bound for intersecting segment
            int l = Math.max(arr1[i][0], arr2[j][0]);
            // Right bound for intersecting segment
            int r = Math.min(arr1[i][1], arr2[j][1]);
            // If segment is valid, store in array
            if (l < r)
                Log.i(TAG, "{" + l + ", " +
                        r + "}");
            int numHoursOverlap = r - l;
            totalHoursOverlap += numHoursOverlap;
            // else if i-th interval's right bound is smaller, increment i else increment j
            if (arr1[i][1] < arr2[j][1])
                i++;
            else
                j++;
        }
        return totalHoursOverlap;
    }
}
