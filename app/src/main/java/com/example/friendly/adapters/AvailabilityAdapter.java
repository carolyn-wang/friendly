package com.example.friendly.adapters;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.friendly.R;

public class AvailabilityAdapter extends BaseAdapter {
    private Context mContext;
    private boolean[] availability = new boolean[]{false, false, false, false, false, false, false, false, false, false, false, false, false, false,
            false, false, false, false, false, false,
            false, false, false, false, false, false,
            false, false, false, false, false};
    private static final String TAG = "AvailabilityAdapter";
    String[] timesArray = {"9 AM - 9:30 AM", "9:30 AM - 10 AM ", "10 AM - 10:30 AM", "10:30 AM - 11 AM",
            "11 AM - 11:30 AM", "11:30 AM - 12 PM ", "12 PM - 12:30 PM", "12:30 PM - 1 PM",
            "1 PM - 1:30 PM", "1:30 PM - 2 PM", "2 PM - 2:30 PM", "2:30 PM - 3 PM",
            "3 PM - 3:30 PM", "3:30 PM - 4 PM", "4 PM - 4:30 PM", "4:30 PM - 5 PM",
            "5 PM - 5:30 PM", "5:30 PM - 6 PM", "6 PM - 6:30 PM", "6:30 PM - 7 PM",
            "7 PM - 7:30 PM", "7:30 PM - 8 PM", "8 PM - 8:30 PM", "8:30 PM - 9 PM",
            "9 PM - 9:30 PM", "9:30 PM - 10 PM", "10 PM - 10:30 PM", "10:30 PM - 11 PM",
            "11 PM - 11:30 PM", "11:30 PM - 12 PM"};

    public AvailabilityAdapter(Context context) {
        mContext = context;
    }

    public int getCount() {
        return timesArray.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view;

        if (convertView == null) {
            view = new TextView(mContext);
            //            view.setLayoutParams(new GridView.LayoutParams(150, 85));
            view.setOnTouchListener(new GridTouchListener());
            view.setOnDragListener(new GridDragListener());
        } else {
            view = (TextView) convertView;
        }
        view.setText(timesArray[position]);
        return view;
    }

    private static final class GridTouchListener implements View.OnTouchListener {
        @Override
        public boolean onTouch(View view, MotionEvent event) {
            ClipData data = ClipData.newPlainText("", "");
            View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(view);
            view.startDragAndDrop(data, shadowBuilder, view, 0);
            return true;
        }
    }

    private static final class GridDragListener implements View.OnDragListener {

        @Override
        public boolean onDrag(View view, DragEvent event) {

            final int action = event.getAction();
            switch (action) {
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_ENTERED:
                    if (view.isActivated()) {
                        view.setBackgroundColor(Color.TRANSPARENT);
                    } else {
                        view.setBackgroundColor(view.getResources().getColor(R.color.light_tan));
                    }
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    if (view.isActivated()) {
                        view.setBackgroundColor(Color.TRANSPARENT);
                        view.setActivated(false);
                    } else {
                        view.setActivated(true);
                        view.setBackgroundColor(view.getResources().getColor(R.color.light_tan));
                    }
                    return true;
                case DragEvent.ACTION_DROP:
                    if (view.isActivated()) {
                        view.setActivated(false);
                    } else {
                        view.setActivated(true);
                    }
                    return true;
                default:
                    break;
            }
            return false;

        }
    }
}