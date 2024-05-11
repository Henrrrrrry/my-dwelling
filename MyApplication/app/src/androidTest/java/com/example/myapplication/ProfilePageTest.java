
package com.example.myapplication;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.test.espresso.matcher.BoundedMatcher;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.intent.rule.IntentsTestRule;

import org.hamcrest.Description;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

import helper_classes_and_methods.*;
/**
 * Author: Hongyu Li
 * Description: profile page test, since we can't use espresso to click on markers or its info window
 */
@RunWith(AndroidJUnit4.class)
public class ProfilePageTest {
    @Rule
    public IntentsTestRule<ProfPageActivity> intentsTestRule = new IntentsTestRule<>(ProfPageActivity.class, true, false);
    public static BoundedMatcher<View, ImageView> withDrawable(final @DrawableRes int resourceId) {
        return new BoundedMatcher<View, ImageView>(ImageView.class) {
            @Override
            protected boolean matchesSafely(ImageView imageView) {
                if (resourceId == 0) {
                    return imageView.getDrawable() == null;
                }
                Resources resources = imageView.getContext().getResources();
                Drawable expectedDrawable = ContextCompat.getDrawable(imageView.getContext(), resourceId);
                Drawable actualDrawable = imageView.getDrawable();
                if (expectedDrawable == null || actualDrawable == null) {
                    return false;
                }
                // Basic comparison based on drawable's constant state, might need more elaborate comparison
                return expectedDrawable.getConstantState().equals(actualDrawable.getConstantState());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("with drawable from resource id: ");
                description.appendValue(resourceId);
            }
        };
    }
    @Test
    public void testFollowUnfollowBehavior() {
        // Create the necessary User and Dwelling objects
        User testUser = new User("comp2100@anu.edu.au","comp2100",false,"Bernardo");
        Dwelling.Location testLocation = new Dwelling.Location(-35.3219007, 149.0565663);
        Dwelling testDwelling = new Dwelling("14A Kirkpatrick St, Weston ACT 2611, Australia", LocalDate.of(1998, 5, 29),BuildingMaterial.STEEL,new ArrayList<>(),testUser,testLocation);

        // Prepare the Intent with extras
        Intent intent = new Intent();
        intent.putExtra("User", testUser);
        intent.putExtra("Dwelling", testDwelling);

        // Launch the Activity with the Intent
        intentsTestRule.launchActivity(intent);

        // Interact with the follow button and check its behavior
        onView(withId(R.id.followButton)).perform(click());
        onView(withId(R.id.followButton)).check(matches(withText("Unfollow")));

        onView(withId(R.id.followButton)).perform(click());
        onView(withId(R.id.followButton)).check(matches(withText("Follow")));
    }


    @Test
    public void testBuildingMaterialImageDisplay() {
        User testUser = new User("comp2100@anu.edu.au", "comp2100", false, "Bernardo");
        Dwelling testDwelling = new Dwelling("14A Kirkpatrick St, Weston ACT 2611, Australia", LocalDate.of(1998, Month.MAY, 29), BuildingMaterial.STEEL, new ArrayList<>(), testUser, new Dwelling.Location(-35.3219007, 149.0565663));

        Intent intent = new Intent();
        intent.putExtra("User", testUser);
        intent.putExtra("Dwelling", testDwelling);

        intentsTestRule.launchActivity(intent);

        // Check if the correct image is displayed for STEEL material
        onView(withId(R.id.buildingImage)).check(matches(withDrawable(R.drawable.steel)));
    }


    @Test
    public void testBackButtonFunctionality() {
        User testUser = new User("comp2100@anu.edu.au", "comp2100", false, "Bernardo");
        Dwelling testDwelling = new Dwelling("14A Kirkpatrick St, Weston ACT 2611, Australia", LocalDate.of(1998, Month.MAY, 29), BuildingMaterial.STEEL, new ArrayList<>(), testUser, new Dwelling.Location(-35.3219007, 149.0565663));

        Intent intent = new Intent();
        intent.putExtra("User", testUser);
        intent.putExtra("Dwelling", testDwelling);
        intentsTestRule.launchActivity(intent);

        onView(withId(R.id.backButton)).perform(click());

        assertTrue(intentsTestRule.getActivity().isFinishing());
    }

}
