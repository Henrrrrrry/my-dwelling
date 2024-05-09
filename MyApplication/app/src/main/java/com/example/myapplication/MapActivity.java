package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;
import android.widget.Toast;

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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import helper_classes_and_methods.Dwelling;
import helper_classes_and_methods.User;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap Mmap;
    private Location location;
    private EditText userInput;
    private ListPopupWindow listPopupWindow;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get a handle to the fragment and register the callback. Means loading map I guess
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        user = (User) getIntent().getExtras().getSerializable("USER");


        //search_text
        userInput = findViewById(R.id.search_text);
        userInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 3) {
                    List<String> addressList = dataLoader.getBTree().getDwellings().stream()
                            .map(Dwelling::getAddress)
                            .filter(address -> address.toLowerCase()
                                    .contains(s.toString().toLowerCase()))
                            .collect(Collectors.toList());
                    showListPopupWindow(addressList);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
        //Search button here
        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get user's input
                String inputStr = userInput.getText().toString();
                Dwelling searchDwelling = dataLoader.getBTree().get(inputStr);
                if (searchDwelling!=null){
                    LatLng latLng = new LatLng(searchDwelling.getLocation().getLat(),  searchDwelling.getLocation().getLng());
                    Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
                }
                else {
                    Toast.makeText(MapActivity.this, "Address not found.", Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    private void showListPopupWindow(List<String> addressList) {
        if (listPopupWindow != null && listPopupWindow.isShowing()) {
            listPopupWindow.dismiss();
            listPopupWindow = null;
        }
        listPopupWindow = new ListPopupWindow(this);
        listPopupWindow.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1, addressList));
        listPopupWindow.setAnchorView(userInput);
//        listPopupWindow.setModal(true);

        listPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String addr = addressList.get(position);
                userInput.setText(addr);
                listPopupWindow.dismiss();
                Dwelling dwelling = dataLoader.getBTree().get(addr);
                LatLng latLng = new LatLng(dwelling.getLocation().getLat(), dwelling.getLocation().getLng());
                Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));

            }
        });
        listPopupWindow.show();
    }

    //    When the map is loaded this method would be called.
    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Mmap = googleMap;
        addMarkers();
        viewCurrentLocation();//Ask for users current location
        Mmap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {

            @Override
            public void onInfoWindowClick(@NonNull Marker marker) {
                Dwelling searchDwelling = dataLoader.getBTree().get((String) marker.getTag());
                if (searchDwelling!=null){
                    Intent intent = new Intent(MapActivity.this, ProfPageActivity.class);
                    intent.putExtra("Dwelling", searchDwelling);
                    intent.putExtra("User", user);
                            startActivity(intent);
                    LatLng latLng = new LatLng(searchDwelling.getLocation().getLat(),  searchDwelling.getLocation().getLng());
                    Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));


                }
                else {
                    Toast.makeText(MapActivity.this, "Address not found.", Toast.LENGTH_SHORT).show();
                }
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
        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 14));
    }
    private void addMarkers() { //markers all dwelling on the initial map
        for (Dwelling d : dataLoader.getBTree().getDwellings()) {
            addOneMarker(new LatLng(d.getLocation().getLat(),
                    d.getLocation().getLng()), d.getSeismicRating(),
                    d.getAddress());
        }
    }
    private void addOneMarker(LatLng coordinates,int colorType,String address){ //add single mark with special color
        Marker marker=
                Mmap.addMarker(new MarkerOptions()
                .position(coordinates)
                .icon(BitmapDescriptorFactory.defaultMarker(getHueFromColorType(colorType)))
                .title("SR: " + colorType+", click to see more detail"));//show SR level 1-10
        marker.setTag(address);
    }
    private float getHueFromColorType(int colorType) {
        return 12f * (colorType - 1); // 1-red,... ,10-green: SR level color
    }
}
