package com.example.friendly.activities;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.friendly.utils.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.adapters.PreferencesAdapter;
import com.example.friendly.objects.Preference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreferencesActivity extends AppCompatActivity {

    private static final String TAG = "PreferencesActivity";
    private static final String KEY_PREFERENCE0 = "yearPreference";
    private static final String KEY_PREFERENCE1 = "similarityPreference";
    private static final String KEY_PREFERENCE2 = "hobbyPreference";
    private static final String KEY_PREFERENCE3 = "activityPreference";

    private Context mContext;
    private RecyclerView rvPreferences;
    private Button nextButton;
    private PreferencesAdapter adapter;
    private List<Preference> allPreferences;

    protected int scrollCounter;
    private List<String> preferenceKeys;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        mContext = this;
        rvPreferences = findViewById(R.id.rvPreferences);
        nextButton = findViewById(R.id.nextButton);

        scrollCounter = 0;
        allPreferences = Arrays.asList(
                new Preference(KEY_PREFERENCE0, getString(R.string.question0), getResources().getStringArray(R.array.option0)),
                new Preference(KEY_PREFERENCE1, getString(R.string.question1), getResources().getStringArray(R.array.option1)),
                new Preference(KEY_PREFERENCE2, getString(R.string.question2), getResources().getStringArray(R.array.option2)),
                new Preference(KEY_PREFERENCE3, getString(R.string.question3), getResources().getStringArray(R.array.option3)));
        preferenceKeys = new ArrayList<>();
        for (Preference preference : allPreferences) {
            preferenceKeys.add(preference.getParseKey());
        }

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

    public List<Preference> getAllPreferences() {
        return allPreferences;
    }

    public List<String> getAllPreferenceKeys() {
        return preferenceKeys;
    }

}