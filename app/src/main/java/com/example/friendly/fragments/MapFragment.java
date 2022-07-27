package com.example.friendly.fragments;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.friendly.activities.MainActivity;
import com.example.friendly.utils.NavigationUtils;
import com.example.friendly.objects.Place;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;
import java.util.Locale;

public class MapFragment extends Fragment implements OnMapReadyCallback, GoogleMap.OnMarkerClickListener {

    private static String KEY_USER_FIRST_NAME;
    private GoogleMap mGoogleMap;
    private MapView mapView;

    private static final String TAG = "MapsActivity";
    private static final String KEY_USER_LOCATION = "location";
    private static final String KEY_PLACE_LOCATION = "location";
    private static final String KEY_HANGOUT_USER = "hangoutUser";
    private static final String KEY_HANGOUT_PLACE = "hangoutPlace";
    private static final float INITIAL_ZOOM = 14.0f;

    private static final float placeMarkerHue = BitmapDescriptorFactory.HUE_GREEN;
    private static final float userMarkerHue = BitmapDescriptorFactory.HUE_MAGENTA;
    private static final float currentUserMarkerHue = BitmapDescriptorFactory.HUE_RED;

    private ParseUser hangoutUser;
    private Place hangoutPlace;

    private Context mContext;
    private Activity mActivity;

    private String tvMarkerNameText;
    private String tvMarkerDetailText;

    private static final int REQUEST_LOCATION = 1;
    private FusedLocationProviderClient fusedLocationClient;

    private Button btnCreateHangout;
    private TextView tvMarkerName;
    private TextView tvMarkerDetail;
    private ParseUser closestUser;
    private List<Place> nearbyPlaces;
    private static final String TAG_CLOSEST_USER = "closest user";
    private static final String TAG_CURRENT_USER = "current user";
    private static final String TAG_PLACE = "place";

    public static MapFragment newInstance(ParseUser hangoutUser, Place hangoutPlace) {

        Bundle args = new Bundle();
        args.putParcelable(KEY_HANGOUT_USER, hangoutUser);
        args.putParcelable(KEY_HANGOUT_PLACE, hangoutPlace);
        MapFragment fragment = new MapFragment();
        fragment.setArguments(args);
        return fragment;
    }

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

        tvMarkerName = view.findViewById(R.id.tvMarkerName);
        tvMarkerDetail = view.findViewById(R.id.tvMarkerDetail);
        btnCreateHangout = view.findViewById(R.id.btnCreateHangoutAtPlace);

