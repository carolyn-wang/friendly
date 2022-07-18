package com.example.friendly.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.example.friendly.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AvailabilityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvailabilityFragment extends Fragment {

    private static final String TAG = "AvailabilityFragment";

    private TableRow row1;
    private TableLayout availabilityTable;


    public AvailabilityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_availability, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        availabilityTable = view.findViewById(R.id.availabilityTable);

        row1 = view.findViewById(R.id.row1);

        row1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                row1.setTag(row1.getId());
                row1.setBackgroundColor(Color.BLUE);
                Log.i(TAG, String.valueOf(row1.getId()));
            }
        });
    }
}