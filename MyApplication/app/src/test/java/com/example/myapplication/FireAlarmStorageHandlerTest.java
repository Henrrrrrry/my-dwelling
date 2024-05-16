package com.example.myapplication;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.content.SharedPreferences;

import org.junit.*;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helper_classes_and_methods.*;
import helper_classes_and_methods.TimeUtil;

public class FireAlarmStorageHandlerTest {
    @Mock
    private Context mockContext;

    @Mock
    private SharedPreferences mockSharedPreferences;

    @Mock
    private SharedPreferences.Editor mockEditor;

    private FireAlarmStorageHandler fireAlarmStorageHandler;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(mockContext.getSharedPreferences("FireAlarmHistory", Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        fireAlarmStorageHandler = new FireAlarmStorageHandler(mockContext);
    }

    @Test
    public void testSaveData() {
        // Arrange
        String dwelling = "123 Main St";
        String message = "Fire alarm activated";
        String timestamp = "2024-05-15 10:00:00";

        // Mock TimeUtil to return a fixed timestamp
        mockStatic(TimeUtil.class);
        when(TimeUtil.getCurrentTimestamp()).thenReturn(timestamp);

        // Act
        fireAlarmStorageHandler.saveData(dwelling, message);

        // Assert
        verify(mockEditor).putString(timestamp, "Dwelling: " + dwelling + " - Activity: " + message);
        verify(mockEditor).apply();
    }

    private void saveData(String dwelling, String message) {

    }

    @Test
    public void testLoadAllLogs() {
        // Arrange
        Map<String, String> allEntries = new HashMap<>();
        allEntries.put("2024-05-15 10:00:00", "Dwelling: 123 Main St - Activity: Fire alarm activated");
        allEntries.put("2024-05-15 10:05:00", "Dwelling: 456 Elm St - Activity: Fire alarm deactivated");

        when(mockSharedPreferences.getAll()).thenReturn((Map) allEntries);

        // Act
        List<String> logs = fireAlarmStorageHandler.loadAllLogs();

        // Assert
        assertEquals(2, logs.size());
        assertTrue(logs.contains("2024-05-15 10:00:00 \n- Dwelling: 123 Main St \n- Activity: Fire alarm activated"));
        assertTrue(logs.contains("2024-05-15 10:05:00 \n- Dwelling: 456 Elm St \n- Activity: Fire alarm deactivated"));
    }


}
