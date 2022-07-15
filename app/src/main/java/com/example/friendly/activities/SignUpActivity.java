package com.example.friendly.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.friendly.NavigationUtils;
import com.example.friendly.R;
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
    private EditText etFirstName;
    private EditText etLastName;
    private EditText etEmail;
    private EditText etUsername;
    private EditText etPassword;
    private Button btnSignupNext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etEmail = findViewById(R.id.etEmail);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnSignupNext = findViewById(R.id.btnSignupNext);

        btnSignupNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick sign up next button");
                String firstName = etFirstName.getText().toString();
                String lastName = etLastName.getText().toString();
                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                try {
                    signupUser(firstName, lastName, email, username, password);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void signupUser(String firstName, String lastName, String email, String username, String password) throws JSONException {
        ParseUser user = new ParseUser();
        // Set the user's username and password, which can be obtained by a forms
        user.put(KEY_FIRST_NAME, firstName);
        user.put(KEY_LAST_NAME, lastName);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);

        // setting default values for database
        int hobby_options_len = this.getResources().getStringArray(R.array.option2).length;
        int activity_options_len = this.getResources().getStringArray(R.array.option3).length;
        int availability_options_len = this.getResources().getStringArray(R.array.option4).length;
        boolean[] hobbyArr = new boolean[hobby_options_len];
        boolean[] activityArr = new boolean[activity_options_len];
        boolean[] availabilityArr = new boolean[availability_options_len];
        user.put(KEY_HOBBY_PREFERENCE, new JSONArray(hobbyArr));
        user.put(KEY_ACTIVITY_PREFERENCE, new JSONArray(activityArr));
        user.put(KEY_AVAILABILITY_PREFERENCE, new JSONArray(availabilityArr));

        user.put(KEY_LOCATION, new ParseGeoPoint());

        user.signUpInBackground(new SignUpCallback() {
            // TODO: throw error for edge cases / authenticate login
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(SignUpActivity.this, "Successful Sign Up!", Toast.LENGTH_LONG).show();
                    NavigationUtils.goPreferencesActivity(SignUpActivity.this);
                } else {
                    ParseUser.logOut();
                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}