package com.example.friendly.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.friendly.activities.MainActivity;
import com.example.friendly.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.adapters.PreferencesAdapter;
import com.parse.ParseUser;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    private static final String TAG = "ProfileFragment";
    private static final String KEY_USER_FIRST_NAME = "firstName";
    private Context mContext;
    private MainActivity mActivity;

    private Button btnLogout;
    private Button btnHangoutHistory;
    private Button btnChangePreferences;
    private TextView tvUsername;
    private TextView tvName;

    public ProfileFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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

        if (ParseUser.getCurrentUser() != null){
            tvUsername.setText(ParseUser.getCurrentUser().getUsername());
            tvName.setText(ParseUser.getCurrentUser().getString(KEY_USER_FIRST_NAME));
        }
        
        btnHangoutHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.displayFragmentHangoutHistory(mActivity.getSupportFragmentManager());
            }
        });

        btnChangePreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.goPreferencesActivity(mActivity);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logOut();
                NavigationUtils.goLoginActivity(mActivity);
            }
        });

    }
}