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
import android.widget.Button;
import android.widget.TextView;

import com.example.friendly.NavigationUtils;
import com.example.friendly.Preference;
import com.example.friendly.PreferencesAdapter;
import com.example.friendly.R;

import java.util.ArrayList;
import java.util.List;

public class PreferencesActivity extends AppCompatActivity {

    private static final String TAG = "PreferencesActivity";
    private Context mContext;
    private RecyclerView rvPreferences;
    private Button nextButton;
    protected PreferencesAdapter adapter;
    protected List<Preference> allPreferences;
    protected int scrollCounter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        mContext = this;
        rvPreferences = findViewById(R.id.rvPreferences);
        // TODO: put next button at bottom of RV
        nextButton = findViewById(R.id.nextButton);
        scrollCounter = 0;

        allPreferences = new ArrayList<>();
        Preference poll0 = new Preference("Hobbies", new String[]{"hiking", "skiing"});
        Preference poll1 = new Preference("Hobbies1", new String[]{"art", "dance"});
        Preference poll2 = new Preference("Hobbies2", new String[]{"hiking", "skiing"});
        Preference poll3 = new Preference("Hobbies3", new String[]{"art", "dance"});
        Preference poll4 = new Preference("Hobbies4", new String[]{"hiking", "skiing"});
        Preference poll5 = new Preference("Hobbies5", new String[]{"art", "dance"});
        Preference poll6 = new Preference("Hobbies6", new String[]{"hiking", "skiing"});
        Preference poll7 = new Preference("Hobbies7", new String[]{"art", "dance"});
        allPreferences.add(poll0);
        allPreferences.add(poll1);
        allPreferences.add(poll2);
        allPreferences.add(poll3);
        allPreferences.add(poll4);
        allPreferences.add(poll5);
        allPreferences.add(poll6);
        allPreferences.add(poll7);
        adapter = new PreferencesAdapter(mContext, allPreferences);
        rvPreferences.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        rvPreferences.setLayoutManager(new LinearLayoutManager(mContext));

        nextButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavigationUtils.goMainActivity(PreferencesActivity.this);
            }
        });

    }
}