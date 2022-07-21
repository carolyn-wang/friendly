package com.example.friendly.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.friendly.R;
import com.example.friendly.adapters.AvailabilityAdapter;
import com.parse.ParseUser;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AvailabilityList#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AvailabilityList extends Fragment {

    private static final String TAG = "AvailabilityList";
    private static final String KEY_AVAILABILITY_PREFERENCE = "availabilityPreference";
    private Context mContext;
    private List<Boolean> userAvailabilityPreference;
    private ListView availabilityList;
    private int dayOfWeek;

    public AvailabilityList(Context mContext, int dayOfWeek) {
        this.mContext = mContext;
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.list_availability, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        availabilityList = view.findViewById(R.id.availabilityGrid);

        userAvailabilityPreference = ParseUser.getCurrentUser().getList(KEY_AVAILABILITY_PREFERENCE);
        AvailabilityAdapter adapter = new AvailabilityAdapter(mContext, userAvailabilityPreference, dayOfWeek);
        availabilityList.setAdapter(adapter);
    }

}