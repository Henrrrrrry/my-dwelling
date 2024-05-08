package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import helper_classes_and_methods.BTree;
import helper_classes_and_methods.DataLoader;
import helper_classes_and_methods.User;


public class LoginActivity extends AppCompatActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        user = new User();
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (user.validateUserCredentials(username, password, getAssets())) {
                    // Successful login, proceed to next activity
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    // Intent to next activity here
                    Intent intent = new Intent(getApplicationContext(), MapActivity.class);
                    intent.putExtra("USER", user);
                    startActivity(intent);
                } else {
                    // Login failed
                    Toast.makeText(LoginActivity.this, "Invalid credentials, please try again.", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}