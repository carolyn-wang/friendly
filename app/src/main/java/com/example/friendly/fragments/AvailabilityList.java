package com.example.friendly.fragments;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.friendly.R;
import com.example.friendly.activities.MainActivity;
import com.example.friendly.adapters.AvailabilityAdapter;
import com.example.friendly.objects.Place;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AvailabilityList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvailabilityList extends Fragment {

    private static final String TAG = "AvailabilityList";
    private List<Boolean> userAvailabilityPreference;
    private ListView availabilityGrid;
    private int dayOfWeek;

    public AvailabilityList(Context mContext, int dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_availability, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        availabilityGrid = view.findViewById(R.id.availabilityGrid);

        userAvailabilityPreference = ParseUser.getCurrentUser().getList("availabilityPreference");
        AvailabilityAdapter adapter = new AvailabilityAdapter(getContext(), userAvailabilityPreference, dayOfWeek);
        availabilityGrid.setAdapter(adapter);
    }

}