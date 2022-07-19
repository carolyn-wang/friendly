package com.example.friendly.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.friendly.R;
import com.example.friendly.adapters.AvailabilityAdapter;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AvailabilityList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvailabilityList extends Fragment {

    private static final String TAG = "AvailabilityList";
    private boolean[] availability = new boolean[30];
    private GridView availabilityGrid;



    public AvailabilityList() {
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

        AvailabilityAdapter adapter = new AvailabilityAdapter(getContext());
        availabilityGrid.setAdapter(adapter);

    }

}