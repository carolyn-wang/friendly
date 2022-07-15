package com.example.friendly.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.friendly.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.fragments.ProfileFragment;
import com.example.friendly.fragments.SearchFragment;
import com.example.friendly.fragments.match.MatchFragment;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.parse.ParseUser;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Context mContext;

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private MaterialToolbar topAppBar;
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getBaseContext();

        topAppBar = findViewById(R.id.topAppBar);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.onBackPressed(fragmentManager);
            }
        });

//        getSupportFragmentManager().addOnBackStackChangedListener(
//                new FragmentManager.OnBackStackChangedListener() {
//                    public void onBackStackChanged() {
//                NavigationUtils.onBackPressed(fragmentManager);
//                    }
//                });


//        // This callback will only be called when MyFragment is at least Started.
//        OnBackPressedCallback callback = new OnBackPressedCallback(true /* enabled by default */) {
//            @Override
//            public void handleOnBackPressed() {
//                NavigationUtils.onBackPressed(fragmentManager);
//            }
//        };
//        MainActivity.this.getOnBackPressedDispatcher().addCallback(this, callback);

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.edit:
                        fragment = new SearchFragment();
                        break;
                    case R.id.favorite:
                        fragment = new MatchFragment();
                        break;
                    case R.id.more:
                        fragment = new ProfileFragment();
                        break;
                    default: return true;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).addToBackStack(null).commit();
                return true;
            }
        });

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
                        fragment = new ProfileFragment();
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
}