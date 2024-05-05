package com.example.myapplication;

import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;

import android.app.Notification;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationManagerCompat;
import androidx.test.core.app.ActivityScenario;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import helper_classes_and_methods.*;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


/**
 * Instrumented test, which will execute on an Android device. Such as UI test, database access, network test...
 *
 * @see <a href="https://developer.android.com/training/testing/instrumented-tests">Testing documentation</a>
 * <a href="https://developer.android.com/studio/test/other-testing-tools/espresso-test-recorder">espresso test, more useful in graphical test</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    private UiDevice device;

    @Before
    public void setUp() {
//        initialize device
        device = UiDevice.getInstance(getInstrumentation());
    }

    @Test
    public void testFireAlarmNotification() throws UiObjectNotFoundException {

        // start the app
        device.pressHome();

        Context context = getInstrumentation().getContext();
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ProfPageActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        ActivityScenario<ProfPageActivity> scenario = ActivityScenario.launch(intent);

        // click fireAlarm button
        UiObject fireAlarmButton = device.findObject(new UiSelector().resourceId("com.example.myapplication:id/fireAlarmButton"));
        fireAlarmButton.click();

        // wait for noti
        device.openNotification();
        UiObject notificationTitle = device.findObject(new UiSelector().textContains("Fire Alarm"));
        assertTrue(notificationTitle.exists());
    }


}