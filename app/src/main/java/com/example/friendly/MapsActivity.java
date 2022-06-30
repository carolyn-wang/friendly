package com.example.friendly;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.app.Activity;
import android.os.Bundle;

import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.friendly.databinding.ActivityGoogleMapsBinding;

// Android Dependencies
import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;
// Google Maps Dependencies
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
// Parse Dependencies
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
// Java dependencies
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    // TODO: fix get current user location
    // TODO: fix refreshing

    private static final String TAG = "MapsActivity";
    private static final String KEY_USER_LOCATION = "Location";
    private static final String KEY_USER_NAME = "name";
    private GoogleMap mMap;
    private ActivityGoogleMapsBinding binding;
    private Context mContext;
    private Activity mActivity;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    private FusedLocationProviderClient fusedLocationClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getBaseContext();
        mActivity = MapsActivity.this;
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);


        binding = ActivityGoogleMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
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
        mMap = googleMap;


        ParseGeoPoint currLocation = getCurrentUserParseLocation();
        LatLng currentLatLng = new LatLng(currLocation.getLatitude(), currLocation.getLongitude());

        mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Marker in current location"));

        getCurrentUserDeviceLocation();
        showCurrentUserInMap(googleMap);
        showClosestUser(googleMap);
        showPlacesInMap(googleMap);
        showClosestPlace(googleMap);
    }

    /**
     * Gets user's current location and saves to database.
     * If location null, then retrieves user's most recent location from database.
     */
    private ParseGeoPoint getCurrentLocation() {
        ParseGeoPoint location = getCurrentUserDeviceLocation();
        if (location != null) {
            saveUserLocation(location);
            return location;
        }
        else{
            return getCurrentUserParseLocation();
        }
    }

    /**
     * Updates user location on Back4App Dashboard
     */

    private void saveUserLocation(ParseGeoPoint location) {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser != null) {
            currentUser.put(KEY_USER_LOCATION, location);
            currentUser.saveInBackground();
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
     * Retreives current user location from Back4App Database.
     * Return to Login Activity if not possible to find user.
     */
    private ParseGeoPoint getCurrentUserParseLocation() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(mContext, "Can't get user's current parse location", Toast.LENGTH_SHORT).show();
            NavigationUtils.goLoginActivity(mActivity);
        }
        return currentUser.getParseGeoPoint(KEY_USER_LOCATION);

    }


    /**
     * Returning the current device location.
     */
    private ParseGeoPoint getCurrentUserDeviceLocation() {

        if (ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    // Got last known location. In some rare situations this can be null.
                    if (location != null) {
                        Log.i(TAG, "onSuccess" + location.toString());
                        Toast.makeText(mContext, "Retrieved user's current location", Toast.LENGTH_SHORT).show();

                    }
                    Log.i(TAG, "Location is null :(");
                }
            });
        }
        return getCurrentUserParseLocation();
    }

    /**
     * Retrieve user's location and creates a marker in the map showing the current user location.
     * Zoom the map to the currentUserLocation.
     *
     * @param googleMap
     */
    private void showCurrentUserInMap(final GoogleMap googleMap) {

        ParseGeoPoint currentUserLocation = getCurrentLocation();
        LatLng currentUser = new LatLng(currentUserLocation.getLatitude(), currentUserLocation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(currentUser).title(ParseUser.getCurrentUser().getUsername()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        moveCamera(googleMap, currentUser, 15.0f);
    }


    /**
     * Find and display the distance between the current user and the closest user to current user.
     * Create marker to show closest user.
     * Zoom the map to the currentUserLocation
     *
     * @param googleMap
     */

    private void showClosestUser(final GoogleMap googleMap) {
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
                    showCurrentUserInMap(mMap);
                    LatLng closestUserLocation = new LatLng(closestUser.getParseGeoPoint(KEY_USER_LOCATION).getLatitude(), closestUser.getParseGeoPoint(KEY_USER_LOCATION).getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(closestUserLocation).title(closestUser.getUsername()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));

                } else {
                    Log.d("store", "Error: " + e.getMessage());
                }
            }
        });
        ParseQuery.clearAllCachedResults();
    }

    private void showPlacesInMap(final GoogleMap googleMap) {

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Place");
        query.whereExists(KEY_USER_LOCATION);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> stores, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < stores.size(); i++) {
                        LatLng storeLocation = new LatLng(stores.get(i).getParseGeoPoint(KEY_USER_LOCATION).getLatitude(), stores.get(i).getParseGeoPoint(KEY_USER_LOCATION).getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(storeLocation).title(stores.get(i).getString(KEY_USER_NAME)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }
                } else {
                    Log.d("Place", "Error: " + e.getMessage());
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
    private void showClosestPlace(final GoogleMap googleMap) {
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Place");
        query.whereNear(KEY_USER_LOCATION, getCurrentUserParseLocation());
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> nearStores, ParseException e) {
                if (e == null) {
                    ParseObject closestPlace = nearStores.get(0);
                    showCurrentUserInMap(mMap);
                    double distance = getCurrentUserParseLocation().distanceInKilometersTo(closestPlace.getParseGeoPoint(KEY_USER_LOCATION));
                    Toast.makeText(mContext, "We found the closest place from you! It's " + closestPlace.getString(KEY_USER_NAME) + ". \nYou are " + Math.round(distance * 100.0) / 100.0 + " km from this store.", Toast.LENGTH_SHORT).show();
                    LatLng closestPlaceLocation = new LatLng(closestPlace.getParseGeoPoint(KEY_USER_LOCATION).getLatitude(), closestPlace.getParseGeoPoint(KEY_USER_LOCATION).getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(closestPlaceLocation).title(closestPlace.getString(KEY_USER_NAME)).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                } else {
                    Log.d("Place", "Error: " + e.getMessage());
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
     *
     * @param latLng
     */
    private void setMarker(final GoogleMap googleMap, LatLng latLng, String title) {
        googleMap.addMarker(new MarkerOptions().position(latLng).title(title));
    }
}