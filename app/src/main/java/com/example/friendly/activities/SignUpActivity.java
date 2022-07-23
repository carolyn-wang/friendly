package com.example.friendly.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.telephony.PhoneNumberFormattingTextWatcher;
import android.telephony.PhoneNumberUtils;
import android.text.Editable;
import android.view.View;
import android.widget.Toast;

import com.example.friendly.utils.NavigationUtils;
import com.example.friendly.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class SignUpActivity extends AppCompatActivity implements android.text.TextWatcher{

    private static final String TAG = "SignUpActivity";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_PHONE = "phone";
    private static final String KEY_HOBBY_PREFERENCE = "hobbyPreference";
    private static final String KEY_ACTIVITY_PREFERENCE = "activityPreference";
    private static final String KEY_AVAILABILITY_PREFERENCE = "availabilityPreference";
    private static final String KEY_LOCATION = "location";
    private static final String KEY_PREFERENCE_WEIGHTS = "preferenceWeights";
    private static final String KEY_AVERAGE_SIMILARITY_SCORES = "averageSimilarityScores";
    private static final int DAYS_IN_WEEK = 7;

    private static Context mContext;
    private TextInputEditText etFirstName;
    private TextInputEditText etLastName;
    private TextInputEditText etEmail;
    private TextInputEditText etPhone;
    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private TextInputEditText etConfirmPassword;
    private MaterialButton btnSignupNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        mContext = SignUpActivity.this;

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignupNext = findViewById(R.id.btnSignupNext);

        etPhone.addTextChangedListener(new PhoneNumberFormattingTextWatcher());
        etPhone.addTextChangedListener(this);

        btnSignupNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String email = etEmail.getText().toString();
                String phone = etPhone.getText().toString().replaceAll(getString(R.string.phone_formatting_regex),"");
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                try {
                    signupUser(firstName, lastName, email, phone, username, password, confirmPassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void signupUser(String firstName, String lastName, String email, String phone, String username, String password, String confirmPassword) throws JSONException {
        ParseUser user = new ParseUser();
        // Set the user's username and password, which can be obtained by a forms
        user.put(KEY_FIRST_NAME, firstName);
        user.put(KEY_LAST_NAME, lastName);
        user.setEmail(email);
        user.put(KEY_PHONE, phone);
        user.setUsername(username);
        user.setPassword(password);

        // setting default values for database
        int hobby_options_len = getResources().getStringArray(R.array.option2).length;
        int activity_options_len = getResources().getStringArray(R.array.option3).length;
        int availability_options_len = getResources().getStringArray(R.array.time_options_array).length * DAYS_IN_WEEK;
        boolean[] hobbyArr = new boolean[hobby_options_len];
        boolean[] activityArr = new boolean[activity_options_len];
        List<Boolean> availabilityArr = Arrays.asList(new Boolean[availability_options_len]);
        Collections.fill(availabilityArr, Boolean.FALSE);
        user.put(KEY_HOBBY_PREFERENCE, new JSONArray(hobbyArr));
        user.put(KEY_ACTIVITY_PREFERENCE, new JSONArray(activityArr));
        user.put(KEY_AVAILABILITY_PREFERENCE, availabilityArr);
        user.put(KEY_PREFERENCE_WEIGHTS, new JSONArray(getResources().getIntArray(R.array.default_weights)));
        user.put(KEY_AVERAGE_SIMILARITY_SCORES, new JSONArray(getResources().getIntArray(R.array.default_average_similarity)));
        user.put(KEY_LOCATION, new ParseGeoPoint());

        user.signUpInBackground(new SignUpCallback() {
            // TODO: throw error for edge cases / authenticate login
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(SignUpActivity.this, getResources().getString(R.string.sign_up_success), Toast.LENGTH_SHORT).show();
                    NavigationUtils.goPreferencesActivity(SignUpActivity.this);
                } else {
                    ParseUser.logOut();
                    Toast.makeText(SignUpActivity.this, (e.getMessage()).substring(e.getMessage().lastIndexOf(":") + 1), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (etPhone.getText().toString().replaceAll(getString(R.string.phone_formatting_regex),"").length() > 11){
            etPhone.setTextColor(Color.RED);
            Toast.makeText(mContext, "Please enter a valid phone number", Toast.LENGTH_SHORT).show();
        }else{
            etPhone.setTextColor(Color.BLACK);
        }
    }
}