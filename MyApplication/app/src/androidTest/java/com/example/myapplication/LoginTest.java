package com.example.myapplication;

import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import android.content.Context;
import android.content.Intent;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.matches;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;


/**
 * Author: Hongyu Li u7776180
 * Description: authorization test
 */
@RunWith(AndroidJUnit4.class)
public class LoginTest {
    @Rule
    public ActivityScenarioRule<LoginActivity> activityScenarioRule = new ActivityScenarioRule<>(LoginActivity.class);

    @Before
    public void setup() {


        Intents.init();
        disableAnimations();
    }
    ;


    @After
    public void tearDown() {
        Intents.release();
        enableAnimations();
    }

    public void disableAnimations() {
        UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());
        try {
            uiDevice.executeShellCommand("settings put global window_animation_scale 0");
            uiDevice.executeShellCommand("settings put global transition_animation_scale 0");
            uiDevice.executeShellCommand("settings put global animator_duration_scale 0");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enableAnimations() {
        UiDevice uiDevice = UiDevice.getInstance(getInstrumentation());
        try {
            uiDevice.executeShellCommand("settings put global window_animation_scale 1");
            uiDevice.executeShellCommand("settings put global transition_animation_scale 1");
            uiDevice.executeShellCommand("settings put global animator_duration_scale 1");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testComp2100() {
        try (ActivityScenario<LoginActivity> scenario = ActivityScenario.launch(LoginActivity.class)) {
            // type username and password
            Espresso.onView(ViewMatchers.withId(R.id.usernameEditText)).perform(ViewActions.typeText("comp2100@anu.edu.au"));
            Espresso.onView(ViewMatchers.withId(R.id.passwordEditText)).perform(ViewActions.typeText("comp2100"));

            Espresso.closeSoftKeyboard();
            // click login button
            Espresso.onView(ViewMatchers.withId(R.id.loginButton))
                    .perform(ViewActions.click());

            // verify the next activity and extra
            Intents.intended(allOf(
                    hasComponent(MapActivity.class.getName()),
                    hasExtraWithKey("USER")
            ));
        }
    }

    @Test
    public void testWrongPassword(){
        //        type username and password
        Espresso.onView(ViewMatchers.withId(R.id.usernameEditText)).perform(ViewActions.typeText("comp2100@anu.edu.au"));
        Espresso.onView(ViewMatchers.withId(R.id.passwordEditText)).perform(ViewActions.typeText("comp2000"));

        Espresso.closeSoftKeyboard();
//      click login button
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        Espresso.onView(ViewMatchers.withId(R.id.usernameEditText)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.passwordEditText)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).check(matches(isDisplayed()));

    }




    @Test
    public void testEmptyUsernamePassword() {
        // Click login button without entering username or password
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        // Verify UI elements are still displayed for retry
        Espresso.onView(ViewMatchers.withId(R.id.usernameEditText)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.passwordEditText)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).check(matches(isDisplayed()));
    }

    @Test
    public void testInvalidUsernameFormat() {
        // Enter invalid email format and valid password
        Espresso.onView(ViewMatchers.withId(R.id.usernameEditText)).perform(ViewActions.typeText("invalidEmailFormat"));
        Espresso.onView(ViewMatchers.withId(R.id.passwordEditText)).perform(ViewActions.typeText("comp2100"));

        Espresso.closeSoftKeyboard();
        // Click login button
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).perform(ViewActions.click());

        // Verify UI elements are still displayed for retry
        Espresso.onView(ViewMatchers.withId(R.id.usernameEditText)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.passwordEditText)).check(matches(isDisplayed()));
        Espresso.onView(ViewMatchers.withId(R.id.loginButton)).check(matches(isDisplayed()));
    }


}
