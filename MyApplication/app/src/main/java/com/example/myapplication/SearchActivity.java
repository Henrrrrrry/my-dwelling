package com.example.myapplication;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import helper_classes_and_methods.DataLoader;
import helper_classes_and_methods.Dwelling;

public class SearchActivity extends AppCompatActivity {
    private SearchView searchView;
    private DataLoader dataLoader;
    private ListView searchResultsListView;
    private ArrayAdapter<String> searchResultsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        searchView = findViewById(R.id.search_bar);
        searchResultsListView = findViewById(R.id.list_item);

        // 初始化 DataLoader 和加载数据
        dataLoader = new DataLoader(this);
        dataLoader.loadDataFromFile("dwellings.json");

        // 初始化 searchResultsAdapter
        searchResultsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new ArrayList<>());
        searchResultsListView.setAdapter(searchResultsAdapter);

        setupSearch();
    }

    private void setupSearch() {
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Dwelling dwelling = dataLoader.getBTree().get(query);
                if (dwelling != null) {
                    // 获取 Dwelling 对象的经纬度和状态信息
                    Dwelling.Location location = dwelling.getLocation();
                    String status = dwelling.getDwellingState().getClass().getSimpleName();

                    // 显示结果
                    String message = "Address: " + dwelling.getAddress() + "\n"
                            + "Latitude: " + location.getLat() + "\n"
                            + "Longitude: " + location.getLng() + "\n"
                            + "Status: " + status;
                    Toast.makeText(SearchActivity.this, message, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(SearchActivity.this, "Address not found.", Toast.LENGTH_SHORT).show();
                }
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()) {
                    clearSearchResults();
                } else {
                    List<Dwelling> searchResults = dataLoader.getBTree().searchPrefix(newText);
                    displaySearchResults(searchResults);
                }
                return true;
            }
        });
    }

    private void clearSearchResults() {
        searchResultsAdapter.clear();
        searchResultsAdapter.notifyDataSetChanged();
    }

    private void displaySearchResults(List<Dwelling> searchResults) {
        List<String> displayResults = new ArrayList<>();
        for (Dwelling dwelling : searchResults) {
            String result = dwelling.getAddress();
            displayResults.add(result);
        }
        searchResultsAdapter.clear();
        searchResultsAdapter.addAll(displayResults);
        searchResultsAdapter.notifyDataSetChanged();
    }
}