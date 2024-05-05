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

public class LoginActivity extends AppCompatActivity {

    public DataLoader dataLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.usernameEditText);
        final EditText passwordEditText = findViewById(R.id.passwordEditText);
        Button loginButton = findViewById(R.id.loginButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                if (validateLogin(username, password)) {
                    Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid username or password", Toast.LENGTH_LONG).show();
                }
            }
        });

        dataLoader = new DataLoader(this);
        dataLoader.loadDataFromFile("dataset.json");
        // BTree datasetBtree = dataLoader.getBTree();
        //dataLoader.saveDwellingsToFile("dataset.json");

    }

    private boolean validateLogin(String username, String password) {
        // Add your authentication logic here
        // For example, checking against hardcoded credentials
        return username.equals("admin") && password.equals("admin123");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataLoader.saveDwellingsToFile("dataset.json");
    }
}