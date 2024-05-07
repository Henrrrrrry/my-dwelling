package com.example.myapplication;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import helper_classes_and_methods.User;

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

        User user = (User) getIntent().getExtras().getSerializable("USER");

    }

    //    When the map is loaded this method would be called.
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Mmap = googleMap;
        addMarkers();
        //Ask for users current location
        String serviceString = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) getSystemService(serviceString); // use getSystemService() to set LocationManager
        String provider = LocationManager.NETWORK_PROVIDER; // use network location
        // check system location private
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1);
            return;
        }

// 获取最后已知位置
        Location location = locationManager.getLastKnownLocation(provider);
        System.out.println("test4");

        if (location != null) {
            double lat = location.getLatitude(); // get current lat
            double lng = location.getLongitude(); // get current lng
            System.out.println("test:" + lat + "," + lng);
            LatLng current = new LatLng(lat, lng);
            seeCurrent(current);
        } else {
            System.out.println("Location not available");
            //try to update location   尝试使用实时更新获取位置
            locationManager.requestLocationUpdates(provider, 0, 0, new LocationListener() {
                @Override
                public void onLocationChanged(Location location) {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    System.out.println("Updated location new: " + lat + ", " + lng);
                    locationManager.removeUpdates(this);
                }
            });
        }
        //LatLng Canberra = new LatLng(lat, lng);
        //LatLng Canberra = new LatLng(-35.2966, 149.1290);
        //seeCurrent(Canberra);
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
