package com.example.friendly.fragments;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.friendly.GoogleMapsClient;
import com.example.friendly.activities.MainActivity;
import com.example.friendly.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.adapters.HangoutsAdapter;
import com.example.friendly.adapters.ProfileAdapter;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import javax.xml.transform.Result;

import okhttp3.Headers;
import okhttp3.Request;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private Context mContext;
    private MainActivity mActivity;
    private ProfileAdapter adapter;

    private Button btnLogout;
    private Button btnHangoutHistory;
    private Button btnChangePreferences;
    private TextView tvUsername;
    private TextView tvName;

    public ProfileFragment() {
    }

    public static ProfileFragment newInstance(ParseUser user) {

        Bundle args = new Bundle();

        ProfileFragment fragment = new ProfileFragment();
        args.putParcelable("user", user);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        mActivity = (MainActivity)mContext;
        btnLogout = view.findViewById(R.id.btnLogout);
        btnHangoutHistory = view.findViewById(R.id.btnHangoutHistory);
        btnChangePreferences = view.findViewById(R.id.btnChangePreferences);
        tvUsername = view.findViewById(R.id.tvUsername);
        tvName = view.findViewById(R.id.tvName);

        tvUsername.setText(ParseUser.getCurrentUser().getUsername());
        tvName.setText(ParseUser.getCurrentUser().getString("firstName"));


        GoogleMapsClient client = new GoogleMapsClient();
        try {
            Request result = client.getNearbyPlaces();

        } catch (IOException e) {
            e.printStackTrace();
        }

        btnHangoutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick Hangout History button");
                NavigationUtils.displayFragmentHangoutHistory(ParseUser.getCurrentUser(), mActivity.getSupportFragmentManager());
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick logout button");
                ParseUser.logOut();
                NavigationUtils.goLoginActivity(mActivity);
            }
        });

        btnChangePreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick logout button");
                ParseUser.logOut();
                NavigationUtils.goPreferencesActivity(mActivity);
            }
        });
    }
}