package com.example.myapplication;

import static org.mockito.Mockito.when;

import android.content.Context;

import org.junit.*;
import org.junit.Rule;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import helper_classes_and_methods.*;

//@RunWith(AndroidJUnit4.class)
public class UserActivityTest {
    @Mock
    Context mockContext;

    @Mock
    FireAlarmStorageHandler fireAlarmStorageHandler;

    @Mock
    LoginStorageHandler loginStorageHandler;

    User user;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        user = new User();

        List<String> fireAlarmLogs = new ArrayList<>();
        fireAlarmLogs.add("2024-05-15 10:00:00 Fire Alarm");
        fireAlarmLogs.add("2024-05-14 09:00:00 Fire Alarm");

        List<String> loginLogs = new ArrayList<>();
        loginLogs.add("2024-05-15 08:00:00 Login");
        loginLogs.add("2024-05-14 07:00:00 Login");

        when(fireAlarmStorageHandler.loadAllLogs()).thenReturn(fireAlarmLogs);
        when(loginStorageHandler.loadAllLogs()).thenReturn(loginLogs);
    }

    @Test
    public void testCombinedLogs() {
        List<String> fireAlarmLogs = fireAlarmStorageHandler.loadAllLogs();
        List<String> loginLogs = loginStorageHandler.loadAllLogs();

        // Combine the two lists
        List<String> combinedData = new ArrayList<>();
        combinedData.addAll(fireAlarmLogs);
        combinedData.addAll(loginLogs);

        // Sort the combined list in descending order
        Collections.sort(combinedData, Collections.reverseOrder());

        // Verify the combined and sorted list
        List<String> expectedData = new ArrayList<>();
        expectedData.add("2024-05-15 10:00:00 Fire Alarm");
        expectedData.add("2024-05-15 08:00:00 Login");
        expectedData.add("2024-05-14 09:00:00 Fire Alarm");
        expectedData.add("2024-05-14 07:00:00 Login");

        assert combinedData.equals(expectedData);
    }
}
