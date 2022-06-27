import static android.provider.Settings.System.getString;

import com.example.friendly.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.maps.GeoApiContext;
import com.google.maps.GeocodingApi;
import com.google.maps.model.GeocodingResult;
import com.parse.Parse;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;

//package com.example.friendly;
//
//import java.io.IOException;
//
//import okhttp3.MediaType;
//import okhttp3.OkHttpClient;
//import okhttp3.Request;
//import okhttp3.RequestBody;
//import okhttp3.Response;
//
public class GoogleMapsClient {
    final private static String MAPS_API_KEY = null;
    public GoogleMapsClient() {
    }

    public getNearby(){
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIza...")
                .build();
        GeocodingResult[] results =  GeocodingApi.geocode(context,
                "1600 Amphitheatre Parkway Mountain View, CA 94043").await();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(results[0].addressComponents));

// Invoke .shutdown() after your application is done making requests
        context.shutdown();
    }

/*
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
    */

//    OkHttpClient client = new OkHttpClient().newBuilder()
//            .build();
//    MediaType mediaType = MediaType.parse("text/plain");
//    RequestBody body = RequestBody.create(mediaType, "");
//    Request request = new Request.Builder()
//            .url("https://maps.googleapis.com/maps/api/place/nearbysearch/json?location=-33.8670522%2C151.1957362&radius=1500&type=restaurant&keyword=cruise&key=YOUR_API_KEY")
//            .method("GET", body)
//            .build();
//    Response response;
//
//    {
//        try {
//            response = client.newCall(request).execute();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
}
