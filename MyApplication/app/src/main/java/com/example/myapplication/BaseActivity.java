package com.example.myapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import helper_classes_and_methods.DataLoader;
/**
 * Author: Xinrui Zhang u7728429:implemented basic activity
 */
public abstract class BaseActivity extends AppCompatActivity {
    protected DataLoader dataLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataLoader = MyApp.getInstance().getDataLoader();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dataLoader != null) {
            dataLoader.saveDwellingsToFile("dataset.json");
        }
    }
}