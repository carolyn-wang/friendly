package com.example.friendly;

import android.util.Log;

import com.example.friendly.objects.Place;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class PlaceQuery {
    private static final String TAG = "PlaceQuery";
    private static final String KEY_LOCATION = "location";
    private static final double MAX_DISTANCE_MILES = 25;
    private List<Place> nearbyPlaces = new ArrayList<Place>();

    public void queryNearbyPlaces() {
        ParseQuery<Place> query = ParseQuery.getQuery(Place.class);
        ParseGeoPoint currentLocation = ParseUser.getCurrentUser().getParseGeoPoint(KEY_LOCATION);
        query.whereNear(KEY_LOCATION, currentLocation);
        query.whereWithinMiles(KEY_LOCATION, currentLocation, MAX_DISTANCE_MILES);

        List<Place> queryResults = null;
        query.findInBackground(new FindCallback<Place>() {
            @Override
            public void done(List<Place> places, ParseException e) {
                nearbyPlaces.addAll(places);
                for (Place p: places){
                    Log.i(TAG, p.getName());
                }
            }
        });

        ParseQuery.clearAllCachedResults();
    }

    public List<Place> getNearbyPlaces(){
        return nearbyPlaces;
    }

}
