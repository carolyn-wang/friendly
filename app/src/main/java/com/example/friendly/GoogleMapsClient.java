package com.example.friendly;

import android.util.Log;

import androidx.arch.core.internal.SafeIterableMap;

import com.parse.Parse;
import com.parse.ParseGeoPoint;


import java.io.IOException;
import java.util.Dictionary;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

public class GoogleMapsClient {
    private static final String TAG = "GoogleMapsClient";
    public static final String SCHEME = "https";
    public static final String REST_URL = "maps.googleapis.com"; // Change this, base API URL
    private static final String MAPS_API_KEY = Parse.getApplicationContext().getResources().getString(R.string.google_maps_api_key);
    private HttpUrl.Builder baseUrl;
    private OkHttpClient client;

    public GoogleMapsClient() {
        client = new OkHttpClient().newBuilder()
                .build();

        baseUrl = new HttpUrl.Builder()
                .scheme(SCHEME)
                .host(REST_URL)
                .addQueryParameter("key", MAPS_API_KEY);

    }

    public Request getNearbyPlaces() throws IOException {
        HttpUrl nearbyUrl = baseUrl.addPathSegment("maps/api/place/nearbysearch/json")
                .addQueryParameter("location", "-33.8670522%2C151.1957362&")
                .addQueryParameter("radius", "1500")
                .addQueryParameter("type", "restaurant")
                .build();
        Request request = new Request.Builder()
                .url(nearbyUrl)
                .get()
                .build();
        return request;
//        Log.i(TAG, request.toString());
    }

}