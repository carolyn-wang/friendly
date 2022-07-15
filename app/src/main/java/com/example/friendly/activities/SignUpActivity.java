package com.example.friendly.activities;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.friendly.NavigationUtils;
import com.example.friendly.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
    private static final String KEY_FIRST_NAME = "firstName";
    private static final String KEY_LAST_NAME = "lastName";
    private static final String KEY_HOBBY_PREFERENCE = "hobbyPreference";
    private static final String KEY_ACTIVITY_PREFERENCE = "activityPreference";
    private static final String KEY_AVAILABILITY_PREFERENCE = "availabilityPreference";
    private static final String KEY_LOCATION = "Location";
    private static final String KEY_PREFERENCE_WEIGHTS = "preferenceWeights";
    private static final String KEY_AVERAGE_SIMILARITY_SCORES = "averageSimilarityScores";

    private static Context mContext;
    private TextInputEditText etFirstName;
    private TextInputEditText etLastName;
    private TextInputEditText etEmail;
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
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPassword = findViewById(R.id.etConfirmPassword);
        btnSignupNext = findViewById(R.id.btnSignupNext);

        btnSignupNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPassword.getText().toString();
                try {
                    signupUser(firstName, lastName, email, username, password, confirmPassword);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void signupUser(String firstName, String lastName, String email, String username, String password, String confirmPassword) throws JSONException {
       if (!password.equals(confirmPassword)){
           Toast.makeText(mContext, "Passwords don't match", Toast.LENGTH_LONG).show();
       }
        ParseUser user = new ParseUser();
        // Set the user's username and password, which can be obtained by a forms
        user.put(KEY_FIRST_NAME, firstName);
        user.put(KEY_LAST_NAME, lastName);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);

        // setting default values for database
        int hobby_options_len = getResources().getStringArray(R.array.option2).length;
        int activity_options_len = getResources().getStringArray(R.array.option3).length;
        int availability_options_len = getResources().getStringArray(R.array.option4).length;
        boolean[] hobbyArr = new boolean[hobby_options_len];
        boolean[] activityArr = new boolean[activity_options_len];
        boolean[] availabilityArr = new boolean[availability_options_len];
        user.put(KEY_HOBBY_PREFERENCE, new JSONArray(hobbyArr));
        user.put(KEY_ACTIVITY_PREFERENCE, new JSONArray(activityArr));
        user.put(KEY_AVAILABILITY_PREFERENCE, new JSONArray(availabilityArr));
        user.put(KEY_PREFERENCE_WEIGHTS, new JSONArray(getResources().getIntArray(R.array.default_weights)));
        user.put(KEY_AVERAGE_SIMILARITY_SCORES, new JSONArray(getResources().getIntArray(R.array.default_average_similarity)));
        user.put(KEY_LOCATION, new ParseGeoPoint());

        user.signUpInBackground(new SignUpCallback() {
            // TODO: throw error for edge cases / authenticate login
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(SignUpActivity.this, "Successful Sign Up!", Toast.LENGTH_SHORT).show();
                    NavigationUtils.goPreferencesActivity(SignUpActivity.this);
                } else {
                    ParseUser.logOut();
                    Toast.makeText(SignUpActivity.this, (e.getMessage()).substring(e.getMessage().lastIndexOf(":") + 1), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}