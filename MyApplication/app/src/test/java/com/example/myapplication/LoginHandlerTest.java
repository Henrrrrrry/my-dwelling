package com.example.myapplication;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;

import org.junit.*;
import org.mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import helper_classes_and_methods.*;

public class LoginHandlerTest {
    @Mock
    private Context mockContext;

    @Mock
    private SharedPreferences mockSharedPreferences;

    @Mock
    private SharedPreferences.Editor mockEditor;

    private LoginStorageHandler loginStorageHandler;

    @Before
    public void setUp() {
        // Initialize mocks
        MockitoAnnotations.initMocks(this);
        when(mockContext.getSharedPreferences("LoginHistory", Context.MODE_PRIVATE)).thenReturn(mockSharedPreferences);
        when(mockSharedPreferences.edit()).thenReturn(mockEditor);
        loginStorageHandler = new LoginStorageHandler(mockContext);

    }


    @Test
    public void testLoadAllLogs() {
        // Arrange
        Map<String, String> allEntries = new HashMap<>();
        allEntries.put("2024-05-15 10:00:00", "Username: testUser - Activity: login");
        allEntries.put("2024-05-15 10:05:00", "Username: anotherUser - Activity: logout");

        when(mockSharedPreferences.getAll()).thenReturn((Map) allEntries);

        // Act
        List<String> logs = loginStorageHandler.loadAllLogs();

        // Assert
        assertEquals(2, logs.size());
        assertEquals("2024-05-15 10:00:00 \n- Username: testUser \n- Activity: login", logs.get(0));
        assertEquals("2024-05-15 10:05:00 \n- Username: anotherUser \n- Activity: logout", logs.get(1));
    }

}
