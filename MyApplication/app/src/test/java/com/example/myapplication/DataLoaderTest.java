package com.example.myapplication;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.res.AssetManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.ArrayList;

import helper_classes_and_methods.*;

@RunWith(MockitoJUnitRunner.class)
public class DataLoaderTest {

    @Mock
    private Context mockContext;

    @Mock
    private AssetManager mockAssetManager;

    @Mock
    private FileInputStream mockFileInputStream;

    @Mock
    private FileOutputStream mockFileOutputStream;

    private DataLoader dataLoader;

    @Before
    public void setUp() {
        dataLoader = new DataLoader(mockContext);
    }
    @Test
    public void testSaveDataInternalStorage() throws Exception {
        when(mockContext.openFileOutput(anyString(), anyInt())).thenReturn(mockFileOutputStream);

        dataLoader.saveDataInternalStorage("data", mockContext, "file.json");

        verify(mockContext).openFileOutput("file.json", Context.MODE_PRIVATE);
    }

    @Test
    public void testGetBTree() {
        assertNotNull(dataLoader.getBTree());
    }

    @Test
    public void testCreateDwellingFromJson_Successful() throws Exception {
        JSONObject jsonObject = mock(JSONObject.class);
        when(jsonObject.optString("address", "")).thenReturn("123 Main St");
        when(jsonObject.optString("constructionDate", "")).thenReturn("2021-05-20");
        when(jsonObject.optString("buildingMaterial", "")).thenReturn("WOOD");

        JSONObject locationObject = mock(JSONObject.class);
        when(locationObject.optDouble("lat", 0.0)).thenReturn(35.6895);
        when(locationObject.optDouble("lng", 0.0)).thenReturn(139.6917);
        when(jsonObject.getJSONObject("location")).thenReturn(locationObject);

        Dwelling dwelling = dataLoader.createDwellingFromJson(jsonObject);

        assertEquals("123 Main St", dwelling.getAddress());
        assertEquals(LocalDate.of(2021, 5, 20), dwelling.getConstructionDate());
        assertEquals(BuildingMaterial.WOOD, dwelling.getBuildingMaterial());
        assertEquals(35.6895, dwelling.getLocation().getLat(), 0.001);
        assertEquals(139.6917, dwelling.getLocation().getLng(), 0.001);
    }


    @Test
    public void testCreateDwellingFromJson_MissingFields() throws Exception {
        JSONObject jsonObject = new JSONObject();
        // Intentionally missing mandatory fields to see how it handles

        assertThrows(Exception.class, () -> {
            dataLoader.createDwellingFromJson(jsonObject);
        });



    }

}
