package com.example.friendly.query;

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
    private final List<Place> nearbyPlaces = new ArrayList<>();

    public void queryNearbyPlaces() {
        ParseQuery<Place> query = ParseQuery.getQuery(Place.class);
        ParseGeoPoint currentLocation = ParseUser.getCurrentUser().getParseGeoPoint(KEY_LOCATION);
        query.whereNear(KEY_LOCATION, currentLocation);
        query.whereWithinMiles(KEY_LOCATION, currentLocation, MAX_DISTANCE_MILES);

        query.findInBackground(new FindCallback<>() {
            @Override
            public void done(List<Place> places, ParseException e) {
                if (places != null){
                    nearbyPlaces.addAll(places);
                }
            }
        });

        ParseQuery.clearAllCachedResults();
    }

    public List<Place> getNearbyPlaces(){
        return nearbyPlaces;
    }

    public String[] getNearbyPlaceNames(){
        List<Place> placeList = getNearbyPlaces();
        String[] nearbyPlaceNames = new String[placeList.size()];
        for (int i=0; i<placeList.size(); i++){
            nearbyPlaceNames[i] = placeList.get(i).getName();
        }
        return nearbyPlaceNames;
    }

}
