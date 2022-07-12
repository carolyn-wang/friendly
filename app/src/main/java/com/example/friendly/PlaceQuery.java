package com.example.friendly;

import android.location.LocationListener;
import android.util.Log;
import android.widget.Toast;

import com.example.friendly.adapters.HangoutsAdapter;
import com.example.friendly.fragments.HangoutsFragment;
import com.example.friendly.objects.Hangout;
import com.example.friendly.objects.Place;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PlaceQuery {
    private static final String TAG = "PlaceQuery";
    private static final String KEY_USER_LOCATION = "Location";
    protected static final int PLACES_TO_LOAD = 10;

    public List<Place> queryNearbyPlaces() {
        ParseQuery<Place> query = ParseQuery.getQuery(Place.class);
        ParseGeoPoint currentLocation = ParseUser.getCurrentUser().getParseGeoPoint(KEY_USER_LOCATION);
        query.whereNear(KEY_USER_LOCATION, currentLocation);
        query.setLimit(PLACES_TO_LOAD);

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
