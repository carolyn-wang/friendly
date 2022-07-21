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
import android.widget.ListView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.friendly.R;
import com.example.friendly.activities.MainActivity;
import com.example.friendly.activities.PreferencesActivity;
import com.parse.ParseUser;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.List;

public class AvailabilityAdapter extends BaseAdapter {
    private Context mContext;
    private static final String TAG = "AvailabilityAdapter";
    private List<Boolean> availabilityPreference;
    private String[] timeOptionsArray;
    private int startIndex;
    private int endIndex;
    private List<Boolean> availabilityPreferenceForDay;

    public AvailabilityAdapter(Context context, List<Boolean> availabilityPreference, int dayOfWeek) {
        this.mContext = context;
        this.timeOptionsArray = ((MainActivity) mContext).getResources().getStringArray(R.array.time_options_array);
        this.availabilityPreference = availabilityPreference;
        startIndex = timeOptionsArray.length * dayOfWeek;
        endIndex = startIndex + timeOptionsArray.length;
        availabilityPreferenceForDay = availabilityPreference.subList(startIndex, endIndex);
    }

    @Override
    public int getCount() {
        return timeOptionsArray.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView view;
        if (convertView == null) {
            view = new TextView(mContext);
            Log.i(TAG, position + " ");
            view.setOnTouchListener(new GridTouchListener());
            view.setOnDragListener(new GridDragListener());
        } else {
            view = (TextView) convertView;
        }
        view.setText(timeOptionsArray[position]);
        // set user's previous time preferences as highlighted
        if (availabilityPreferenceForDay.get(position)) {
            view.setActivated(true);
            view.setBackgroundColor(view.getResources().getColor(R.color.light_tan));
        } else {
            view.setActivated(false);
            view.setBackgroundColor(view.getResources().getColor(R.color.white));
        }
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
            outShadowSize.set(1, 1);
            outShadowTouchPoint.set(0, 0);
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