package com.example.friendly.fragments;

import android.content.ClipData;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.friendly.R;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AvailabilityFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvailabilityFragment extends Fragment {

    private static final String TAG = "AvailabilityFragment";
    private boolean[] availability = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false, false};
    private ListView lvAvailability;
    private ListView lvAvailability1;
    private ConstraintLayout availabilityLayout;
    private ConstraintLayout availabilityLayout1;


    String[] timesArray = {"9 AM - 9:30 AM", "9:30 AM - 10 AM ", "10 AM - 10:30 AM", "10:30 AM - 11 AM",
            "11 AM - 11:30 AM", "11:30 AM - 12 PM ", "12 PM - 12:30 PM", "12:30 PM - 1 PM",
            "1 PM - 2 PM", "2 PM - 3 PM", "3 PM - 4 PM", "4 PM - 5 PM", "5 PM - 6 PM", "6 PM - 7 PM",
            "7 PM - 8 PM", "8 PM - 9 PM", "9 PM - 10 PM", "10 PM - 11 PM", "11 PM - 12 AM"
    };


    public AvailabilityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_availability, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        lvAvailability = view.findViewById(R.id.availabilityTable);
        lvAvailability1 = view.findViewById(R.id.availabilityTable1);

        availabilityLayout = view.findViewById(R.id.availabilityLayout);


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(),
                R.layout.item_availability_row, timesArray);
        lvAvailability.setAdapter(adapter);
        lvAvailability1.setAdapter(adapter);

        lvAvailability.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                availability[position] = !availability[position];
                if (availability[position]) {
                    view.setBackgroundColor(getResources().getColor(R.color.light_tan));
                } else {
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        lvAvailability1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                availability[position] = !availability[position];
                if (availability[position]) {
                    view.setBackgroundColor(getResources().getColor(R.color.light_tan));
                } else {
                    view.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        lvAvailability.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                                     @Override
                                                     public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                         ClipData data = ClipData.newPlainText("", "");
                                                         View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
                                                         view.startDragAndDrop(data, shadowBuilder, view, 0);
                                                         Log.i(TAG, "select");
                                                     }

                                                     @Override
                                                     public void onNothingSelected(AdapterView<?> parent) {

                                                     }
                                                 }
        );
//        lvAvailability.setOnTouchListener(new MyTouchListener());

        availabilityLayout.setOnDragListener(new View.OnDragListener() {
            @Override
            public boolean onDrag(View view, DragEvent event) {
                final int action = event.getAction();
                switch (action) {
                    case DragEvent.ACTION_DRAG_STARTED:
                        Log.i(TAG, "drag started **");
                        return true;
                    case DragEvent.ACTION_DRAG_ENTERED:
                        Log.i(TAG, "drag entered");
                        view.setBackgroundColor(Color.GREEN);
                        return true;
                    case DragEvent.ACTION_DRAG_EXITED:
                        Log.i(TAG, "drag exited");
                        view.setBackgroundColor(Color.BLUE);
                        view.invalidate();
                        return true;
                    case DragEvent.ACTION_DROP:
                    case DragEvent.ACTION_DRAG_ENDED:

                        view.setBackgroundColor(Color.YELLOW);
                        return true;
                    default:
                        break;
                }
                return false;

            }
        });
    }


    // This defines your touch listener
    private final class MyTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDragAndDrop(data, shadowBuilder, view, 0);
            return true;
        }
    }

}