package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

//    create a dwelling entity test: Dwelling dwelling= new Dwelling();
    private GoogleMap Mmap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get a handle to the fragment and register the callback. Means loading map I guess
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            mapFragment.getMapAsync(this);

    }
//    When the map is loaded this method would be called. edit Mmap if you want to implement heat map or flag and stuff
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Mmap= googleMap;
    }
}