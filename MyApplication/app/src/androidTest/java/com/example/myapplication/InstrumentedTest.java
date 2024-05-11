package com.example.myapplication;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.content.Context;
import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.ArrayList;

import helper_classes_and_methods.BuildingMaterial;
import helper_classes_and_methods.Dwelling;
import helper_classes_and_methods.User;


/**
 * Author: Hongyu Li
 * Description: notification test
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTest {
    private UiDevice device;

    @Before
    public void setUp() {
//        initialize device
        device = UiDevice.getInstance(getInstrumentation());
    }

    @Test
    public void testFireAlarmNotification() throws UiObjectNotFoundException {
        User testUser = new User("comp6442@anu.edu.au","comp6442",true,"Bernardo");
        Dwelling.Location testLocation = new Dwelling.Location(-35.3219007, 149.0565663);
        Dwelling testDwelling = new Dwelling("14A Kirkpatrick St, Weston ACT 2611, Australia", LocalDate.of(1998, 5, 29), BuildingMaterial.STEEL,new ArrayList<>(),testUser,testLocation);

        // start the app
        device.pressHome();

        Context context = getInstrumentation().getContext();
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ProfPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra("Dwelling", testDwelling);
        intent.putExtra("User", testUser);
        ActivityScenario<ProfPageActivity> scenario = ActivityScenario.launch(intent);
        device.waitForIdle();
        // click fireAlarm button
        UiObject fireAlarmButton = device.findObject(new UiSelector().resourceId("com.example.myapplication:id/fireAlarmButton"));
        fireAlarmButton.click();

        // wait for noti
        device.openNotification();
        UiObject notificationTitle = device.findObject(new UiSelector().textContains("Fire Alarm"));
        assertTrue(notificationTitle.exists());
    }


}