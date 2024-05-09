package com.example.myapplication;

import org.json.JSONArray;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RuntimeEnvironment;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.robolectric.RobolectricTestRunner;


import android.content.Context;
import android.content.res.AssetManager;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.time.LocalDate;
import java.util.ArrayList;

import helper_classes_and_methods.*;
import helper_classes_and_methods.DataLoader;

@RunWith(RobolectricTestRunner.class)
public class UnitTest {
    private File tempFile;
    private DataLoader dataLoader;
    private Context context;

    @Before
    public void setUp() throws Exception {
        // Initialize DataLoader with the simulated context and create a temporary JSON file in the app's file directory
        context = RuntimeEnvironment.application;
        dataLoader = new DataLoader(context);
        tempFile = new File(context.getFilesDir(), "test.json");
        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(tempFile))) {
            writer.write("[{\"address\":\"123 Test St\"}]");
        }
    }

    @After
    public void tearDown() {
        tempFile.delete();
    }
//    loader tests
    @Test
    public void testLoadData_Success() throws Exception {
        // Load data from the file and assert that the content is as expected
        JSONArray result = dataLoader.loadData("test.json");

        assertNotNull(result);
        assertEquals(1, result.length());// Verify the address field in the JSON object

        assertEquals("123 Test St", result.getJSONObject(0).getString("address"));
    }

    @Test(expected = IOException.class)
    public void testLoadData_FileNotFound() throws Exception {
        // preparation
        String fileName = "nonexistent.json";
        Context context = mock(Context.class);
        AssetManager assetManager = mock(AssetManager.class);

        // when using openFileInput throw FileNotFoundException
        when(context.openFileInput(fileName)).thenThrow(new FileNotFoundException("File not found"));
        // when calling getAssets() return the mocked AssetManager
        when(context.getAssets()).thenReturn(assetManager);
        // when opening file from asset throw IOException，no such assets
        when(assetManager.open(fileName)).thenThrow(new IOException("File not found in assets"));

        DataLoader dataLoader = new DataLoader(context);

        // execution
        dataLoader.loadData(fileName);
    }

//    BTree tests
    @Test
    public void testPut_NewKey() {
        BTree bTree = new BTree(3, String::compareTo);
        Dwelling dwelling = new Dwelling("123 Test St", LocalDate.now(), BuildingMaterial.WOOD, new ArrayList<>(), null, new Dwelling.Location(0, 0));

        assertNull(bTree.put("123 Test St", dwelling));
        assertNotNull(bTree.get("123 Test St"));
    }

    @Test
    public void testPut_ExistingKey() {
        BTree bTree = new BTree(3, String::compareTo);
        Dwelling oldDwelling = new Dwelling("123 Test St", LocalDate.now(), BuildingMaterial.WOOD, new ArrayList<>(), null, new Dwelling.Location(0, 0));
        bTree.put("123 Test St", oldDwelling);
        Dwelling newDwelling = new Dwelling("123 Test St", LocalDate.now().plusDays(1), BuildingMaterial.BRICK, new ArrayList<>(), null, new Dwelling.Location(0, 0));

        assertNotNull(bTree.put("123 Test St", newDwelling));
        assertEquals(BuildingMaterial.BRICK, bTree.get("123 Test St").getBuildingMaterial());
    }

    @Test
    public void testRemove_ExistingKey() {
        BTree bTree = new BTree(3, String::compareTo);
        Dwelling dwelling = new Dwelling("123 Test St", LocalDate.now(), BuildingMaterial.WOOD, new ArrayList<>(), null, new Dwelling.Location(0, 0));
        bTree.put("123 Test St", dwelling);

        assertNotNull(bTree.remove("123 Test St"));
        assertNull(bTree.get("123 Test St"));
    }

    @Test

    public void testRemove_NonExistingKey() {
        BTree bTree = new BTree(5, String::compareTo);
        assertNull(bTree.remove("nonexistent"));
    }
}