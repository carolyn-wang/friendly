package com.example.friendly.fragments;

import android.content.ClipData;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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
    private boolean[] availability = new boolean[]{false, false, false, false, false};

    private TableRow row1;
    private TableRow row2;
    private TableRow row3;
    private TableRow row4;
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
        row2 = view.findViewById(R.id.row2);
        row3 = view.findViewById(R.id.row3);
        row4 = view.findViewById(R.id.row4);
//        row5 = view.findViewById(R.id.row5);


//        row1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                row1.setTag(row1.getId());
//                int row_index = availabilityTable.indexOfChild(row1);
//                row1.setBackgroundColor(Color.BLUE);
//                Log.i(TAG, String.valueOf(row_index));
//            }
//        });


        row1.setOnTouchListener(new MyTouchListener());
        row2.setOnTouchListener(new MyTouchListener());
        row3.setOnTouchListener(new MyTouchListener());
        row4.setOnTouchListener(new MyTouchListener());

//        row1.setOnDragListener(new MyDragListener());
        availabilityTable.setOnDragListener(new MyDragListener());

    }

    private final class MyTouchListener implements View.OnTouchListener {
        public boolean onTouch(View view, MotionEvent motionEvent) {
            if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
//                ClipData data = ClipData.newPlainText("", "");
//                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
//                view.startDrag(data, shadowBuilder, view, 0);
                changeAvailability(view);
                return true;
            } else {
                return false;
            }
        }
    }

    /**
     * Invert color and availability for given time slot.
     * @param view Row that was just selected.
     */
    private void changeAvailability(View view) {
        int rowIndex = availabilityTable.indexOfChild(view);
        availability[rowIndex] = !availability[rowIndex];
        if (availability[rowIndex]){
            view.setBackgroundColor(getResources().getColor(R.color.light_tan));
        }
        else{
            view.setBackgroundColor(Color.TRANSPARENT);
        }
    }


    class MyDragListener implements View.OnDragListener {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            int action = event.getAction();
            switch (event.getAction()) {
                case DragEvent.ACTION_DRAG_STARTED:
                    Log.i(TAG, "drag started **");
                    // do nothing
                    break;
                case DragEvent.ACTION_DRAG_ENTERED:
                    Log.i(TAG, "drag entered");
                    break;
                case DragEvent.ACTION_DRAG_EXITED:
                    Log.i(TAG, "drag exited **");
                    break;
//                case DragEvent.ACTION_DROP:
//                    // Dropped, reassign View to ViewGroup
//                    View view = (View) event.getLocalState();
//                    ViewGroup owner = (ViewGroup) view.getParent();
//                    owner.removeView(view);
//                    LinearLayout container = (LinearLayout) v;
//                    container.addView(view);
//                    view.setVisibility(View.VISIBLE);
//                    break;
                case DragEvent.ACTION_DRAG_ENDED:
                    Log.i(TAG, "drag ended");
                default:
                    break;
            }
            return true;
        }
    }


}