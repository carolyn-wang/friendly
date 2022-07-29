package com.example.friendly.activities;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.friendly.R;
import com.example.friendly.utils.NavigationUtils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LoginActivity";
    private Context mContext;
    private TextInputEditText etUsername;
    private TextInputEditText etPassword;
    private MaterialButton btnLogin;
    private MaterialButton btnSignup;
    private ImageView ivLoginLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (ParseUser.getCurrentUser() != null) {
            NavigationUtils.goMainActivity(LoginActivity.this);
        }

        mContext = LoginActivity.this;

        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignup = findViewById(R.id.btnSignup);
        ivLoginLogo = findViewById(R.id.ivLoginLogo);

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                if (username.isEmpty() || password.isEmpty()) {
                    Toast.makeText(mContext, R.string.login_null_error, Toast.LENGTH_SHORT).show();
                    return;
                }
                loginUser(username, password);
            }
        });

        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "onClick sign up button");
                signupUser();
            }
        });

        ivLoginLogo.animate().rotation(360f).setDuration(15000).start();
    }

    //TODO: Limit to only 1 login in case of glitch
    private void loginUser(String username, String password) {
        ParseUser.logInInBackground(username, password, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException e) {
                if (e == null) {
                    NavigationUtils.goMainActivity(LoginActivity.this);
                    Toast.makeText(mContext, R.string.login_success, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(mContext, R.string.login_error, Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Issue with login", e);
                }

            }
        });
    }

    private void signupUser() {
        NavigationUtils.goSignupActivity(LoginActivity.this);
    }
}
//