package com.example.friendly;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.friendly.databinding.ActivityGoogleMapsBinding;
import com.example.friendly.objects.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
@Deprecated
public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivity";
    private static final String KEY_USER_LOCATION = "Location";
    private static final String KEY_USER_NAME = "firstName";
    private static final String KEY_PLACE_NAME = "name";
    private static final String KEY_PLACE_LOCATION = "Location";
    private static final float INITIAL_ZOOM = 14.0f;

    private ActivityGoogleMapsBinding binding;
    private Context mContext;
    private Activity mActivity;

    private static final int REQUEST_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mActivity = MapsActivity.this;

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);

        binding = ActivityGoogleMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        showCurrentUserInMap(googleMap);
        showClosestUser(googleMap);
        showPlacesInMap(googleMap);
        showClosestPlace(googleMap);
    }

    /**
     * Gets user's current location.
     * If location null, then retrieves user's most recent location from database.
     */
    public ParseGeoPoint getCurrentLocation() {
        ParseGeoPoint location = getSaveCurrentUserDeviceLocation();
        if (location != null) {
            return location;
        }
        else{
            return getCurrentUserParseLocation();
        }
    }


    /**
     * Retrieves current user location from Back4App Database.
     * Return to Login Activity if not possible to find user.
     */
    public ParseGeoPoint getCurrentUserParseLocation() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(mContext, "Can't get user's current parse location", Toast.LENGTH_SHORT).show();
            NavigationUtils.goLoginActivity(mActivity);
        }
        return currentUser.getParseGeoPoint(KEY_USER_LOCATION);
    }

    /**
     * Returns the current device location and saves to Back4App database.
     */
    public ParseGeoPoint getSaveCurrentUserDeviceLocation() {
        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(mActivity, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(mActivity, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        Log.i(TAG, "onSuccess" + location);
                        ParseGeoPoint geoLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
                        saveUserLocation(geoLocation);
                        Toast.makeText(mContext, "Retrieved user's current location: " + location.toString(), Toast.LENGTH_SHORT).show();
                    }
                    Log.i(TAG, "Location is null :(");
                }
            });
        }
        return getCurrentUserParseLocation();
    }


    /**
     * Saves updated user location to Back4App Dashboard.
     * If user is null, sends back to login screen.
     */

    public void saveUserLocation(ParseGeoPoint location) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            currentUser.put(KEY_USER_LOCATION, location);
            currentUser.saveInBackground();
            Log.i(TAG, "saved user location to database");
        } else {
            Toast.makeText(mContext, "Can't save user's current location", Toast.LENGTH_SHORT).show();
            NavigationUtils.goLoginActivity(mActivity);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_LOCATION:
                getCurrentLocation();
                break;
        }
    }

    /**
     * Retrieve user's location and creates a marker in the map showing the current user location.
     * Zoom the map to the currentUserLocation.
     *
     * @param googleMap
     */
    public void showCurrentUserInMap(final GoogleMap googleMap) {

        ParseGeoPoint currentUserLocation = getCurrentLocation();
        LatLng currentUser = new LatLng(currentUserLocation.getLatitude(), currentUserLocation.getLongitude());
        setMarker(googleMap, currentUser, ParseUser.getCurrentUser().getString(KEY_USER_NAME), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        moveCamera(googleMap, currentUser, INITIAL_ZOOM);
    }


    /**
     * Find and display the distance between the current user and the closest user to current user.
     * Create marker to show closest user.
     * Zoom the map to the currentUserLocation
     *
     * @param googleMap
     */

    public void showClosestUser(final GoogleMap googleMap) {
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNear(KEY_USER_LOCATION, getCurrentUserParseLocation());
        // setting the limit of near users to find to 2, you'll have in the nearUsers list only two users: the current user and the closest user from the current
        query.setLimit(2);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> nearUsers, ParseException e) {
                if (e == null) {
                    ParseUser closestUser = ParseUser.getCurrentUser();
                    for (int i = 0; i < nearUsers.size(); i++) {
                        if (!nearUsers.get(i).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                            closestUser = nearUsers.get(i);
                        }
                    }
                    double distance = getCurrentUserParseLocation().distanceInKilometersTo(closestUser.getParseGeoPoint(KEY_USER_LOCATION));
                    Toast.makeText(mContext, "We found the closest user from you! It's " + closestUser.getUsername() + ". \n You are " + Math.round(distance * 100.0) / 100.0 + " km from this user.", Toast.LENGTH_SHORT).show();
                    showCurrentUserInMap(googleMap);
                    LatLng closestUserLocation = new LatLng(closestUser.getParseGeoPoint(KEY_USER_LOCATION).getLatitude(), closestUser.getParseGeoPoint(KEY_USER_LOCATION).getLongitude());
                    setMarker(googleMap, closestUserLocation, closestUser.getUsername(), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    moveCamera(googleMap, closestUserLocation, INITIAL_ZOOM);
                } else {
                    Log.d("store", "Error: " + e.getMessage());
                }
            }
        });
        ParseQuery.clearAllCachedResults();
    }

    public void showPlacesInMap(final GoogleMap googleMap) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Place");
        query.whereExists(KEY_USER_LOCATION);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> stores, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < stores.size(); i++) {
                        Log.i(TAG, stores.get(i).getObjectId());
                        LatLng storeLocation = new LatLng(stores.get(i).getParseGeoPoint(KEY_PLACE_LOCATION).getLatitude(), stores.get(i).getParseGeoPoint(KEY_PLACE_LOCATION).getLongitude());
                        Toast.makeText(mContext, storeLocation.toString(), Toast.LENGTH_SHORT).show();
                        setMarker(googleMap,storeLocation, stores.get(i).getString(KEY_PLACE_NAME),BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE) );
                    }
                } else {
                    Log.i(TAG, "Error: " + e.getMessage());
                }
            }
        });
        ParseQuery.clearAllCachedResults();
    }


    /**
     * Finding and creating a marker in the map showing the closest store to the current user
     * Zoom the map to the closestPlaceLocation
     *
     * @param googleMap
     */
    public void showClosestPlace(final GoogleMap googleMap) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Place");
        query.whereNear(KEY_USER_LOCATION, getCurrentUserParseLocation());
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> nearStores, ParseException e) {
                if (e == null) {
                    ParseObject closestPlace = nearStores.get(0);
                    showCurrentUserInMap(googleMap);
                    double distance = getCurrentUserParseLocation().distanceInKilometersTo(closestPlace.getParseGeoPoint(KEY_USER_LOCATION));
                    Toast.makeText(mContext, "We found the closest place from you! It's " + closestPlace.getString(KEY_PLACE_NAME) + ". \nYou are " + Math.round(distance * 100.0) / 100.0 + " km from this store.", Toast.LENGTH_SHORT).show();
                    LatLng closestPlaceLocation = new LatLng(closestPlace.getParseGeoPoint(KEY_USER_LOCATION).getLatitude(), closestPlace.getParseGeoPoint(KEY_USER_LOCATION).getLongitude());
                    setMarker(googleMap, closestPlaceLocation, closestPlace.getString(KEY_PLACE_NAME), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });

        ParseQuery.clearAllCachedResults();

    }


    /**
     * Zoom camera to location
     *
     * @param googleMap
     * @param latLng
     */
    private void moveCamera(final GoogleMap googleMap, LatLng latLng, float zoom) {
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), 1, null);
    }

    /**
     * Add marker on given point.
     * @param latLng
     */
    private void setMarker(final GoogleMap googleMap, LatLng latLng, String title, BitmapDescriptor icon) {
        googleMap.addMarker(new MarkerOptions().position(latLng).title(title).icon(icon));
    }
}