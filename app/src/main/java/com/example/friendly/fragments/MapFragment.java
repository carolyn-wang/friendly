package com.example.friendly.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.friendly.HangoutQuery;
import com.example.friendly.PlaceQuery;
import com.example.friendly.R;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.friendly.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.adapters.HangoutsAdapter;
import com.example.friendly.objects.Hangout;
import com.example.friendly.objects.Place;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mGoogleMap;
    private MapView mapView;
    private PlaceQuery placeQuery;

    private static final String TAG = "MapsActivity";
    private static final String KEY_USER_LOCATION = "Location";
    private static final String KEY_USER_NAME = "firstName";
    private static final String KEY_PLACE_NAME = "name";
    private static final String KEY_PLACE_LOCATION = "Location";
    private static final float INITIAL_ZOOM = 14.0f;

    private Context mContext;
    private Activity mActivity;

    private static final int REQUEST_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = view.findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);

        mapView.getMapAsync(this);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mActivity = getActivity();
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        showCurrentUserInMap();
        showClosestUser();
        showPlacesInMap();
        showClosestPlace();

        //in old Api Needs to call MapsInitializer before doing any CameraUpdateFactory call
        MapsInitializer.initialize(mActivity);
    }


    /**
     * Function that saves user's current location to database then returns it.
     * If location null, then retrieves user's most recent location from database.
     */
    public void getCurrentLocation(LocationListener listener) {
        updateCurrentUserDeviceLocation(new LocationListener() {
            @Override
            public void onSuccess(ParseGeoPoint location) {
                listener.onSuccess(location);
            }

            @Override
            public void onFailure() {
                listener.onSuccess(getCurrentUserParseLocation());
            }
        });
    }

    /**
     * Retrieves current user location from Back4App Database.
     * Return to Login Activity if not possible to find user.
     */
    public ParseGeoPoint getCurrentUserParseLocation() {
        ParseUser currentUser = ParseUser.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(mContext, "Can't access user's current location from database", Toast.LENGTH_SHORT).show();
            NavigationUtils.goLoginActivity(mActivity);
        }
        return currentUser.getParseGeoPoint(KEY_USER_LOCATION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_LOCATION) {
            showCurrentUserInMap();
        }
    }

    /**
     * Asynchronously retrieves current device location, saves to Back4App database, and saves in LocationListener.
     */
    public void updateCurrentUserDeviceLocation(LocationListener listener) {
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
                        listener.onSuccess(geoLocation);
                    } else {
                        Log.i(TAG, "Location is null");
                        listener.onFailure();
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    listener.onFailure();
                }
            }).addOnCanceledListener(new OnCanceledListener() {
                @Override
                public void onCanceled() {
                    listener.onFailure();
                }
            });
        }
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
            Log.i(TAG, "Saved user location to database");
        } else {
            Toast.makeText(mContext, "Can't save user's current location", Toast.LENGTH_SHORT).show();
            NavigationUtils.goLoginActivity(mActivity);
        }
    }

    /**
     * Retrieve user's location and creates a marker in the map showing the current user location.
     * Zoom the map to the currentUserLocation.
     */
    public void showCurrentUserInMap() {

        // TODO: loading page while retrieving location
        getCurrentLocation(new LocationListener() {
            @Override
            public void onSuccess(ParseGeoPoint location) {
                LatLng currentUser = new LatLng(location.getLatitude(), location.getLongitude());
                setMarker(currentUser, ParseUser.getCurrentUser().getUsername(), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                moveCamera(currentUser, INITIAL_ZOOM);
            }

            @Override
            public void onFailure() {

            }
        });

    }

    /**
     * Find and display the distance between the current user and the closest user to current user.
     * Create marker to show closest user.
     * Zoom the map to the currentUserLocation
     */

    public void showClosestUser() {
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
                    LatLng closestUserLocation = new LatLng(closestUser.getParseGeoPoint(KEY_USER_LOCATION).getLatitude(), closestUser.getParseGeoPoint(KEY_USER_LOCATION).getLongitude());
                    setMarker(closestUserLocation, closestUser.getUsername(), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    moveCamera(closestUserLocation, INITIAL_ZOOM);
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
        ParseQuery.clearAllCachedResults();
    }

    public void showPlacesInMap() {
        ParseQuery<Place> query = ParseQuery.getQuery(Place.class);
        query.whereExists(KEY_USER_LOCATION);
        query.findInBackground(new FindCallback<Place>() {
            @Override
            public void done(List<Place> places, ParseException e) {
                if (e == null) {
                    for (int i = 0; i < places.size(); i++) {
                        LatLng storeLocation = new LatLng(places.get(i).getLocation().getLatitude(), places.get(i).getLocation().getLongitude());
                        setMarker(storeLocation, places.get(i).getName(), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
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
     */
    public void showClosestPlace() {
        ParseQuery<Place> query = ParseQuery.getQuery(Place.class);
        query.whereNear(KEY_USER_LOCATION, getCurrentUserParseLocation());
        query.setLimit(1);
        query.findInBackground(new FindCallback<Place>() {
            @Override
            public void done(List<Place> nearStores, ParseException e) {
                if (e == null) {
                    Place closestPlace = nearStores.get(0);
                    double distance = getCurrentUserParseLocation().distanceInKilometersTo(closestPlace.getLocation());
                    Toast.makeText(mContext, "We found the closest place from you! It's " + closestPlace.getName() + ". \nYou are " + Math.round(distance * 100.0) / 100.0 + " km from this store.", Toast.LENGTH_SHORT).show();
                    LatLng closestPlaceLocation = new LatLng(closestPlace.getLocation().getLatitude(), closestPlace.getLocation().getLongitude());
                    setMarker(closestPlaceLocation, closestPlace.getName(), BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
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
     * @param latLng
     */
    private void moveCamera(LatLng latLng, float zoom) {
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom), 1, null);
    }

    /**
     * Add marker on given point.
     *
     * @param latLng
     */
    private void setMarker(LatLng latLng, String title, BitmapDescriptor icon) {
        mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(title).icon(icon));
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    public interface LocationListener {
        void onSuccess(ParseGeoPoint location);

        void onFailure();
    }

}