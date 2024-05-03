package com.example.myapplication;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

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

    //    When the map is loaded this method would be called.
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Mmap = googleMap;
        addMarkers();
        //TODO：Ask for users current location,and replace 'sydney'
        LatLng Canberra = new LatLng(-35.2966, 149.1290);
        seeCurrent(Canberra);
    }
   //markers in map
    private void addMarkers() {
        List<Object[]> locations = new ArrayList<>();//test array
        locations.add(new Object[]{new LatLng(-35.2913, 149.1205), 1});
        locations.add(new Object[]{new LatLng(-35.2812, 149.1484), 2});
        locations.add(new Object[]{new LatLng(-35.3080, 149.1244), 3});
        locations.add(new Object[]{new LatLng(-35.2966, 149.1290), 5});
        locations.add(new Object[]{new LatLng(-35.2777, 149.1085), 10});
        // list all the position and color,add to Mmap
        for (Object[] location : locations) {
            LatLng coordinates = (LatLng) location[0];
            int colorType = (Integer) location[1];
            Mmap.addMarker(new MarkerOptions()
                    .position(coordinates)
                    .icon(BitmapDescriptorFactory.defaultMarker(getHueFromColorType(colorType)))
                    .title("SR: " + colorType));//show SR level 1-10
        }

    }

    private float getHueFromColorType(int colorType) {
        return 12f * (colorType - 1); // 1-red,... ,10-green: SR level color
    }
    //set camera and zoom in
    private void seeCurrent(LatLng current) {
        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 10)); // location to current place，zoom in size5
    }
}
