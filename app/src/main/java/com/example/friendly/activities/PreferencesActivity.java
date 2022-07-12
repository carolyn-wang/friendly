package com.example.friendly.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.example.friendly.NavigationUtils;
import com.example.friendly.objects.Preference;
import com.example.friendly.adapters.PreferencesAdapter;
import com.example.friendly.R;
import com.example.friendly.objects.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

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
    private ListView lvOptions;

    public static void savePreference(CharSequence text) {
        User user = (User) MainActivity.getCurrentUser();
        user.setPreference1(text.toString());
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                Log.i(TAG, "saved preference");
            }
        });
    }

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
                new Preference(getString(R.string.question0), mContext.getResources().getStringArray(R.array.option0)),
                new Preference(getString(R.string.question1), mContext.getResources().getStringArray(R.array.option1)),
                new Preference(getString(R.string.question2), mContext.getResources().getStringArray(R.array.option2)));

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