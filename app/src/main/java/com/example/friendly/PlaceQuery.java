package com.example.friendly;

import com.example.friendly.objects.Place;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

public class PlaceQuery {
    private static final String TAG = "PlaceQuery";
    private static final String KEY_USER_LOCATION = "location";
    private static final double MAX_DISTANCE_MILES = 15;

    public List<Place> queryNearbyPlaces() {
        ParseQuery<Place> query = ParseQuery.getQuery(Place.class);
        ParseGeoPoint currentLocation = ParseUser.getCurrentUser().getParseGeoPoint(KEY_USER_LOCATION);
        query.whereNear(KEY_USER_LOCATION, currentLocation);
        query.whereWithinMiles(KEY_USER_LOCATION, currentLocation, MAX_DISTANCE_MILES);

        List<Place> queryResults = null;
        try {
            queryResults = query.find();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        ParseQuery.clearAllCachedResults();
        return queryResults;
    }

}
