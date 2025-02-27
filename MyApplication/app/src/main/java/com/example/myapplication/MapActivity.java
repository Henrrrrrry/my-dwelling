package com.example.myapplication;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
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

import android.widget.Toast;

import androidx.annotation.NonNull;
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
//    private Location location;
    private EditText userInput;
    private ListPopupWindow listPopupWindow;
    User user;
    private Button searchButton;

    /**
     * Author: Hongyu Li u7776180: implemented initial Google Maps API, created edit text and search button, implemented navigation bar
     * Author: Xinrui Zhang u7728429: implemented fuzzy search
     * Author: Juliang Xiao u7757949: Reload data when the search box is cleared
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get a handle to the fragment and register the callback. (loading map )
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        user = (User) getIntent().getExtras().getSerializable("USER");
//        data stream simulate method, uncomment the following line if want to see the data stream
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
                if (s.toString().isEmpty()) {
                    List<Dwelling> allDwellings = dataLoader.getBTree().getDwellings();
                    showDwellingsOnMap(allDwellings);
                }
            }
        });
        //Search button here
        searchButton = findViewById(R.id.search_button);

        /**
         * Author: Juliang Xiao u7757949 : judge the input of the search. If the content of the search contains ':', employ parser search; otherwise, employ fuzz search
         *
         *
         *
         */



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

    /**
     * Author: Xinrui Zhang: u7728429:show list popup window
     */
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

    /**
     * Author: Juliang Xiao: u7757949:Filter the search content using ExpressionParser
     * @param input
     * @return
     */
    private List<Dwelling> searchWithParser(String input) {
        List<Dwelling> filteredDwellings;
        Expression expression = null;
        try {
            ExpressionParser expressionParser = new ExpressionParser(input);
            expression = expressionParser.getExpression();

            List<Dwelling> dwellings = dataLoader.getBTree().getDwellings();
            filteredDwellings = new ArrayList<>();
            for (Dwelling dwelling : dwellings) {
                if (evaluateExpression(expression, dwelling)) {
                    filteredDwellings.add(dwelling);
                }
            }


        } catch (IllegalArgumentException e) {
            Toast.makeText(this, "Invalid input for search.", Toast.LENGTH_SHORT).show();
            return new ArrayList<>();
        }
        return filteredDwellings;
    }

    /**
     * Author :Juliang Xiao : u7757949:Display the filtered address results on the map.
     * @param dwellings
     */
    private void showDwellingsOnMap(List<Dwelling> dwellings) {
        Mmap.clear();
        for (Dwelling dwelling : dwellings) {
            LatLng location = new LatLng(dwelling.getLocation().getLat(), dwelling.getLocation().getLng());
            int colorType = dwelling.getSeismicRating();
            String address = dwelling.getAddress();
            addOneMarker(location, colorType, address);
        }
        if (!dwellings.isEmpty()) {
            LatLng firstLoc = new LatLng(dwellings.get(0).getLocation().getLat(), dwellings.get(0).getLocation().getLng());
            Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(firstLoc, 15));
        } else {
            Toast.makeText(this, "No dwellings match the criteria.", Toast.LENGTH_SHORT).show();
        }

        Mmap.setOnInfoWindowClickListener(marker -> {
            String address = (String) marker.getTag();
            Dwelling dwelling = dataLoader.getBTree().get(address);
            if (dwelling != null) {
                Intent intent = new Intent(MapActivity.this, ProfPageActivity.class);
                intent.putExtra("Dwelling", dwelling);
                intent.putExtra("User", user);
                startActivity(intent);
            }
        });
    }

    /**
     * Author :Juliang Xiao u7757949:Analyze the input content using a tokenizer.
     * @param expression
     * @param dwelling
     * @return
     */
    private boolean evaluateExpression(Expression expression, Dwelling dwelling) {
        if (expression instanceof Condition) {
            Condition condition = (Condition) expression;
            String valueWithoutQuotes = condition.getValue().replace("\"", "");
            Log.d("EvaluateExpression", "Evaluating condition: " + condition.getKey() + " - " + valueWithoutQuotes);
            switch (condition.getKey()) {
                case "address":
                    return dwelling.getAddress().toLowerCase().contains(valueWithoutQuotes.toLowerCase());
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
            Log.d("EvaluateExpression", "Evaluating AND expression");
            return evaluateExpression(((AndExp) expression).getLeft(), dwelling) &&
                    evaluateExpression(((AndExp) expression).getRight(), dwelling);
        } else if (expression instanceof OrExp) {
            Log.d("EvaluateExpression", "Evaluating OR expression");
            return evaluateExpression(((OrExp) expression).getLeft(), dwelling) ||
                    evaluateExpression(((OrExp) expression).getRight(), dwelling);
        } else if (expression instanceof NotExp) {
            Log.d("EvaluateExpression", "Evaluating NOT expression");
            return !evaluateExpression(((NotExp) expression).getExpression(), dwelling);
        }
        return false;
    }


    /**
     * Author: Hongyu Li: implemented marker's info window onclick function
     * Description: When the map is loaded this method would be called.
      */

    @SuppressLint("PotentialBehaviorOverride")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        Mmap = googleMap;
        addMarkers();
        viewCurrentLocation();//Ask for users current location
        Mmap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            /**
             * Author: Hongyu Li u7776180
             * @param marker:the marker clicked on
             */
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
    /**
     * Author：Yujing Zhang u7671098
     * Description: Get GPS coordination, and move camera
     */
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
    /**
     * Author：Yujing Zhang u7671098
     * Discription: Move camera and set zoom in
     *
     * @param current
     */
    //set camera and zoom in default 15
    private void seeCurrent(LatLng current) {
        Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(current, 15));
    }
    /**
     * Author：Yujing Zhang u7671098: created method and implement with test dataset.
     * Author: Hongyu Li: edited argument in addOneMarker method
     */
    private void addMarkers() { //markers all dwelling on the initial map
        for (Dwelling d : dataLoader.getBTree().getDwellings()) {
            addOneMarker(new LatLng(d.getLocation().getLat(),
                    d.getLocation().getLng()), d.getSeismicRating(),
                    d.getAddress());
        }
    }

    /**
     * Author：Yujing Zhang u7671098: Created method and implement Markers with colors, and show text)
     * Author Hongyu Li u7776180: added another argument address, also added a "guide" in title
     *
     * @param coordinates
     * @param colorType
     * @param address
     */
    private void addOneMarker(LatLng coordinates,int colorType,String address){ //add single mark with special color
        Marker marker=
                Mmap.addMarker(new MarkerOptions()
                .position(coordinates)
                .icon(BitmapDescriptorFactory.defaultMarker(getHueFromColorType(colorType)))
                .title("SR: " + colorType+", click to see more detail"));//show SR level 1-10
        marker.setTag(address);
    }
    /**
     * Author：Yujing Zhang u7671098
     *
     * @param colorType:sceismic level
     */
    // how different colors according to sceismic level
    private float getHueFromColorType(int colorType) {
        return 12f * (colorType - 1); // 1-red,... ,10-green: SR level color
    }
    /**
     * Author: Hongyu Li u7776180
     * Description: simulating data stream
     */
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

    /**
     * Author: Hongyu Li u7776180
     * Description: helper method, since there's no performclick() method for info window, so use this mock clicking on info window
     * @param dwelling: the dwelling appeared in the edit text
     */
    public void onMarkerClicked(Dwelling dwelling) {
        Intent intent = new Intent(MapActivity.this, ProfPageActivity.class);
        intent.putExtra("Dwelling", dwelling);
        intent.putExtra("User", user);
        startActivity(intent);
    }
}
