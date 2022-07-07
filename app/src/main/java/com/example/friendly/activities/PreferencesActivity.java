package com.example.friendly.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.friendly.NavigationUtils;
import com.example.friendly.objects.Preference;
import com.example.friendly.adapters.PreferencesAdapter;
import com.example.friendly.R;

import java.util.ArrayList;
import java.util.Arrays;
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
        nextButton = findViewById(R.id.nextButton);
        scrollCounter = 0;

        allPreferences = Arrays.asList(
                new Preference(getString(R.string.question0), new String[]{"Year 1", "Year 2", "Year 3", "Year 4", "Year 5+ (Masters/Postgrad)"}),
                new Preference(getString(R.string.question1), new String[]{"Very Similar", "Somewhat similar", "Neutral (either works)", "Somewhat different", "Very Different"}),
                new Preference(getString(R.string.question2), new String[]{"gaming", "reading", "hiking", "gym", "art", "music", "anime", "sports", "politics"}));

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