package com.example.friendly.fragments.match;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.friendly.utils.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.activities.MainActivity;
import com.example.friendly.objects.Hangout;
import com.example.friendly.objects.Place;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateQuickMatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateQuickMatchFragment extends Fragment {

    private Context mContext;
    private Activity mActivity;
    private static final String TAG = "CreateQuickMatchFragment";

    private Calendar calendar;
    private AutoCompleteTextView autoCompletePlaces;
    private TextInputEditText editTextDate;
    private TextInputEditText editTextTime;
    private Button btnCreateHangout;

    private static final boolean is24HView = false;
    private int lastSelectedHour = 0;
    private int lastSelectedMinute = 0;
    private static SimpleDateFormat dateFormat;
    private static SimpleDateFormat timeFormat;

    private String[] placeNameArray;
    private List<Place> placeList = new ArrayList<>();

    public CreateQuickMatchFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_quick_match, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mContext = getContext();
        mActivity = getActivity();

        placeList = ((MainActivity) mContext).getPlaceList();
        placeNameArray = ((MainActivity) mContext).getPlaceNames();

        autoCompletePlaces = view.findViewById(R.id.autoCompletePlaces);
        editTextDate = view.findViewById(R.id.editTextDate);
        editTextTime = view.findViewById(R.id.editTextTime);
        btnCreateHangout = view.findViewById(R.id.btnCreateHangout);
        dateFormat = new SimpleDateFormat(mContext.getString(R.string.dateFormat), Locale.US);
        timeFormat = new SimpleDateFormat(mContext.getString(R.string.timeFormat), Locale.US);

//        Places Autocomplete
        ArrayAdapter<String> adapter = new ArrayAdapter<>(mContext,
                android.R.layout.select_dialog_item, placeNameArray);
        autoCompletePlaces.setAdapter(adapter);
        autoCompletePlaces.setThreshold(1); //Autocomplete will start working from first character

//        Date Floating Dialog
        calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String formattedDate = dateFormat.format(calendar.getTime());
                editTextDate.setText(formattedDate);
            }
        };
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(mContext, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

//        Time Spinner
        initializeLastSelectedTime();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hour, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hour);
                calendar.set(Calendar.MINUTE, minute);
                String formattedTime = timeFormat.format(calendar.getTime());
                editTextTime.setText(formattedTime);
                lastSelectedHour = hour;
                lastSelectedMinute = minute;
            }
        };
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(mContext, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, timeSetListener, lastSelectedHour, lastSelectedMinute, is24HView).show();
            }
        });

        btnCreateHangout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String place = autoCompletePlaces.getText().toString();
                Date date = calendar.getTime();
                ParseUser user1 = ParseUser.getCurrentUser();
                createHangout(date, user1, place);
            }
        });

    }

    //TODO: add checks here (cannot create hangout in past)
    private void createHangout(Date date, ParseUser user1, String place) {
        Hangout hangout = new Hangout();
        List<String> placeNameList = Arrays.asList(placeNameArray);
        if (placeNameList.contains(place)) { // if location is one of autocomplete options
            int index = placeNameList.indexOf(place);
            hangout.setLocation(placeList.get(index));
        }
        // TODO: else create some way to store location that's not preexisting
        hangout.setDate(date);
        hangout.setUser1(user1);
        hangout.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null) {
                    Toast.makeText(mContext, ((MainActivity) mContext).getResources().getString(R.string.create_hangout_error), Toast.LENGTH_LONG).show();
                    Log.i(TAG, e.getMessage());
                }
                Toast.makeText(mContext, ((MainActivity) mContext).getResources().getString(R.string.create_hangout_success), Toast.LENGTH_SHORT).show();
                NavigationUtils.goMainActivity(getActivity());
            }
        });
    }


    /**
     * Sets the spinner hour and minutes to current time
     */
    private void initializeLastSelectedTime() {
        lastSelectedHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        lastSelectedMinute = Calendar.getInstance().get(Calendar.MINUTE);
    }
}
