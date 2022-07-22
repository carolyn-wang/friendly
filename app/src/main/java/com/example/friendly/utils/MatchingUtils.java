package com.example.friendly.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.friendly.activities.MainActivity;
import com.example.friendly.objects.Hangout;
import com.example.friendly.objects.Place;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import java.util.stream.DoubleStream;

public class MatchingUtils {
    private static final String TAG = "MatchingUtils";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_SIMILARITY_PREFERENCE = "similarityPreference";
    private static final String KEY_HOBBY_PREFERENCE = "hobbyPreference";
    private static final String KEY_YEAR_PREFERENCE = "yearPreference";
    private static final String KEY_ACTIVITY_PREFERENCE = "activityPreference";
    private static final String KEY_AVAILABILITY_PREFERENCE = "availabilityPreference";
    private static final int USER_QUERY_LIMIT = 13;
    private static final double MAX_DISTANCE_MILES = 3.0;
    private static final int YEAR_OPTIONS_LENGTH = 5;
    private static final String KEY_PREFERENCE_WEIGHTS = "preferenceWeights";
    private static final String KEY_AVERAGE_SIMILARITY_SCORES = "averageSimilarityScores";
    private static final int KEY_DISTANCE_WEIGHT_INDEX = 0;
    private static final int KEY_ACTIVITY_WEIGHT_INDEX = 1;
    private static final int KEY_HOBBY_WEIGHT_INDEX = 2;
    private static final int KEY_YEAR_WEIGHT_INDEX = 3;
    private static final int KEY_AVAILABILITY_WEIGHT_INDEX = 4;
    private static final int NUM_WEIGHTS = 5;

    /**
     * Matching algorithm that retrieves best user matches for current user based on:
     * - location (user must be within certain radius)
     * - time availability (number overlapping hours)
     * - similarity preference (negatively or positively
     * weigh hobbies, year, and mutual friends score,
     * depending on how similar users want matches to be)
     * - hangout activity preference
     * - year
     * - hobbies
     * - mutual friends?? -- TODO
     *
     * @return Set of optimal users to hangout with
     */
    public static Collection<ParseUser> getMatches() {
        return getSortedMatches().values();
    }


