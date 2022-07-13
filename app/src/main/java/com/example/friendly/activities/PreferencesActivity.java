package com.example.friendly.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.example.friendly.NavigationUtils;
import com.example.friendly.objects.Preference;
import com.example.friendly.adapters.PreferencesAdapter;
import com.example.friendly.R;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PreferencesActivity extends AppCompatActivity {

    private static final String TAG = "PreferencesActivity";
    private static final String KEY_PREFERENCE0 = "yearPreference";
    private static final String KEY_PREFERENCE1 = "similarityPreference";
    private static final String KEY_PREFERENCE2 = "activitiesPreference";

    private Context mContext;
    private RecyclerView rvPreferences;
    private Button nextButton;
    protected PreferencesAdapter adapter;
    protected List<Preference> allPreferences;
    protected int scrollCounter;
    private ListView lvOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preferences);

        mContext = this;
        rvPreferences = findViewById(R.id.rvPreferences);
        nextButton = findViewById(R.id.nextButton);
        lvOptions = findViewById(R.id.lvOptions);

        scrollCounter = 0;

        allPreferences = Arrays.asList(
                new Preference(KEY_PREFERENCE0, getString(R.string.question0), mContext.getResources().getStringArray(R.array.option0)),
                new Preference(KEY_PREFERENCE1, getString(R.string.question1), mContext.getResources().getStringArray(R.array.option1)),
                new Preference(KEY_PREFERENCE2, getString(R.string.question2), mContext.getResources().getStringArray(R.array.option2)));

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

    public List<Preference> getAllPreferences(){
        return allPreferences;
    }

    public List<String> getAllPreferenceKeys(){
        List<String> preferenceKeys = new ArrayList<String>();
        for (int i = 0; i < allPreferences.size(); i++){
            preferenceKeys.add(allPreferences.get(i).getParseKey());
        }
        return preferenceKeys;
    }
}