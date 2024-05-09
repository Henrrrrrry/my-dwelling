package com.example.myapplication;

import android.app.Application;
import helper_classes_and_methods.DataLoader;

public class MyApp extends Application {
    private static MyApp instance;
    private DataLoader dataLoader;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        dataLoader = new DataLoader(this);
        dataLoader.loadDataFromFile("dataset.json");
    }

    public static MyApp getInstance() {
        return instance;
    }

    public DataLoader getDataLoader() {
        return dataLoader;
    }
}
