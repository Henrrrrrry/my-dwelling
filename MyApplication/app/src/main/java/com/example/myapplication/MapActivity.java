package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import helper_classes_and_methods.Dwelling;
import helper_classes_and_methods.User;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {
    //    create a dwelling entity test: Dwelling dwelling= new Dwelling();
    private GoogleMap Mmap;
    private Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get a handle to the fragment and register the callback. Means loading map I guess
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        User user = (User) getIntent().getExtras().getSerializable("USER");
        Button searchButton = findViewById(R.id.search_button);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MapActivity.this, ProfPageActivity.class);

                startActivity(intent);
            }
        });

    }

    //    When the map is loaded this method would be called.
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Mmap = googleMap;
        addMarkers();
        viewCurrentLocation();//Ask for users current location
        //LatLng Canberra = new LatLng(-35.2966, 149.1290); seeCurrent(Canberra);//for test
        Mmap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(@NonNull Marker marker) {
                marker.showInfoWindow();
                return true;
            }
        });
    }


    private void viewCurrentLocation() {
        String serviceString = Context.LOCATION_SERVICE;
        LocationManager locationManager = (LocationManager) getSystemService(serviceString); // use  getSystemService() set LocationManager
        String provider = LocationManager.GPS_PROVIDER; // use GPS method
        // check the access
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
            }, 1);
            return;
        }
        // get new location from current phone
        locationManager.requestLocationUpdates(provider, 0, 0, new LocationListener() {
            @Override
            public void onLocationChanged(@NonNull Location location) {
                //get lat and lng
                double lat = location.getLatitude();
                double lng = location.getLongitude();
                LatLng current = new LatLng(lat, lng);
                seeCurrent(current);// move camera to current coordination
               locationManager.removeUpdates(this);
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(@NonNull String provider) {
            }

            @Override
            public void onProviderDisabled(@NonNull String provider) {
            }
        });
    }
    //set camera and zoom in default 15
    private void seeCurrent(LatLng current) {
        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
    }
    private void addMarkers() { //markers all dwelling on the initial map
        for (Dwelling d : dataLoader.getBTree().getDwellings()) {
            addOneMarker(new LatLng(d.getLocation().getLat(),
                    d.getLocation().getLng()), d.getSeismicRating());
//            addOneMarker(new LatLng(d.getLocation().getLat(),
//                    d.getLocation().getLng()), 5);
        }
    }
    private void addOneMarker(LatLng coordinates,int colorType){ //add single mark with special color
        Mmap.addMarker(new MarkerOptions()
                .position(coordinates)
                .icon(BitmapDescriptorFactory.defaultMarker(getHueFromColorType(colorType)))
                .title("SR: " + colorType));//show SR level 1-10
    }
    private float getHueFromColorType(int colorType) {
        return 12f * (colorType - 1); // 1-red,... ,10-green: SR level color
    }
}
