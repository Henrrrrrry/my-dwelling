package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import helper_classes_and_methods.*;
import android.os.Bundle;
import android.widget.SearchView;

public class SearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_src);


        SearchView searchView = null;
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });
    }

//    maybe need to return some type of value
    private  void getCurrentLocation(){

    }

}