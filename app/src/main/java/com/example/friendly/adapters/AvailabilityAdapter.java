package com.example.friendly.adapters;

import android.content.ClipData;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Point;
import android.util.Log;
import android.view.DragEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.example.friendly.R;
import com.example.friendly.activities.MainActivity;

public class AvailabilityAdapter extends BaseAdapter {
    private Context mContext;
    private static final String TAG = "AvailabilityAdapter";
    private String[] timesArray;

    public AvailabilityAdapter(Context context) {
        mContext = context;
        timesArray = ((MainActivity) mContext).getResources().getStringArray(R.array.times_array);
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
            view.startDragAndDrop(data, new EmptyDragShadowBuilder(), view, 0);
            return true;
        }
    }

    public static class EmptyDragShadowBuilder extends View.DragShadowBuilder {
        @Override
        public void onProvideShadowMetrics(Point outShadowSize, Point outShadowTouchPoint) {
            outShadowSize.set(1,1);
            outShadowTouchPoint.set(0,0);
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