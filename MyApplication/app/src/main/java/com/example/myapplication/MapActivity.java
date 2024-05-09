package com.example.myapplication;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListPopupWindow;
import android.widget.TextView;

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
import java.util.stream.Collectors;

import helper_classes_and_methods.Dwelling;
import helper_classes_and_methods.User;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {
    //    create a dwelling entity test: Dwelling dwelling= new Dwelling();
    private GoogleMap Mmap;
    private Location location;
    private EditText userInput;
    private ListPopupWindow listPopupWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get a handle to the fragment and register the callback. Means loading map I guess
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        User user = (User) getIntent().getExtras().getSerializable("USER");

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
                Intent intent = new Intent(MapActivity.this, ProfPageActivity.class);
                intent.putExtra("Dwelling",searchDwelling);
                intent.putExtra("User",user);
                startActivity(intent);
                finish();
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
                userInput.setText(addressList.get(position));
                listPopupWindow.dismiss();
            }
        });
        listPopupWindow.show();
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
