package com.example.friendly;

import android.util.Log;

import androidx.arch.core.internal.SafeIterableMap;

import com.parse.Parse;
import com.parse.ParseGeoPoint;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class GoogleMapsClient {
    private static final String MAPS_API_KEY = Parse.getApplicationContext().getResources().getString(R.string.google_maps_api_key);
    private static final String TAG = "GoogleMapsClient";

    private HttpUrl mySearchUrl;
    OkHttpClient client;

    public GoogleMapsClient() {
//        client = new OkHttpClient().newBuilder()
//            .build();
//        mySearchUrl = new HttpUrl.Builder()
//                .scheme("https")
//                .host("maps.googleapis.com")
//                .addPathSegment("maps/api/place/nearbysearch/json")
//                .addQueryParameter("location", "-33.8670522%2C151.1957362&")
//                .addQueryParameter("radius", "1500")
//                .addQueryParameter("type", "restaurant")
//                .addQueryParameter("key", MAPS_API_KEY)
//                .build();
    }

    public void getNearbyPlaces() throws IOException {
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
//        MediaType mediaType = MediaType.parse("text/plain");
//        RequestBody body = RequestBody.create(mediaType, "");
//        Request request = new Request.Builder()
//                .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522%2C151.1957362&radius=1500&type=restaurant&keyword=cruise&key="+MAPS_API_KEY)
//                .method("GET", body)
//                .build();
//        Response response = client.newCall(request).execute();

    }

}
