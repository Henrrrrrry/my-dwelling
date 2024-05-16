package com.example.myapplication;

import static org.junit.Assert.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.Lifecycle;
import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.lang.reflect.Field;

import helper_classes_and_methods.*;

@RunWith(AndroidJUnit4.class)
public class BaseActivityTest {

    private DataLoader mockDataLoader;



    @Before
    public void setUp() throws Exception {
        // Mock the DataLoader instance
        mockDataLoader = Mockito.mock(DataLoader.class);

        // Use reflection to set the dataLoader field in MyApp
        Context context = ApplicationProvider.getApplicationContext();
        MyApp myApp = (MyApp) context;
        Field dataLoaderField = MyApp.class.getDeclaredField("dataLoader");
        dataLoaderField.setAccessible(true);
        dataLoaderField.set(myApp, mockDataLoader);
    }

    @After
    public void tearDown() throws Exception {
        // Reset the dataLoader field in MyApp
        Context context = ApplicationProvider.getApplicationContext();
        MyApp myApp = (MyApp) context;
        Field dataLoaderField = MyApp.class.getDeclaredField("dataLoader");
        dataLoaderField.setAccessible(true);
        dataLoaderField.set(myApp, null);

    }

    @Test
    public void testOnCreate() {
        // Use an ActivityScenario to create an instance of the TestBaseActivity
        ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class);

        scenario.onActivity(activity -> {
            // Verify that the dataLoader is set correctly
            assertNotNull(activity.dataLoader);
            assertEquals(mockDataLoader, activity.dataLoader);
        });
    }

    @Test
    public void testOnDestroy() {
        // Use an ActivityScenario to create an instance of the TestBaseActivity
        ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class);

        scenario.moveToState(Lifecycle.State.DESTROYED);

        // Verify that saveDwellingsToFile was called with the correct parameter
        verify(mockDataLoader, times(1)).saveDwellingsToFile("dataset.json");
    }
}
