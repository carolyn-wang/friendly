package com.example.friendly.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import com.example.friendly.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.fragments.SearchFragment;
import com.example.friendly.fragments.match.MatchFragment;
import com.example.friendly.fragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Context mContext;
    private Activity mActivity;

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getBaseContext();
        mActivity = this;

        bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.action_search:
                        fragment = new SearchFragment();
                        break;
                    case R.id.action_match:
                        fragment = new MatchFragment();
                        break;
                    case R.id.action_profile:
                        fragment = new ProfileFragment().newInstance(ParseUser.getCurrentUser());
                        break;
                    default: return true;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).commit();
                return true;
            }


        });
        // Set default fragment
        bottomNavigationView.setSelectedItemId(R.id.action_match);
    }

    /***
     * Composes tweet and refreshes timeline to show new tweet
     * @param v View passed in by onClick call in xml file
     */
    public void createQuickHangout(View v) {
        NavigationUtils.displayCreateQuickMatch(ParseUser.getCurrentUser(), fragmentManager);
    }

}