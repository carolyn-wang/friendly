package com.example.friendly.fragments.match;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.TimePicker;

import com.example.friendly.R;
import com.example.friendly.fragments.HangoutsFragment;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CreateQuickMatchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CreateQuickMatchFragment extends Fragment {

    private Activity mActivity;
    private Context mContext;
    private static final String TAG = "CreateQuickMatchFragment";

    EditText editTextDate;
    EditText editTextTime;
    Calendar calendar;

    boolean is24HView = false;
    int lastSelectedHour = 0;
    int lastSelectedMinute = 0;

    //TODO: move to string/values.xml
    private static final String[] PLACES = new String[]{
            "Taco Chunkis", "Kati Vegan Thai", "Cinerama", "Tapster"
    };

    public CreateQuickMatchFragment() {
    }

    public static CreateQuickMatchFragment newInstance() {
        Bundle args = new Bundle();
        CreateQuickMatchFragment fragment = new CreateQuickMatchFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_create_quick_match, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mActivity = getActivity();
        mContext = getContext();

        editTextDate = view.findViewById(R.id.editTextDate);
        editTextTime = view.findViewById(R.id.editTextTime);

        Calendar calendar = Calendar.getInstance();
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                editTextDate.setText(formatDate(calendar));
            }
        };
        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(mContext, date, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        setCurrentTime();
        TimePickerDialog.OnTimeSetListener timeSetListener = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                editTextTime.setText(formatTime12Hr(hourOfDay, minute));
                lastSelectedHour = hourOfDay;
                lastSelectedMinute = minute;
            }
        };
        editTextTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new TimePickerDialog(mContext, android.R.style.Theme_Holo_Light_Dialog_NoActionBar, timeSetListener, lastSelectedHour, lastSelectedMinute, is24HView).show();
            }
        });


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext,
                android.R.layout.select_dialog_item, PLACES);
        AutoCompleteTextView textView = (AutoCompleteTextView) view.findViewById(R.id.autoCompleteTextView);
        textView.setAdapter(adapter);
        textView.setThreshold(1); //Autocomplete will start working from first character

    }

    private String formatDate(Calendar calendar) {
        String Format = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(Format, Locale.US);
        return sdf.format(calendar.getTime());
    }

    // TODO: move to some utils
    private String formatTime12Hr(int hourOfDay, int minute) {
        String pattern = "hh:mm a";
        LocalTime time = LocalTime.parse(hourOfDay + ":" + minute);
        return time.format(DateTimeFormatter.ofPattern(pattern));
    }

    private void setCurrentTime() {
        lastSelectedHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
        lastSelectedMinute = Calendar.getInstance().get(Calendar.MINUTE);

    }
}
