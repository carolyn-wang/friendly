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
    protected List<Place> nearbyPlaces = new ArrayList<>();

//    // TODO: maybe change queryConditions into separate booleans
//    public void queryHangouts(HangoutsAdapter adapter, ArrayList<String> queryConditions) {
//        ParseQuery<Hangout> query = ParseQuery.getQuery(Hangout.class);
//        query.include(Hangout.KEY_USER1);
//        query.include(Hangout.KEY_USER2);
//        query.include(Hangout.KEY_DATE);
//        query.include(Hangout.KEY_LOCATION);
//    }

    public void queryNearbyPlaces(int limit) {
        ParseQuery<Place> query = ParseQuery.getQuery(Place.class);
        ParseGeoPoint currentLocation = ParseUser.getCurrentUser().getParseGeoPoint(KEY_USER_LOCATION);
        query.whereNear(KEY_USER_LOCATION, currentLocation);
        query.setLimit(limit);

        query.findInBackground(new FindCallback<Place>() {
            @Override
            public void done(List<Place> places, ParseException e) {
                if (e != null) {
                    Log.e(TAG, "Issue with getting places", e);
                    return;
                }
                nearbyPlaces = places;
            }
        });
        ParseQuery.clearAllCachedResults();
    }


}