    public static Map<Double, ParseUser> getSortedMatches() {
        // TODO: move places query to separate file
        Map<Double, ParseUser> topMatches = new TreeMap<>();
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        ParseGeoPoint currentLocation = ParseUser.getCurrentUser().getParseGeoPoint(KEY_LOCATION);
        query.whereNear(KEY_LOCATION, currentLocation);
        query.whereWithinMiles(KEY_LOCATION, currentLocation, MAX_DISTANCE_MILES);
        query.setLimit(USER_QUERY_LIMIT); // out of 12 other nearest users

        List<ParseUser> nearUsers = null;
        try {
            nearUsers = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 1; i < nearUsers.size(); i++) { // index set to 1 to skip over current user (index 0)
            ParseUser nearbyUser = nearUsers.get(i);
            double[] scoresArray = new double[0];
            try {
                scoresArray = calculateSimilarityArray(nearbyUser);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            double overallScore = DoubleStream.of(scoresArray).sum();
            topMatches.put(overallScore, nearbyUser);
        }

        ArrayList<int[]> arr = findConsecutiveRanges(
                new boolean[]{true, true, false, true, true, true, false, false, false, false, false, false, true, true, true, true, false},
                new boolean[]{true, true, true, true, true, false, false, false, false, false, false, true, true, true, true, false, false});

        for (int[] ints : arr) {
            Log.i(TAG, ints[0] + " " + ints[1]);
        }

        ParseQuery.clearAllCachedResults();
        return topMatches;
    }

    /**
     * Calculates overall similarity score between current user and another given user.
     * Queries user's weights and preference values from database.
     * Doesn't take into account user preferences for how similar other person should be
     *
     * @param nearbyUser ParseUser to compare
     * @return double[] containing similarity score for each category
     */
    private static double[] calculateSimilarityArray(ParseUser nearbyUser) throws JSONException {// if number of overlapping hours is 0, deduct score by a lot; and vice versa
        JSONArray preferenceWeights = ParseUser.getCurrentUser().getJSONArray(KEY_PREFERENCE_WEIGHTS);
        ParseGeoPoint currentLocation = ParseUser.getCurrentUser().getParseGeoPoint(KEY_LOCATION);
        double distanceWeight = preferenceWeights.getDouble(KEY_DISTANCE_WEIGHT_INDEX);
        double activityWeight = preferenceWeights.getDouble(KEY_ACTIVITY_WEIGHT_INDEX);
        double hobbyWeight = preferenceWeights.getDouble(KEY_HOBBY_WEIGHT_INDEX);
        double yearWeight = preferenceWeights.getDouble(KEY_YEAR_WEIGHT_INDEX);
        double availabilityWeight = preferenceWeights.getDouble(KEY_AVAILABILITY_WEIGHT_INDEX);
        double distanceScore = MAX_DISTANCE_MILES - currentLocation.distanceInMilesTo(nearbyUser.getParseGeoPoint(KEY_LOCATION));
        double hobbyScore = getArraySimilarityScore(nearbyUser, KEY_HOBBY_PREFERENCE);
        double activityScore = getArraySimilarityScore(nearbyUser, KEY_ACTIVITY_PREFERENCE);
        double yearScore = getIntSimilarityScore(nearbyUser, KEY_YEAR_PREFERENCE, YEAR_OPTIONS_LENGTH);
        double availabilityScore = getArraySimilarityScore(nearbyUser, KEY_AVAILABILITY_PREFERENCE);

        Log.d(TAG, "distance: " + distanceScore
                + "; hobby: " + hobbyScore
                + "; year: " + yearScore
                + "; activity: " + activityScore
                + "; availability: " + availabilityScore);

        double[] scoresArray = {distanceWeight * distanceScore,
                activityWeight * activityScore,
                hobbyWeight * hobbyScore,
                yearWeight * yearScore,
                availabilityWeight * availabilityScore};
        return scoresArray;
    }


    /**
     * Counts number of overlapping elements between preference array for current user and another user.
     *
     * @param nearbyUser - current other ParseUser to compare preferences to
     * @param listKey    - String database key to access lists
     * @return double number of overlapping preferences
     */
    private static double getArraySimilarityScore(ParseUser nearbyUser, String listKey) {
        JSONArray currentUserList = ParseUser.getCurrentUser().getJSONArray(listKey);
        JSONArray nearbyUserList = nearbyUser.getJSONArray(listKey);
        int score = 0;
        for (int i = 0; i < currentUserList.length(); i++) {
            try {
                if ((nearbyUserList != null) &&
                        Objects.equals(currentUserList.getBoolean(i), nearbyUserList.getBoolean(i))) {
                    score++;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return score;
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
        int currentUserInt = ParseUser.getCurrentUser().getInt(listKey);
        int nearbyUserInt = nearbyUser.getInt(listKey);
        return (double) (lenArray - Math.abs(currentUserInt - nearbyUserInt)) / lenArray;
    }

    /**
     * Finds indicies ranges where both array values are true
     * and returns sorted ArrayList<int[]> with longer ranges towards the beginning of the list.
     * rangeIndex tracks the index for adding ranges to availabilityRanges.
     *
<<<<<<< HEAD
     * @param arr1 int[][] - first User's time availability
     * @param arr2 int[][] - second User's time availability
     * @return - int number of overlapping hours in availabilities
     */
    static int findIntersection(int[][] arr1,
                                int[][] arr2) {
        int totalHoursOverlap = 0;

        // index1 and index2 pointers for arr1 and arr2 respectively
        int index1 = 0, index2 = 0;
        int len1 = arr1.length, len2 = arr2.length;

        while (index1 < len1 && index2 < len2) {
            // Left bound for intersecting segment
            int l = Math.max(arr1[index1][0], arr2[index2][0]);
            // Right bound for intersecting segment
            int r = Math.min(arr1[index1][1], arr2[index2][1]);
            // If segment is valid, store in array
            if (l < r)
                Log.i(TAG, "{" + l + ", " +
                        r + "}");
            int numHoursOverlap = r - l;
            totalHoursOverlap += numHoursOverlap;
            // else if index1 interval's right bound is smaller, increment index1; else increment index2
            if (arr1[index1][1] < arr2[index2][1])
                index1++;
            else
                index2++;
        }
        return totalHoursOverlap;
    }

    /**
     * @param arr1 boolean [] User1 availability (boolean for each 30 minute slot)
     * @param arr2 boolean [] User2 availability (boolean for each 30 minute slot)
     * @return availabilityRanges ArrayList<int[]> containing ranges for overlapping truth values
     */
    private static ArrayList<int[]> findConsecutiveRanges(boolean[] arr1, boolean[] arr2) {
        ArrayList<int[]> availabilityRanges = new ArrayList<>();
        int rangeLength = 0;
        if (arr1[0]) {
            rangeLength = 1;
        }
        // Traverse the array from first position
        for (int i = 0; i < arr1.length - 1; i++) {
            // If current bool true and next bool is false
            if (arr1[i] == arr2[i]) {
                if (arr1[i] && arr2[i] && !(arr1[i + 1] && arr2[i + 1])) {
                    // If the range contains only one true element, add to array.
                    if (rangeLength == 1) {
                        availabilityRanges.add(new int[]{i + 1, i});
                    } else {
                        // Build range between the first element of the range and the
                        // current previous element as the end range.
                        availabilityRanges.add(new int[]{i + 1 - rangeLength, i});
                    }
                    rangeLength = 1;
                } else if (!arr1[i] && !arr2[i] && !arr1[i + 1] & !arr2[i]) {
                    rangeLength = 1;
                    // singular true element at end of list
                } else if (i == arr1.length - 1 && arr1[i] && arr2[i]) {
                    availabilityRanges.add(new int[]{i + 1, i + 1});
                } else {
                    rangeLength++;
                }
            }
        }

        availabilityRanges.sort((array1, array2) -> (array2[1] - array2[0]) - ((array1[1] - array1[0])));
        return availabilityRanges;
    }

    /**
     * Dynamically adjust weights after current user matches with a new user using Quick Match.
     * Compares scores between current and matched User, then adjusts current user's weights
     * and updates current user's average hangout similarity scores dependent on
     * recent match's deviation from average.
     *
     * @param matchedUser ParseUser that current user chose to hangout with.
     */
    private static void adjustWeights(ParseUser matchedUser, int direction) throws JSONException {
        double[] similarityScores = new double[NUM_WEIGHTS];
        try {
            similarityScores = calculateSimilarityArray(matchedUser);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JSONArray averageSimilarityScore = ParseUser.getCurrentUser().getJSONArray(KEY_AVERAGE_SIMILARITY_SCORES);
        JSONArray preferenceWeights = ParseUser.getCurrentUser().getJSONArray(KEY_PREFERENCE_WEIGHTS);

        if (averageSimilarityScore != null && preferenceWeights != null) {
            double[] updatedScores = new double[NUM_WEIGHTS];
            double[] updatedWeights = new double[NUM_WEIGHTS];
            for (int i = 0; i < averageSimilarityScore.length(); i++) {
                try {
                    double averageScore = averageSimilarityScore.getDouble(i);
                    double differenceScore = similarityScores[i] - (direction * averageScore);
                    double updatedScore = averageScore + (differenceScore / averageScore);
                    updatedScores[i] = updatedScore;
                    double updatedWeight = preferenceWeights.getDouble(i) * (updatedScore / averageScore);
                    updatedWeights[i] = updatedWeight;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            ParseUser.getCurrentUser().put(KEY_AVERAGE_SIMILARITY_SCORES, new JSONArray(updatedScores));
            ParseUser.getCurrentUser().put(KEY_PREFERENCE_WEIGHTS, new JSONArray(updatedWeights));
            ParseUser.getCurrentUser().saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                }
            });
        }
    }

    public static void adjustWeightsPositive(ParseUser matchedUser) throws JSONException {
        adjustWeights(matchedUser, 1);
    }

    public static void adjustWeightsNegative(ParseUser matchedUser) throws JSONException {
        adjustWeights(matchedUser, -1);
    }

    public static List<Object> getMatchDetails(ParseUser matchedUser, List<Place> placeList) {
        List<Object> matchDetails = new ArrayList<Object>();
        int randomPlaceInd = (int) Math.floor(Math.random() * placeList.size());
        Object matchPlace = placeList.get(randomPlaceInd);
        matchDetails.add(matchPlace);

        matchDetails.add("Monday 4PM - 6PM");
        matchDetails.add("Tuesday 12PM - 2:30PM");
        matchDetails.add("Thursday 4PM - 6PM");

        return matchDetails;
    }
}