        btnCreateHangout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.displayFragmentCreateQuickMatch(getParentFragmentManager(), tvMarkerNameText);
            }
        });
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getContext();
        mActivity = getActivity();
        KEY_USER_FIRST_NAME = getString(R.string.KEY_USER_FIRST_NAME);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mActivity);
        if (getArguments() != null) {
            hangoutUser = getArguments().getParcelable(KEY_HANGOUT_USER);
            hangoutPlace = getArguments().getParcelable(KEY_HANGOUT_PLACE);
        }
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        nearbyPlaces = ((MainActivity) mContext).getPlaceList();

        if (getArguments() != null) {
            showCurrentUserInMap();
            showUserInMap(hangoutUser);
            showPlaceInMap(hangoutPlace);
        } else {
            showCurrentUserInMap();
            showClosestUser();
            showPlacesInMap();
            showClosestPlace();
        }

        //in old Api Needs to call MapsInitializer before doing any CameraUpdateFactory call
        MapsInitializer.initialize(mActivity);

        mGoogleMap.setOnMarkerClickListener(this);
    }

    @Override
    public boolean onMarkerClick(@NonNull final Marker marker) {
        String tag = String.valueOf(marker.getTag());
        tvMarkerNameText = "";
        tvMarkerDetailText = "";
        if (!tag.equals("-1")) {
            btnCreateHangout.setVisibility(View.INVISIBLE);
            switch (tag) {
                case TAG_CURRENT_USER:
                    tvMarkerNameText = ParseUser.getCurrentUser().getString(KEY_USER_FIRST_NAME);
                    tvMarkerDetailText = ParseUser.getCurrentUser().getUsername();
                    break;
                case TAG_CLOSEST_USER:
                    tvMarkerNameText = closestUser.getString(KEY_USER_FIRST_NAME);
                    tvMarkerDetailText = closestUser.getUsername();
                    break;
                default:
                    Place place = nearbyPlaces.get((Integer) marker.getTag());
                    tvMarkerNameText = place.getName();
                    tvMarkerDetailText = place.getCategory();
                    btnCreateHangout.setVisibility(View.VISIBLE);
                    btnCreateHangout.setText(String.format(Locale.US, getResources().getString(R.string.create_quick_hangout_at_place), place.getName()));
                    break;
            }
        }
        tvMarkerName.setText(tvMarkerNameText);
        tvMarkerDetail.setText(tvMarkerDetailText);
        return false;
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
            Toast.makeText(mContext, R.string.map_error_location, Toast.LENGTH_SHORT).show();
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
            Toast.makeText(mContext, R.string.map_error_save, Toast.LENGTH_SHORT).show();
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
                setMarker(currentUser, ParseUser.getCurrentUser().getUsername(), BitmapDescriptorFactory.defaultMarker(currentUserMarkerHue))
                        .setTag("current user");
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
        query.setLimit(2);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> nearUsers, ParseException e) {
                if (e == null) {
                    closestUser = ParseUser.getCurrentUser();
                    for (ParseUser nearbyUser : nearUsers) {
                        if (!nearbyUser.getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                            closestUser = nearbyUser;
                        }
                    }
                    double distance = getCurrentUserParseLocation().distanceInMilesTo(closestUser.getParseGeoPoint(KEY_USER_LOCATION));
                    Toast.makeText(mContext, String.format(Locale.US, getResources().getString(R.string.showClosestUser), closestUser.getUsername(), Math.round(distance * 100.0) / 100.0), Toast.LENGTH_LONG).show();
                    LatLng closestUserLocation = new LatLng(closestUser.getParseGeoPoint(KEY_USER_LOCATION).getLatitude(), closestUser.getParseGeoPoint(KEY_USER_LOCATION).getLongitude());
                    setMarker(closestUserLocation, closestUser.getUsername(), BitmapDescriptorFactory.defaultMarker(userMarkerHue))
                            .setTag(TAG_CLOSEST_USER);
                    moveCamera(closestUserLocation, INITIAL_ZOOM);
                } else {
                    Log.d(TAG, "Error: " + e.getMessage());
                }
            }
        });
        ParseQuery.clearAllCachedResults();
    }

    public void showPlacesInMap() {
        nearbyPlaces = ((MainActivity) mContext).getPlaceList();
        for (Place place : nearbyPlaces) {
            showPlaceInMap(place);
        }
    }


    /**
     * Finding and creating a marker in the map showing the closest store to the current user
     * Zoom the map to the closestPlaceLocation
     */
    public void showClosestPlace() {
        List<Place> nearbyPlaces = ((MainActivity) mContext).getPlaceList();
        Place closestPlace = nearbyPlaces.get(0);
        showPlaceInMap(closestPlace);
        double distance = getCurrentUserParseLocation().distanceInMilesTo(closestPlace.getLocation());
        Toast.makeText(mContext, String.format(Locale.US, getResources().getString(R.string.showClosestPlace), closestPlace.getName(), Math.round(distance * 100.0) / 100.0), Toast.LENGTH_SHORT).show();
    }


    /**
     * Retrieves User location from database and places marker.
     *
     * @param hangoutUser ParseUser to create marker for.
     */
    private void showUserInMap(ParseUser hangoutUser) {
        LatLng hangoutUserLocation = new LatLng(hangoutUser.getParseGeoPoint(KEY_USER_LOCATION).getLatitude(), hangoutUser.getParseGeoPoint(KEY_USER_LOCATION).getLongitude());
        setMarker(hangoutUserLocation, hangoutUser.getUsername(), BitmapDescriptorFactory.defaultMarker(userMarkerHue));
    }

    /**
     * Retrieves Place location from database and places marker.
     * Tags marker with index of hangoutPlace in nearbyPlaces.
     *
     * @param hangoutPlace Place to create marker for.
     */
    private void showPlaceInMap(Place hangoutPlace) {
        ParseGeoPoint hangoutGeopoint = hangoutPlace.getLocation();
        LatLng hangoutPlaceLocation = new LatLng(hangoutGeopoint.getLatitude(), hangoutGeopoint.getLongitude());
        setMarker(hangoutPlaceLocation, hangoutPlace.getName(), BitmapDescriptorFactory.defaultMarker(placeMarkerHue)).setTag(nearbyPlaces.indexOf(hangoutPlace));
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
    private Marker setMarker(LatLng latLng, String title, BitmapDescriptor icon) {
        return mGoogleMap.addMarker(new MarkerOptions().position(latLng).title(title).icon(icon));
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