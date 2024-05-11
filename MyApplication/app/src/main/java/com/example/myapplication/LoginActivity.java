package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import helper_classes_and_methods.BTree;
import helper_classes_and_methods.DataLoader;
import helper_classes_and_methods.User;
import helper_classes_and_methods.StorageFactory;
import helper_classes_and_methods.StorageHandler;

/**
 * Author: Xinfei Li
 */
public class LoginActivity extends BaseActivity {

    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private User user;
    private StorageHandler storageHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.usernameEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);
        user = new User();
//        uncomment to utilize simulating data stream
        simulateUserInput();

        storageHandler = StorageFactory.getStorageHandler(this, StorageFactory.HandlerType.LOGIN);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString().trim();
                String password = passwordEditText.getText().toString();

                if (user.validateUserCredentials(username, password, getAssets())) {
                    // Successful login, save login status
                    Toast.makeText(LoginActivity.this, "Login successful!", Toast.LENGTH_SHORT).show();
                    storageHandler.saveData(username, "logged_in");

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

    /**
     * Author: Hongyu Li
     * Description: simulate a data stream, enter username and password then click login
     */
    @SuppressLint("SetTextI18n")
    private void simulateUserInput() {
        // delay 5s
        Handler handler = new Handler();
        handler.postDelayed(() -> {
            // write username
            usernameEditText.setText("comp6442@anu.edu.au");
        }, 3000);

        // write password
        handler.postDelayed(() -> {
            passwordEditText.setText("comp6442");
        }, 5000);

        // click login button
        handler.postDelayed(() -> {
            loginButton.performClick();
        }, 8000);
    }
}
