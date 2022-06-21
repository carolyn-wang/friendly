package com.example.friendly.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.example.friendly.Preference;
import com.example.friendly.PreferencesAdapter;
import com.example.friendly.R;

import java.util.ArrayList;
import java.util.List;

public class PreferencesActivity extends AppCompatActivity {

    private static final String TAG = "PreferencesActivity";
    protected static final int POSTS_TO_LOAD = 5;
    private Context mContext;
    private RecyclerView rvPreferences;
    protected PreferencesAdapter adapter;
    protected List<Preference> allPreferences;
//    private SwipeRefreshLayout swipeContainer;
//    private EndlessRecyclerViewScrollListener scrollListener;
    private View preference1;
    private TextView tvTest;
    protected int scrollCounter;

    private TextView tvQuestion;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        mContext = this;
        rvPreferences = findViewById(R.id.rvPreferences);
//        tvQuestion = findViewById(R.id.tvQuestion);
        scrollCounter = 0;

        allPreferences = new ArrayList<>();
        Preference poll0 = new Preference("Hobbies", new String[]{"hiking", "skiing"});
//        Preference poll1 = new Preference("Hobbies2", new String[]{"art", "dance"});
        allPreferences.add(poll0);
//        allPreferences.add(poll1);
        adapter = new PreferencesAdapter(mContext, allPreferences);
//        Log.i(TAG, "adapting");
        rvPreferences.setAdapter(adapter);
        adapter.notifyDataSetChanged();



        rvPreferences.setLayoutManager(new LinearLayoutManager(mContext));

//        swipeContainer = (SwipeRefreshLayout) view.findViewById(R.id.swipeContainer);
//        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {

//        rvPreferences.addOnScrollListener(scrollListener);

        }
}