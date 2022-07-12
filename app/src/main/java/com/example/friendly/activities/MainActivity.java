package com.example.friendly.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.friendly.R;
import com.example.friendly.fragments.ProfileFragment;
import com.example.friendly.fragments.SearchFragment;
import com.example.friendly.fragments.match.MatchFragment;
<<<<<<< HEAD
=======
import com.example.friendly.fragments.ProfileFragment;
import com.example.friendly.objects.User;
>>>>>>> 8815daa (saving progress; unable to save preference changes to database)
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Context mContext;
    private static User currentUser;

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getBaseContext();

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
<<<<<<< HEAD
=======

    public static void setCurrentUser(User user){
        currentUser = user;
    }
    public static User getCurrentUser() {
        return currentUser;
    }
>>>>>>> 8815daa (saving progress; unable to save preference changes to database)
}