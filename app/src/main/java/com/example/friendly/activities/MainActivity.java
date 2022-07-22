package com.example.friendly.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.example.friendly.objects.Hangout;
import com.example.friendly.utils.NavigationUtils;
import com.example.friendly.query.PlaceQuery;
import com.example.friendly.R;
import com.example.friendly.fragments.MapFragment;
import com.example.friendly.fragments.SearchFragment;
import com.example.friendly.fragments.match.CreateQuickMatchFragment;
import com.example.friendly.fragments.match.MatchFragment;
import com.example.friendly.objects.Place;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.parse.ParseException;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private Context mContext;

    final FragmentManager fragmentManager = getSupportFragmentManager();
    private MaterialToolbar topAppBar;
    private BottomNavigationView bottomNavigationView;
    private Fragment fragment;
    private PlaceQuery placeQuery = new PlaceQuery();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = getBaseContext();

        placeQuery.queryNearbyPlaces();

        topAppBar = findViewById(R.id.topAppBar);
        bottomNavigationView = findViewById(R.id.bottomNavigation);

        topAppBar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.onBackPressed(fragmentManager);
            }
        });

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.map:
                        fragment = new MapFragment();
                        break;
                    case R.id.create:
                        fragment = new CreateQuickMatchFragment();
                        break;
                    default:
                        fragment = new MatchFragment();
                        return true;
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
                        NavigationUtils.displayFragmentProfile(getSupportFragmentManager());
                        return true;
                    default: return true;
                }
                fragmentManager.beginTransaction().replace(R.id.flContainer, fragment).addToBackStack(null).commit();
                return true;
            }


        });

        // Set default fragment
        bottomNavigationView.setSelectedItemId(R.id.action_match);
    }

    public List<Place> getPlaceList(){
        return placeQuery.getNearbyPlaces();
    }
}