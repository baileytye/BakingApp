package com.tye.bakingapp;

import android.os.RemoteException;
import android.widget.TextView;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withResourceName;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.CoreMatchers.anyOf;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.AllOf.allOf;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.filters.LargeTest;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.uiautomator.UiDevice;

import com.google.android.exoplayer2.ui.PlayerView;
import com.tye.bakingapp.Activities.DetailsActivity;
import com.tye.bakingapp.Activities.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
@LargeTest
public class MyTestSuite {

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        ActivityScenario activityScenario = ActivityScenario.launch(MainActivity.class);
        activityScenario.onActivity(new ActivityScenario.ActivityAction<MainActivity>() {
            @Override
            public void perform(MainActivity activity) {
                mIdlingResource = activity.getIdlingResource();
                // To prove that the test fails, omit this call:
                IdlingRegistry.getInstance().register(mIdlingResource);
            }
        });
    }

    @Test
    public void checkContentMatch() {

        onView(withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Check steps and ingridients are displayed
        onView(withId(R.id.rv_recipe_fragment)).check(matches(isDisplayed()));
        onView(withId(R.id.layout_ingridient_item)).check(matches(isDisplayed()));

        //Click step 1
        onView(withId(R.id.rv_recipe_fragment)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, click()));

        //Make sure directions are displayed
        onView(withId(R.id.tv_step_instruction)).check(matches(isDisplayed()));
    }

    
    //Test rotation of steps fragment to make sure the video is displayed if present
    @Test
    public void testRotation(){
        UiDevice device = UiDevice.getInstance(getInstrumentation());

        onView(withId(R.id.rv_recipes))
                .perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));

        //Click step 1 which has a video
        onView(withId(R.id.rv_recipe_fragment)).perform(
                RecyclerViewActions.actionOnItemAtPosition(1, click()));

        try {
            device.setOrientationLeft();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        //Check to make sure video is present and textview is gone
        onView(withId(R.id.exo_player_view)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.tv_no_video)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));


        try {
            device.setOrientationNatural();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Espresso.pressBack();

        //Click step 2, which has no video
        onView(withId(R.id.rv_recipe_fragment)).perform(
                RecyclerViewActions.actionOnItemAtPosition(2, click()));

        try {
            device.setOrientationRight();
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        //Check video is gone and textview is present saying no video
        onView(withId(R.id.tv_no_video)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.VISIBLE)));
        onView(withId(R.id.exo_player_view)).check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
    }



    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }

}
