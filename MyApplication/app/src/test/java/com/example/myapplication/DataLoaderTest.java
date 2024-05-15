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

import org.json.JSONObject;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.*;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
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
}
