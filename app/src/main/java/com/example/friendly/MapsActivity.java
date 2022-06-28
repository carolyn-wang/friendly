package com.example.friendly;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.os.Bundle;

import com.example.friendly.activities.MainActivity;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.friendly.databinding.ActivityGoogleMapsBinding;

// Android Dependencies
import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
//import android.support.annotation.NonNull;
//import android.support.v4.app.ActivityCompat;
//import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
// Google Maps Dependencies
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
// Parse Dependencies
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
// Java dependencies
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityGoogleMapsBinding binding;
    private Context mContext;

    private static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
        mContext = getBaseContext();


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

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

        saveCurrentUserLocation();

        ParseGeoPoint currLocation = getCurrentUserLocation();
        double lat = currLocation.getLatitude();
        double lng = currLocation.getLongitude();
        LatLng latLng = new LatLng(lat, lng);

        mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in current location"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

//        showCurrentUserInMap(googleMap);
//        showClosestUser(googleMap);
        showPlacesInMap(googleMap);
//        showClosestPlace(googleMap);
    }

    private void saveCurrentUserLocation() {
        // requesting permission to get user's location
        if(ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(mContext, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else {
            // getting last know user's location
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);

            // checking if the location is null
            if(location != null){
                // if it isn't, save it to Back4App Dashboard
                ParseGeoPoint currentUserLocation = new ParseGeoPoint(location.getLatitude(), location.getLongitude());

                ParseUser currentUser = ParseUser.getCurrentUser();

                if (currentUser != null) {
                    currentUser.put("Location", currentUserLocation);
                    currentUser.saveInBackground();
                } else {
                    // do something like coming back to the login activity
                }
            }
            else {
                // if it is null, do something like displaying error and coming back to the menu activity
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case REQUEST_LOCATION:
                saveCurrentUserLocation();
                break;
        }
    }


    private ParseGeoPoint getCurrentUserLocation(){

        /* saving the current user location, using the saveCurrentUserLocation method of Step 3, to avoid
null pointer exception and also to return the user's most current location */
        //save used to be outside of get Curr User location
        saveCurrentUserLocation();

        // finding currentUser
        ParseUser currentUser = ParseUser.getCurrentUser();

        if (currentUser == null) {
            // if it's not possible to find the user, do something like returning to login activity
        }
        // otherwise, return the current user location
        return currentUser.getParseGeoPoint("Location");

    }

    private void showCurrentUserInMap(final GoogleMap googleMap){

        // calling retrieve user's location method of Step 4
        ParseGeoPoint currentUserLocation = getCurrentUserLocation();

        // creating a marker in the map showing the current user location
        LatLng currentUser = new LatLng(currentUserLocation.getLatitude(), currentUserLocation.getLongitude());
        googleMap.addMarker(new MarkerOptions().position(currentUser).title(ParseUser.getCurrentUser().getUsername()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));

        // zoom the map to the currentUserLocation
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentUser, 5));
    }

    private void showClosestUser(final GoogleMap googleMap){
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereNear("Location", getCurrentUserLocation());
        // setting the limit of near users to find to 2, you'll have in the nearUsers list only two users: the current user and the closest user from the current
        query.setLimit(2);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override  public void done(List<ParseUser> nearUsers, ParseException e) {
                if (e == null) {
                    // avoiding null pointer
                    ParseUser closestUser = ParseUser.getCurrentUser();
                    // set the closestUser to the one that isn't the current user
                    for(int i = 0; i < nearUsers.size(); i++) {
                        if(!nearUsers.get(i).getObjectId().equals(ParseUser.getCurrentUser().getObjectId())) {
                            closestUser = nearUsers.get(i);
                        }
                    }
                    // finding and displaying the distance between the current user and the closest user to him, using method implemented in Step 4
                    double distance = getCurrentUserLocation().distanceInKilometersTo(closestUser.getParseGeoPoint("Location"));
                    alertDisplayer("We found the closest user from you!", "It's " + closestUser.getUsername() + ". \n You are " + Math.round (distance * 100.0) / 100.0  + " km from this user.");
                    // showing current user in map, using the method implemented in Step 5
                    showCurrentUserInMap(mMap);
                    // creating a marker in the map showing the closest user to the current user location
                    LatLng closestUserLocation = new LatLng(closestUser.getParseGeoPoint("Location").getLatitude(), closestUser.getParseGeoPoint("Location").getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(closestUserLocation).title(closestUser.getUsername()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    // zoom the map to the currentUserLocation
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(closestUserLocation, 3));
                } else {
                    Log.d("store", "Error: " + e.getMessage());
                }
            }
        });
        ParseQuery.clearAllCachedResults();
    }


    // todo: move to utils
    private void alertDisplayer(String title,String message){
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(mContext)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog ok = builder.create();
        ok.show();
    }



    private void showPlacesInMap(final GoogleMap googleMap){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("Place");
        query.whereExists("Location");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override  public void done(List<ParseObject> stores, ParseException e) {
                if (e == null) {
                    for(int i = 0; i < stores.size(); i++) {
                        LatLng storeLocation = new LatLng(stores.get(i).getParseGeoPoint("Location").getLatitude(), stores.get(i).getParseGeoPoint("Location").getLongitude());
                        googleMap.addMarker(new MarkerOptions().position(storeLocation).title(stores.get(i).getString("Name")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
                    }
                } else {
                    // handle the error
                    Log.d("Place", "Error: " + e.getMessage());
                }
            }
        });
        ParseQuery.clearAllCachedResults();
    }


    private void showClosestPlace(final GoogleMap googleMap){
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Place");
        query.whereNear("Location", getCurrentUserLocation());
        // setting the limit of near stores to 1, you'll have in the nearStores list only one object: the closest store from the current user
        query.setLimit(1);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override  public void done(List<ParseObject> nearStores, ParseException e) {
                if (e == null) {
                    ParseObject closestPlace = nearStores.get(0);
                    // showing current user location, using the method implemented in Step 5
                    showCurrentUserInMap(mMap);
                    // finding and displaying the distance between the current user and the closest store to him, using method implemented in Step 4
                    double distance = getCurrentUserLocation().distanceInKilometersTo(closestPlace.getParseGeoPoint("Location"));
                    alertDisplayer("We found the closest place from you!", "It's " + closestPlace.getString("Name") + ". \nYou are " + Math.round (distance * 100.0) / 100.0  + " km from this store.");
                    // creating a marker in the map showing the closest store to the current user
                    LatLng closestPlaceLocation = new LatLng(closestPlace.getParseGeoPoint("Location").getLatitude(), closestPlace.getParseGeoPoint("Location").getLongitude());
                    googleMap.addMarker(new MarkerOptions().position(closestPlaceLocation).title(closestPlace.getString("Name")).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN)));
                    // zoom the map to the closestPlaceLocation
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(closestPlaceLocation, 3));
                } else {
                    Log.d("store", "Error: " + e.getMessage());
                }
            }
        });

        ParseQuery.clearAllCachedResults();

    }
}