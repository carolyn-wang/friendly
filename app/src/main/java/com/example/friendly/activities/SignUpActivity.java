package com.example.friendly.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.friendly.NavigationUtils;
import com.example.friendly.R;
import com.example.friendly.objects.User;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends AppCompatActivity {

    private static final String TAG = "SignUpActivity";
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
                signupUser(firstName, lastName, email, username, password);
            }
        });
    }

    private void signupUser(String firstName, String lastName, String email, String username, String password) {
        User user = new User();
        // Set the user's username and password, which can be obtained by a forms
        user.put("firstName", firstName);
        user.put("lastName", firstName);
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(password);
        MainActivity.setCurrentUser(user);
        user.signUpInBackground(new SignUpCallback() {
            // TODO: throw error for edge cases / authenticate login
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Toast.makeText(SignUpActivity.this, "Successful Sign Up!", Toast.LENGTH_LONG).show();
                    NavigationUtils.goPreferencesActivity(SignUpActivity.this);
                } else {
                    User.logOut();
                    Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}