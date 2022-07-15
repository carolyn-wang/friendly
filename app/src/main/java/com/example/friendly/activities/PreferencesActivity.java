package com.example.friendly.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.friendly.NavigationUtils;
import com.example.friendly.objects.Preference;
import com.example.friendly.adapters.PreferencesAdapter;
import com.example.friendly.R;
import com.michaelflisar.dragselectrecyclerview.DragSelectTouchListener;
import com.michaelflisar.dragselectrecyclerview.DragSelectionProcessor;

import java.util.ArrayList;
import java.util.HashSet;
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

        allPreferences = new ArrayList<>();
        Preference poll0 = new Preference("What are your hobbies?", new String[]{"gaming", "reading", "hiking", "gym", "art", "music"});
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

        DragSelectionProcessor mDragSelectionProcessor;

        mDragSelectionProcessor = new DragSelectionProcessor(new DragSelectionProcessor.ISelectionHandler() {
            @Override
            public HashSet<Integer> getSelection() {
//                return adapter.getSelection();
                Log.i(TAG, "getSelection");
                return new HashSet<>();
            }

            @Override
            public boolean isSelected(int index) {
//                return adapter.getSelection().contains(index);
                return false;
            }

            @Override
            public void updateSelection(int start, int end, boolean isSelected, boolean calledFromOnStart) {
//                mAdapter.selectRange(start, end, isSelected);
            }
        });
        DragSelectTouchListener mDragSelectTouchListener = new DragSelectTouchListener()
                .withSelectListener(mDragSelectionProcessor);
//        updateSelectionListener();
        rvPreferences.addOnItemTouchListener(mDragSelectTouchListener);


    // ---------------------
    // Selection Listener
    // ---------------------

//    private void updateSelectionListener() {
//        mDragSelectionProcessor.withMode(mMode);
//        mToolbar.setSubtitle("Mode: " + mMode.name());
//    }
//
//
//        nextButton.setOnClickListener(new View.OnClickListener()
//
//    {
//        @Override
//        public void onClick (View v){
//        NavigationUtils.goMainActivity(PreferencesActivity.this);
//    }
//    });

}
}