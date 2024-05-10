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
import android.util.Log;
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

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import helper_classes_and_methods.Dwelling;
import helper_classes_and_methods.User;
import helper_classes_and_methods.parser.AndExp;
import helper_classes_and_methods.parser.Condition;
import helper_classes_and_methods.parser.Expression;
import helper_classes_and_methods.parser.ExpressionParser;
import helper_classes_and_methods.parser.NotExp;
import helper_classes_and_methods.parser.OrExp;

public class MapActivity extends BaseActivity implements OnMapReadyCallback {

    private GoogleMap Mmap;
    private Location location;
    private EditText userInput;
    private ListPopupWindow listPopupWindow;
    User user;
    private Button searchButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get a handle to the fragment and register the callback. Means loading map I guess
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        user = (User) getIntent().getExtras().getSerializable("USER");
//        simulateSearch();

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
        searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // get user's input
                String inputStr = userInput.getText().toString();

                if (inputStr.contains(":")) {
                    // Use the expression parser if the input seems to be an expression
                    List<Dwelling> searchResults = searchWithParser(inputStr);
                    if (!searchResults.isEmpty()) {
                        showDwellingsOnMap(searchResults);
                    } else {
                        Toast.makeText(MapActivity.this, "No matching dwellings found.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // If the input doesn't contain ":", use the simple search method
                    Dwelling searchDwelling = dataLoader.getBTree().get(inputStr);
                    if (searchDwelling != null) {
                        LatLng latLng = new LatLng(searchDwelling.getLocation().getLat(), searchDwelling.getLocation().getLng());
                        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 19));
                    } else {
                        Toast.makeText(MapActivity.this, "Address not found.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnItemSelectedListener(item -> {
                    int itemId = item.getItemId();
                    if (itemId == R.id.nav_map) {
                        viewCurrentLocation();
                        return true;
                    } else if (itemId == R.id.nav_user) {

                        if (!(getApplicationContext() instanceof UserActivity)) {
                            Intent intent = new Intent(this, UserActivity.class);
                            intent.putExtra("User",user);
                            startActivity(intent);
                        }
                        return true;
                    }
                    return false;
                }

        );

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
//                Dwelling dwelling = dataLoader.getBTree().get(addr);
//                LatLng latLng = new LatLng(dwelling.getLocation().getLat(), dwelling.getLocation().getLng());
//                Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));

            }
        });
        listPopupWindow.show();
    }

    private List<Dwelling> searchWithParser(String input) {
        List<Dwelling> filteredDwellings;
        try {
            ExpressionParser expressionParser = new ExpressionParser(input);
            Log.d("SearchWithParser", "Starting search with input: " + input);
            Expression expression = expressionParser.getExpression();
            Log.d("SearchWithParser", "Expression parsed from input: " + expression);

            List<Dwelling> dwellings = dataLoader.getBTree().getDwellings();
            filteredDwellings = new ArrayList<>();
            for (Dwelling dwelling : dwellings) {
                if (evaluateExpression(expression, dwelling)) {
                    filteredDwellings.add(dwelling);
                }
            }
            Log.d("SearchWithParser", "Filtered " + filteredDwellings.size() + " dwellings based on the search expression");
            Log.d("SearchWithParser", "Filtered Dwellings: " + filteredDwellings);
        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Invalid input for search.", Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        }
        return filteredDwellings;
    }

    private void showDwellingsOnMap(List<Dwelling> dwellings) {
        Mmap.clear();
        for (Dwelling dwelling : dwellings) {
            LatLng location = new LatLng(dwelling.getLocation().getLat(), dwelling.getLocation().getLng());
            Marker marker = Mmap.addMarker(new MarkerOptions().position(location).title(dwelling.getAddress()));
            marker.setTag(dwelling); // 将Dwelling对象与标记关联
        }
        if (!dwellings.isEmpty()) {
            // Zoom to the first dwelling
            LatLng firstLoc = new LatLng(dwellings.get(0).getLocation().getLat(), dwellings.get(0).getLocation().getLng());
            Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLoc, 15));
        } else {
            Toast.makeText(this, "No dwellings match the criteria.", Toast.LENGTH_SHORT).show();
        }

        Mmap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Dwelling dwelling = (Dwelling) marker.getTag(); // 获取与标记关联的Dwelling对象
                if (dwelling != null) {
                    Intent intent = new Intent(MapActivity.this, ProfPageActivity.class);
                    intent.putExtra("Dwelling", dwelling);
                    intent.putExtra("User", user);
                    startActivity(intent);
                }
            }
        });
    }


    private boolean evaluateExpression(Expression expression, Dwelling dwelling) {
        if (expression instanceof Condition) {
            Condition condition = (Condition) expression;
            String valueWithoutQuotes = condition.getValue().replace("\"", "");
            switch (condition.getKey()) {
                case "address":
                    return dwelling.getAddress().contains(valueWithoutQuotes);
                case "constructionDate":
                    return dwelling.getConstructionDate().toString().equals(valueWithoutQuotes);
                case "fireAlarm":
                    return (dwelling.isFireAlarm() == Boolean.parseBoolean(valueWithoutQuotes));
                case "buildingMaterial":
                    return dwelling.getBuildingMaterial().toString().equalsIgnoreCase(valueWithoutQuotes);
                case "dwellingState":
                    return dwelling.getDwellingState().toString().equalsIgnoreCase(valueWithoutQuotes);
                case "lastRepairDate":
                    return dwelling.getLastRepairDate().toString().equals(valueWithoutQuotes);
                default:
                    return false;
            }
        } else if (expression instanceof AndExp) {
            return evaluateExpression(((AndExp) expression).getLeft(), dwelling) &&
                    evaluateExpression(((AndExp) expression).getRight(), dwelling);
        } else if (expression instanceof OrExp) {
            return evaluateExpression(((OrExp) expression).getLeft(), dwelling) ||
                    evaluateExpression(((OrExp) expression).getRight(), dwelling);
        } else if (expression instanceof NotExp) {
            return !evaluateExpression(((NotExp) expression).getExpression(), dwelling);
        }
        return false;
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


    private void simulateSearch() {
        // write "ACT"
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                userInput.setText("ACT");
                // mock a list
                List<String> addressList = dataLoader.getBTree().getDwellings().stream()
                        .map(Dwelling::getAddress)
                        .filter(address -> address.toLowerCase().contains("act"))
                        .collect(Collectors.toList());
                showListPopupWindow(addressList);
            }
        }, 2000);

        // click the third address in the list
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                if (listPopupWindow != null) {
                    listPopupWindow.getListView().performItemClick(
                            listPopupWindow.getListView().getAdapter().getView(1, null, null),
                            1,
                            listPopupWindow.getListView().getAdapter().getItemId(1)
                    );
                }
            }
        }, 5000);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                searchButton.performClick();
            }
        },8000);

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                Dwelling dwelling = dataLoader.getBTree().get(userInput.getText().toString());
                if (dwelling != null) {
                    LatLng latLng = new LatLng(dwelling.getLocation().getLat(), dwelling.getLocation().getLng());

                    onMarkerClicked(dwelling);
                }
            }
        }, 13000);
    }
//since there's no performclick for info window, so use this mock clicking on info window
    private void onMarkerClicked(Dwelling dwelling) {
        Intent intent = new Intent(MapActivity.this, ProfPageActivity.class);
        intent.putExtra("Dwelling", dwelling);
        intent.putExtra("User", user);
        startActivity(intent);
    }

}
